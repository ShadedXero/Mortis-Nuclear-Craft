package me.none030.mortisnuclearcraft.nuclearcraft.radiatiton;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

public class RadiationEffect {

    private final PotionEffect effect;
    private final double radiation;

    public RadiationEffect(PotionEffect effect, double radiation) {
        this.effect = effect;
        this.radiation = radiation;
    }

    public boolean hasAbove(RadiationManager radiationManager, Player player) {
        return radiationManager.getRadiation(player) >= radiation;
    }

    public void applyEffect(Player player) {
        player.addPotionEffect(effect);
    }

    public void removeEffect(Player player) {
        player.removePotionEffect(effect.getType());
    }

    public PotionEffect getEffect() {
        return effect;
    }

    public double getRadiation() {
        return radiation;
    }
}
