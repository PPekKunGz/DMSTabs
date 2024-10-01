package net.dimension.Config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.dimension.DMSTabs;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;

public class Config {
    public static Config INSTANCE = new Config();
    public int configVersion; // v0

    // v1
    public boolean online_player; // false by default
    public String onlinePlayerText; // "Players Online: {online}/{max}" by default
    public String header; // null
    public String footer; // null

    // v2
    public int refreshTickInterval; // 0
    public boolean enabled; // false

    public void setDefaults() throws IOException {
        // v1 defaults
        if (this.configVersion < 1) {
            this.configVersion = 1;
            this.header = "§f⛵\n\n\n\n";
            this.footer = "§fʙʏ§7. §fᴀᴘʟᴀᴄᴇ ꜱᴛᴜᴅɪᴏ§8,\n§dᴅɪᴍᴇɴꜱɪᴏɴ ꜱᴛᴜᴅɪᴏ\n";
            this.save();
            DMSTabs.LOGGER.info("Migrated configuration file to v1");
        }

        // v2 defaults
        if (this.configVersion < 2) {
            this.configVersion = 2;
            this.enabled = true;
            this.refreshTickInterval = 20;
            this.save();
            DMSTabs.LOGGER.info("Migrated configuration file to v2");
        }

        // v3 defaults for online player config
        if (this.configVersion < 3) {
            this.configVersion = 3;
            this.online_player = false; // Default is not to show player count
            this.onlinePlayerText = "Players Online: {online}"; // Default text format
            this.save();
            DMSTabs.LOGGER.info("Migrated configuration file to v3");
        }
    }


    public void save() throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        File config = new File(FabricLoader.getInstance().getConfigDirectory(), "dmstabs.json");
        try (FileWriter file = new FileWriter(config)) {
            file.write(gson.toJson(this));
        }
    }

    public void load() throws IOException {
        File config1 = new File(FabricLoader.getInstance().getConfigDirectory(), "dmstabs.json");

        // FILE DOES NOT EXIST
        if (!config1.exists()) {
            this.save();
        }

        if (config1.isDirectory()) {
            if (config1.delete()) {
                this.save(); // v0
            }
        }

        // FILE EXISTS NOW
        try (FileReader reader = new FileReader(config1)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            Config readConfig = gson.fromJson(reader, Config.class);

            // v0
            this.configVersion = readConfig.configVersion;

            // v1
            this.header = readConfig.header;
            this.footer = readConfig.footer;

            // v2
            this.refreshTickInterval = readConfig.refreshTickInterval;
            this.enabled = readConfig.enabled;

            // v3
            this.online_player = readConfig.online_player;
            this.onlinePlayerText = readConfig.onlinePlayerText;

            setDefaults();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
