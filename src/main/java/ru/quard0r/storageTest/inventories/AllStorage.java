package ru.quard0r.storageTest.inventories;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import ru.quard0r.storageTest.StorageTest;

public class AllStorage extends Storage {

    public AllStorage(@NotNull Player player, JavaPlugin plugin, int page) {
        super(player, plugin, StorageTest.getItems().getAll(), "Хранилище {page}/{max_pages}", page);
    }
}
