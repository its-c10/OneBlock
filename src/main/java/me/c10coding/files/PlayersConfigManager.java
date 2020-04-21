package me.c10coding.files;

import me.c10coding.OneBlock;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.List;
import java.util.UUID;

public class PlayersConfigManager extends ConfigManager{

    public PlayersConfigManager(OneBlock plugin) {
        super(plugin, "players.yml");
    }

    public void validatePlayersConfig(){
        reload();
        List<String> knownPlayersUUIDs = getKnownPlayers();

        if(!knownPlayersUUIDs.isEmpty()){
            for(String uuid : knownPlayersUUIDs){
                OfflinePlayer op = Bukkit.getOfflinePlayer(UUID.fromString(uuid));
                if(!config.getString("UUIDsAndPlayers." + uuid).equals(op.getName())){
                    String newName = op.getName();
                    config.set("UUIDsAndPlayers." + uuid, newName);
                }
            }
        }

        saveConfig();
        oneBlockLogger.info("Validated player UUIDs and names!");
    }

    /*
    Adds player to KnownPlayers list in config
     */
    public void addPlayer(UUID u){
        List<String> knownPlayersUUIDs = config.getStringList("KnownPlayersUUIDs");
        OfflinePlayer op = Bukkit.getOfflinePlayer(u);

        knownPlayersUUIDs.add(u.toString());

        config.set("KnownPlayersUUIDs", knownPlayersUUIDs);
        config.set("UUIDsAndPlayers." + u.toString(), op.getName());

        saveConfig();
    }

    public List<String> getKnownPlayers(){
        return config.getStringList("KnownPlayersUUIDs");
    }

}
