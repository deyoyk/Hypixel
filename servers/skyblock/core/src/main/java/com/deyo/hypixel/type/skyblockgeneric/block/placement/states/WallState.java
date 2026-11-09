package com.deyo.hypixel.type.skyblockgeneric.block.placement.states;

import net.minestom.server.instance.block.Block;
import com.deyo.hypixel.type.skyblockgeneric.block.placement.states.state.BooleanState;
import com.deyo.hypixel.type.skyblockgeneric.block.placement.states.state.State;
import com.deyo.hypixel.type.skyblockgeneric.block.placement.states.state.WallHeight;
import org.jetbrains.annotations.NotNull;

public class WallState extends WaterloggedState {
    private final BooleanState up = BooleanState.Of("up", false);
    private final HeightState north = new HeightState("north");
    private final HeightState south = new HeightState("south");
    private final HeightState east = new HeightState("east");
    private final HeightState west = new HeightState("west");

    public WallState(Block alternative) {
        super(alternative);
    }

    public boolean getUp() {
        return get(up);
    }

    public void setUp(boolean raised) {
        up.setValue(raised);
        set(up);
    }

    public WallHeight getNorth() {
        return get(north);
    }

    public void setNorth(WallHeight height) {
        north.value = height;
        set(north);
    }

    public WallHeight getSouth() {
        return get(south);
    }

    public void setSouth(WallHeight height) {
        south.value = height;
        set(south);
    }

    public WallHeight getEast() {
        return get(east);
    }

    public void setEast(WallHeight height) {
        east.value = height;
        set(east);
    }

    public WallHeight getWest() {
        return get(west);
    }

    public void setWest(WallHeight height) {
        west.value = height;
        set(west);
    }

    @NotNull
    @Override
    public WallState clone() {
        return new WallState(block());
    }

    private static final class HeightState implements State<WallHeight> {
        private final String key;
        private WallHeight value = WallHeight.NONE;

        private HeightState(String key) {
            this.key = key;
        }

        @Override
        public @NotNull String getKey() {
            return key;
        }

        @Override
        public @NotNull String getValue() {
            return value.name().toLowerCase();
        }

        @Override
        public @NotNull WallHeight parse(String rawValue) {
            if (rawValue == null) return WallHeight.NONE;
            for (WallHeight height : WallHeight.values()) {
                if (height.name().equalsIgnoreCase(rawValue)) {
                    return height;
                }
            }
            return WallHeight.NONE;
        }
    }
}
