package ru.quard0r.storageTest.inventories;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import ru.quard0r.storageTest.StorageTest;

public class PlayerStorage extends Storage {
    public PlayerStorage(@NotNull Player player, Player target, JavaPlugin plugin, int page) {
        super(player, plugin, StorageTest.getItems().getItems(target.getUniqueId()), "Хранилище " + target.getName() + " {page}/{max_pages}", page);
    }
}
