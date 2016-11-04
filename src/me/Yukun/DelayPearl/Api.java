package me.Yukun.DelayPearl;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class Api {
	public static Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("RankQuests");

	@SuppressWarnings("static-access")
	public Api(Plugin plugin) {
		this.plugin = plugin;
	}

	@SuppressWarnings("deprecation")
	public static ItemStack getItemInHand(Player player) {
		if (getVersion() >= 191) {
			return player.getInventory().getItemInMainHand();
		} else {
			return player.getItemInHand();
		}
	}
	
	public static String getConfigString(String path) {
		String msg = Main.settings.getConfig().getString(path);
		return msg;
	}
	
	public static String getMessagesString(String path) {
		String msg = Main.settings.getMessages().getString(path);
		return msg;
	}

	public static boolean containsItem(Player player, ItemStack item) {
		for (ItemStack items : player.getInventory().getContents()) {
			if (items.isSimilar(item) && (items.getAmount() + item.getAmount()) <= 64) {
				return true;
			} else {
				continue;
			}
		}
		return false;
	}

	public static ItemStack getItem(Player player, ItemStack item) {
		if (Api.containsItem(player, item)) {
			for (ItemStack items : player.getInventory().getContents()) {
				if (items.isSimilar(item) && (items.getAmount() + item.getAmount()) <= 64) {
					return items;
				} else {
					continue;
				}
			}
		}
		return null;
	}

	@SuppressWarnings("deprecation")
	public static void setItemInHand(Player player, ItemStack item) {
		if (getVersion() >= 191) {
			player.getInventory().setItemInMainHand(item);
		} else {
			player.setItemInHand(item);
		}
	}

	public static String color(String msg) {
		return ChatColor.translateAlternateColorCodes('&', msg);
	}

	public static String removeColor(String msg) {
		msg = ChatColor.stripColor(msg);
		return msg;
	}

	public static Integer getVersion() {
		String ver = Bukkit.getServer().getClass().getPackage().getName();
		ver = ver.substring(ver.lastIndexOf('.') + 1);
		ver = ver.replaceAll("_", "").replaceAll("R", "").replaceAll("v", "");
		return Integer.parseInt(ver);
	}

	public static boolean isInt(String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	public static Player getPlayer(String name) {
		return Bukkit.getServer().getPlayer(name);
	}
}