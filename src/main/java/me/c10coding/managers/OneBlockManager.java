package me.c10coding.managers;

import com.sk89q.worldedit.*;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.adapter.BukkitImplAdapter;
import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import com.sk89q.worldedit.bukkit.selections.Selection;
import com.sk89q.worldedit.regions.CuboidRegion;
import me.c10coding.OneBlock;
import me.c10coding.files.AreaConfigManager;
import me.c10coding.files.ConfigManager;
import me.c10coding.utils.Chat;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OneBlockManager {

    private OneBlock plugin;
    private OneBlockLogicManager logicManager;
    private AreaConfigManager acm;
    private ConfigManager cm;
    private String prefix;
    WorldEditPlugin worldEdit = (WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");

    public OneBlockManager(OneBlock plugin){
        this.plugin = plugin;
        this.logicManager = new OneBlockLogicManager(plugin);
        this.acm = new AreaConfigManager(plugin);
        this.cm = new ConfigManager(plugin);
        this.prefix = plugin.getConfig().getString("Prefix");
    }

    public void createArea(Player p){

        if(!acm.hasArea(p.getUniqueId())){

            Location uniqueLocation = logicManager.generateUniqueLocation();
            Location teleportationLoc = new Location(uniqueLocation.getWorld(), uniqueLocation.getX(), uniqueLocation.getY() + 0.5, uniqueLocation.getZ());

            acm.insertPlayer(p, uniqueLocation);

            File playerAreaFile = new File(plugin.getDataFolder() + "/playerAreas", p.getUniqueId() + ".yml");
            FileConfiguration playerAreaConfig = YamlConfiguration.loadConfiguration(playerAreaFile);

            try {
                playerAreaFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            playerAreaConfig.set("Blocks", new ArrayList<String>());

            try {
                playerAreaConfig.save(playerAreaFile);
            } catch (IOException e) {
                e.printStackTrace();
            }

            uniqueLocation.getBlock().setType(Material.DIRT);
            p.teleport(teleportationLoc);
            Chat.sendPlayerMessage("&lGood luck! You have one block. What will you do with it?", true, p, prefix);
        }else{
            Chat.sendPlayerMessage("You already have an area! To go to it, do &6/ob home", true, p, prefix);
        }


    }

    public void deleteArea(Player p){

        if(!acm.hasArea(p.getUniqueId())){
            Chat.sendPlayerMessage("You don't have a area to delete!", true, p, prefix);
        }else{

            p.performCommand("/spawn");
            Location playerAreaLoc = acm.getPlayerAreaLocation(p);
            int size = cm.getSize();

            Location selection1 = new Location(playerAreaLoc.getWorld(), playerAreaLoc.getX() + size, playerAreaLoc.getY() - 100, playerAreaLoc.getZ() + size);
            Location selection2 = new Location(playerAreaLoc.getWorld(), playerAreaLoc.getX() - size, 256, playerAreaLoc.getZ() - size);

            Vector v1 = new Vector(selection1.getX(), selection1.getY(), selection1.getZ());
            Vector v2 = new Vector(selection2.getX(), selection2.getY(), selection2.getZ());

            CuboidSelection cs = new CuboidSelection(playerAreaLoc.getWorld(), v1, v2);
            CuboidRegion cr = new CuboidRegion(v1,v2);

            for(BlockVector block : cr){
                Block bukkitBlock = BukkitUtil.toBlock(new BlockWorldVector(BukkitUtil.getLocalWorld(playerAreaLoc.getWorld()), block));
                Bukkit.broadcastMessage(bukkitBlock.toString());
            }


            //acm.removePlayer(p);

        }
    }





}
