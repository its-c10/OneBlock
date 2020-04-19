package me.c10coding.events;

import me.c10coding.OneBlock;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class OneBlockListener implements Listener {

    private OneBlock plugin;

    public OneBlockListener(OneBlock plugin){
        this.plugin = plugin;
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e){
        
    }

}
