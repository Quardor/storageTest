package ru.quard0r.storageTest;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ItemsMap {

    private final HashMap<UUID, List<ItemStack>> items;

    public ItemsMap() {
        items = new HashMap<>();
        fill();
    }

    public void fill() {
        items.putAll(StorageTest.getDatabase().getAllPlayerItems());
    }

    public void add(UUID uuid, ItemStack item) {
        items.computeIfAbsent(uuid, k -> new ArrayList<>()).add(item);
        StorageTest.getDatabase().addItem(uuid, item);
    }

    public List<ItemStack> getItems(UUID uuid) {
        return items.containsKey(uuid) ? items.get(uuid) : new ArrayList<>();
    }

    public List<ItemStack> getAll() {
        return items.values().stream().flatMap(List::stream).collect(Collectors.toList());
    }
}
