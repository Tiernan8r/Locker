package me.Tiernanator.Locker;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import me.Tiernanator.Locker.Events.PlayerBreakLockedBlock;
import me.Tiernanator.Locker.Events.PlayerLockBlockEvent;
import me.Tiernanator.Locker.Events.PlayerLockedBlockInteract;
import me.Tiernanator.SQL.SQLServer;
import me.Tiernanator.SQL.MySQL.MySQL;

public class Main extends JavaPlugin {

	@Override
	public void onEnable() {
		registerEvents();
		initialiseSQL();
		LockedBlock.setPlugin(this);

	}

	@Override
	public void onDisable() {

		try {
			getSQL().closeConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	private void registerEvents() {
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new PlayerLockBlockEvent(this), this);
		pm.registerEvents(new PlayerLockedBlockInteract(this), this);
		pm.registerEvents(new PlayerBreakLockedBlock(this), this);
	}

	private static MySQL mySQL;

	private void initialiseSQL() {

		mySQL = new MySQL(SQLServer.HOSTNAME, SQLServer.PORT,  SQLServer.DATABASE,
				SQLServer.USERNAME, SQLServer.PASSWORD);
		
//		String query = "CREATE DATABASE IF NOT EXISTS lockedBlocks;";
		
		Connection connection = null;
		try {
			connection = mySQL.openConnection();
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		Statement statement = null;
		try {
			statement = connection.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
//		try {
//			statement.execute(query);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
		
//		query = "USE lockedBlocks;";
		String query = "USE " + SQLServer.DATABASE.getInfo() + ";";
		
		statement = null;
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
		
		query = "CREATE TABLE IF NOT EXISTS Blocks ( "
				+ "ID int NOT NULL AUTO_INCREMENT,"
				+ "World varchar(15) NOT NULL, "
				+ "X int NOT NULL, "
				+ "Y int NOT NULL, "
				+ "Z int NOT NULL, "
				+ "Players varchar(720), "
				+ "PRIMARY KEY (ID) "
				+ ");";
		
		statement = null;
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
	}

	public static MySQL getSQL() {
		return mySQL;
	}

//	public static Connection getSQLConnection() {
//
//		try {
//			if (!getSQL().checkConnection()) {
//			return getSQL().openConnection();
//		} else {
//			return getSQL().getConnection();
//		}
//		} catch (ClassNotFoundException | SQLException e) {
//			e.printStackTrace();
//		}
//		return null;
//		Connection connection = null;
//		try {
//			if (!getSQL().checkConnection()) {
//				connection = getSQL().openConnection();
//			} else {
//				connection = getSQL().getConnection();
//			}
//		} catch (ClassNotFoundException | SQLException e) {
//			e.printStackTrace();
//		}
//		String query = "USE " + SQLServer.DATABASE.getInfo() + ";";
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
//		return connection;
//	}

}
