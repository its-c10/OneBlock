package me.c10coding.managers;

import me.c10coding.OneBlock;
import me.c10coding.files.AreaConfigManager;
import me.c10coding.phases.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class OneBlockLogicManager {

    private AreaConfigManager ac;
    private OneBlock plugin;
    private Random rnd = new Random();

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

        FileConfiguration config = plugin.getConfig();
        int bounds = config.getInt("Bounds");

        int x = rnd.nextInt(bounds);
        int y = 100;
        int z = rnd.nextInt(bounds);
        World w = Bukkit.getWorld(config.getString("World"));

        return new Location(w, x, y, z);
    }

    /*
        If player is inside their own area
     */
    public boolean isInsideArea(Player p){
        ac.reloadConfig();
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

    public Phase phaseEnumToClass(Phase.Phases phaseKey){
        switch(phaseKey){
            case STARTING_PHASE:
                return new StartingPhase();
            case TOOLS_PHASE:
                return new ToolsPhase();
            case ORE_PHASE:
                return new OrePhase();
            case FARMING_PHASE:
                return new FarmingPhase();
            case WINTER_PHASE:
                return new WinterPhase();
            case NETHER_PHASE:
                return new NetherPhase();
            case ADVANCEMENT_PHASE:
                return new AdvancementPhase();
            case DECORATION_PHASE:
                return new DecorationPhase();
            case END_PHASE:
                return new EndPhase();
            default:
                return new StartingPhase();
        }
    }

    /*
    Gets all the phases that your area has either surpassed or is on. Turns them into the actual class as well instead of just an enum
     */
    private List<Phase> getAreaPhases(Phase.Phases currentPhase){
        List<Phase> areaPhases = new ArrayList<>();
        List<Phase.Phases> phasesBefore = Phase.Phases.getAreaPhases(currentPhase);

        for(Phase.Phases p : phasesBefore){
            areaPhases.add(phaseEnumToClass(p));
        }
        return areaPhases;
    }

    /*
    Gets a random material from your current or previous phases to replace the infinite block
     */
    public Material getRandomMaterial(Phase.Phases phase){

        //The phases that come before the area's current phase
        List<Phase> phasesBefore = getAreaPhases(phase);
        final int chanceToObtainChest = plugin.getConfig().getInt("ChanceToObtainChest");

        if(chanceToObtainChest > rnd.nextInt(150)){
            return Material.CHEST;
        }

        if(!phasesBefore.isEmpty()){
            List<Material> potentialMaterials = new ArrayList<>();

            /*
            Get the phases' list of materials, and then add those materials to the potential materials.
             */
            for(Phase p : phasesBefore){
                List<Material> phaseMaterials = p.getPotentialBlocks();
                potentialMaterials.addAll(phaseMaterials);
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

    public List<ItemStack> getRandomChestItems(Phase.Phases phase){

        List<ItemStack> randomItems = new ArrayList<>();
        List<Phase> phasesBefore = getAreaPhases(phase);
        final int NUM_ITEMS_IN_CHEST = plugin.getConfig().getInt("NumItemsInChest");

        if(!phasesBefore.isEmpty()){

            List<ItemStack> allPotentialItems = new ArrayList<>();

            for(Phase p : phasesBefore){
                List<ItemStack> phaseItems = p.getPotentialItems();
                allPotentialItems.addAll(phaseItems);
            }

            /*
            Adds however amount of items NUM_ITEMS_IN_CHEST says to
             */
            while(randomItems.size() != NUM_ITEMS_IN_CHEST){

                int randIndex = rnd.nextInt(allPotentialItems.size());
                // 1 - 15
                ItemStack randomItem = allPotentialItems.get(randIndex);

                if(!randomItem.getType().equals(Material.WATER_BUCKET) && !randomItem.getType().equals(Material.COMPASS) && !randomItem.getType().equals(Material.LAVA_BUCKET)){
                    int randAmount = rnd.nextInt(14) + 1;
                    randomItem.setAmount(randAmount);
                }

                List<Material> randomItemMaterials = new ArrayList<>();

                for(ItemStack i : randomItems){
                    randomItemMaterials.add(i.getType());
                }

                if(!randomItemMaterials.contains(randomItem.getType())){
                    randomItems.add(randomItem);
                }
            }

        }else{
            /*
            Adds however amount of items NUM_ITEMS_IN_CHEST says to
             */
            Phase startingPhase = new StartingPhase();
            List<ItemStack> startingPhaseItems = startingPhase.getPotentialItems();
            while(randomItems.size() != NUM_ITEMS_IN_CHEST){

                int randIndex = rnd.nextInt(startingPhaseItems.size());
                int randAmount = rnd.nextInt(15);
                ItemStack randomItem = startingPhaseItems.get(randIndex);

                List<Material> randomItemMaterials = new ArrayList<>();

                for(ItemStack i : randomItems){
                    randomItemMaterials.add(i.getType());
                }

                if(!randomItemMaterials.contains(randomItem.getType())){
                    randomItems.add(randomItem);
                }

            }
        }

        return randomItems;

      }

    public List<Byte> getLog1ByteList(){

        Byte oakLog = 0;
        Byte spruceLog = 1;
        Byte birchLog = 2;
        Byte jungleLog = 3;

        List<Byte> log1Bytes = new ArrayList<>();

        log1Bytes.add(oakLog);
        log1Bytes.add(spruceLog);
        log1Bytes.add(birchLog);
        log1Bytes.add(jungleLog);

        return log1Bytes;
    }

    public List<Byte> getLog2ByteList(){

        Byte acaciaLog = 0;
        Byte darkOakLog = 1;

        List<Byte> log2Bytes = new ArrayList<>();

        log2Bytes.add(acaciaLog);
        log2Bytes.add(darkOakLog);
        return log2Bytes;

    }

    public void setChestContents(Chest c, Phase.Phases p){

        Inventory chestInv = c.getBlockInventory();
        List<ItemStack> randomItems = getRandomChestItems(p);
        for(ItemStack item : randomItems){
            int randomChestIndex = rnd.nextInt(chestInv.getSize());
            chestInv.setItem(randomChestIndex, item);
        }

    }

    /*
    Gets to see if any mobs can spawn based on the phases your area is applicable to. (Your current phase and the phases before yours)
     */
    public boolean canSpawnMobs(Phase.Phases currentPhase){
        List<Phase> areaPhases = getAreaPhases(currentPhase);
        boolean hasMobs = false;
        if(!areaPhases.isEmpty()){
            for(Phase p : areaPhases){
                if(p.hasMobs()){
                    hasMobs = true;
                }
            }
        }
        return hasMobs;
    }

    /*
    Gets a random mob type
     */
    public EntityType getRandomMob(Phase.Phases currentPhase) {
        List<Phase> areaPhases = getAreaPhases(currentPhase);
        List<EntityType> potentialMobTypes = new ArrayList<>();
        for(Phase p : areaPhases){
            if(p.hasMobs()){
                List<EntityType> mobs = p.getSpawnableMobs();
                potentialMobTypes.addAll(mobs);
            }
        }
        //Returns a random mob type based on the size of the list
        return potentialMobTypes.get(rnd.nextInt(potentialMobTypes.size()));
    }
}
