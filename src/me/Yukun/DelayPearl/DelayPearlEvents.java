package me.Yukun.DelayPearl;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import me.Yukun.DelayPearl.Api;
import me.Yukun.DelayPearl.Main;

public class DelayPearlEvents implements Listener {
	static Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("RankQuests");
	String prefix = Api.getMessagesString("Messages.Prefix");
	String delaymsg = Api.getMessagesString("Messages.DelayMessage");

	@SuppressWarnings("static-access")
	public DelayPearlEvents(Plugin plugin) {
		this.plugin = plugin;
	}

	HashMap<Player, Boolean> Active = Main.getActive();
	HashMap<Player, Integer> Cooldown = new HashMap<Player, Integer>();
	HashMap<Player, Integer> Timer = new HashMap<Player, Integer>();

	@EventHandler
	public void playerJoinEvent(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		Active.put(player, false);
		return;
	}

	@EventHandler
	public void playerDeathEvent(PlayerDeathEvent e) {
		Player player = e.getEntity();
		Active.put(player, false);
		return;
	}

	@EventHandler
	public void playerQuitEvent(PlayerQuitEvent e) {
		Player player = e.getPlayer();
		Active.put(player, false);
		return;
	}

	@EventHandler
	public void pearlLandEvent(PlayerTeleportEvent e) {
		if (e.getPlayer() != null && e.getPlayer() instanceof Player) {
			Player player = e.getPlayer();
			if (e.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
				if (player.hasPermission("PearlCooldown.NoDamage")) {
					e.setCancelled(true);
					player.setNoDamageTicks(1);
					player.teleport(e.getTo());
				}
			}
		}
	}

	@EventHandler
	public void startQuestEvent(final PlayerInteractEvent e) {
		final Player player = e.getPlayer();
		if (player.getGameMode() == GameMode.SURVIVAL || player.getGameMode() == GameMode.ADVENTURE) {
			if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
				if (Api.getVersion() >= 191) {
					ItemStack item = player.getInventory().getItemInMainHand();
					ItemStack off = player.getInventory().getItemInOffHand();
					if (item.getType() == Material.ENDER_PEARL && off.getType() == Material.ENDER_PEARL) {
						if (!player.hasPermission("PearlCooldown.Bypass") || !player.isOp()) {
							if (Active.get(player) == true) {
								e.setCancelled(true);
								player.updateInventory();
								player.sendMessage(
										Api.color(prefix + delaymsg.replace("%time%", (Timer.get(player) + ""))));
								return;
							}
							if (Active.get(player) == false) {
								Active.put(player, true);
								if (Api.isInt(Api.getConfigString("Options.Delay"))) {
									Timer.put(player, Integer.parseInt(Api.getConfigString("Options.Delay")));
								}
								Cooldown.put(player, Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin,
										new Runnable() {
											public void run() {
												if (Active.get(player) != null && Active.get(player) == true) {
													if (Timer.get(player) != null && Timer.get(player) >= 1) {
														Timer.put(player, (Timer.get(player) - 1));
													}
													if (Timer.get(player) != null && Timer.get(player) == 0) {
														Active.put(player, false);
														Timer.remove(player);
														Bukkit.getServer().getScheduler()
																.cancelTask(Cooldown.get(player));
														Cooldown.remove(player);
														return;
													}
												}
											}
										}, 20, 0));
								return;
							}
						}
					}
				} else {
					@SuppressWarnings("deprecation")
					ItemStack item = player.getItemInHand();
					if (item.getType() == Material.ENDER_PEARL) {
						if (!player.hasPermission("PearlCooldown.Bypass") || !player.isOp()) {
							if (Active.get(player) == true) {
								e.setCancelled(true);
								player.updateInventory();
								player.sendMessage(
										Api.color(prefix + delaymsg.replace("%time%", (Timer.get(player) + ""))));
								return;
							}
							if (Active.get(player) == false) {
								Active.put(player, true);
								if (Api.isInt(Api.getConfigString("Options.Delay"))) {
									Timer.put(player, Integer.parseInt(Api.getConfigString("Options.Delay")));
								}
								Cooldown.put(player, Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin,
										new Runnable() {
											public void run() {
												if (Active.get(player) != null && Active.get(player) == true) {
													if (Timer.get(player) != null && Timer.get(player) >= 1) {
														Timer.put(player, (Timer.get(player) - 1));
													}
													if (Timer.get(player) != null && Timer.get(player) == 0) {
														Active.put(player, false);
														Timer.remove(player);
														Bukkit.getServer().getScheduler()
																.cancelTask(Cooldown.get(player));
														Cooldown.remove(player);
														return;
													}
												}
											}
										}, 0, 20));
								return;
							}
						}
					}
				}
			}
		}
	}
}
