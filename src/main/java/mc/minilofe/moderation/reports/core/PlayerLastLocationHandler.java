package mc.minilofe.moderation.reports.core;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.UUID;

public class PlayerLastLocationHandler implements Listener {

    private static final HashMap<UUID, Location> lastLocations = new HashMap<>();

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        lastLocations.put(event.getPlayer().getUniqueId(), event.getPlayer().getLocation());
    }

    public static Location getLastLocation(UUID uuid) {
        if (!lastLocations.containsKey(uuid)) return null;
        return lastLocations.get(uuid);
    }



}
