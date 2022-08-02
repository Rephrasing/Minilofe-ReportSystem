package mc.minilofe.moderation.reports.commands.mod.listeners;

import mc.minilofe.moderation.reports.MinilofeReportsPlugin;
import mc.minilofe.moderation.reports.commands.mod.guis.PlayerReportsGUI;
import mc.minilofe.moderation.reports.commands.mod.guis.ReportChoiceGUI;
import mc.minilofe.moderation.reports.core.PlayerLastLocationHandler;
import mc.minilofe.moderation.reports.core.objects.PlayerReportRequest;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.UUID;

public class PlayerReportsListener implements Listener {

    public static final HashMap<UUID, PlayerReportRequest> moderator_report_bound = new HashMap<>();

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getInventory().getHolder() instanceof PlayerReportsGUI)) return;

        ItemStack stack = event.getCurrentItem();
        event.setCancelled(true);
        if (stack == null) return;

        Player clicker = (Player) event.getWhoClicked();
        PlayerReportsGUI gui = (PlayerReportsGUI) event.getInventory().getHolder();

        PlayerReportRequest request = gui.getRequestByItemStack(stack);

        clicker.closeInventory();
        if (request == null) {
            MinilofeReportsPlugin.sendPlayerMessage(clicker, "&cCould not find the player report.");
            return;
        }

        if (moderator_report_bound.containsKey(clicker.getUniqueId())) {
            MinilofeReportsPlugin.sendPlayerMessage(clicker, "&cYou are already bound to a player report! you may have an action for this report first then you may teleport to another one");
            return;
        }
        if (moderator_report_bound.containsValue(request)) {
            MinilofeReportsPlugin.sendPlayerMessage(clicker, "&cThis report request is being handled by another moderator.");
            return;
        }

        OfflinePlayer reported = Bukkit.getPlayer(request.getReported());
        MinilofeReportsPlugin.sendPlayerMessage(clicker, "&cInitiating teleport. . .");
        if (reported.getPlayer() == null) {
            MinilofeReportsPlugin.sendPlayerMessage(clicker, "&c" + reported.getName() + " is not online! Trying to find their last location. . .");
            Location location = PlayerLastLocationHandler.getLastLocation(reported.getUniqueId());

            if (location == null) {
                MinilofeReportsPlugin.sendPlayerMessage(clicker, "&cCould not find a last location for " + reported.getName()  +". Teleportation failed.");
                return;
            }
            MinilofeReportsPlugin.sendPlayerMessage(clicker, "&7A last location for &e" + reported.getName() + "&7 was found. Teleporting. . .");
            PotionEffect effect = new PotionEffect(PotionEffectType.INVISIBILITY, 999999, 10, false, false);
            clicker.addPotionEffect(effect);
            clicker.teleport(location);
            MinilofeReportsPlugin.sendPlayerMessage(clicker, "&cYou were teleported to the last location of " + reported.getName());

        } else {
            Player onlineReported = reported.getPlayer();
            PotionEffect effect = new PotionEffect(PotionEffectType.INVISIBILITY, 999999, 10, false, false);
            clicker.addPotionEffect(effect);
            clicker.teleport(onlineReported.getLocation());
            MinilofeReportsPlugin.sendPlayerMessage(clicker, "&cYou were teleported to " + reported.getName());
        }
        ItemStack item = new ItemStack(Material.GHAST_TEAR);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&cPlayer Report Options"));
        item.setItemMeta(meta);
        clicker.getInventory().setItem(8, item);
        moderator_report_bound.put(clicker.getUniqueId(), request);
    }


    @EventHandler
    public void onGhastTearClick(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {

            ItemStack heldItem = event.getItem();

            if (heldItem.getType() != Material.GHAST_TEAR) return;
            if (!heldItem.getItemMeta().hasDisplayName()) return;
            String displayName = heldItem.getItemMeta().getDisplayName();
            if (!displayName.equals(ChatColor.translateAlternateColorCodes('&', "&cPlayer Report Options"))) return;

            Player player = event.getPlayer();

            if (moderator_report_bound.containsKey(player.getUniqueId())) {
                ReportChoiceGUI gui = new ReportChoiceGUI(moderator_report_bound.get(player.getUniqueId()));
                player.openInventory(gui.getInventory());
                return;
            }
            MinilofeReportsPlugin.sendPlayerMessage(player, "&cYou are not bound to a report request at the moment.");
        }
    }



}
