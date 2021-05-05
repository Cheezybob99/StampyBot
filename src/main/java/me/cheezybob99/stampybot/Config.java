package me.cheezybob99.stampybot;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.util.List;

public class Config {

    private final String token;
    private final String guild;
    private final String adminRole;
    private final List<String> statsRoles;
    private final List<String> alertStatus;
    private final String statusChannel;

    public Config(String token, String guild, String adminRole, List<String> statsRoles, List<String> alertStatus, String statusChannel) {

        this.token = token;
        this.guild = guild;
        this.adminRole = adminRole;
        this.statsRoles = statsRoles;
        this.alertStatus = alertStatus;
        this.statusChannel = statusChannel;

    }

    public static Config loadConfig(String name, Class<?> clazz) throws IOException {

        File file = new File(name);
        if (!file.exists()) {
            file.createNewFile();
            InputStream from = Thread.currentThread().getContextClassLoader().getResourceAsStream(name);
            try (InputStream input = from) {
                try (FileOutputStream output = new FileOutputStream(file)) {
                    byte[] b = new byte[8192];
                    int length;
                    while ((length = input.read(b)) > 0)
                        output.write(b, 0, length);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        JsonReader jsonReader = new JsonReader(new FileReader(file));
        Gson gson = new Gson();
        return gson.fromJson(jsonReader, clazz);

    }

    public String getToken() {
        return token;
    }

    public String getGuild() {
        return guild;
    }

    public String getAdminRole() {
        return adminRole;
    }

    public List<String> getStatsRoles() {
        return statsRoles;
    }

    public List<String> getAlertStatus() {
        return alertStatus;
    }

    public String getStatusChannel() {
        return statusChannel;
    }

}
