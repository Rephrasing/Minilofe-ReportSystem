package mc.minilofe.moderation.reports;

import mc.minilofe.moderation.reports.commands.mod.PlayerReportsCommand;
import mc.minilofe.moderation.reports.commands.mod.listeners.PlayerReportsListener;
import mc.minilofe.moderation.reports.commands.mod.listeners.ReportChoiceListener;
import mc.minilofe.moderation.reports.commands.player.ReportCommand;
import mc.minilofe.moderation.reports.commands.player.listeners.ReportListener;
import mc.minilofe.moderation.reports.core.PlayerLastLocationHandler;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class MinilofeReportsPlugin extends JavaPlugin {


    @Override
    public void onEnable() {
        getDataFolder().mkdirs();
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new ReportListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerReportsListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerLastLocationHandler(), this);
        getServer().getPluginManager().registerEvents(new ReportChoiceListener(), this);
        registerCommands();
        sendConsoleMessage("&aSuccessfully enabled " + getName());
    }

    @Override
    public void onDisable() {
        sendConsoleMessage("&cSuccessfully disabled " + getName());
    }

    private void registerCommands() {
        getServer().getPluginCommand("report").setExecutor(new ReportCommand());
        getServer().getPluginCommand("playerReports").setExecutor(new PlayerReportsCommand());
    }


    public static MinilofeReportsPlugin getInstance() {
        return JavaPlugin.getPlugin(MinilofeReportsPlugin.class);
    }

    public static void sendPlayerMessage(CommandSender sender, String message) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    public static void sendConsoleMessage(String message) {
        getInstance().getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&cMinilofeReports&8] &7" + message));
    }
}
