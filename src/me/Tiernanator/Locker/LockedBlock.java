package me.Tiernanator.Locker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.Tiernanator.Utilities.Blocks.MultiBlocks;
import me.Tiernanator.Utilities.Players.PlayerLogger;

public class LockedBlock {

	private static Main plugin;

	public static void setPlugin(Main main) {
		plugin = main;
	}

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
			case BED_BLOCK :
				return true;
			case BIRCH_DOOR :
				return true;
			case BIRCH_FENCE_GATE :
				return true;
			case BREWING_STAND :
				return true;
			case CAKE_BLOCK :
				return true;
			case CHEST :
				return true;
			case COMMAND :
				return true;
			case COMMAND_CHAIN :
				return true;
			case COMMAND_MINECART :
				return true;
			case COMMAND_REPEATING :
				return true;
			case DARK_OAK_DOOR :
				return true;
			case DARK_OAK_FENCE_GATE :
				return true;
			case DAYLIGHT_DETECTOR :
				return true;
			case DAYLIGHT_DETECTOR_INVERTED :
				return true;
			case DIODE_BLOCK_OFF :
				return true;
			case DIODE_BLOCK_ON :
				return true;
			case DISPENSER :
				return true;
			case DRAGON_EGG :
				return true;
			case DROPPER :
				return true;
			case ENCHANTMENT_TABLE :
				return true;
			case ENDER_CHEST :
				return true;
			case FENCE_GATE :
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
			case REDSTONE_COMPARATOR :
				return true;
			case REDSTONE_COMPARATOR_OFF :
				return true;
			case REDSTONE_COMPARATOR_ON :
				return true;
			case SPRUCE_DOOR :
				return true;
			case SPRUCE_FENCE_GATE :
				return true;
			case STONE_BUTTON :
				return true;
			case STONE_PLATE :
				return true;
			case STRUCTURE_BLOCK :
				return true;
			case TRAPPED_CHEST :
				return true;
			case TRAP_DOOR :
				return true;
			case WOOD_BUTTON :
				return true;
			case WOODEN_DOOR :
				return true;
			case WOOD_PLATE :
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

