package me.none030.mortisnuclearcraft.addons;

import com.dre.brewery.Brew;
import com.dre.brewery.api.events.brew.BrewDrinkEvent;
import me.none030.mortisnuclearcraft.MortisNuclearCraft;
import me.none030.mortisnuclearcraft.utils.addons.Drink;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

public class BreweryAddon implements Listener {

    private final boolean enabled;
    private final List<Drink> drinks;

    public BreweryAddon(boolean enabled) {
        this.enabled = enabled;
        this.drinks = new ArrayList<>();
    }

    public boolean isEnabled() {
        return enabled;
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
            drink.changeRadiation(player);
        }
    }
}
