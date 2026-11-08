package com.deyo.hypixel.type.generic.gui.v2;

import com.deyo.hypixel.type.generic.gui.v2.context.ClickContext;
import com.deyo.hypixel.type.generic.gui.v2.context.RawClickContext;
import com.deyo.hypixel.type.generic.gui.v2.context.ViewContext;

public interface View<S> {

    ViewConfiguration<S> configuration();

    void layout(ViewLayout<S> layout, S state, ViewContext ctx);

    default void onOpen(S state, ViewContext ctx) {
    }

    default void onClose(S state, ViewContext ctx, ViewSession.CloseReason reason) {
    }

    default boolean onClick(ClickContext<S> click, ViewContext ctx) {
        return false;
    }

    default void onRefresh(S state, ViewContext ctx) {
    }

    default boolean onBottomClick(ClickContext<S> click, ViewContext ctx) {
        return false;
    }

    default boolean onRawClick(RawClickContext<S> click, ViewContext ctx) {
        return false;
    }
}
