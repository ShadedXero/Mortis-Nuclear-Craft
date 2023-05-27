package me.none030.mortisnuclearcraft.nuclearcraft.centrifuge;

import me.none030.mortisnuclearcraft.utils.Fuel;
import me.none030.mortisnuclearcraft.utils.Randomizer;
import org.bukkit.inventory.ItemStack;

public class CentrifugeRecipe {

    private final String id;
    private final ItemStack input1;
    private final ItemStack input2;
    private final Randomizer<ItemStack> output1;
    private final Randomizer<ItemStack> output2;
    private final long duration;
    private final int fuelPower;

    public CentrifugeRecipe(String id, ItemStack input1, ItemStack input2, Randomizer<ItemStack> output1, Randomizer<ItemStack> output2, long duration, int fuelPower) {
        this.id = id;
        this.input1 = input1;
        this.input2 = input2;
        this.output1 = output1;
        this.output2 = output2;
        this.duration = duration;
        this.fuelPower = fuelPower;
    }

    public boolean isRecipe(ItemStack input1, ItemStack input2) {
        if ((this.input1 != null && input1 == null) || (this.input2 != null && input2 == null)) {
            return false;
        }
        if ((this.input1 != null && !this.input1.isSimilar(input1)) || (this.input2 !=null && !this.input2.isSimilar(input2))) {
            return false;
        }
        return (this.input1 != null && input1.getAmount() >= this.input1.getAmount()) && (this.input2 != null && input2.getAmount() >= this.input2.getAmount());
    }

    public boolean hasEnoughFuel(int power) {
        return power >= fuelPower;
    }

    public void endProcess(CentrifugeData data) {
        data.setOutput1(getOutput1());
        data.setOutput2(getOutput2());
        data.setProcess(null);
        data.setTimer(-1);
    }

    public void process(CentrifugeManager centrifugeManager, CentrifugeData data) {
        if (!isRecipe(data.getInput1(), data.getInput2())) {
            return;
        }
        int power = centrifugeManager.getCentrifuge().getFuelPower(data.getFuel());
        if (!hasEnoughFuel(power)) {
            return;
        }
        if (input1 != null) {
            int input1Amount = Math.max(data.getInput1().getAmount(), this.input1.getAmount()) - Math.min(data.getInput1().getAmount(), this.input1.getAmount());
            if (input1Amount <= 0) {
                data.setInput1(null);
            } else {
                ItemStack input1 = data.getInput1();
                input1.setAmount(input1Amount);
                data.setInput1(input1);
            }
        }
        if (input2 != null) {
            int input2Amount = Math.max(data.getInput2().getAmount(), this.input2.getAmount()) - Math.min(data.getInput2().getAmount(), this.input2.getAmount());
            if (input2Amount <= 0) {
                data.setInput2(null);
            } else {
                ItemStack input2 = data.getInput2();
                input2.setAmount(input2Amount);
                data.setInput2(input2);
            }
        }
        Fuel fuelItem = centrifugeManager.getCentrifuge().getFuel(data.getFuel());
        int fuelAmount = Math.max(fuelItem.getAmount(fuelPower), data.getFuel().getAmount()) - Math.min(fuelItem.getAmount(fuelPower), data.getFuel().getAmount());
        if (fuelAmount <= 0) {
            data.setFuel(null);
        }else {
            ItemStack fuel = data.getFuel();
            fuel.setAmount(fuelAmount);
            data.setFuel(fuel);
        }
        data.setProcess(id);
        data.setTimer(duration);
    }

    public ItemStack getOutput1() {
        ItemStack item = output1.getRandom();
        if (item == null) {
            return null;
        }
        return item.clone();
    }

    public ItemStack getOutput2() {
        ItemStack item = output2.getRandom();
        if (item == null) {
            return null;
        }
        return item.clone();
    }


    public String getId() {
        return id;
    }

    public ItemStack getInput1() {
        return input1;
    }

    public ItemStack getInput2() {
        return input2;
    }

    public long getDuration() {
        return duration;
    }

    public int getFuelPower() {
        return fuelPower;
    }
}
