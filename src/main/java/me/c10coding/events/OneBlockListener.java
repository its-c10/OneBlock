package me.c10coding.events;

import me.c10coding.OneBlock;
import me.c10coding.files.AreaConfigManager;
import me.c10coding.files.PlayerAreaConfigManager;
import me.c10coding.files.PlayersConfigManager;
import me.c10coding.managers.OneBlockLogicManager;
import me.c10coding.managers.OneBlockManager;
import me.c10coding.phases.Phase;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class OneBlockListener implements Listener {

    private OneBlock plugin;
    private AreaConfigManager acm;
    private OneBlockLogicManager lm;

    public OneBlockListener(OneBlock plugin){
        this.plugin = plugin;
        this.acm = new AreaConfigManager(plugin);
        this.lm = new OneBlockLogicManager(plugin);
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e){
        if(!e.isCancelled()){
            Player p = e.getPlayer();
            Block b = e.getBlock();
            Location blockLocation = b.getLocation();
            if(acm.hasArea(p.getUniqueId())){
                //If the block is within their own region
                if(lm.isInsideArea(b, p)){
                    if(!lm.isInfiniteBlock(b)){
                        Bukkit.broadcastMessage("not infinite block");

                        PlayerAreaConfigManager pacm = new PlayerAreaConfigManager(plugin, "playerAreas/" + p.getUniqueId() + ".yml");
                        pacm.removeBlock(b);
                    }else{
                        Bukkit.broadcastMessage("was infinited block");
                        Phase.Phases currentPhase = acm.getPhase(p);
                        Material newBlockMat = lm.getRandomMaterial(currentPhase);
                        Material oldBlockMat = b.getType();

                        b.getWorld().dropItemNaturally(blockLocation, new ItemStack(oldBlockMat));
                        b.breakNaturally();
                        blockLocation.getBlock().setType(newBlockMat);
                    }
                }else{
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e){
        if(!e.isCancelled()){
            if(e.canBuild()){
                Player p = e.getPlayer();
                Block b = e.getBlock();
                if(acm.hasArea(p.getUniqueId())){
                    //If the block is within their own region
                    if(lm.isInsideArea(b, p)){
                        Bukkit.broadcastMessage("testing");
                        PlayerAreaConfigManager pacm = new PlayerAreaConfigManager(plugin, "playerAreas/" + p.getUniqueId() + ".yml");
                        pacm.addBlock(b);
                    }else{
                        Bukkit.broadcastMessage("testin2g");
                        e.setCancelled(true);
                    }
                    Bukkit.broadcastMessage("testing3");
                }
            }
        }
    }

    /*
    Validates the players UUID and name. This saves headaches down the road.
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){

        PlayersConfigManager pc = new PlayersConfigManager(plugin);
        List<String> knownPlayersUUIDs = pc.getKnownPlayers();
        Player p = e.getPlayer();

        if(!knownPlayersUUIDs.contains(p.getUniqueId().toString())){
            pc.addPlayer(e.getPlayer().getUniqueId());
        }
    }

}
