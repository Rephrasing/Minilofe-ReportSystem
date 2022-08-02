package mc.minilofe.moderation.reports.commands.player.listeners;

import mc.minilofe.moderation.reports.MinilofeReportsPlugin;
import mc.minilofe.moderation.reports.commands.player.guis.ReportGUI;
import mc.minilofe.moderation.reports.core.PlayerReportsHandler;
import mc.minilofe.moderation.reports.core.objects.ReportReason;
import mc.minilofe.moderation.reports.core.exceptions.ReportAlreadyExistsException;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class ReportListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getInventory().getHolder() instanceof ReportGUI)) return;

        ItemStack stack = event.getCurrentItem();
        event.setCancelled(true);
        if (stack == null) return;

        Player clicker = (Player) event.getWhoClicked();
        ReportGUI gui = (ReportGUI) event.getInventory().getHolder();
        Player toBeReported = gui.getToBeReported();

        int slot = event.getSlot();
        ReportReason reasonBySlot = ReportGUI.getReportReasonByInventorySlot(slot);

        if (reasonBySlot == null) return;

        try {
            PlayerReportsHandler.createPlayerReport(clicker, toBeReported, reasonBySlot);
            MinilofeReportsPlugin.sendPlayerMessage(clicker, "&aYou have successfully reported " + toBeReported.getName());
            MinilofeReportsPlugin.sendConsoleMessage("&e" + clicker.getName() + "&a reported " + "&c"  + toBeReported.getName() + "&a for &3" + reasonBySlot);
        } catch (ReportAlreadyExistsException e) {
            MinilofeReportsPlugin.sendPlayerMessage(clicker, "&cYou have already reported this player.");
        }
        clicker.closeInventory();
    }

}
