package me.Tiernanator.Locker.Events;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import me.Tiernanator.Locker.LockedBlock;
import me.Tiernanator.Locker.LockerMain;
import me.Tiernanator.Utilities.Blocks.MultiBlocks;
import me.Tiernanator.Utilities.Colours.Colour;

public class PlayerBreakLockedBlock implements Listener {

	@SuppressWarnings("unused")
	private static LockerMain plugin;
	private ChatColor warning = Colour.WARNING.getColour();

	public PlayerBreakLockedBlock(LockerMain main) {
		plugin = main;
	}

	@EventHandler
	public void breakLockedBlock(BlockBreakEvent event) {

		if(event.isCancelled()) {
			return;
		}
		
		Block block = event.getBlock();
		block = MultiBlocks.getCorrectBlock(block);

		if (!LockedBlock.isLockableBlock(block)) {
			return;
		}

		boolean blockIsLocked = LockedBlock.blockIsLocked(block);
		if (!blockIsLocked) {
			return;
		}

		Player player = event.getPlayer();

		boolean canBlockInteract = LockedBlock.canBlockInteract(block, player);
		if (!canBlockInteract) {
			event.setCancelled(true);
			player.sendMessage(warning
					+ "This block is locked and you cannot interact with it.");
			return;
		} else {

			LockedBlock.removeLockedBlock(block);

		}

	}

}
