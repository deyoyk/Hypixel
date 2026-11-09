package com.deyo.hypixel.service.store;

import org.bson.Document;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StoreDatabaseLeaseTest {

    @Test
    void leaseFilterRequiresOwnershipAndAnActiveLease() {
        Document filter = StoreDatabase.leaseFilter("purchase-1", "worker-a");

        assertEquals("purchase-1", filter.getString("_id"));
        assertEquals("FULFILLING", filter.getString("status"));
        assertEquals("worker-a", filter.getString("fulfillmentLeaseOwner"));
    }
}
