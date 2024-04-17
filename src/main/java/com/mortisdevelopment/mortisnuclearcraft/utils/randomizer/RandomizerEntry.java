package com.mortisdevelopment.mortisnuclearcraft.utils.randomizer;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public abstract class RandomizerEntry<T> implements Cloneable {

    private final double weight;
    private double accumulatedWeight;

    public RandomizerEntry(double weight) {
        this.weight = weight;
    }

    public abstract List<T> getObjects();

    public abstract List<T> getRandom();

    public abstract T getSingleRandom();

    @Override
    @SuppressWarnings("unchecked")
    public RandomizerEntry<T> clone() {
        try {
            return (RandomizerEntry<T>) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
