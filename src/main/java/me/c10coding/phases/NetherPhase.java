package me.c10coding.phases;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class NetherPhase extends Phase{

    public NetherPhase() {
        super(Phases.NETHER_PHASE, 100);
    }

    /*
        Potential blocks
         */
    public enum Blocks{

        NETHER_BRICK(Material.NETHER_BRICK),
        NETHER_FENCE(Material.NETHER_FENCE),
        SOUL_SAND(Material.SOUL_SAND),
        GLOWSTONE(Material.GLOWSTONE),
        CHISELED_PILLAR(Material.QUARTZ_BLOCK),
        QUARTZ_PILLAR(Material.QUARTZ_BLOCK),
        QUARTZ_BLOCK(Material.QUARTZ_BLOCK),
        QUARTZ_ORE(Material.QUARTZ_ORE),
        NETHERRACK(Material.NETHERRACK);

        private Material mat;
        Blocks(Material mat){
            this.mat = mat;
        }

    }

    public enum Items{

        NETHER_STAR(new ItemStack(Material.NETHER_STAR)),
        NETHER_BRICK(new ItemStack(Material.NETHER_BRICK_ITEM)),
        BLAZE_POWDER(new ItemStack(Material.BLAZE_POWDER)),
        LAVA_BUCKET(new ItemStack(Material.LAVA_BUCKET)),
        NETHER_WARTS(new ItemStack(Material.NETHER_WARTS)),
        MAGMA(new ItemStack(Material.MAGMA_CREAM)),
        GHAST_TEARS(new ItemStack(Material.GHAST_TEAR)),
        GLISTERING_MELON(new ItemStack(Material.SPECKLED_MELON)),
        GOLD_CARROT(new ItemStack(Material.GOLDEN_CARROT)),
        FERMENTED_SPIDER_EYE(new ItemStack(Material.FERMENTED_SPIDER_EYE)),
        BLAZE_ROD(new ItemStack(Material.BLAZE_ROD));

        private ItemStack item;
        Items(ItemStack item) {
            this.item = item;
        }
    }


    public enum Mobs{

        ZOMBIE_PIGMAN(EntityType.PIG_ZOMBIE),
        MAGMA_CUBE(EntityType.MAGMA_CUBE),
        BLAZE(EntityType.BLAZE);

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
