package mc.minilofe.moderation.reports.commands.mod;


import mc.minilofe.moderation.reports.MinilofeReportsPlugin;
import mc.minilofe.moderation.reports.commands.mod.guis.PlayerReportsGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class PlayerReportsCommand implements CommandExecutor {

    private final HashMap<UUID, Long> cooldowns = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player player = (Player) sender;

        if (cooldowns.containsKey(player.getUniqueId())) {
            Long secondsLeft = getCooldownByTimestamp(cooldowns.get(player.getUniqueId()));
            if (secondsLeft != null) {
                MinilofeReportsPlugin.sendPlayerMessage(player,"&cYou are still cooling down. You may use this command in &e" + secondsLeft + "&c seconds");
                return true;
            }
        }

        PlayerReportsGUI gui = new PlayerReportsGUI();
        player.openInventory(gui.getInventory());
        cooldowns.put(player.getUniqueId(), System.currentTimeMillis());
        return true;
    }

    private Long getCooldownByTimestamp(Long lastTime) {
        long secondsLeft = ((lastTime/1000)+ MinilofeReportsPlugin.getInstance().getConfig().getInt("command-cooldowns.report")) - (System.currentTimeMillis()/1000);
        if(secondsLeft>0) return secondsLeft;
        return null;
    }
}
