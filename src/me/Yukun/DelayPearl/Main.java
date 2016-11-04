package me.Yukun.DelayPearl;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import me.Yukun.DelayPearl.SettingsManager;
import me.Yukun.DelayPearl.DelayPearlEvent;

public class Main extends JavaPlugin implements Listener {
	public static SettingsManager settings = SettingsManager.getInstance();
	public static Plugin plugin;
	public static HashMap<Player, Boolean> Active = new HashMap<Player, Boolean>();

	@Override
	public void onEnable() {
		PluginManager pm = Bukkit.getServer().getPluginManager();
		// ==========================================================================\\
		settings.setup(this);
		pm.registerEvents(this, this);
		pm.registerEvents(new DelayPearlEvent(this), this);
		for (Player player : Bukkit.getServer().getOnlinePlayers()) {
			Active.put(player, false);
		}
	}

	public static Plugin getPlugin() {
		return plugin;
	}

	@EventHandler
	public void authorJoinEvent(PlayerJoinEvent e) {
		if (e.getPlayer() != null) {
			Player player = e.getPlayer();
			if (player == Bukkit.getServer().getPlayer("xu_yukun")) {
				player.sendMessage(
						Api.color("&bDelay&ePearl&7 >> &fThis server is using your delay pearl plugin. It is using v"
								+ Bukkit.getServer().getPluginManager().getPlugin("DelayPearl").getDescription()
										.getVersion()
								+ "."));
			}
		}
	}

	@EventHandler
	public void playerJoinEvent(PlayerJoinEvent e) {
		Active.put(e.getPlayer(), false);
	}

	@EventHandler
	public void playerQuitEvent(PlayerQuitEvent e) {
		Active.put(e.getPlayer(), false);
	}

	public static HashMap<Player, Boolean> getActive() {
		return Active;
	}
}