package mc.minilofe.moderation.reports.commands.mod.guis;

import mc.minilofe.moderation.reports.MinilofeReportsPlugin;
import mc.minilofe.moderation.reports.core.PlayerReportsHandler;
import mc.minilofe.moderation.reports.core.objects.PlayerReportRequest;
import mc.minilofe.moderation.reports.core.objects.ReportReason;
import org.bukkit.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class PlayerReportsGUI implements InventoryHolder {

    private final Inventory inv;
    private final List<PlayerReportRequest> data;

    public PlayerReportsGUI() {
        this.inv = Bukkit.createInventory(this, 54, "Player reports");
        data = PlayerReportsHandler.getActiveRequests();

        List<Long> timestamps = new ArrayList<>();

        // loops over each timestamp and puts it in a list
        for (PlayerReportRequest request : data) {
            timestamps.add(request.getTimestamp());
        }

        // sorts the timestamps list
        Collections.sort(timestamps);

        // loops over the timestamps
        for (Long timestamp : timestamps) {

            // loops over the data
            for (PlayerReportRequest request : data) {
                if (request.getTimestamp() == timestamp) {

                    for (int i = 0; i <= 52; i++) { // loops over numbers from 52 to 0
                        if (inv.getItem(i) == null) { // if the inventory item is empty
                            inv.setItem(i, createReportItem(request)); // sets the item
                            break;
                        }
                    }

                }
            }

        }
    }

    private ItemStack createReportItem(PlayerReportRequest request) {
        ItemStack stack = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3); // the byte (3) to specify that this skull is a player skull
        SkullMeta meta = (SkullMeta) stack.getItemMeta(); // getting skull meta from the stack
        OfflinePlayer reported = Bukkit.getOfflinePlayer(request.getReported());
        OfflinePlayer reporter = Bukkit.getOfflinePlayer(request.getReporter());
        meta.setOwner(reported.getName()); // setting the owner of the skull to the reported player

        // display name of item
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&fPlayer Report"));

        // lore
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.translateAlternateColorCodes('&',"&7Reported player: &c" + reported.getName()));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7Reported by: &b" + reporter.getName()));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7Report reason: &a" + request.getReportReason()));

        Date date = new Date(request.getTimestamp());
        DateFormat df = new SimpleDateFormat("HH:mm:ss @ dd/MM/yyyy z");
        String formattedDate = df.format(date);
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7Timestamp: &d" + formattedDate));
        lore.add(" ");
        lore.add(ChatColor.translateAlternateColorCodes('&', "&l&eClick here to teleport to the reported player"));
        meta.setLore(lore);

        stack.setItemMeta(meta);
        return stack;
    }

    public PlayerReportRequest getRequestByItemStack(ItemStack item) {
        if (!item.getItemMeta().hasLore()) return null;
        if (!item.getItemMeta().hasDisplayName()) return null;

        List<String> lore = item.getItemMeta().getLore();

        String reportedPlayerName = ChatColor.stripColor(lore.get(0).split(":")[1].substring(1));
        String reportedByPlayerName = ChatColor.stripColor(lore.get(1).split(":")[1].substring(1));
        String reportReason = ChatColor.stripColor(lore.get(2).split(":")[1].substring(1));


        for (PlayerReportRequest request : data) {
            OfflinePlayer requestReportedPlayer = Bukkit.getOfflinePlayer(request.getReported());
            OfflinePlayer requestReporterPlayer = Bukkit.getOfflinePlayer(request.getReporter());

            if (request.getReportReason().equals(ReportReason.valueOf(reportReason))) {
                if (requestReportedPlayer.getName().equals(reportedPlayerName)) {
                    if (requestReporterPlayer.getName().equals(reportedByPlayerName)) {
                        return request;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public Inventory getInventory() {
        return inv;
    }
}
