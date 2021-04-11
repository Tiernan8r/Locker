package me.Tiernanator.Locker;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import me.Tiernanator.Utilities.Blocks.MultiBlocks;
import me.Tiernanator.Utilities.Players.GetPlayer;
import me.Tiernanator.Utilities.Players.PlayerLogger;
import me.Tiernanator.Utilities.SQL.SQLServer;

public class LockedBlock {

	private static LockerMain plugin;

	public static void setPlugin(LockerMain main) {
		plugin = main;
	}

	@SuppressWarnings("deprecation")
	public static boolean isLockableBlock(Block block) {

		block = MultiBlocks.getCorrectBlock(block);

		if (block.equals(null) || block == null || block.isEmpty()) {
			return false;
		}

		Material blockMaterial = block.getType();
		switch (blockMaterial) {
			case ACACIA_DOOR :
				return true;
			case ACACIA_FENCE_GATE :
				return true;
			case ANVIL :
				return true;
			case ARMOR_STAND :
				return true;
			case BEACON :
				return true;
			case LEGACY_BED_BLOCK :
				return true;
			case BIRCH_DOOR :
				return true;
			case BIRCH_FENCE_GATE :
				return true;
			case BREWING_STAND :
				return true;
			case LEGACY_CAKE_BLOCK :
				return true;
			case CHEST :
				return true;
			case LEGACY_COMMAND :
				return true;
			case LEGACY_COMMAND_CHAIN :
				return true;
			case LEGACY_COMMAND_MINECART :
				return true;
			case LEGACY_COMMAND_REPEATING :
				return true;
			case DARK_OAK_DOOR :
				return true;
			case DARK_OAK_FENCE_GATE :
				return true;
			case DAYLIGHT_DETECTOR :
				return true;
			case LEGACY_DAYLIGHT_DETECTOR_INVERTED :
				return true;
			case LEGACY_DIODE_BLOCK_OFF :
				return true;
			case LEGACY_DIODE_BLOCK_ON :
				return true;
			case DISPENSER :
				return true;
			case DRAGON_EGG :
				return true;
			case DROPPER :
				return true;
			case LEGACY_ENCHANTMENT_TABLE :
				return true;
			case ENDER_CHEST :
				return true;
			case LEGACY_FENCE_GATE :
				return true;
			case FURNACE :
				return true;
			case HOPPER :
				return true;
			case HOPPER_MINECART :
				return true;
			case ITEM_FRAME :
				return true;
			case JUKEBOX :
				return true;
			case JUNGLE_DOOR :
				return true;
			case JUNGLE_FENCE_GATE :
				return true;
			case LEVER :
				return true;
			case NOTE_BLOCK :
				return true;
			case LEGACY_REDSTONE_COMPARATOR :
				return true;
			case LEGACY_REDSTONE_COMPARATOR_OFF :
				return true;
			case LEGACY_REDSTONE_COMPARATOR_ON :
				return true;
			case SPRUCE_DOOR :
				return true;
			case SPRUCE_FENCE_GATE :
				return true;
			case STONE_BUTTON :
				return true;
			case LEGACY_STONE_PLATE :
				return true;
			case STRUCTURE_BLOCK :
				return true;
			case TRAPPED_CHEST :
				return true;
			case LEGACY_TRAP_DOOR :
				return true;
			case LEGACY_WOOD_BUTTON :
				return true;
			case LEGACY_WOODEN_DOOR :
				return true;
			case LEGACY_WOOD_PLATE :
				return true;
			default :
				return false;

		}

	}

	public static int getBlockIndex(Block block) {

		block = MultiBlocks.getCorrectBlock(block);

		if (!isLockableBlock(block)) {
			return -1;
		}

		if (!blockIsLocked(block)) {
			return -1;
		}
		Location location = block.getLocation();
		int x = location.getBlockX();
		int y = location.getBlockY();
		int z = location.getBlockZ();
		String world = location.getWorld().getName();

		String query = "SELECT ID FROM Blocks WHERE " + "World = '" + world
				+ "' AND " + "X = '" + x + "' AND " + "Y = '" + y + "' AND "
				+ "Z = '" + z + "';";
		int index = SQLServer.getInt(query, "ID");

		return index;
	}

	public static boolean blockIsLocked(Block block) {

		block = MultiBlocks.getCorrectBlock(block);

		if (!isLockableBlock(block)) {
			return false;
		}
		Location blockLocation = block.getLocation();

		int x = blockLocation.getBlockX();
		int y = blockLocation.getBlockY();
		int z = blockLocation.getBlockZ();
		String world = blockLocation.getWorld().getName();

		String query = "SELECT * FROM Blocks WHERE " + "World = '" + world
				+ "' AND " + "X = '" + x + "' AND " + "Y = '" + y + "' AND "
				+ "Z = '" + z + "';";

		Location storedLocation = SQLServer.getLocation(query);

		if (storedLocation == null) {
			return false;
		}

		return storedLocation.equals(blockLocation);
	}

	public static boolean canBlockInteract(Block block, Player player) {

		block = MultiBlocks.getCorrectBlock(block);

		if (!isLockableBlock(block)) {
			return true;
		}
		Location blockLocation = block.getLocation();
		int x = blockLocation.getBlockX();
		int y = blockLocation.getBlockY();
		int z = blockLocation.getBlockZ();
		String world = blockLocation.getWorld().getName();

		String query = "SELECT Players FROM Blocks WHERE " + "World = '" + world
				+ "' AND " + "X = '" + x + "' AND " + "Y = '" + y + "' AND "
				+ "Z = '" + z + "';";

		String playersString = SQLServer.getString(query, "Players");

		String[] UUIDs = playersString.split(", ");

		List<String> allowedPlayers = new ArrayList<String>();
		for (String UUID : UUIDs) {
			OfflinePlayer offlinePlayer = GetPlayer
					.getOfflinePlayerByUUID(UUID);
			if (offlinePlayer != null) {
				allowedPlayers.add(offlinePlayer.getName());
			}
		}
		return allowedPlayers.contains(player.getName());
	}

