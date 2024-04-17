package com.mortisdevelopment.mortisnuclearcraft.utils.randomizer;

import lombok.Getter;

import java.util.List;

@Getter
public class RandomizerSelf<T> extends RandomizerEntry<T> {

    private Randomizer<T> randomizer;

    public RandomizerSelf(double weight, Randomizer<T> randomizer) {
        super(weight);
        this.randomizer = randomizer;
    }

    @Override
    public List<T> getObjects() {
        return randomizer.getObjects();
    }

    @Override
    public List<T> getRandom() {
        return randomizer.getRandom();
    }

    @Override
    public T getSingleRandom() {
        return randomizer.getSingleRandom();
    }

    @Override
    public RandomizerSelf<T> clone() {
        RandomizerSelf<T> clone = (RandomizerSelf<T>) super.clone();
        clone.randomizer = randomizer.clone();
        return clone;
    }
}
