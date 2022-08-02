package mc.minilofe.moderation.reports.commands.mod.listeners;


import mc.minilofe.moderation.reports.MinilofeReportsPlugin;
import mc.minilofe.moderation.reports.commands.mod.guis.ReportChoiceGUI;
import mc.minilofe.moderation.reports.core.PlayerReportsHandler;
import mc.minilofe.moderation.reports.core.exceptions.ReportNotFoundException;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class ReportChoiceListener implements Listener {


    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getInventory().getHolder() instanceof ReportChoiceGUI)) return;

        ItemStack stack = event.getCurrentItem();
        event.setCancelled(true);
        if (stack == null) return;

        Player clicker = (Player) event.getWhoClicked();
        ReportChoiceGUI gui = (ReportChoiceGUI) event.getInventory().getHolder();

        if (event.getSlot() == 11) {
            try {
                PlayerReportsHandler.acceptPlayerReport(clicker, gui.getRequest());
                MinilofeReportsPlugin.sendPlayerMessage(clicker, "&aYou have successfully accepted the report");
                PlayerReportsListener.moderator_report_bound.remove(clicker.getUniqueId());
            } catch (ReportNotFoundException e) {
                MinilofeReportsPlugin.sendPlayerMessage(clicker, "&cCould not find the report to accept it.");
            }
        } else if (event.getSlot() == 15) {
            try {
                PlayerReportsHandler.denyPlayerReport(clicker, gui.getRequest());
                MinilofeReportsPlugin.sendPlayerMessage(clicker, "&cYou have successfully denied the report");
                PlayerReportsListener.moderator_report_bound.remove(clicker.getUniqueId());
            } catch (ReportNotFoundException e) {
                MinilofeReportsPlugin.sendPlayerMessage(clicker, "&cCould not find the report to deny it.");
            }
        }
        clicker.closeInventory();
    }


}
