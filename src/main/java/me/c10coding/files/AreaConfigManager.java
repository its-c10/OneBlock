package me.c10coding.files;

import me.c10coding.OneBlock;
import me.c10coding.managers.OneBlockLogicManager;
import me.c10coding.phases.Phase;
import me.c10coding.utils.Chat;
import me.c10coding.utils.LocationSerializer;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class AreaConfigManager extends ConfigManager{

    private LocationSerializer ls;

    public AreaConfigManager(OneBlock plugin) {
        super(plugin, "areas.yml");
        ls = new LocationSerializer(config);
    }

    public void insertPlayer(Player p, Location loc){

        UUID playerUUID = p.getUniqueId();
        String path = "Areas." + p.getUniqueId();
        //Adds players UUID to the list of players that have a area
        List<String> playersWithAreas = config.getStringList("PlayersWithAreas");
        playersWithAreas.add(playerUUID.toString());
        config.set("PlayersWithAreas", playersWithAreas);

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();

        config.set(path + ".Location", ls.toString(loc));
        config.set(path + ".DateCreated", dateFormat.format(date));
        config.set(path + ".Phase", Phase.Phases.STARTING_PHASE.name());
        config.set(path + ".BlockCount", 0);


        saveConfig();
    }

    public void removePlayer(Player p){

        UUID playerUUID = p.getUniqueId();
        String path = "Areas." + p.getUniqueId();
        List<String> playersWithAreas = config.getStringList("PlayersWithAreas");

        playersWithAreas.remove(playerUUID.toString());
        config.set("PlayersWithAreas", playersWithAreas);

        config.set(path, null);
        config.set(path + ".Location", null);
        config.set(path + ".DateCreated", null);
        config.set(path + ".Phase", null);
        config.set(path + ".BlockCount", null);

        saveConfig();
    }

    public boolean hasArea(UUID u){
        return getPlayerUUIDsWithAreas().contains(u);
    }

    public List<UUID> getPlayerUUIDsWithAreas(){
        List<String> playersWithAreas = config.getStringList("PlayersWithAreas");
        List<UUID> playersUUIDs = new ArrayList<>();
        for(String s : playersWithAreas){
            playersUUIDs.add(UUID.fromString(s));
        }
        return playersUUIDs;
    }

    public List<Location> getAllAreaLocations(){

        List<Location> areaLocs = new ArrayList<>();
        List<UUID> playerUUIDsWithAreas = getPlayerUUIDsWithAreas();

        for(UUID u : playerUUIDsWithAreas){
            OfflinePlayer op = Bukkit.getOfflinePlayer(u);
            String line = config.getString("Areas." + u + ".Location");
            Location loc = ls.toLocationFromLine(line);
            areaLocs.add(loc);
        }

        return areaLocs;
    }

    public Location getPlayerAreaLocation(Player p){
        UUID playerUUID = p.getUniqueId();
        Location areaLocation = null;
        if(hasArea(playerUUID)){
            String line = config.getString("Areas." + playerUUID + ".Location");
            areaLocation = ls.toLocationFromLine(line);
        }
        return areaLocation;
    }

    public Phase.Phases getPhase(Player p){
        UUID playerUUID = p.getUniqueId();
        String phaseString = config.getString("Areas." + playerUUID + ".Phase");
        return Phase.Phases.valueOf(phaseString);
    }

    public void upgradePhase(Player player, Phase phase){

        UUID playerUUID = player.getUniqueId();
        List<Phase.Phases> allPhases = Arrays.asList(Phase.Phases.values());
        //Gets the next phase after your phase
        Phase.Phases nextPhase = allPhases.get(allPhases.indexOf(phase.getKey()) + 1);
        Location playerLocation = player.getLocation();
        World playerWorld = player.getWorld();
        config.set("Areas." + playerUUID + ".Phase", nextPhase.name());

        /*
        Congratulates the person
         */
        Chat.sendPlayerMessage("&lYou have upgraded to the " + nextPhase.format() + "!", true, player, prefix);
        if(nextPhase.equals(Phase.Phases.FARMING_PHASE)){
            Chat.sendPlayerMessage("&lQuick note: You will get a water bucket this phase :)", true, player, prefix);
        }else if(nextPhase.equals(Phase.Phases.WINTER_PHASE)){
            Chat.sendPlayerMessage("&lLet it snow!", true, player, prefix);
        }else if(nextPhase.equals(Phase.Phases.NETHER_PHASE)){
            Chat.sendPlayerMessage("&lYou're in hell...", true, player, prefix);
        }else if(nextPhase.equals(Phase.Phases.ADVANCEMENT_PHASE)){
            Chat.sendPlayerMessage("&lThings are going to get pretty &oadvanced", true, player, prefix);
            //playerWorld.setBiome((int) playerLocation.getX() * 16, (int) playerLocation.getZ() * 16, Biome.BEACH);
        }else if(nextPhase.equals(Phase.Phases.DECORATION_PHASE)){
            Chat.sendPlayerMessage("&lI hope you have a house to decorate, because you're going to get a lot of decoration items and blocks this phase!", true, player, prefix);
        }else if(nextPhase.equals(Phase.Phases.END_PHASE)){
            Chat.sendPlayerMessage("&lYou are on the last phase! When you get to the last block of this phase, you will obtain materials to make a end portal!", true, player, prefix);
        }

        player.getWorld().spawnEntity(playerLocation, EntityType.SPLASH_POTION);

    }

    public boolean isLastPhase(Phase.Phases p){
        List<Phase.Phases> allPhases = Arrays.asList(Phase.Phases.values());
        return allPhases.get(allPhases.size()-1).equals(p);
    }

    public void updateBlockCount(Player p){

        OneBlockLogicManager lm = new OneBlockLogicManager(plugin);
        UUID playerUUID = p.getUniqueId();
        int currentBlockCount = config.getInt("Areas." + playerUUID + ".BlockCount");
        Phase.Phases currentPhaseEnum = getPhase(p);
        Phase currentPhase = lm.phaseEnumToClass(currentPhaseEnum);

        if(currentBlockCount == currentPhase.getThreshold() && !isLastPhase(currentPhaseEnum)){
            upgradePhase(p, currentPhase);
            currentBlockCount = 0;
        }else{
            if(isLastPhase(currentPhaseEnum) && currentBlockCount == currentPhase.getThreshold()){
                Inventory playerInv = p.getInventory();
                playerInv.addItem(new ItemStack(Material.ENDER_PORTAL_FRAME, 12));
                Chat.sendPlayerMessage("You're a boss! You have reached the threshold of the last phase. Take these end portal frames and go kill the end dragon! Good luck.", true, p, prefix);
                Chat.sendPlayerMessage("Quick tip: The last eye of ender must be facing IN the portal for the portal to properly form!", true, p, prefix);
            }
            currentBlockCount++;
        }
        config.set("Areas." + playerUUID + ".BlockCount", currentBlockCount);
        saveConfig();
    }

    public void sethomeField(Player p) {
        UUID playerUUID = p.getUniqueId();
        config.set("Areas." + p.getUniqueId() + ".HomeLocation", ls.toString(p.getLocation()));
        saveConfig();
    }

    public boolean hasHomeSet(Player p){
        UUID playerUUId = p.getUniqueId();
        String path = "Areas." + p.getUniqueId() + ".HomeLocation";
        return config.get("Areas." + p.getUniqueId() + ".HomeLocation") != null;
    }

    public Location getAreaHomeLoc(Player p){
        UUID playerUUID = p.getUniqueId();
        String path = "Areas." + p.getUniqueId() + ".HomeLocation";
        String locationLine = config.getString(path);
        return ls.toLocationFromLine(locationLine);
    }

    public void delHome(Player p) {
        UUID playerUUID = p.getUniqueId();
        config.set("Areas." + p.getUniqueId() + ".HomeLocation", null);
        saveConfig();
    }
}
