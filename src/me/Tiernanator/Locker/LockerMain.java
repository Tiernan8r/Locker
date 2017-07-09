package me.Tiernanator.Locker;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import me.Tiernanator.Locker.Events.PlayerBreakLockedBlock;
import me.Tiernanator.Locker.Events.PlayerLockBlockEvent;
import me.Tiernanator.Locker.Events.PlayerLockedBlockInteract;
import me.Tiernanator.SQL.SQLServer;

public class LockerMain extends JavaPlugin {

	@Override
	public void onEnable() {
		registerEvents();
		initialiseSQL();
		LockedBlock.setPlugin(this);

	}

	private void registerEvents() {
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new PlayerLockBlockEvent(this), this);
		pm.registerEvents(new PlayerLockedBlockInteract(this), this);
		pm.registerEvents(new PlayerBreakLockedBlock(this), this);
	}

	private void initialiseSQL() {

		String query = "CREATE TABLE IF NOT EXISTS Blocks ( "
				+ "ID int NOT NULL AUTO_INCREMENT,"
				+ "World varchar(15) NOT NULL, "
				+ "X int NOT NULL, "
				+ "Y int NOT NULL, "
				+ "Z int NOT NULL, "
				+ "Players varchar(720), "
				+ "PRIMARY KEY (ID) "
				+ ");";
		SQLServer.executeQuery(query);
		
	}
	
}
