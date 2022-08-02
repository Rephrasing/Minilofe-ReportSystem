package mc.minilofe.moderation.reports.commands.mod.guis;

import mc.minilofe.moderation.reports.core.objects.PlayerReportRequest;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ReportChoiceGUI implements InventoryHolder {

    private final Inventory inventory;
    private final PlayerReportRequest request;

    public ReportChoiceGUI(PlayerReportRequest request) {
        this.inventory = Bukkit.createInventory(this, 27, "Report choice.");
        this.request = request;

        ItemStack accept = new ItemStack(Material.WOOL, 1,(byte) 5);
        ItemMeta acceptMeta = accept.getItemMeta();
        acceptMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&aAccept report"));
        accept.setItemMeta(acceptMeta);

        ItemStack deny = new ItemStack(Material.WOOL, 1, (byte) 14);
        ItemMeta denyMeta = deny.getItemMeta();
        denyMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&cDeny report"));
        deny.setItemMeta(denyMeta);
        this.inventory.setItem(11, accept);
        this.inventory.setItem(15, deny);
    }

    public PlayerReportRequest getRequest() {
        return request;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