	public static void addLockedBlock(int x, int y, int z, String world,
			List<String> playersUUIDs) {

		String playerString = "";
		for (int i = 0; i < playersUUIDs.size(); i++) {
			playerString += playersUUIDs.get(i);
			if (!(i + 1 >= playersUUIDs.size())) {
				playerString += ", ";
			}
		}

		String statement = "INSERT INTO Blocks (World, X, Y, Z, Players) VALUES (?, ?, ?, ?, ?);";
		Object[] values = new Object[]{world, x, y, z, playerString};
		SQLServer.executePreparedStatement(statement, values);

	}

	public static void addLockedBlock(Block block, List<String> playersUUIDs) {
		Location location = block.getLocation();
		addLockedBlock(location.getBlockX(), location.getBlockY(),
				location.getBlockZ(), location.getWorld().getName(),
				playersUUIDs);
	}

	public static void addLockedBlock(Location location,
			List<String> playersUUIDs) {
		addLockedBlock(location.getBlockX(), location.getBlockY(),
				location.getBlockZ(), location.getWorld().getName(),
				playersUUIDs);
	}

	public static List<String> getAllowedPlayers(Block block) {

		List<String> playerUUIDs = getAllowedPlayersUUIDs(block);
		if (playerUUIDs == null) {
			return null;
		}
		List<String> playerNames = new ArrayList<String>();
		for (String uuid : playerUUIDs) {
			String playerName = PlayerLogger.getPlayerNameByUUID(uuid);
			playerNames.add(playerName);
		}
		return playerNames;

	}

	public static List<String> getAllowedPlayersUUIDs(Block block) {

		block = MultiBlocks.getCorrectBlock(block);

		if (!isLockableBlock(block)) {
			return null;
		}
		Location blockLocation = block.getLocation();
		int x = blockLocation.getBlockX();
		int y = blockLocation.getBlockY();
		int z = blockLocation.getBlockZ();
		String world = blockLocation.getWorld().getName();

		String query = "SELECT Players FROM Blocks WHERE " + "World = '" + world
				+ "' AND " + "X = '" + x + "' AND " + "Y = '" + y + "' AND "
				+ "Z = '" + z + "';";

		String playersString = SQLServer.getString(query, "Players");

		String[] playerUUIDs = playersString.split(", ");
		List<String> allowedPlayers = new ArrayList<String>();
		List<String> offlinePlayerUUIDs = new ArrayList<String>();
		for (OfflinePlayer offlinePlayer : Bukkit.getServer()
				.getOfflinePlayers()) {
			offlinePlayerUUIDs.add(offlinePlayer.getUniqueId().toString());
		}
		for (String playerUUID : playerUUIDs) {
			if (offlinePlayerUUIDs.contains(playerUUID)) {
				allowedPlayers.add(playerUUID);
			}
		}

		return allowedPlayers;
	}

	public static void setAllowedPlayers(Block block,
			List<String> allowedPlayersUUIDs) {

		block = MultiBlocks.getCorrectBlock(block);
		Block correctBlock = block;

		if (!isLockableBlock(block)) {
			return;
		}

		String allowedPlayersUUIDsString = "";
		for (int i = 0; i < allowedPlayersUUIDs.size(); i++) {
			allowedPlayersUUIDsString += allowedPlayersUUIDs.get(i);
			if (!(i + 1 >= allowedPlayersUUIDs.size())) {
				allowedPlayersUUIDsString += ", ";
			}
		}

		Location blockLocation = correctBlock.getLocation();
		int x = blockLocation.getBlockX();
		int y = blockLocation.getBlockY();
		int z = blockLocation.getBlockZ();
		String world = blockLocation.getWorld().getName();

		String statement = "UPDATE Blocks "
				+ "SET Players = ? WHERE World = ? AND X = ? AND Y = ? AND Z = ?;";
		Object[] values = new Object[]{allowedPlayersUUIDsString, world, x, y,
				z};

		SQLServer.executePreparedStatement(statement, values);

	}

	public static void removeAllowedPlayers(Block block,
			List<String> playersUUIDsToRemove) {

		block = MultiBlocks.getCorrectBlock(block);

		List<String> currentAllowedPlayers = new ArrayList<String>();
		currentAllowedPlayers = getAllowedPlayers(block);

		currentAllowedPlayers.removeAll(playersUUIDsToRemove);
		setAllowedPlayers(block, currentAllowedPlayers);
	}

	public static void removeLockedBlock(int x, int y, int z, String world) {

		World blockWorld = plugin.getServer().getWorld(world);
		Location location = new Location(blockWorld, x, y, z);
		Block block = location.getBlock();
		if (!blockIsLocked(block)) {
			return;
		}

		String statement = "DELETE FROM Blocks WHERE WORLD = ? AND X = ? AND Y = ? AND Z = ?;";
		Object[] values = new Object[] {world, x, y, z};
		
		SQLServer.executePreparedStatement(statement, values);
		
	}

	public static void removeLockedBlock(Location location) {

		removeLockedBlock(location.getBlockX(), location.getBlockY(),
				location.getBlockZ(), location.getWorld().getName());

	}

	public static void removeLockedBlock(Block block) {

		Location location = block.getLocation();

		removeLockedBlock(location.getBlockX(), location.getBlockY(),
				location.getBlockZ(), location.getWorld().getName());

	}

}
