package com.deyo.hypixel.type.skyblockgeneric.structure.tree;

import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.block.Block;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TreeRegistryTest {

    private static InstanceContainer instance;

    @BeforeAll
    static void initServer() {
        MinecraftServer.init();
        instance = MinecraftServer.getInstanceManager().createInstanceContainer();
    }

    @AfterEach
    void clearRegistry() {
        TreeRegistry.clearInstance(instance);
    }

    @Test
    void returnsTreesWithinRange() {
        TreeRegistry.RegisteredTree tree = tree(0, 0, 0);
        TreeRegistry.registerTree(instance, tree);

        assertEquals(List.of(tree), TreeRegistry.getTreesInRange(instance, 0, 0, 0, 0.5));
        assertTrue(TreeRegistry.getTreesInRange(instance, 10, 0, 0, 1).isEmpty());
    }

    @Test
    void iterationToleratesConcurrentRegistration() {
        TreeRegistry.registerTree(instance, tree(0, 0, 0));
        TreeRegistry.registerTree(instance, tree(2, 0, 0));

        int visited = 0;
        for (TreeRegistry.RegisteredTree ignored : TreeRegistry.getTreesForInstance(instance)) {
            visited++;
            if (visited == 1) {
                TreeRegistry.registerTree(instance, tree(4, 0, 0));
            }
        }

        assertEquals(2, visited);
        assertEquals(3, TreeRegistry.getTreesForInstance(instance).size());
    }

    @Test
    void unregisterRemovesOnlyTheRequestedTree() {
        TreeRegistry.RegisteredTree first = tree(0, 0, 0);
        TreeRegistry.RegisteredTree second = tree(2, 0, 0);
        TreeRegistry.registerTree(instance, first);
        TreeRegistry.registerTree(instance, second);

        TreeRegistry.unregisterTree(instance, first);

        assertEquals(List.of(second), TreeRegistry.getTreesForInstance(instance));
    }

    private static TreeRegistry.RegisteredTree tree(int x, int y, int z) {
        return new TreeRegistry.RegisteredTree(x, y, z, null, 0,
                List.of(new TreeRegistry.BlockEntry(x, y, z, Block.STONE)));
    }
}
