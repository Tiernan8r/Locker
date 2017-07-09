package me.Tiernanator.Locker.Events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import me.Tiernanator.Colours.Colour;
import me.Tiernanator.Locker.LockedBlock;
import me.Tiernanator.Locker.LockerMain;
import me.Tiernanator.Utilities.Blocks.MultiBlocks;

public class PlayerLockedBlockInteract implements Listener {

	@SuppressWarnings("unused")
	private static LockerMain plugin;
	private ChatColor warning = Colour.WARNING.getColour();
	private ChatColor informative = Colour.INFORMATIVE.getColour();

	public PlayerLockedBlockInteract(LockerMain main) {
		plugin = main;
	}

	@EventHandler
	public void playerBlockInteract(PlayerInteractEvent event) {

		if (event.isCancelled()) {
			return;
		}

		if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			if(!event.getAction().equals(Action.PHYSICAL)) {
				return;
			}
		}

		Block block = event.getClickedBlock();
		block = MultiBlocks.getCorrectBlock(block);

		if (!LockedBlock.isLockableBlock(block)) {
			return;
		}

		if (!LockedBlock.blockIsLocked(block)) {
			return;
		}

		Player player = event.getPlayer();

		boolean canOpenBlock = LockedBlock.canBlockInteract(block, player);

		if (!canOpenBlock) {

			List<String> allowedPlayers = new ArrayList<String>();
			allowedPlayers = LockedBlock.getAllowedPlayers(block);
			if(allowedPlayers.isEmpty() || allowedPlayers == null) {
				return;
			}
			event.setCancelled(true);
			player.sendMessage(warning + "This block is locked! Only:");
			
			for (String i : allowedPlayers) {
				player.sendMessage(informative + " - " + i);
			}
			player.sendMessage(warning + "can interact with it.");
			return;

		}

	}
	
}
