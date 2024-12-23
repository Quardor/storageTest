package ru.quard0r.storageTest;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import ru.quard0r.storageTest.commands.GlobalStorage;
import ru.quard0r.storageTest.database.StorageDatabase;
import ru.zwanter.utils.bukkit.inventory.GuiListener;

public final class StorageTest extends JavaPlugin {

    @Getter
    private static StorageTest instance;
    @Getter
    private static StorageDatabase database;
    @Getter
    private static ItemsMap items;

    @Override
    public void onEnable() {
        instance = this;
        database = new StorageDatabase(instance, "database");
        items = new ItemsMap();

        GuiListener.init(this);
        GlobalStorage.init(this);
    }


    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
