package ru.quard0r.storageTest.inventories;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import ru.zwanter.utils.bukkit.inventory.PluginInventory;

import java.util.ArrayList;
import java.util.List;

public class Storage extends PluginInventory {

    private final List<ItemStack> storageItems;

    private int page;

    private String storageName;

    public Storage(@NotNull Player player, JavaPlugin plugin, List<ItemStack> storageItems, String storageName, int page) {
        super(player, plugin);
        this.storageItems = storageItems;
        this.storageName = storageName;
        this.page = page;
    }

    @Override
    public @NotNull Inventory getInventory() {
        List<List<ItemStack>> items = divideList(storageItems, 53);

        if (page > items.size() || page < 1) {
            page = 1;
        }

        int maxPages = items.size();

        String name = storageName.replace("{page}", String.valueOf(page)).replace("{max_pages}", String.valueOf(maxPages));

        Inventory inventory = getEmptyInventory(name);

        if (!items.isEmpty()) {
            List<ItemStack> itemStacks = items.get(page-1);
            for (int i = 0; i <= 44; i++) {
                if (inventory.getItem(i) == null) {
                    if (i < itemStacks.size()) {
                        inventory.setItem(i, itemStacks.get(i));
                    }
                }
            }
            for (int i = 46; i <= 52; i++) {
                if (inventory.getItem(i) == null) {
                    if (i < itemStacks.size()) {
                        inventory.setItem(i, itemStacks.get(i));
                    }
                }
            }
        }
        return inventory;
    }

    @Override
    public void whenClicked(InventoryClickEvent event) {
        ItemStack item = event.getCurrentItem();
        if (item == null) {
            return;
        }

        event.setCancelled(true);

        if (event.getSlot() == 45) {
            new Storage(player, plugin, storageItems, storageName, page - 1).open();
        }
        if (event.getSlot() == 53) {
            new Storage(player, plugin, storageItems, storageName, page + 1).open();
        }
    }

    private List<List<ItemStack>> divideList(List<ItemStack> itemList, int partitionSize) {
        List<List<ItemStack>> dividedList = new ArrayList<>();
        for (int i = 0; i < itemList.size(); i += partitionSize) {
            int end = Math.min(itemList.size(), i + partitionSize);
            dividedList.add(new ArrayList<>(itemList.subList(i, end)));
        }

        return dividedList;
    }

    private Inventory getEmptyInventory(String storageName) {
        Inventory inventory = Bukkit.createInventory(this, 54, storageName);

        ItemStack item = new ItemStack(Material.ARROW);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName("§fНазад");
        item.setItemMeta(itemMeta);
        inventory.setItem(45, item);

        itemMeta = item.getItemMeta();
        itemMeta.setDisplayName("§fВперёд");
        item.setItemMeta(itemMeta);

        inventory.setItem(53, item);

        return inventory;
    }
}
