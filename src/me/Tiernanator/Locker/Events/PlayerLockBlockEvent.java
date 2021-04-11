package me.Tiernanator.Locker.Events;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.Tiernanator.Locker.LockedBlock;
import me.Tiernanator.Locker.LockerMain;
import me.Tiernanator.Utilities.Blocks.MultiBlocks;
import me.Tiernanator.Utilities.Colours.Colour;
import me.Tiernanator.Zoning.Zone.Zone;

@SuppressWarnings("deprecation")
public class PlayerLockBlockEvent implements Listener {

	private static LockerMain plugin;
	private ChatColor warning = Colour.WARNING.getColour();
	private ChatColor informative = Colour.INFORMATIVE.getColour();
	private ChatColor good = Colour.GOOD.getColour();
	private ChatColor highlight = Colour.HIGHLIGHT.getColour();

	public PlayerLockBlockEvent(LockerMain main) {
		plugin = main;
	}

	@EventHandler
	public void lockBlock(PlayerInteractEvent event) {

		if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			return;
		}

		Block block = event.getClickedBlock();

		block = MultiBlocks.getCorrectBlock(block);

		if (!LockedBlock.isLockableBlock(block)) {
			return;
		}
		Player player = event.getPlayer();
		
		if (player.isSneaking()) {
			return;
		}

		ItemStack heldItem = player.getItemInHand();
		if (!heldItem.getType().equals(Material.TRIPWIRE_HOOK)) {
			return;
		}
		String itemName = heldItem.getItemMeta().getDisplayName();

		if (itemName == null) {
			return;
		}

		if (itemName.equals("Tripwire Hook") || itemName.isEmpty()
				|| itemName.equals(null)) {
			return;
		}

		OfflinePlayer[] allPlayers = plugin.getServer().getOfflinePlayers();
		List<String> allowedPlayers = new ArrayList<String>();
		List<String> allowedPlayersUUID = new ArrayList<String>();

		List<String> namesOnKey = new ArrayList<String>();
		namesOnKey = Arrays.asList(itemName.split(","));
		
		for(OfflinePlayer offlinePlayer : allPlayers) {
			String playerName = offlinePlayer.getName();
			for(String keyName : namesOnKey) {
				keyName = keyName.trim();
				if(playerName.equalsIgnoreCase(keyName)) {
					String playerUUID = offlinePlayer.getUniqueId().toString();
					allowedPlayers.add(playerName);
					allowedPlayersUUID.add(playerUUID);
				}
			}
			
			
		}

		if (allowedPlayers.isEmpty() || allowedPlayersUUID.isEmpty()) {
			return;
		}

		boolean blockIsLocked = LockedBlock.blockIsLocked(block);
		if (blockIsLocked) {
			player.sendMessage(warning + "This block is already locked!");
			event.setCancelled(true);
			return;
		}
		
		for(Zone zone : Zone.allZones()) {
			boolean inSubZone = zone.isInZone(block);
			boolean canInteract = zone.canBuild(player);
			if(inSubZone && !canInteract) {
				String zoneName = zone.getDisplayName();
				List<String> zoneOwners = zone.getOwnerNames();
				player.sendMessage(warning + "The zone " + informative + zoneName + warning + " belongs to: ");
				for(String ownerName : zoneOwners) {
					player.sendMessage(highlight + " - " + ownerName);
				}
				player.sendMessage(warning + " and you can't affect here!");
				event.setCancelled(true);
				return;
			}
		}
		
		event.setCancelled(true);
		LockedBlock.addLockedBlock(block, allowedPlayersUUID);

		int amount = heldItem.getAmount() - 1;
		if(amount == 0) {
			player.getInventory().remove(heldItem);
		} else {
			heldItem.setAmount(amount);
		}
		
