package com.mortisdevelopment.mortisnuclearcraft.utils.randomizer;

import lombok.Getter;

import java.util.Collections;
import java.util.List;

@Getter
public class RandomizerObject<T> extends RandomizerEntry<T> {

    private T object;

    public RandomizerObject(double weight, T object) {
        super(weight);
        this.object = object;
    }

    @Override
    public List<T> getObjects() {
        return Collections.singletonList(object);
    }

    @Override
    public List<T> getRandom() {
        return Collections.singletonList(object);
    }

    @Override
    public T getSingleRandom() {
        return object;
    }
}
