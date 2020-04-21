package me.c10coding;

import me.c10coding.commands.Command;
import me.c10coding.events.OneBlockListener;
import me.c10coding.files.ConfigManager;
import me.c10coding.files.PlayersConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class OneBlock extends JavaPlugin {

    private Logger oneblockLogger = this.getLogger();
    private static OneBlock instance;
    private PlayersConfigManager pcm = new PlayersConfigManager(this);

    @Override
    public void onEnable() {

        instance = this;

        ConfigManager.validateConfigs(this);
        validateWorld();
        pcm.validatePlayersConfig();

        new OneBlockListener(this);
        new Command(this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static OneBlock getInstance(){
        return instance;
    }

    public void validateWorld(){
        FileConfiguration config = this.getConfig();
        if(config.getString("World") == null){
            oneblockLogger.severe("The \"World\" value in the configuration is empty!");
        }else{
            String worldName = config.getString("World");
            if(Bukkit.getWorld(worldName) == null){
                oneblockLogger.info("The \"World\" value in the config is not a valid world!");
            }else{
                oneblockLogger.info("The \"World\" value in the config is a valid world!");
            }
        }
    }
}
