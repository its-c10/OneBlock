package me.c10coding;

import org.bukkit.plugin.java.JavaPlugin;

public final class OneBlock extends JavaPlugin {

    private static OneBlock instance;

    @Override
    public void onEnable() {
        instance = this;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static OneBlock getInstance(){
        return instance;
    }
}
