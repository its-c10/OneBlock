package me.c10coding.events;

import me.c10coding.OneBlock;
import me.c10coding.files.AreaConfigManager;
import me.c10coding.files.PlayerAreaConfigManager;
import me.c10coding.files.PlayersConfigManager;
import me.c10coding.managers.OneBlockLogicManager;
import me.c10coding.managers.OneBlockManager;
import me.c10coding.phases.Phase;
import me.c10coding.utils.Chat;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class OneBlockListener implements Listener {

    private OneBlock plugin;
    private AreaConfigManager acm;
    private OneBlockLogicManager lm;
    private HashMap<UUID, ItemStack[]> playerInventoryMap = new HashMap<>();

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
                        Material oldBlockMat = e.getBlock().getType();
                        Material newBlockMat = lm.getRandomMaterial(currentPhase);
                        Location dropLocation = new Location(blockLocation.getWorld(), blockLocation.getX() + 0.5, blockLocation.getY() + 2, blockLocation.getZ() + 0.5);
                        ItemStack droppedItem = new ItemStack(oldBlockMat);
                        Random rnd = new Random();

                        if(lm.canSpawnMobs(currentPhase)){
                            final int CHANCE_SPAWN_MOB = plugin.getConfig().getInt("ChanceToSpawnMob");
                            if(CHANCE_SPAWN_MOB > rnd.nextInt(100)){
                                EntityType mobType = lm.getRandomMob(currentPhase);
                                blockLocation.getWorld().spawnEntity(dropLocation, mobType);
                            }
                        }

                        /*
                        Sets the dropped item as the proper log
                         */
                        if(oldBlockMat.equals(Material.LOG) || oldBlockMat.equals(Material.LOG_2)) {
                            MaterialData mData = e.getBlock().getState().getData();
                            droppedItem = setDroppedItem(mData);
                        }

                         /*
                        Cancels the breaking of the block, drops the itemstack in a more desirable place to prevent it from falling
                        Also sets the block to air to simulate the breaking of a block. Then it sets it to the new block
                         */
                        blockLocation.getBlock().setType(Material.AIR);
                        blockLocation.getBlock().setType(newBlockMat);
                        acm.updateBlockCount(p);
                        b.getWorld().dropItemNaturally(dropLocation, droppedItem);

                        /*
                        ALL THIS STUFF BELOW THIS COMMENT HAPPENS AFTER THE BLOCK HAS BEEN PLACED DOWN
                         */

                        /*
                           Since there aren't different materials for the different logs, I adjust the data for the log here.
                           If the new material is something other than a log, it doesn't do anything
                         */
                        if(newBlockMat.equals(Material.LOG) || newBlockMat.equals(Material.LOG_2)){

                            List<Byte> log1Bytes = lm.getLog1ByteList();
                            List<Byte> log2Bytes = lm.getLog2ByteList();

                            int randomNum;
                            Byte chosenLog;

                            if(newBlockMat.equals(Material.LOG)){
                                randomNum = rnd.nextInt(log1Bytes.size());
                                chosenLog = log1Bytes.get(randomNum);
                            }else{
                                randomNum = rnd.nextInt(log2Bytes.size());
                                chosenLog = log2Bytes.get(randomNum);
                            }
                            blockLocation.getBlock().setData(chosenLog);
                        }else if(newBlockMat.equals(Material.CHEST)){
                            Block block = blockLocation.getBlock();
                            if(block.getState() instanceof InventoryHolder){
                                InventoryHolder ih = (InventoryHolder) block.getState();
                                if(ih instanceof Chest){
                                    Chest chestBlock = (Chest) ih;
                                    lm.setChestContents(chestBlock, currentPhase);
                                }
                            }
                        }else if(newBlockMat.equals(Material.DIRT)){
                            Block newBlock = blockLocation.getBlock();
                            newBlock.setData((byte)rnd.nextInt(3));
                        }
                        e.setCancelled(true);
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

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    private void onSandFall(EntityChangeBlockEvent event){
        if(event.getEntityType()==EntityType.FALLING_BLOCK && event.getTo()==Material.AIR){
            if(event.getBlock().getType().equals(Material.SAND) || event.getBlock().getType().equals(Material.GRAVEL)){
                event.setCancelled(true);
                //Update the block to fix a visual client bug, but don't apply physics
                event.getBlock().getState().update(false, false);
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

     /*
    Stores their items upon death in a hashmap
     */
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e){
        boolean keepInv = plugin.getConfig().getBoolean("KeepInventory");
        if(keepInv){
            /*
                If the world is null, then the config value for "World" hasn't been set.
            */
            World configWorld;
            try{
                configWorld = acm.getWorld();
            }catch(NullPointerException n){
                return;
            }

            if(e.getEntity() != null){
                Player p = e.getEntity();
                if(p.getLocation().getWorld().equals(configWorld)) {
                    Inventory playerInv = p.getInventory();
                    playerInventoryMap.put(p.getUniqueId(), playerInv.getContents());
                    e.getKeepLevel();
                }
            }
        }
    }

    /*
        Gives the items back upon respawn if they're in the hashmap
     */
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e){
        Player p = e.getPlayer();
        if(playerInventoryMap.containsKey(p.getUniqueId())){
            ItemStack[] playerContents = playerInventoryMap.get(p.getUniqueId());
            p.getInventory().setContents(playerContents);
            playerInventoryMap.remove(p.getUniqueId());

            OneBlockManager obm = new OneBlockManager(plugin);
            e.setRespawnLocation(obm.getTeleportLocation(p));

        }
    }



    public ItemStack setDroppedItem(MaterialData md){
        return md.getItemType().equals(Material.LOG) ? new ItemStack(Material.LOG, 1, md.getData()) : new ItemStack(Material.LOG_2, 1, md.getData());
    }



}
