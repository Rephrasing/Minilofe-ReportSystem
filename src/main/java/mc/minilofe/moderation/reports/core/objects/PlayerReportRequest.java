package mc.minilofe.moderation.reports.core.objects;

import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.UUID;

@Getter
public class PlayerReportRequest {

    private final UUID reporter, reported;
    private final ReportReason reportReason;
    private final long timestamp;

    public PlayerReportRequest(UUID reporter, UUID reported, ReportReason reportReason) {
        this.reporter = reporter;
        this.reported = reported;
        this.reportReason = reportReason;
        this.timestamp = System.currentTimeMillis();
    }

    public PlayerReportRequest(UUID reporter, UUID reported, ReportReason reportReason, long timestamp) {
        this.reporter = reporter;
        this.reported = reported;
        this.reportReason = reportReason;
        this.timestamp = timestamp;
    }
}
