package com.deyo.hypixel.type.generic.gui.v2;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PaginatedViewTest {

    @Test
    void emptyContentHasOneEmptyPage() {
        PaginatedView.PageSlice page = PaginatedView.pageBounds(0, 28, 0);

        assertEquals(0, page.page());
        assertEquals(1, page.totalPages());
        assertEquals(0, page.startIndex());
        assertEquals(0, page.endIndex());
    }

    @Test
    void exactPageBoundaryDoesNotCreateAnExtraPage() {
        PaginatedView.PageSlice page = PaginatedView.pageBounds(28, 28, 0);

        assertEquals(1, page.totalPages());
        assertEquals(0, page.startIndex());
        assertEquals(28, page.endIndex());
    }

    @Test
    void laterPagesUseTheCorrectSlice() {
        PaginatedView.PageSlice page = PaginatedView.pageBounds(29, 28, 1);

        assertEquals(1, page.page());
        assertEquals(2, page.totalPages());
        assertEquals(28, page.startIndex());
        assertEquals(29, page.endIndex());
    }

    @Test
    void requestedPagesAreClampedToValidRange() {
        PaginatedView.PageSlice high = PaginatedView.pageBounds(50, 28, 99);
        PaginatedView.PageSlice low = PaginatedView.pageBounds(50, 28, -3);

        assertEquals(1, high.page());
        assertEquals(0, low.page());
        assertEquals(0, low.startIndex());
        assertEquals(28, low.endIndex());
    }

    @Test
    void invalidPageSizeIsTreatedAsOne() {
        PaginatedView.PageSlice page = PaginatedView.pageBounds(5, 0, 0);

        assertEquals(5, page.totalPages());
        assertEquals(0, page.startIndex());
        assertEquals(1, page.endIndex());
    }
}
