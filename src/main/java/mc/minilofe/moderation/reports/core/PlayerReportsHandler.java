package mc.minilofe.moderation.reports.core;

import mc.minilofe.moderation.reports.MinilofeReportsPlugin;
import mc.minilofe.moderation.reports.core.exceptions.ReportAlreadyExistsException;
import mc.minilofe.moderation.reports.core.exceptions.ReportNotFoundException;
import mc.minilofe.moderation.reports.core.objects.PlayerReportRequest;
import mc.minilofe.moderation.reports.core.objects.ReportReason;
import mc.minilofe.moderation.reports.data.FileHandler;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class PlayerReportsHandler {

    private final static List<PlayerReportRequest> activeRequests = FileHandler.get().getDataFromFile();

    public static void createPlayerReport(Player reporter, Player reported, ReportReason reportReason) throws ReportAlreadyExistsException {
        if (requestIsPresent(reporter, reported)) throw new ReportAlreadyExistsException();

        activeRequests.add(new PlayerReportRequest(reporter.getUniqueId(), reported.getUniqueId(), reportReason));
        saveData();

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission("reports.mod")) {

                TextComponent text = new TextComponent(ChatColor.translateAlternateColorCodes('&', "&7[&eClick here to show reports GUI&7]"));
                text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("").create()));
                text.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/playerReports"));

                TextComponent toSend = new TextComponent(ChatColor.translateAlternateColorCodes('&', "&c" + reported.getName() + " &7was reported by &e" + reporter.getName() + " &7for &a" + reportReason + " " ));
                toSend.addExtra(text);
                player.spigot().sendMessage(toSend);
            }
        }
    }

    public static void acceptPlayerReport(Player moderator, PlayerReportRequest request) throws ReportNotFoundException {
        if (!activeRequests.contains(request)) throw new ReportNotFoundException();
        OfflinePlayer reportedPlayer = Bukkit.getOfflinePlayer(request.getReported());
        MinilofeReportsPlugin.sendConsoleMessage("&aReport request for &c" + reportedPlayer.getName() + "&a was accepted by " + moderator.getName() + " and said player was punished.");
        Player player = Bukkit.getPlayer(request.getReporter());
        if (player != null) MinilofeReportsPlugin.sendPlayerMessage(player, "&aYour recent report had been reviewed and handled, a punishment was issued. Thanks for reporting it.");

        if (request.getReportReason() == ReportReason.CHAT_ABUSE) {
            Bukkit.dispatchCommand(moderator, "tempmute -s " + reportedPlayer.getName() + " 1d CHAT_ABUSE");
        } else {
            Bukkit.dispatchCommand(moderator, "tempban " + reportedPlayer.getName() + " 30d " + request.getReportReason());
        }
        moderator.removePotionEffect(PotionEffectType.INVISIBILITY);
        moderator.getInventory().setItem(8, null);
        Bukkit.dispatchCommand(moderator, "lobby");
        activeRequests.remove(request);
        saveData();
    }

    public static void denyPlayerReport(Player moderator, PlayerReportRequest request) throws ReportNotFoundException {
        if (!activeRequests.contains(request)) throw new ReportNotFoundException();
        OfflinePlayer reportedPlayer = Bukkit.getOfflinePlayer(request.getReported());
        MinilofeReportsPlugin.sendConsoleMessage("&aReport request for &c" + reportedPlayer.getName() + "&a was denied by " + moderator.getName());
        Player player = Bukkit.getPlayer(request.getReporter());
        if (player != null) MinilofeReportsPlugin.sendPlayerMessage(player, "&aYour recent report was reviewed and handled. Although a punishment was not issued, we encourage you to keep on reporting for future purposes");
        moderator.removePotionEffect(PotionEffectType.INVISIBILITY);
        moderator.getInventory().setItem(8, null);
        Bukkit.dispatchCommand(moderator, "lobby");
        activeRequests.remove(request);
        saveData();
    }

    public static List<PlayerReportRequest> getActiveRequests() {
        return activeRequests;
    }

    private static boolean requestIsPresent(Player reporter, Player reported) {
        if (activeRequests.isEmpty()) return false;
        for (PlayerReportRequest req : activeRequests) {
            if (req.getReporter().equals(reporter.getUniqueId()) && req.getReported().equals(reported.getUniqueId())) return true;
        }
        return false;
    }

    public static void saveData() {
        FileHandler.get().setFileData(activeRequests);
    }
}
