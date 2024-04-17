package com.mortisdevelopment.mortisnuclearcraft.utils.randomizer;

import com.mortisdevelopment.mortisnuclearcraft.config.ConfigManager;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public class Randomizer<T> implements Cloneable {

    private final Random random = new Random();
    private List<RandomizerEntry<T>> entries;
    private List<RandomizerEntry<T>> constantEntries;
    @Setter
    private double accumulatedWeight;
    @Setter
    private int rolls;

    public Randomizer() {
        this.entries = new ArrayList<>();
        this.constantEntries = new ArrayList<>();
        this.accumulatedWeight = 0;
        this.rolls = 1;
    }

    public void addEntry(double weight, RandomizerEntry<T> entry) {
        if (weight >= 100) {
            constantEntries.add(entry);
            return;
        }
        accumulatedWeight += weight;
        entry.setAccumulatedWeight(accumulatedWeight);
        entries.add(entry);
    }

    public void addEntry(double weight, T object) {
        addEntry(weight, new RandomizerObject<>(weight, object));
    }

    public void addEntry(double weight, Randomizer<T> randomizer) {
        addEntry(weight, new RandomizerSelf<>(weight, randomizer));
    }

    public boolean hasConstantEntries() {
        return !constantEntries.isEmpty();
    }

    public List<RandomizerEntry<T>> getAllEntries() {
        List<RandomizerEntry<T>> allEntries = new ArrayList<>(entries);
        if (hasConstantEntries()) {
            allEntries.addAll(constantEntries);
        }
        return allEntries;
    }

    public List<T> getObjects() {
        List<T> objects = new ArrayList<>();
        for (RandomizerEntry<T> entry : getAllEntries()) {
            objects.addAll(entry.getObjects());
        }
        return objects;
    }

    public T getConstantSingleRandom() {
        int index = random.nextInt(0, constantEntries.size());
        RandomizerEntry<T> entry = constantEntries.get(index);
        return entry.getSingleRandom();
    }

    public T getSingleRandom() {
        if (hasConstantEntries()) {
            return getConstantSingleRandom();
        }
        double r = random.nextDouble() * accumulatedWeight;
        for (RandomizerEntry<T> entry : entries) {
            if (entry.getAccumulatedWeight() >= r) {
                return entry.getSingleRandom();
            }
        }
        return null;
    }

    public List<T> getConstantRandom() {
        List<T> objects = new ArrayList<>();
        for (RandomizerEntry<T> entry : constantEntries) {
            objects.addAll(entry.getRandom());
        }
        return objects;
    }

    public List<T> getRandom() {
        if (hasConstantEntries()) {
            return getConstantRandom();
        }
        List<T> objects = new ArrayList<>();
        for (int i = 0; i < rolls; i++) {
            double r = random.nextDouble() * accumulatedWeight;
            for (RandomizerEntry<T> entry : entries) {
                if (entry.getAccumulatedWeight() >= r) {
                    objects.addAll(entry.getRandom());
                    break;
                }
            }
        }
        return objects;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Randomizer<T> clone() {
        try {
            Randomizer<T> clone = (Randomizer<T>) super.clone();
            clone.entries = entries.stream().map(RandomizerEntry::clone).collect(Collectors.toList());
            clone.constantEntries = constantEntries.stream().map(RandomizerEntry::clone).collect(Collectors.toList());
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public static <V> Randomizer<V> getRandomizer(ConfigurationSection randomizerSection, Function<ConfigurationSection, V> manager) {
        Randomizer<V> randomizer = new Randomizer<>();
        if (randomizerSection.contains("rolls")) {
            randomizer.setRolls(randomizerSection.getInt("rolls"));
        }
        Set<String> keys = randomizerSection.getKeys(false);
        keys.remove("rolls");
        for (String key : keys) {
            ConfigurationSection section = Objects.requireNonNull(randomizerSection.getConfigurationSection(key));
            double chance;
            if (section.contains("chance")) {
                chance = section.getDouble("chance");
            }else {
                chance = 100;
            }
            if (section.contains("objects")) {
                Randomizer<V> objectRandomizer = getRandomizer(Objects.requireNonNull(section.getConfigurationSection("objects")), manager);
                randomizer.addEntry(chance, objectRandomizer);
            }else {
                randomizer.addEntry(chance, manager.apply(section));
            }
        }
        return randomizer;
    }

    public static Randomizer<ItemStack> getItemRandomizer(ConfigManager configManager, ConfigurationSection randomizerSection) {
        return getRandomizer(randomizerSection, section -> configManager.getManager().getItemManager().getItem(section.getString("id")));
    }
}
