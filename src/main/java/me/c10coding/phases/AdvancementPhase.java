package me.c10coding.phases;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class AdvancementPhase extends Phase{

    public AdvancementPhase(){
        super(Phases.ADVANCEMENT_PHASE);
    }

    /*
    Potential blocks
     */
    public enum Blocks{

        REDSTONE_BLOCK(Material.REDSTONE_BLOCK),
        GOLD_BLOCK(Material.GOLD_BLOCK),
        GOLD_ORE(Material.GOLD_ORE),
        DIAMOND_ORE(Material.DIAMOND_ORE),
        EMERALD_ORE(Material.EMERALD_ORE),
        REDSTONE_LAMP(Material.REDSTONE_LAMP_OFF),
        DAYLIGHT_SENSOR(Material.DAYLIGHT_DETECTOR),
        JUKEBOX(Material.JUKEBOX),
        NOTEBLOCK(Material.NOTE_BLOCK),
        OBSIDIAN(Material.OBSIDIAN),
        REDSTONE_ORE(Material.REDSTONE_ORE);

        private Material mat;
        Blocks(Material mat){
            this.mat = mat;
        }

    }

    public enum Items{

        REDSTONE_TORCH(new ItemStack(Material.REDSTONE_TORCH_OFF)),
        REDSTONE_COMPARATOR(new ItemStack(Material.REDSTONE_COMPARATOR)),
        BREWING_STAND(new ItemStack(Material.BREWING_STAND_ITEM)),
        RAILS(new ItemStack(Material.RAILS)),
        POWERED_RAILS(new ItemStack(Material.POWERED_RAIL)),
        DETECTOR_RAILS(new ItemStack(Material.DETECTOR_RAIL)),
        GOLD_INGOT(new ItemStack(Material.GOLD_INGOT)),
        COMPASS(new ItemStack(Material.COMPASS)),
        DISPENSER(new ItemStack(Material.DISPENSER)),
        EYE_OF_ENDER(new ItemStack(Material.EYE_OF_ENDER)),
        ENDER_PEARL(new ItemStack(Material.ENDER_PEARL)),
        FIREWORKS(new ItemStack(Material.FIREWORK)),
        CAULDRON(new ItemStack(Material.CAULDRON)),
        PISTON(new ItemStack(Material.PISTON_BASE)),
        SADDLE(new ItemStack(Material.SADDLE)),
        STICKY_PISTON(new ItemStack(Material.PISTON_STICKY_BASE)),
        DIAMOND(new ItemStack(Material.DIAMOND));

        private ItemStack item;
        Items(ItemStack item) {
            this.item = item;
        }
    }


    public enum Mobs{

        WITCH(EntityType.WITCH),
        SQUID(EntityType.SQUID),
        HORSE(EntityType.HORSE),
        VILLAGER(EntityType.VILLAGER),
        ENDERMAN(EntityType.ENDERMAN);

        private EntityType mobType;
        Mobs(EntityType e){
            this.mobType = e;
        }

    }

    @Override
    public List<EntityType> getSpawnableMobs() {
        List<EntityType> potentialMobs = new ArrayList<>();
        for(Mobs currentMob : Mobs.values()){
            potentialMobs.add(currentMob.mobType);
        }
        return potentialMobs;
    }

    @Override
    public List<ItemStack> getPotentialItems() {
        List<ItemStack> potentialItems = new ArrayList<>();
        for(Items currentItem : Items.values()){
            potentialItems.add(currentItem.item);
        }
        return potentialItems;
    }

    @Override
    public List<Material> getPotentialBlocks() {
        List<Material> potentialBlocks = new ArrayList<>();
        for(Blocks currentBlock : Blocks.values()){
            potentialBlocks.add(currentBlock.mat);
        }
        return potentialBlocks;
    }

    @Override
    public boolean hasMobs() {
        return true;
    }
}
