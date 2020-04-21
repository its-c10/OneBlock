package me.c10coding.managers;

import me.c10coding.OneBlock;
import me.c10coding.files.AreaConfigManager;
import me.c10coding.phases.Phase;
import me.c10coding.phases.StartingPhase;
import me.c10coding.phases.ToolsPhase;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class OneBlockLogicManager {

    private AreaConfigManager ac;
    private OneBlock plugin;

    public OneBlockLogicManager(OneBlock plugin){
        this.plugin = plugin;
        ac = new AreaConfigManager(plugin);
    }

    public Location generateUniqueLocation(){

        Location freshLocation;
        List<Location> allAreaLocations = ac.getAllAreaLocations();

        freshLocation = generateNewLocation();

        if(!allAreaLocations.isEmpty()){
            for(Location l : allAreaLocations){
                /*
                The area must be within 250 blocks away from each other
                 */
                while(l.distance(freshLocation) < 250){
                    freshLocation = generateNewLocation();
                    Bukkit.broadcastMessage(freshLocation.toString());
                }
            }
        }

        return freshLocation;
    }

    public Location generateNewLocation(){

        Random rnd = new Random();
        FileConfiguration config = plugin.getConfig();
        int bounds = config.getInt("Bounds");

        int x = rnd.nextInt(bounds);
        int y = 100;
        int z = rnd.nextInt(bounds);
        World w = Bukkit.getWorld(config.getString("World"));

        return new Location(w, x, y, z);
    }

    public boolean isInsideArea(Player p){

        Location loc = ac.getPlayerAreaLocation(p);
        Location playerLocation = p.getLocation();
        int size = plugin.getConfig().getInt("Size");

        int minX = loc.getBlockX() - size;
        int maxX = loc.getBlockX() + size;
        int minZ = loc.getBlockZ() - size;
        int maxZ = loc.getBlockZ() + size;

        int playerX = playerLocation.getBlockX();
        int playerZ = playerLocation.getBlockZ();

        if(playerX < maxX && playerX > minX){
            return playerZ < maxZ && playerZ > minZ;
        }
        return false;
    }

    public boolean isInsideArea(Block b, Player p){

        ac.reloadConfig();
        Location loc = ac.getPlayerAreaLocation(p);
        Location blockLocation = b.getLocation();
        int size = plugin.getConfig().getInt("Size");

        int minX = loc.getBlockX() - size;
        int maxX = loc.getBlockX() + size;
        int minZ = loc.getBlockZ() - size;
        int maxZ = loc.getBlockZ() + size;

        int blockX = blockLocation.getBlockX();
        int blockZ = blockLocation.getBlockZ();

        if(blockX < maxX && blockX > minX){
            return blockZ < maxZ && blockZ > minZ;
        }
        return false;
    }

    public boolean isInfiniteBlock(Block b){
        List<Location> areaLocations = ac.getAllAreaLocations();
        return areaLocations.contains(b.getLocation());
    }

    private Phase phaseEnumToClass(Phase.Phases phaseKey){
        switch(phaseKey){
            case STARTING_PHASE:
                return new StartingPhase();
            case TOOLS_PHASE:
                return new ToolsPhase();
        }
        return new StartingPhase();
    }

    /*
    Gets a random material from your current or previous phases to replace the infinite block
     */
    public Material getRandomMaterial(Phase.Phases phase){

        Random rnd = new Random();
        List<Phase.Phases> phasesBefore = Phase.Phases.getPhasesBefore(phase);

        if(!phasesBefore.isEmpty()){
            List<Phase> potentialPhases = new ArrayList<>();
            List<Material> potentialMaterials = new ArrayList<>();
            /*
            For loop creates all the potential classes of phases we can have
             */
            for(Phase.Phases p : phasesBefore){
                potentialPhases.add(phaseEnumToClass(p));
            }

            /*
            Get the phases' list of materials, and then add those materials to the potential materials.
             */
            for(Phase p : potentialPhases){
                List<Material> phaseMaterials = p.getPotentialBlocks();
                for(Material mat : phaseMaterials){
                    potentialMaterials.add(mat);
                }
            }

            int randomNumMaterial = rnd.nextInt(potentialMaterials.size());
            return potentialMaterials.get(randomNumMaterial);

        }else{
            Phase startingPhase = new StartingPhase();
            List<Material> startPhaseBlockMats = startingPhase.getPotentialBlocks();
            int randomNumMaterial = rnd.nextInt(startPhaseBlockMats.size());
            return startPhaseBlockMats.get(randomNumMaterial);
        }
    }

}
