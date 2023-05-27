package me.none030.mortisnuclearcraft.utils.addons;

import me.none030.mortisnuclearcraft.nuclearcraft.radiatiton.RadiationManager;
import me.none030.mortisnuclearcraft.utils.radiation.RadiationType;
import org.bukkit.entity.Player;

public class Drink {

    private final String id;
    private final Equation equation;
    private final int alcohol;
    private final RadiationType type;
    private final double rad;

    public Drink(String id, Equation equation, int alcohol, RadiationType type, double rad) {
        this.id = id;
        this.equation = equation;
        this.alcohol = alcohol;
        this.type = type;
        this.rad = rad;
    }

    public boolean isEquated(int alcohol) {
        if (equation.equals(Equation.EQUAL)) {
            return alcohol == this.alcohol;
        }
        if (equation.equals(Equation.MORE)) {
            return alcohol > this.alcohol;
        }
        if (equation.equals(Equation.LESS)) {
            return alcohol < this.alcohol;
        }
        if (equation.equals(Equation.EQUAL_OR_MORE)) {
            return alcohol >= this.alcohol;
        }
        if (equation.equals(Equation.EQUAL_OR_LESS)) {
            return alcohol <= this.alcohol;
        }
        return false;
    }

    public void changeRadiation(RadiationManager radiationManager, Player player) {
        if (type.equals(RadiationType.INCREASE)) {
            radiationManager.addRadiation(player, rad);
        } else {
            radiationManager.removeRadiation(player, rad);
        }
    }

    public String getId() {
        return id;
    }

    public Equation getEquation() {
        return equation;
    }

    public int getAlcohol() {
        return alcohol;
    }

    public RadiationType getType() {
        return type;
    }

    public double getRadiation() {
        return rad;
    }
}
