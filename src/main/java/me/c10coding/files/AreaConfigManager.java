package me.c10coding.files;

import me.c10coding.OneBlock;
import me.c10coding.phases.Phase;
import me.caleb.UtilsAPI.utils.Chat;
import me.caleb.UtilsAPI.utils.LocationSerializer;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class AreaConfigManager extends ConfigManager{

    private String prefix;

    public AreaConfigManager(OneBlock plugin) {
        super(plugin, "areas.yml");
        prefix = getPluginPrefix();
    }

    public void insertPlayer(Player p, Location loc){
        UUID playerUUID = p.getUniqueId();
        if(hasArea(playerUUID)){
            Chat.sendPlayerMessage("You already have an area!", true, p, prefix);
        }else{
            String path = "Areas." + p.getName();
            //Adds players UUID to the list of players that have a area
            List<String> playersWithAreas = config.getStringList("PlayersWithAreas");
            playersWithAreas.add(playerUUID.toString());
            config.set("PlayersWithAreas", playersWithAreas);

            LocationSerializer ls = new LocationSerializer(config);
            //ls.storeLocation(path +  ".Location");

            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            Date date = new Date();

            config.set(path + ".DateCreated", dateFormat.format(date));
            config.set(path + ".Phase", Phase.Phases.STARTING_PHASE.name());

            saveConfig();
        }
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


        return areaLocs;
    }

}