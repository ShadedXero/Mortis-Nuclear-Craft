package com.mortisdevelopment.mortisnuclearcraft.nuclearcraft.addons;

import com.dre.brewery.Brew;
import com.dre.brewery.api.events.brew.BrewDrinkEvent;
import com.mortisdevelopment.mortisnuclearcraft.MortisNuclearCraft;
import com.mortisdevelopment.mortisnuclearcraft.utils.addons.Drink;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

public class BreweryManager implements Listener {

    private final AddonManager addonManager;
    private final List<Drink> drinks;

    public BreweryManager(AddonManager addonManager) {
        this.addonManager = addonManager;
        this.drinks = new ArrayList<>();
        MortisNuclearCraft plugin = MortisNuclearCraft.getInstance();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public List<Drink> getDrinks() {
        return drinks;
    }

    @EventHandler
    public void onBrewDrink(BrewDrinkEvent e) {
        Player player = e.getPlayer();
        Brew brew = e.getBrew();
        for (Drink drink : drinks) {
            if (!drink.isEquated(brew.getOrCalcAlc())) {
                continue;
            }
            drink.changeRadiation(addonManager.getRadiationManager(), player);
        }
    }
}
