package me.c10coding.files;

import me.c10coding.OneBlock;
import me.c10coding.phases.Phase;
import me.c10coding.utils.LocationSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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

        saveConfig();
    }

    public boolean hasArea(UUID u){
        Bukkit.broadcastMessage(getPlayerUUIDsWithAreas().toString());
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
            Location loc = ls.toLocationFromPath("Areas." + u + ".Location");
            areaLocs.add(loc);
        }

        return areaLocs;
    }

    public Location getPlayerAreaLocation(Player p){
        UUID playerUUID = p.getUniqueId();
        Location areaLocation = null;
        if(hasArea(playerUUID)){
            areaLocation = ls.toLocationFromPath("Areas." + playerUUID + ".Location");
        }
        return areaLocation;
    }

    public Phase.Phases getPhase(Player p){
        UUID playerUUID = p.getUniqueId();
        String phaseString = config.getString("Areas." + playerUUID + ".Phase");
        return Phase.Phases.valueOf(phaseString);
    }

}
