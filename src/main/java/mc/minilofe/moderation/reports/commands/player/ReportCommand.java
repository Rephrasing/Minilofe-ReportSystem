package mc.minilofe.moderation.reports.commands.player;

import mc.minilofe.moderation.reports.MinilofeReportsPlugin;
import mc.minilofe.moderation.reports.commands.player.guis.ReportGUI;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;


public class ReportCommand implements CommandExecutor {

    private final HashMap<UUID, Long> cooldowns = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player player = (Player) sender;
        if (args.length != 1) {
            MinilofeReportsPlugin.sendPlayerMessage(player,"&cTry /report <player>");
            return true;
        }

        if (cooldowns.containsKey(player.getUniqueId())) {
            Long secondsLeft = getCooldownByTimestamp(cooldowns.get(player.getUniqueId()));
            if (secondsLeft != null) {
                MinilofeReportsPlugin.sendPlayerMessage(player,"&cYou are still cooling down. You may use this command in &e" + secondsLeft + "&c seconds");
                return true;
            }
        }

        String playerName = args[0];
        Player toBeReported = Bukkit.getPlayer(playerName);
        if (toBeReported == null) {
            MinilofeReportsPlugin.sendPlayerMessage(player,"&e" + playerName + " &cis not a valid player");
            return true;
        }

        if (toBeReported.getUniqueId().equals(player.getUniqueId())) {
            MinilofeReportsPlugin.sendPlayerMessage(player,"&cYou cannot report yourself.");
            return true;
        }


        ReportGUI gui = new ReportGUI(toBeReported);
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
