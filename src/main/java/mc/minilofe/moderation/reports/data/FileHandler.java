package mc.minilofe.moderation.reports.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import lombok.SneakyThrows;
import mc.minilofe.moderation.reports.MinilofeReportsPlugin;
import mc.minilofe.moderation.reports.core.exceptions.ReportAlreadyExistsException;
import mc.minilofe.moderation.reports.core.objects.PlayerReportRequest;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {

    private static FileHandler instance;
    private final File dataFile;
    private final Gson gson;

    @SneakyThrows
    private FileHandler() {
        this.dataFile = new File(MinilofeReportsPlugin.getInstance().getDataFolder(), "reports.json");
        this.gson = new GsonBuilder().registerTypeAdapter(PlayerReportRequest.class, new ReportsSerializer()).serializeNulls().setPrettyPrinting().create();
        if (!dataFile.exists()) {
            dataFile.createNewFile();
            setFileData(new ArrayList<>());
        }
    }

    public static FileHandler get() {
        if (instance == null) instance = new FileHandler();
        return instance;
    }

    @SneakyThrows
    public List<PlayerReportRequest> getDataFromFile() {
        FileReader reader = new FileReader(dataFile);
        Type listType = new TypeToken<ArrayList<PlayerReportRequest>>(){}.getType();
        return gson.fromJson(reader, listType);
    }

    @SneakyThrows
    public void setFileData(List<PlayerReportRequest> data) {
        FileWriter writer = new FileWriter(dataFile);
        writer.write(gson.toJson(data));
        writer.close();
    }
}
