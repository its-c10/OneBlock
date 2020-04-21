package me.c10coding.files;

import me.c10coding.OneBlock;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

public class ConfigManager {

    protected Logger oneBlockLogger;
    protected OneBlock plugin;
    protected String fileName;
    protected File file;
    protected FileConfiguration config;
    public String prefix;

    public ConfigManager(OneBlock plugin, String fileName){
        this.plugin = plugin;
        this.fileName = fileName;
        this.file = new File(plugin.getDataFolder(), fileName);
        this.config = YamlConfiguration.loadConfiguration(file);
        this.oneBlockLogger = plugin.getLogger();
        this.prefix = getPluginPrefix();
    }

    public ConfigManager(OneBlock plugin){
        this.plugin = plugin;
        this.config = plugin.getConfig();
        this.oneBlockLogger = plugin.getLogger();
        this.prefix = getPluginPrefix();
    }

    protected void saveConfig(){
        try {
            config.save(file);
        }catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void reload() {
        file = new File(plugin.getDataFolder(), fileName);
        config = YamlConfiguration.loadConfiguration(file);
    }

    public static void validateConfigs(OneBlock plugin){

        File[] files = {new File(plugin.getDataFolder(), "areas.yml"), new File(plugin.getDataFolder(), "config.yml")};
        Logger logger = plugin.getLogger();

        for(File f : files){
            if(!f.exists()){
                plugin.saveResource(f.getName(), false);
                logger.info(f.getName() + " does not exist! Creating it right now...");
            }
        }

        File playerAreasFolder = new File(plugin.getDataFolder(), "playerAreas");

        if(!playerAreasFolder.exists()){
            //noinspection ResultOfMethodCallIgnored
            playerAreasFolder.mkdirs();
        }

    }

    public String getPluginPrefix(){
        return config.getString("Prefix");
    }

    protected FileConfiguration getConfig(){
        return config;
    }

    public int getSize(){
        return config.getInt("Size");
    }







}
