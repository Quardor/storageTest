package ru.quard0r.storageTest.database;

import com.google.gson.JsonParser;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import ru.zwanter.utils.bukkit.GsonBukkit;

import java.sql.*;
import java.util.*;

public class StorageDatabase {

    private final Connection connection;

    public StorageDatabase(JavaPlugin plugin, String databaseName) {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + plugin.getDataFolder() + "\\" + databaseName + ".db");
            Statement statement = connection.createStatement();

            statement.execute("CREATE TABLE IF NOT EXISTS players (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "uuid TEXT NOT NULL UNIQUE);");

            statement.execute("CREATE TABLE IF NOT EXISTS items (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "item TEXT NOT NULL, " +
                    "playerId INTEGER NOT NULL, " +
                    "FOREIGN KEY(playerId) REFERENCES players(id));");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addItem(UUID uuid, ItemStack item) {
        try (PreparedStatement ps = connection.prepareStatement("SELECT id FROM players WHERE uuid = ?;")) {
            ps.setString(1, uuid.toString());

            int playerId;
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    playerId = rs.getInt("id");
                } else {
                    try (PreparedStatement insertPlayer = connection.prepareStatement(
                            "INSERT INTO players (uuid) VALUES (?);",
                            Statement.RETURN_GENERATED_KEYS)) {
                        insertPlayer.setString(1, uuid.toString());
                        insertPlayer.executeUpdate();

                        try (ResultSet generatedKeys = insertPlayer.getGeneratedKeys()) {
                            if (generatedKeys.next()) {
                                playerId = generatedKeys.getInt(1);
                            } else {
                                throw new SQLException("Failed to retrieve player ID.");
                            }
                        }
                    }
                }
            }

            try (PreparedStatement ps2 = connection.prepareStatement("INSERT INTO items (item, playerId) VALUES (?, ?);")) {
                ps2.setString(1, GsonBukkit.itemToJson(item).toString());
                ps2.setInt(2, playerId);
                ps2.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Map<UUID, List<ItemStack>> getAllPlayerItems() {
        Map<UUID, List<ItemStack>> playerItemsMap = new HashMap<>();

        String query = "SELECT players.uuid, items.item FROM items " +
                "JOIN players ON items.playerId = players.id;";

        try (PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                UUID playerUuid = UUID.fromString(rs.getString("uuid"));
                String itemJson = rs.getString("item");
                ItemStack item = GsonBukkit.jsonToItem(JsonParser.parseString(itemJson));

                playerItemsMap.computeIfAbsent(playerUuid, k -> new ArrayList<>()).add(item);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return playerItemsMap;
    }
}