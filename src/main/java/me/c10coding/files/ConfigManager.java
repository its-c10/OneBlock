package me.c10coding.files;

import me.c10coding.OneBlock;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

public class ConfigManager {

    protected Logger oneBlockLogger;
    protected OneBlock plugin;
    protected String fileName;
    protected File file;
    protected FileConfiguration config = new YamlConfiguration();
    public String prefix;

    public ConfigManager(OneBlock plugin, String fileName){
        this.plugin = plugin;
        this.fileName = fileName;
        this.file = new File(plugin.getDataFolder(), fileName);
        this.oneBlockLogger = plugin.getLogger();
        this.prefix = getPluginPrefix();
        loadConfig();
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
            plugin.getLogger().warning("Unable to save " + fileName);
        }
    }

    public void loadConfig(){
        try {
            config.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            plugin.getLogger().warning("Unable to load " + fileName);
        }
    }

    public void reloadConfig() {
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
        return plugin.getConfig().getString("Prefix");
    }

    protected FileConfiguration getConfig(){
        return config;
    }

    public int getSize(){
        return config.getInt("Size");
    }

    public World getWorld() throws NullPointerException{
        String worldValue = plugin.getConfig().getString("World");
        return Bukkit.getWorld(worldValue);
    }







}
