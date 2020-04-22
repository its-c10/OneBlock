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
import me.c10coding.files.PlayerAreaConfigManager;
import me.c10coding.utils.Chat;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
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
            Location teleportationLoc = new Location(uniqueLocation.getWorld(), uniqueLocation.getBlockX(), uniqueLocation.getY() + 1, uniqueLocation.getBlockZ());

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
            Chat.sendPlayerMessage("&lTo teleport back to this area, do &6/ob home", true, p, prefix);
        }else{
            Chat.sendPlayerMessage("&lYou already have an area! To go to it, do &6/ob home", true, p, prefix);
        }


    }

    public void deleteArea(Player p){

        if(!acm.hasArea(p.getUniqueId())){
            Chat.sendPlayerMessage("&lYou don't have a area to delete!", true, p, prefix);
        }else{

            p.performCommand("spawn");
            Location playerAreaLoc = acm.getPlayerAreaLocation(p);
            int size = cm.getSize();

            PlayerAreaConfigManager pacm = new PlayerAreaConfigManager(plugin, "playerAreas/" + p.getUniqueId() + ".yml");
            List<Block> allAreaBlocks = pacm.getAllBlocks();
            //Adds the infinity block itself as well
            allAreaBlocks.add(acm.getPlayerAreaLocation(p).getBlock());

            for(Block b : allAreaBlocks){
                b.setType(Material.AIR);
            }

            /*
            Removes the player from the Area config and deletes their player area file
             */
            acm.removePlayer(p);
            p.getInventory().clear();
            pacm.deleteFile();
            Chat.sendPlayerMessage("&lYour area has been deleted for good! You can create another area by doing &6/ob create", true, p, prefix);
        }
    }

    public Location getTeleportLocation(Player p){
        Location teleportationLocation = null;
        if(acm.hasArea(p.getUniqueId())) {
            if (acm.hasHomeSet(p)) {
                teleportationLocation = acm.getAreaHomeLoc(p);
            } else {
                teleportationLocation = acm.getPlayerAreaLocation(p);
                teleportationLocation.setY(teleportationLocation.getY() + 1);
                teleportationLocation.setX(teleportationLocation.getX() + 0.5);
                teleportationLocation.setZ(teleportationLocation.getZ() + 0.5);
            }

            while(!teleportationLocation.getBlock().getType().equals(Material.AIR)){
                teleportationLocation.setY(teleportationLocation.getY()+1);
            }
        }
        return teleportationLocation;
    }

    public void teleportHome(Player p) {
        if(acm.hasArea(p.getUniqueId())){
            p.teleport(getTeleportLocation(p));
            Chat.sendPlayerMessage("Teleporting you to your area...", true,p, prefix);
        }else{
            Chat.sendPlayerMessage("&lYou don't have an area!", true, p, prefix);
        }
    }

    public void setHome(Player p) {

        World playerWorld = p.getWorld();
        World configWorld;
        /*
        This gets triggered if the world value in the config is either empty or not a valid world
         */
        try{
           configWorld = acm.getWorld();
        }catch(NullPointerException n){
            Chat.sendPlayerMessage("&lThere was an error trying to set your home! Contact a administrator", true, p, prefix);
            return;
        }

        if(configWorld.equals(playerWorld)){

            Location playerLoc = p.getLocation();

            //If they are setting their home inside of their own area
            if(logicManager.isInsideArea(p)){
                Chat.sendPlayerMessage("&lYou will now spawn here whenever you die or do &6/ob home", true, p , prefix);
                acm.sethomeField(p);
            }else{
                Chat.sendPlayerMessage("&lYou are not inside your own region!", true, p, prefix);
            }

        }

    }

    public void delHome(Player p) {
        if(acm.hasArea(p.getUniqueId())){
            if(acm.hasHomeSet(p)){
                acm.delHome(p);
                Chat.sendPlayerMessage("&lThe home has been deleted! You will now spawn on top of the infinite block whenever you do or do &6/ob home",true, p, prefix);
            }else{
                Chat.sendPlayerMessage("&lYou don't have a home to delete!", true, p, prefix);
            }
        }else{
            Chat.sendPlayerMessage("&lYou don't even have a area to delete the home of silly!", true, p, prefix);
        }
    }
}
