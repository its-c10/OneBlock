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
import org.bukkit.block.Chest;
import org.bukkit.entity.EntityType;
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

import java.util.*;

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
            World blockWorld = blockLocation.getWorld();
            World configWorld = acm.getWorld();
            //Refreshes the config for those that just got an area
            acm.reloadConfig();
            if(blockWorld.equals(configWorld)){
                if(acm.hasArea(p.getUniqueId())){
                    //If the block is within their own region
                    if(lm.isInsideArea(b, p)){
                        if(!lm.isInfiniteBlock(b)){
                            PlayerAreaConfigManager pacm = new PlayerAreaConfigManager(plugin, "playerAreas/" + p.getUniqueId() + ".yml");
                            pacm.removeBlock(b);
                        }else{

                            /*Velocity test */

                            Phase.Phases currentPhase = acm.getPhase(p);
                            Material oldBlockMat = e.getBlock().getType();
                            Material newBlockMat = lm.getRandomMaterial(currentPhase);
                            Location dropLocation = new Location(blockLocation.getWorld(), blockLocation.getX() + 0.5, blockLocation.getY() + 2, blockLocation.getZ() + 0.5);
                            Random rnd = new Random();
                            ItemStack droppedItem = new ItemStack(oldBlockMat);

                            /*
                                Fixes the issue when you're on top of the chest and you go through the chest while trying to mine it.
                             */
                            if(oldBlockMat.equals(Material.CHEST)){

                                Location playerLoc = p.getLocation();
                                Location locUnderPlayer = new Location(playerLoc.getWorld(),playerLoc.getBlockX(), playerLoc.getBlockY(), playerLoc.getBlockZ());
                                Block blockUnderPlayer = locUnderPlayer.getBlock();

                                if(blockUnderPlayer.equals(b)){
                                    playerLoc.setY(playerLoc.getY()+0.18);
                                    p.teleport(playerLoc);
                                }
                            }

                            if(lm.canSpawnMobs(currentPhase)){
                                final int CHANCE_SPAWN_MOB = plugin.getConfig().getInt("ChanceToSpawnMob");
                                int randomNum = rnd.nextInt(100);
                                if(CHANCE_SPAWN_MOB > randomNum){
                                    EntityType mobType = lm.getRandomMob(currentPhase);
                                    blockLocation.getWorld().spawnEntity(dropLocation, mobType);
                                }
                            }

                        /*
                        Sets the dropped item as the proper block
                         */
                        List<Material> multiDrops = Arrays.asList(Material.MELON_BLOCK, Material.CLAY, Material.COAL_ORE, Material.DIAMOND_ORE, Material.EMERALD_ORE, Material.LAPIS_ORE, Material.QUARTZ_ORE, Material.REDSTONE_ORE, Material.GLOWING_REDSTONE_ORE);
                            if(multiDrops.contains(e.getBlock().getType())){
                                Collection<ItemStack> drops = blockLocation.getBlock().getDrops();
                                for(ItemStack drop : drops){
                                    b.getWorld().dropItem(dropLocation, drop);
                                }
                            }else{
                                MaterialData mData = e.getBlock().getState().getData();
                                droppedItem = setDroppedItem(mData);
                                b.getWorld().dropItemNaturally(dropLocation, droppedItem);
                            }
                         /*
                        Cancels the breaking of the block, drops the itemstack in a more desirable place to prevent it from falling
                        Also sets the block to air to simulate the breaking of a block. Then it sets it to the new block
                         */
                            blockLocation.getBlock().setType(newBlockMat);
                            acm.updateBlockCount(p);
                        /*
                        ALL THIS STUFF BELOW THIS COMMENT HAPPENS AFTER THE BLOCK HAS BEEN PLACED DOWN
                         */

                        /*
                           Since there aren't different materials for the different logs, I adjust the data for the block here.
                         */
                            Block newBlock = blockLocation.getBlock();
                            if(newBlockMat.equals(Material.LOG)) {
                                newBlock.setData((byte) rnd.nextInt(3));
                            }else if(newBlockMat.equals(Material.LOG_2)){
                                newBlock.setData((byte) rnd.nextInt(1));
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
                                newBlock.setData((byte)rnd.nextInt(3));
                            }else if(newBlockMat.equals(Material.STONE)){
                                newBlock.setData((byte)rnd.nextInt(6));
                            }else if(newBlockMat.equals(Material.WOOL)){
                                newBlock.setData((byte)rnd.nextInt(15));
                            }else if(newBlockMat.equals(Material.STAINED_CLAY)){
                                newBlock.setData((byte)rnd.nextInt(15));
                            }else if(newBlockMat.equals(Material.STAINED_GLASS_PANE)){
                                newBlock.setData((byte)rnd.nextInt(15));
                            }else if(newBlockMat.equals(Material.STAINED_GLASS)){
                                newBlock.setData((byte)rnd.nextInt(15));
                            }else if(newBlockMat.equals(Material.LEAVES)){
                                newBlock.setData((byte)rnd.nextInt(3));
                            }else if(newBlockMat.equals(Material.LEAVES_2)){
                                newBlock.setData((byte)rnd.nextInt(1));
                            }
                            e.setCancelled(true);
                        }
                    }else{
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
                }else{
                    e.setCancelled(true);
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
            e.setRespawnLocation(obm.getInfiteBlockLocation(p));

        }
    }



    public ItemStack setDroppedItem(MaterialData md){
        return new ItemStack(md.getItemType(), 1, md.getData());
    }



}