		Connection connection = Main.getSQL().getConnection();
		Statement statement = null;
		try {
			statement = connection.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		ResultSet resultSet = null;
		try {
			resultSet = statement.executeQuery(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			if (!resultSet.isBeforeFirst()) {
				return -1;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			resultSet.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		int index = -1;
		try {
			index = resultSet.getInt("ID");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			statement.closeOnCompletion();
			resultSet.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
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

		Connection connection = Main.getSQL().getConnection();
		Statement statement = null;
		try {
			statement = connection.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		ResultSet resultSet = null;
		try {
			resultSet = statement.executeQuery(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if (!resultSet.isBeforeFirst()) {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			resultSet.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		World thisWorld = null;
		int thisX = 0;
		int thisY = 0;
		int thisZ = 0;

		Location thisBlockLocation = null;

		try {
			String worldName = resultSet.getString("World");
			thisWorld = plugin.getServer().getWorld(worldName);

			thisX = resultSet.getInt("X");
			thisY = resultSet.getInt("Y");
			thisZ = resultSet.getInt("Z");

			thisBlockLocation = new Location(thisWorld, thisX, thisY, thisZ);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			statement.closeOnCompletion();
			resultSet.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return thisBlockLocation.equals(blockLocation);
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

		Connection connection = Main.getSQL().getConnection();
		Statement statement = null;
		try {
			statement = connection.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		ResultSet resultSet = null;
		try {
			resultSet = statement.executeQuery(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			if (!resultSet.isBeforeFirst()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			resultSet.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		String playersString = "";
		try {
			playersString = resultSet.getString("Players");
		} catch (SQLException e) {
			e.printStackTrace();
		}

		List<String> allowedPlayers = new ArrayList<String>();
		OfflinePlayer[] allPlayers = plugin.getServer().getOfflinePlayers();
		for (OfflinePlayer offlinePlayer : allPlayers) {
			if (playersString
					.contains(offlinePlayer.getUniqueId().toString())) {
				allowedPlayers.add(offlinePlayer.getName());
			}
		}

		try {
			statement.closeOnCompletion();
			resultSet.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return allowedPlayers.contains(player.getName());
	}

	public static void addLockedBlock(int x, int y, int z, String world,
			List<String> playersUUIDs) {

		BukkitRunnable runnable = new BukkitRunnable() {
			
			@Override
			public void run() {
				
				String playerString = "";
				for (int i = 0; i < playersUUIDs.size(); i++) {
					playerString += playersUUIDs.get(i);
					if (!(i + 1 >= playersUUIDs.size())) {
						playerString += ", ";
					}
				}

				Connection connection = Main.getSQL().getConnection();
				PreparedStatement preparedStatement = null;
				try {
					preparedStatement = connection.prepareStatement(
							"INSERT INTO Blocks (World, X, Y, Z, Players) VALUES (?, ?, ?, ?, ?);");
				} catch (SQLException e) {
					e.printStackTrace();
				}
				try {
					preparedStatement.setString(1, world);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				try {
					preparedStatement.setInt(2, x);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				try {
					preparedStatement.setInt(3, y);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				try {
					preparedStatement.setInt(4, z);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				try {
					preparedStatement.setString(5, playerString);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				try {
					preparedStatement.executeUpdate();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				try {
					preparedStatement.closeOnCompletion();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		};
		runnable.runTaskAsynchronously(plugin);
		
//		String playerString = "";
//		for (int i = 0; i < playersUUIDs.size(); i++) {
//			playerString += playersUUIDs.get(i);
//			if (!(i + 1 >= playersUUIDs.size())) {
//				playerString += ", ";
//			}
//		}
//
//		Connection connection = Main.getSQL().getConnection();
//		PreparedStatement preparedStatement = null;
//		try {
//			preparedStatement = connection.prepareStatement(
//					"INSERT INTO Blocks (World, X, Y, Z, Players) VALUES (?, ?, ?, ?, ?);");
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		try {
//			preparedStatement.setString(1, world);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		try {
//			preparedStatement.setInt(2, x);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		try {
//			preparedStatement.setInt(3, y);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		try {
//			preparedStatement.setInt(4, z);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		try {
//			preparedStatement.setString(5, playerString);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		try {
//			preparedStatement.executeUpdate();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}

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

//		block = MultiBlocks.getCorrectBlock(block);
//
//		if (!isLockableBlock(block)) {
//			return null;
//		}
//		Location blockLocation = block.getLocation();
//		int x = blockLocation.getBlockX();
//		int y = blockLocation.getBlockY();
//		int z = blockLocation.getBlockZ();
//		String world = blockLocation.getWorld().getName();
//
//		String query = "SELECT Players FROM Blocks WHERE " + "World = '" + world
//				+ "' AND " + "X = '" + x + "' AND " + "Y = '" + y + "' AND "
//				+ "Z = '" + z + "';";
//
//		Connection connection = Main.getSQL().getConnection();
//		Statement statement = null;
//		try {
//			statement = connection.createStatement();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		ResultSet resultSet = null;
//		try {
//			resultSet = statement.executeQuery(query);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//
//		try {
//			if (!resultSet.isBeforeFirst()) {
//				return null;
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		try {
//			resultSet.next();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//
//		String playersString = "";
//		try {
//			playersString = resultSet.getString("Players");
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		playersString = playersString.replaceAll(", ", " ");
//
//		try {
//			statement.closeOnCompletion();
//			resultSet.close();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
		List<String> playerUUIDs = getAllowedPlayersUUIDs(block);
		if(playerUUIDs == null) {
			return null;
		}
		List<String> playerNames = new ArrayList<String>();
		PlayerLogger playerLogger = new PlayerLogger();
		for(String uuid : playerUUIDs) {
			String playerName = playerLogger.getPlayerNameByUUID(uuid);
			playerNames.add(playerName);
		}
		return playerNames;
//		List<String> allowedPlayers = new ArrayList<String>();
//		OfflinePlayer[] allPlayers = plugin.getServer().getOfflinePlayers();
//		for (OfflinePlayer offlinePlayer : allPlayers) {
//			if (playersString
//					.contains(offlinePlayer.getUniqueId().toString())) {
//				allowedPlayers.add(offlinePlayer.getName());
//			}
//		}
//		return allowedPlayers;
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

		Connection connection = Main.getSQL().getConnection();
		Statement statement = null;
		try {
			statement = connection.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		ResultSet resultSet = null;
		try {
			resultSet = statement.executeQuery(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			if (!resultSet.isBeforeFirst()) {
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			resultSet.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		String playersString = "";
		try {
			playersString = resultSet.getString("Players");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			statement.closeOnCompletion();
			resultSet.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		playersString = playersString.replaceAll(", ", " ");

		List<String> allowedPlayers = new ArrayList<String>();
		OfflinePlayer[] allPlayers = plugin.getServer().getOfflinePlayers();
		for (OfflinePlayer offlinePlayer : allPlayers) {
			if (playersString
					.contains(offlinePlayer.getUniqueId().toString())) {
				allowedPlayers.add(offlinePlayer.getUniqueId().toString());
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
		
		BukkitRunnable runnable = new BukkitRunnable() {
			
			@Override
			public void run() {
				
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

				String query = "UPDATE Blocks " + "SET Players = '"
						+ allowedPlayersUUIDsString + "'" + " WHERE " + "World = '"
						+ world + "' AND " + "X = '" + x + "' AND " + "Y = '" + y
						+ "' AND " + "Z = '" + z + "';";

				Connection connection = Main.getSQL().getConnection();
				Statement statement = null;
				try {
					statement = connection.createStatement();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				try {
					statement.execute(query);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				try {
					statement.closeOnCompletion();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
			}
		};
		runnable.runTaskAsynchronously(plugin);
		
//		String allowedPlayersUUIDsString = "";
//		for (int i = 0; i < allowedPlayersUUIDs.size(); i++) {
//			allowedPlayersUUIDsString += allowedPlayersUUIDs.get(i);
//			if (!(i + 1 >= allowedPlayersUUIDs.size())) {
//				allowedPlayersUUIDsString += ", ";
//			}
//		}
//
//		Location blockLocation = block.getLocation();
//		int x = blockLocation.getBlockX();
//		int y = blockLocation.getBlockY();
//		int z = blockLocation.getBlockZ();
//		String world = blockLocation.getWorld().getName();
//
//		String query = "UPDATE Blocks " + "SET Players = '"
//				+ allowedPlayersUUIDsString + "'" + " WHERE " + "World = '"
//				+ world + "' AND " + "X = '" + x + "' AND " + "Y = '" + y
//				+ "' AND " + "Z = '" + z + "';";
//
//		Connection connection = Main.getSQL().getConnection();
//		Statement statement = null;
//		try {
//			statement = connection.createStatement();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		try {
//			statement.execute(query);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return true;
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
		if(!blockIsLocked(block)) {
			return;
		}
		
		BukkitRunnable runnable = new BukkitRunnable() {
			
			@Override
			public void run() {
				
				String query = "DELETE FROM Blocks "
						+ "WHERE WORLD = '" + world + "' AND "
						+ "X = '" + x + "' AND "
						+ "Y = '" + y + "' AND "
						+ "Z = '" + z + "';"; 
				
				Connection connection = Main.getSQL().getConnection();
				Statement statement = null;
				try {
					statement = connection.createStatement();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				try {
					statement.execute(query);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				try {
					statement.closeOnCompletion();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
			}
		};
		runnable.runTaskAsynchronously(plugin);
		
//		String query = "DELETE FROM Blocks "
//				+ "WHERE WORLD = '" + world + "' AND "
//				+ "X = '" + x + "' AND "
//				+ "Y = '" + y + "' AND "
//				+ "Z = '" + z + "';"; 
//		
//		Connection connection = Main.getSQL().getConnection();
//		Statement statement = null;
//		try {
//			statement = connection.createStatement();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		try {
//			statement.execute(query);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
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
