package com.mortisdevelopment.mortisnuclearcraft.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Deprecated
public class Randomizer<T> {

    private class Entry {
        double accumulatedWeight;
        T object;
    }

    private final List<T> confirmedObjects = new ArrayList<>();
    private final List<Entry> entries = new ArrayList<>();
    private double accumulatedWeight;
    private final Random random = new Random();

    public void addEntry(T object, double weight) {
        if (weight == -1) {
            confirmedObjects.add(object);
            return;
        }
        accumulatedWeight += weight;
        Entry e = new Entry();
        e.object = object;
        e.accumulatedWeight = accumulatedWeight;
        entries.add(e);
    }

    public List<T> getRandom() {
        List<T> objects = new ArrayList<>(confirmedObjects);
        double r = random.nextDouble() * accumulatedWeight;

        for (Entry entry: entries) {
            if (entry.accumulatedWeight >= r) {
                T object = entry.object;
                if (object != null) {
                    objects.add(entry.object);
                }
                return objects;
            }
        }
        return null; //should only happen when there are no entries
    }

    public int getSize() {
        return entries.size();
    }
}
