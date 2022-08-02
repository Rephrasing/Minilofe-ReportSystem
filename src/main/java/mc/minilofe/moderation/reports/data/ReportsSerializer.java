package mc.minilofe.moderation.reports.data;

import com.google.gson.*;
import mc.minilofe.moderation.reports.core.objects.PlayerReportRequest;
import mc.minilofe.moderation.reports.core.objects.ReportReason;

import java.lang.reflect.Type;
import java.util.UUID;

public class ReportsSerializer implements JsonDeserializer<PlayerReportRequest>, JsonSerializer<PlayerReportRequest> {

    @Override
    public PlayerReportRequest deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();
        UUID reporter_uid = UUID.fromString(object.get("reporter_uid").getAsString());
        UUID reported_uid = UUID.fromString(object.get("reported_uid").getAsString());
        ReportReason reason = ReportReason.valueOf(object.get("report_reason").getAsString());
        long timestamp = object.get("timestamp").getAsLong();

        return new PlayerReportRequest(reporter_uid, reported_uid, reason, timestamp);
    }

    @Override
    public JsonElement serialize(PlayerReportRequest src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        object.addProperty("reporter_uid", src.getReporter().toString());
        object.addProperty("reported_uid", src.getReported().toString());
        object.addProperty("report_reason", src.getReportReason().name());
        object.addProperty("timestamp", src.getTimestamp());
        return object;
    }

}
