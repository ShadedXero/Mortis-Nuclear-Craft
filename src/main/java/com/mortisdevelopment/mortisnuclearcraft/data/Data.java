package com.mortisdevelopment.mortisnuclearcraft.data;

import com.mortisdevelopment.mortisnuclearcraft.utils.NuclearType;
import me.none030.mortishoppers.utils.MessageUtils;
import com.mortisdevelopment.mortisnuclearcraft.MortisNuclearCraft;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

public abstract class Data {

    private final MortisNuclearCraft plugin = MortisNuclearCraft.getInstance();
    private final PersistentDataContainer container;

    public Data(@NotNull PersistentDataContainer container) {
        this.container = container;
    }

    public boolean isType(NuclearType type) {
        String nuclearTypeKey = get("NuclearCraft");
        if (nuclearTypeKey == null) {
            return false;
        }
        NuclearType nuclearType;
        try {
            nuclearType = NuclearType.valueOf(nuclearTypeKey);
        }catch (IllegalArgumentException exp) {
            return false;
        }
        return nuclearType.equals(type);
    }

    public void set(String key, String value) {
        if (key == null) {
            return;
        }
        if (value == null) {
            remove(key);
            return;
        }
        container.set(new NamespacedKey(plugin, key), PersistentDataType.STRING, value);
    }

    public String get(String key) {
        if (key == null) {
            return null;
        }
        return container.get(new NamespacedKey(plugin, key), PersistentDataType.STRING);
    }

    public void remove(String key) {
        if (key == null) {
            return;
        }
        container.remove(new NamespacedKey(plugin, key));
    }

    public void clear() {
        container.getKeys().clear();
    }

    public String serialize(ItemStack item) {
        if (item == null) {
            return null;
        }
        try {
            ByteArrayOutputStream io = new ByteArrayOutputStream();
            BukkitObjectOutputStream os = new BukkitObjectOutputStream(io);
            os.writeObject(item);
            os.flush();
            return new String(Base64.getEncoder().encode(io.toByteArray()));
        }catch (IOException exp) {
            return null;
        }
    }

    public ItemStack deserialize(String rawItem) {
        if (rawItem == null) {
            return null;
        }
        try {
            ByteArrayInputStream in = new ByteArrayInputStream(Base64.getDecoder().decode(rawItem));
            BukkitObjectInputStream is = new BukkitObjectInputStream(in);
            return (ItemStack) is.readObject();
        }catch (IOException | ClassNotFoundException exp) {
            return null;
        }
    }

    public static String serialize(@NotNull Inventory inventory) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeInt(inventory.getSize());
            for (int i = 0; i < inventory.getSize(); i++) {
                dataOutput.writeObject(inventory.getItem(i));
            }
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return null;
    }

    public static Inventory deserialize(@NotNull String data, InventoryHolder holder, String title) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            Inventory inventory;
            if (title != null) {
                inventory = Bukkit.getServer().createInventory(holder, dataInput.readInt(), MessageUtils.color(title));
            }else {
                inventory = Bukkit.getServer().createInventory(holder, dataInput.readInt());
            }
            for (int i = 0; i < inventory.getSize(); i++) {
                inventory.setItem(i, (ItemStack) dataInput.readObject());
            }
            dataInput.close();
            return inventory;
        } catch (ClassNotFoundException | IOException exp) {
            exp.printStackTrace();
        }
        return null;
    }

    public PersistentDataContainer getContainer() {
        return container;
    }
}
