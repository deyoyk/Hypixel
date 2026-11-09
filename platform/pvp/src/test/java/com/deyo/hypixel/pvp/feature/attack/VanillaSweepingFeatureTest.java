package com.deyo.hypixel.pvp.feature.attack;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EntityCreature;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.damage.Damage;
import net.minestom.server.instance.Chunk;
import net.minestom.server.instance.ChunkLoader;
import net.minestom.server.instance.DynamicChunk;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.InstanceContainer;
import com.deyo.hypixel.pvp.feature.FeatureType;
import com.deyo.hypixel.pvp.feature.config.FeatureConfiguration;
import com.deyo.hypixel.pvp.feature.enchantment.EnchantmentFeature;
import com.deyo.hypixel.pvp.feature.knockback.KnockbackFeature;
import com.deyo.hypixel.pvp.feature.provider.PacketProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class VanillaSweepingFeatureTest {

    private InstanceContainer instance;
    private VanillaSweepingFeature feature;
    private final List<LivingEntity> sweptTargets = new ArrayList<>();
    private EntityCreature attacker;
    private EntityCreature target;
    private EntityCreature nearby;

    @BeforeAll
    static void initServer() {
        MinecraftServer.init();
    }

    @BeforeEach
    void setUp() {
        instance = MinecraftServer.getInstanceManager().createInstanceContainer();
        instance.setChunkLoader(new ChunkLoader() {
            @Override
            public Chunk loadChunk(Instance instance, int chunkX, int chunkZ) {
                return new DynamicChunk(instance, chunkX, chunkZ);
            }

            @Override
            public void saveChunk(Chunk chunk) {
            }
        });
        instance.loadChunk(0, 0).join();

        EnchantmentFeature enchantmentFeature = (EnchantmentFeature) Proxy.newProxyInstance(
                EnchantmentFeature.class.getClassLoader(),
                new Class<?>[]{EnchantmentFeature.class},
                (proxy, method, args) -> {
                    if (method.getName().equals("getSweeping")) return 1;
                    Class<?> returnType = method.getReturnType();
                    if (returnType == boolean.class) return false;
                    if (returnType == int.class) return 0;
                    if (returnType == float.class) return 0f;
                    if (returnType == double.class) return 0d;
                    return null;
                });

        KnockbackFeature knockbackFeature = new KnockbackFeature() {
            @Override
            public boolean applyDamageKnockback(Damage damage, LivingEntity target) {
                return false;
            }

            @Override
            public boolean applyAttackKnockback(LivingEntity attacker, LivingEntity target, int knockback) {
                return false;
            }

            @Override
            public boolean applySweepingKnockback(LivingEntity attacker, LivingEntity target) {
                sweptTargets.add(target);
                return true;
            }
        };

        PacketProvider packetProvider = (viewable, packet) -> {
        };

        FeatureConfiguration configuration = new FeatureConfiguration()
                .add(FeatureType.ENCHANTMENT, enchantmentFeature)
                .add(FeatureType.KNOCKBACK, knockbackFeature)
                .add(FeatureType.PACKET, packetProvider);
        feature = new VanillaSweepingFeature(configuration);
        feature.initDependencies();

        attacker = spawn(new Pos(0, 0, 0));
        target = spawn(new Pos(1, 0, 0));
        nearby = spawn(new Pos(1.5, 0, 0));
    }

    @Test
    void sweepingKnocksBackAndDamagesEachNearbyEntity() {
        Collection<LivingEntity> affected = feature.applySweeping(attacker, target, 10f);

        assertTrue(affected.contains(nearby));
        assertEquals(List.of(nearby), sweptTargets);
        assertEquals(14f, nearby.getHealth(), 0.001f);
        assertEquals(20f, target.getHealth(), 0.001f);
    }

    private EntityCreature spawn(Pos position) {
        EntityCreature entity = new EntityCreature(EntityType.ZOMBIE);
        entity.setInstance(instance, position).join();
        return entity;
    }
}
