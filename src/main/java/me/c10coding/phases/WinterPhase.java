package me.c10coding.phases;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class WinterPhase extends Phase{

    public WinterPhase(){
        super(Phases.WINTER_PHASE, 150);
    }

    /*
    Potential blocks
     */
    public enum Blocks{

        ICE_BLOCK(Material.ICE),
        LAPIS(Material.LAPIS_ORE),
        LAPIS_BLOCK(Material.LAPIS_BLOCK),
        PACKED_ICE(Material.PACKED_ICE);

        private Material mat;
        Blocks(Material mat){
            this.mat = mat;
        }

    }

    public enum Items{

        SNOW(new ItemStack(Material.SNOW)),
        SNOW_BALL(new ItemStack(Material.SNOW_BALL)),
        SNOW_BLOCK(new ItemStack(Material.SNOW_BLOCK));

        private ItemStack item;
        Items(ItemStack item) {
            this.item = item;
        }
    }


    public enum Mobs{

        SNOWMAN(EntityType.SNOWMAN),
        POLAR_BEAR(EntityType.CREEPER),
        WOLF(EntityType.WOLF);

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
