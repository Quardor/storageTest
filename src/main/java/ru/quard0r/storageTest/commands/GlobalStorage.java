package ru.quard0r.storageTest.commands;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import ru.quard0r.storageTest.StorageTest;
import ru.quard0r.storageTest.inventories.AllStorage;
import ru.quard0r.storageTest.inventories.PlayerStorage;
import wf.utils.bukkit.command.handler.CommandHandler;
import wf.utils.bukkit.command.handler.ExecutionCommand;
import wf.utils.bukkit.command.subcommand.SubCommand;
import wf.utils.bukkit.command.subcommand.executor.Argument;
import wf.utils.bukkit.command.subcommand.executor.types.bukkit.BukkitArgumentType;

import java.util.ArrayList;
import java.util.List;

public class GlobalStorage {
    public static void init(JavaPlugin plugin) {
        CommandHandler command =CommandHandler.builder()
                .setCommands("storage")
                .setPlugin(plugin)
                .build();

        command.addSubcommand(SubCommand.builder()
                .setRunnable(GlobalStorage::openAll)
                .setCommand("open")
                .setOnlyPlayer(true)
                .build());

        command.addSubcommand(SubCommand.builder()
                .setRunnable(GlobalStorage::save)
                .setCommand("save")
                .setOnlyPlayer(true)
                .build());

        command.addSubcommand(SubCommand.builder()
                .setRunnable(GlobalStorage::openPlayer)
                .setCommand("player")
                .setOnlyPlayer(true)
                .setArguments(new Argument("player", BukkitArgumentType.ONLINE_PLAYER))
                .build());

    }

    private static void save(CommandSender commandSender, ExecutionCommand command, Object[] args) {
        Player player = (Player) commandSender;
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType() == Material.AIR) {
            return;
        }
        ItemMeta meta = item.getItemMeta();
        if (meta.lore() == null) {
            List<String> lore = new ArrayList<>();
            lore.add(player.getName());
            meta.setLore(lore);
        }
        item.setItemMeta(meta);
        StorageTest.getItems().add(player.getUniqueId(), item);
        player.getInventory().setItemInMainHand(null);
    }

    private static void openPlayer(CommandSender commandSender, ExecutionCommand command, Object[] args) {
        Player player = (Player) commandSender;
        Player target = (Player) args[0];
        new PlayerStorage(player, target, StorageTest.getInstance(), 0).open();
    }

    private static void openAll(CommandSender commandSender, ExecutionCommand command, Object[] args) {
        Player player = (Player) commandSender;
        new AllStorage(player, StorageTest.getInstance(), 0).open();
    }
}
