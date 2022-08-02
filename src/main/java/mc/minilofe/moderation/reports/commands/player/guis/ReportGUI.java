package mc.minilofe.moderation.reports.commands.player.guis;

import mc.minilofe.moderation.reports.MinilofeReportsPlugin;
import mc.minilofe.moderation.reports.core.objects.ReportReason;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ReportGUI implements InventoryHolder {

    private final Inventory inventory;
    private final Player toBeReported;

    public ReportGUI(Player reported) {
        this.toBeReported = reported;
        this.inventory = MinilofeReportsPlugin.getInstance().getServer().createInventory(this, 27, "Reporting " + toBeReported.getName() + " for:");

        ItemStack chatAbuse = createItem("Chat Abuse/Scam", Material.SIGN);
        ItemStack cheating = createItem("Cheating (Hacking)", Material.BARRIER);
        ItemStack statBoost = createItem("Stats boosting", Material.GOLD_HELMET);

        inventory.setItem(2,  chatAbuse);
        inventory.setItem(4, cheating);
        inventory.setItem(6, statBoost);

        ItemStack glass = createItem(" ", Material.STAINED_GLASS_PANE);
        for (int i = 0; i <= 26; i++) {
            if (inventory.getItem(i) == null) inventory.setItem(i, glass);
        }
    }

    public static ReportReason getReportReasonByInventorySlot(int slot) {
        switch (slot) {
            case 2: return ReportReason.CHAT_ABUSE;
            case 4: return ReportReason.CHEATING;
            case 6: return ReportReason.STAT_BOOST;
        }
        return null;
    }

    private ItemStack createItem(String name, Material material) {
        ItemStack stack = new ItemStack(material);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(name);
        stack.setItemMeta(meta);
        return stack;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public Player getToBeReported() {
        return toBeReported;
    }
}
