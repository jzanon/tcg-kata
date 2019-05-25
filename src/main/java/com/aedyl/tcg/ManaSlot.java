package com.aedyl.tcg;

import java.util.Objects;

public class ManaSlot {

    private static final int MAX_MANA = 10;
    public int current;
    public int max;

    public ManaSlot(int current, int max) {
        this.current = current;
        this.max = max;
    }

    public ManaSlot copy() {
        return new ManaSlot(current, max);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ManaSlot manaSlot = (ManaSlot) o;
        return current == manaSlot.current &&
                max == manaSlot.max;
    }

    @Override
    public int hashCode() {
        return Objects.hash(current, max);
    }

    @Override
    public String toString() {
        return "ManaSlot(" +
                current +
                "/" + max +
                ')';
    }

    public void increaseMax() {
        if (this.max < MAX_MANA) {
            this.max++;
        }
    }

    public void refill() {
        this.current = this.max;
    }
}
