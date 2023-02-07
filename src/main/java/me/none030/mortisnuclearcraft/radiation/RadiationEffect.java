package me.none030.mortisnuclearcraft.radiation;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

public class RadiationEffect {

    private final RadiationManager radiationManager;
    private final PotionEffect effect;
    private final double radiation;

    public RadiationEffect(RadiationManager radiationManager, PotionEffect effect, double radiation) {
        this.radiationManager = radiationManager;
        this.effect = effect;
        this.radiation = radiation;
    }

    public boolean hasAbove(Player player) {
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
