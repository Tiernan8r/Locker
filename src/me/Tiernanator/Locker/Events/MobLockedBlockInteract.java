package me.Tiernanator.Locker.Events;

import org.bukkit.event.Listener;


//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.bukkit.Location;
//import org.bukkit.World;
//import org.bukkit.block.Block;
//import org.bukkit.entity.Entity;
//import org.bukkit.event.EventHandler;
//import org.bukkit.event.entity.EntityInteractEvent;
//
//import me.Tiernanator.Config.ConfigAccessor;
//import me.Tiernanator.Locker.Main;
//
public class MobLockedBlockInteract implements Listener {
//
//	private static Main plugin;
//
//	public MobLockedBlockInteract(Main main) {
//		plugin = main;
//	}
//
//	@EventHandler
//	public void entityBlockInteract(BlockMultiPlaceEvent event) {
//
//
//		if (event.isCancelled()) {
//			return;
//		}
//
//		Block block = event.getBlock();
//		if (block.getState() instanceof Chest ){
//            Chest chest = (Chest) block.getState();
//            InventoryHolder ih = chest.getInventory().getHolder();
//            if (ih instanceof DoubleChest) {
//                DoubleChest dc = (DoubleChest) ih;
//                block = dc.getLocation().getBlock();
//            }
//        }
//		if (block.getState().getData() instanceof Door) {
//	Door door = (Door) block.getState();
//	if (door.isTopHalf()) {
//		block = block.getRelative(BlockFace.DOWN);
//	}
//}
//		
//		if (!PlayerLockedBlockInteract.isLockableBlock(block)) {
//			return;
//		}
//
//		if(!PlayerLockedBlockInteract.blockIsLocked(block)) {
//			return;
//		}
//
//		Entity entity = event.getEntity();
//
//		boolean canOpenBlock = canBlockInteract(block, entity);
//
//		if (!canOpenBlock) {
//
//			event.setCancelled(true);
//			return;
//
//		}
//
//	}
//
//	public static boolean canBlockInteract(Block block, Entity entity) {
//
//		if (!PlayerLockedBlockInteract.isLockableBlock(block)) {
//			return true;
//		}
//		Location blockLocation = block.getLocation();
//
//		ConfigAccessor lockerAccessor = new ConfigAccessor(plugin,
//				"lockedBlocks.yml");
//
//		int amountOfBlocks = lockerAccessor.getConfig().getInt("TotalBlocks");
//		for (int i = 1; i <= amountOfBlocks; i++) {
//			String index = Integer.toString(i);
//
//			List<Integer> blockLocationList = new ArrayList<Integer>();
//			blockLocationList = lockerAccessor.getConfig()
//					.getIntegerList("Block." + index + ".Location");
//
//			String worldName = lockerAccessor.getConfig()
//					.getString("Block." + index + ".World");
//			World world = null;
//			boolean isValid = true;
//			try {
//				world = plugin.getServer().getWorld(worldName);
//			} catch (Exception e) {
//				isValid = false;
//			}
//
//			if (isValid) {
//				Location thisBlockLocation = new Location(world,
//						blockLocationList.get(0), blockLocationList.get(1),
//						blockLocationList.get(2));
//
//				if (thisBlockLocation.equals(blockLocation)) {
//					
//					try {
//						List<String> allowedEntities = lockerAccessor.getConfig()
//								.getStringList("Block." + index + ".Entities");
//						if (allowedEntities.contains("All")) {
//							return true;
//						}
//						if (allowedEntities.contains("None")) {
//							return false;
//						}
//						if (allowedEntities.contains(entity.getName())) {
//							return true;
//						} else {
//							return false;
//						}
//					} catch (Exception e) {
//					}
//				}
//			}
//		}
//		return false;
//	}
//	
//	public static boolean isValidEntity(String entityName) {
//		
//		entityName = entityName.toUpperCase();
//		
//		switch(entityName) {
//			case "ARROW" :
//				return true;
//			case "BAT" :
//				return true;
//			case "BLAZE" :
//				return true;
//			case "BOAT" :
//				return true;
//			case "CAVE_SPIDER" :
//				return true;
//			case "CHICKEN" :
//				return true;
//			case "COW" :
//				return true;
//			case "CREEPER" :
//				return true;
//			case "DROPPED_ITEM" :
//				return true;
//			case "ITEM" :
//				return true;
//			case "ENDERMAN" :
//				return true;
//			case "ENDERMITE" :
//				return true;
//			case "EXPERIENCE_ORB" :
//				return true;
//			case "FISHING_HOOK" :
//				return true;
//			case "GHAST" :
//				return true;
//			case "GUARDIAN" :
//				return true;
//			case "HORSE" :
//				return true;
//			case "IRON_GOLEM" :
//				return true;
//			case "MAGMA_CUBE" :
//				return true;
//			case "MINECART" :
//				return true;
//			case "MINECART_CHEST" :
//				return true;
//			case "MINECART_COMMAND" :
//				return true;
//			case "MINECART_FURNACE" :
//				return true;
//			case "MINECART_HOPPER" :
//				return true;
//			case "MINECART_MOB_SPAWNER" :
//				return true;
//			case "MINECART_TNT" :
//				return true;
//			case "MUSHROOM_COW" :
//				return true;
//			case "MOOSHROOM" :
//				return true;
//			case "OCELOT" :
//				return true;
//			case "PIG" :
//				return true;
//			case "PIG_ZOMBIE" :
//				return true;
//			case "ZOMBIE_PIGMAN" :
//				return true;
//			case "PLAYER" :
//				return true;
//			case "POLAR_BEAR" :
//				return true;
//			case "PRIMED_TNT" :
//				return true;
//			case "RABBIT" :
//				return true;
//			case "SHEEP" :
//				return true;
//			case "SHULKER" :
//				return true;
//			case "SILVERFISH" :
//				return true;
//			case "SKELETON" :
//				return true;
//			case "SLIME" :
//				return true;
//			case "SNOWMAN" :
//				return true;
//			case "SPECTRAL_ARROW" :
//				return true;
//			case "SPIDER" :
//				return true;
//			case "SQUID" :
//				return true;
//			case "TIPPED_ARROW" :
//				return true;
//			case "VILLAGER" :
//				return true;
//			case "WITCH" :
//				return true;
//			case "WITHER" :
//				return true;
//			case "WOLF" :
//				return true;
//			case "ZOMBIE" :
//				return true;
//			case "ALL" :
//				return true;
//			case "NONE" :
//				return false;
//			default :
//				return false;
//			
//		}
//	}
//	
}
