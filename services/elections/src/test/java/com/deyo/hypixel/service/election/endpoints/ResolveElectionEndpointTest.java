package com.deyo.hypixel.service.election.endpoints;

import com.deyo.hypixel.commons.protocol.objects.election.ResolveElectionProtocol;
import com.deyo.hypixel.service.election.ElectionDatabase;
import com.google.gson.Gson;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ResolveElectionEndpointTest {

    private static final Gson GSON = new Gson();

    private MongoCollection<Document> originalElectionCollection;
    private MongoCollection<Document> originalTalliesCollection;
    private final AtomicReference<String> savedData = new AtomicReference<>();

    @BeforeEach
    void setUp() {
        originalElectionCollection = ElectionDatabase.electionCollection;
        originalTalliesCollection = ElectionDatabase.talliesCollection;
    }

    @AfterEach
    void tearDown() {
        ElectionDatabase.electionCollection = originalElectionCollection;
        ElectionDatabase.talliesCollection = originalTalliesCollection;
    }

    @Test
    void successfulPerkRollAddsAnUnusedPerkToTheLosingCandidate() {
        Map<String, Object> winner = candidate("COLE", 0, List.of("PROSPECTION"),
                List.of("PROSPECTION", "MINING_XP_BUFF", "MINING_FIESTA", "MOLTEN_FORGE"));
        Map<String, Object> loser = candidate("PAUL", 1, List.of("MARAUDER"),
                List.of("MARAUDER", "BENEDICTION", "EZPZ"));
        Map<String, Object> failed = new HashMap<>();
        failed.put("PAUL", true);

        stubDatabase(electionData(winner, loser, failed), Map.of("COLE", 5L, "PAUL", 3L));

        ResolveElectionProtocol.ResolveElectionResponse response = resolve(12);

        assertTrue(response.resolved());
        Map<String, Object> result = GSON.fromJson(response.serializedData(), Map.class);
        Map<String, Object> activePerks = (Map<String, Object>) result.get("candidateActivePerks");

        List<String> paulPerks = (List<String>) activePerks.get("PAUL");
        assertEquals(2, paulPerks.size());
        assertEquals("MARAUDER", paulPerks.getFirst());
        assertTrue(List.of("BENEDICTION", "EZPZ").contains(paulPerks.get(1)));

        Map<String, Object> failedAfter = (Map<String, Object>) result.get("failedPerkGainLastTime");
        assertEquals(Boolean.FALSE, failedAfter.get("PAUL"));

        assertEquals(List.of("PROSPECTION"), activePerks.get("COLE"));
        assertNotNull(savedData.get());
        assertEquals(result, GSON.fromJson(savedData.get(), Map.class));
    }

    @Test
    void candidateWithEveryPerkAlreadyUnlockedDoesNotGainAnother() {
        Map<String, Object> winner = candidate("COLE", 0, List.of("PROSPECTION"),
                List.of("PROSPECTION", "MINING_XP_BUFF", "MINING_FIESTA", "MOLTEN_FORGE"));
        Map<String, Object> loser = candidate("PAUL", 1,
                List.of("MARAUDER", "BENEDICTION", "EZPZ"),
                List.of("MARAUDER", "BENEDICTION", "EZPZ"));
        Map<String, Object> failed = new HashMap<>();
        failed.put("PAUL", true);

        stubDatabase(electionData(winner, loser, failed), Map.of("COLE", 5L, "PAUL", 3L));

        ResolveElectionProtocol.ResolveElectionResponse response = resolve(12);

        assertTrue(response.resolved());
        Map<String, Object> result = GSON.fromJson(response.serializedData(), Map.class);
        Map<String, Object> activePerks = (Map<String, Object>) result.get("candidateActivePerks");

        assertEquals(List.of("MARAUDER", "BENEDICTION", "EZPZ"), activePerks.get("PAUL"));
        assertFalse(((Map<String, Object>) result.get("failedPerkGainLastTime")).containsKey("PAUL"));
    }

    @Test
    void candidateWithoutAPerkPoolKeepsItsCurrentPerks() {
        Map<String, Object> winner = candidate("COLE", 0, List.of("PROSPECTION"),
                List.of("PROSPECTION", "MINING_XP_BUFF", "MINING_FIESTA", "MOLTEN_FORGE"));
        Map<String, Object> loser = candidate("PAUL", 1, List.of("MARAUDER"), List.of());
        Map<String, Object> failed = new HashMap<>();
        failed.put("PAUL", true);

        stubDatabase(electionData(winner, loser, failed), Map.of("COLE", 5L, "PAUL", 3L));

        ResolveElectionProtocol.ResolveElectionResponse response = resolve(12);

        assertTrue(response.resolved());
        Map<String, Object> result = GSON.fromJson(response.serializedData(), Map.class);
        Map<String, Object> activePerks = (Map<String, Object>) result.get("candidateActivePerks");

        assertEquals(List.of("MARAUDER"), activePerks.get("PAUL"));
    }

    private ResolveElectionProtocol.ResolveElectionResponse resolve(int year) {
        return new ResolveElectionEndpoint().handle(
                new ResolveElectionProtocol.ResolveElectionMessage(year), null);
    }

    private static Map<String, Object> candidate(String name, int index,
                                                 List<String> activePerks, List<String> perkPool) {
        Map<String, Object> candidate = new HashMap<>();
        candidate.put("mayorName", name);
        candidate.put("index", index);
        candidate.put("activePerks", activePerks);
        candidate.put("perkPool", perkPool);
        return candidate;
    }

    private static Map<String, Object> electionData(Map<String, Object> winner,
                                                    Map<String, Object> loser,
                                                    Map<String, Object> failedPerkGainLastTime) {
        Map<String, Object> data = new HashMap<>();
        data.put("electionOpen", true);
        data.put("electionYear", 12);
        data.put("candidates", List.of(winner, loser));
        data.put("candidateActivePerks", new HashMap<>());
        data.put("failedPerkGainLastTime", failedPerkGainLastTime);
        return data;
    }

    @SuppressWarnings("unchecked")
    private void stubDatabase(Map<String, Object> data, Map<String, Long> tallies) {
        Document electionDocument = new Document("_id", "election_data")
                .append("data", GSON.toJson(data));
        ElectionDatabase.electionCollection = collection((method, args) -> {
            if (method.equals("find")) return iterable(electionDocument);
            if (method.equals("replaceOne")) {
                savedData.set(((Document) args[1]).getString("data"));
                return null;
            }
            throw new AssertionError("Unexpected election collection call: " + method);
        });

        Document tallyDocument = new Document("_id", "tallies_12");
        tallies.forEach(tallyDocument::append);
        ElectionDatabase.talliesCollection = collection((method, args) -> {
            if (method.equals("find")) return iterable(tallyDocument);
            throw new AssertionError("Unexpected tallies collection call: " + method);
        });
    }

    private static FindIterable<Document> iterable(Document document) {
        return (FindIterable<Document>) Proxy.newProxyInstance(
                FindIterable.class.getClassLoader(),
                new Class<?>[]{FindIterable.class},
                (instance, method, args) -> {
                    if (method.getName().equals("first")) return document;
                    throw new AssertionError("Unexpected iterable call: " + method.getName());
                });
    }

    @SuppressWarnings("unchecked")
    private static MongoCollection<Document> collection(Handler handler) {
        return (MongoCollection<Document>) Proxy.newProxyInstance(
                MongoCollection.class.getClassLoader(),
                new Class<?>[]{MongoCollection.class},
                (instance, method, args) -> handler.handle(method.getName(), args));
    }

    private interface Handler {
        Object handle(String method, Object[] args);
    }
}