		player.sendMessage(good
				+ "This block is now locked and can only be interacted with by:");
		for (String i : allowedPlayers) {
			player.sendMessage(informative + " - " + i);
		}

	}

	@EventHandler
	public void unlockBlock(PlayerInteractEvent event) {

		Player player = event.getPlayer();

		if (!player.isSneaking()) {
			return;
		}
		if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			return;
		}

		Block block = event.getClickedBlock();
		block = MultiBlocks.getCorrectBlock(block);

		if (!LockedBlock.isLockableBlock(block)) {
			return;
		}

		ItemStack heldItem = player.getItemInHand();
		if (!heldItem.getType().equals(Material.TRIPWIRE_HOOK)) {
			return;
		}
		String itemName = heldItem.getItemMeta().getDisplayName();

		if (itemName.equals("Tripwire Hook")) {
			return;
		}

		if (!LockedBlock.blockIsLocked(block)) {
			player.sendMessage(warning + "This block isn't locked!");
			event.setCancelled(true);
			return;
		}

		if (!LockedBlock.canBlockInteract(block, player)) {
			if (event.isCancelled()) {
				return;
			}
			event.setCancelled(true);
			player.sendMessage(warning + "This block is locked! Only:");

			List<String> allowedPlayers = LockedBlock.getAllowedPlayers(block);

			for (String i : allowedPlayers) {
				player.sendMessage(informative + " - " + i);
			}
			player.sendMessage(warning + "can interact with it.");
			return;
		}

		OfflinePlayer[] allPlayers = plugin.getServer().getOfflinePlayers();
		List<String> playersToRemove = new ArrayList<String>();
		List<String> playerUUIDsToRemove = new ArrayList<String>();

		for (OfflinePlayer offlinePlayer : allPlayers) {

			String playerName = offlinePlayer.getName();
			if (itemName.contains(playerName)) {
				playersToRemove.add(playerName);
				playerUUIDsToRemove.add(offlinePlayer.getUniqueId().toString());
				itemName = itemName.replace(playerName, "");
			}
		}

		List<String> allowedPlayersUUIDs = new ArrayList<String>();
		allowedPlayersUUIDs = LockedBlock.getAllowedPlayersUUIDs(block);

		if (!allowedPlayersUUIDs.isEmpty() || !playerUUIDsToRemove.isEmpty()) {
			boolean containedAValidPlayer = false;
			for (int i = 0; i < allowedPlayersUUIDs.size(); i++) {
				for (int j = 0; j < playerUUIDsToRemove.size(); j++) {
					String iPlayer = allowedPlayersUUIDs.get(i);
					String jPlayer = playerUUIDsToRemove.get(j);
					if (iPlayer.equalsIgnoreCase(jPlayer)) {
						allowedPlayersUUIDs.remove(iPlayer);
						containedAValidPlayer = true;
					}
				}
			}

			if (!containedAValidPlayer) {
				player.sendMessage(warning
						+ "Invalid key! None of these named players can interact with this block anyway.");
				event.setCancelled(true);
				return;
			}
		}
		allowedPlayersUUIDs.removeAll(playerUUIDsToRemove);

//		for(String zone : CoreZone.allZones()) {
//			boolean inCoreZone = CoreZone.isInCoreZone(zone, block.getLocation());
//			boolean canInteract = CoreZone.canBuild(zone, player);
//			if(inCoreZone && !canInteract) {
//				player.sendMessage(warning + "This is the " + highlight
//						+ zone + warning
//						+ " zone and you can't affect here!");
//				event.setCancelled(true);
//				return;
//			}
//		}
		for(Zone zone : Zone.allZones()) {
			boolean inSubZone = zone.isInZone(block);
			boolean canInteract = zone.canBuild(player);
			if(inSubZone && !canInteract) {
				String zoneName = zone.getDisplayName();
				List<String> zoneOwners = zone.getOwnerNames();
//				String playerName = PlayerLogger.getPlayerNameByUUID(zoneOwner);
				player.sendMessage(warning + "The zone " + informative + zoneName + warning + " belongs to: ");
				for(String ownerName : zoneOwners) {
					player.sendMessage(highlight + " - " + ownerName);
				}
				player.sendMessage(warning + " and you can't affect here!");
				event.setCancelled(true);
				return;
			}
		}
		
		if (allowedPlayersUUIDs.isEmpty()) {
			LockedBlock.removeLockedBlock(block);
			player.sendMessage(good + "This block is now unlocked.");
			heldItem.setAmount(heldItem.getAmount() + 1);
		} else {
			LockedBlock.setAllowedPlayers(block, allowedPlayersUUIDs);
			
			List<String> allowedPlayers = new ArrayList<String>();
			for(OfflinePlayer offlinePlayer : allPlayers) {
				if(allowedPlayersUUIDs.contains(offlinePlayer.getUniqueId().toString())) {
					allowedPlayers.add(offlinePlayer.getName());
				}
			}
			player.sendMessage(good
					+ "The lock on this block has been modified, it can now only be interacted with by:");
			for (String i : allowedPlayers) {
				player.sendMessage(informative + " - " + i);
			}
		}
		event.setCancelled(true);

	}

}
