package me.c10coding.utils;

import me.c10coding.OneBlock;
import me.c10coding.files.AreaConfigManager;
import me.c10coding.files.ConfigManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class OneBlockManager {

    private OneBlock plugin;

    public OneBlockManager(OneBlock plugin){
        this.plugin = plugin;
    }

    public void createArea(Player p){
        AreaConfigManager am = new AreaConfigManager(plugin);
        //am.insertPlayer(p, );
    }





}
