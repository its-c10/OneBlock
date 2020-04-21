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
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
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
            //Refreshes the config for those that just got an area
            acm.reloadConfig();
            if(acm.hasArea(p.getUniqueId())){
                //If the block is within their own region
                if(lm.isInsideArea(b, p)){
                    if(!lm.isInfiniteBlock(b)){
                        PlayerAreaConfigManager pacm = new PlayerAreaConfigManager(plugin, "playerAreas/" + p.getUniqueId() + ".yml");
                        pacm.removeBlock(b);
                    }else{

                        Phase.Phases currentPhase = acm.getPhase(p);
                        Material newBlockMat = lm.getRandomMaterial(currentPhase);
                        Material oldBlockMat = b.getType();
                        Location dropLocation = new Location(blockLocation.getWorld(), blockLocation.getX(), blockLocation.getY() + 2, blockLocation.getZ());

                        /*
                        Cancels the breaking of the block, drops the itemstack in a more desirable place to prevent it from falling
                        Also sets the block to air to simulate the breaking of a block. Then it sets it to the new block
                         */
                        e.setCancelled(true);
                        b.getWorld().dropItemNaturally(dropLocation, new ItemStack(oldBlockMat));
                        blockLocation.getBlock().setType(Material.AIR);
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
                //Refreshes the config for those that just got an area
                acm.reloadConfig();
                if(acm.hasArea(p.getUniqueId())){
                    //If the block is within their own region
                    if(lm.isInsideArea(b, p)){
                        PlayerAreaConfigManager pacm = new PlayerAreaConfigManager(plugin, "playerAreas/" + p.getUniqueId() + ".yml");
                        pacm.addBlock(b);
                    }else{
                        e.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onSandFall(EntityChangeBlockEvent event){
        if(event.getEntityType()== EntityType.FALLING_BLOCK && event.getTo()== Material.AIR){
            String blockWorldName = event.getBlock().getWorld().getName();
            String pluginWorldName = plugin.getConfig().getString("World");
            if(pluginWorldName != null){
                if(blockWorldName.equalsIgnoreCase(pluginWorldName)){
                    if(event.getBlock().getType() == Material.SAND){
                        event.setCancelled(true);
                        //Update the block to fix a visual client bug, but don't apply physics
                        event.getBlock().getState().update(false, false);
                    }
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
