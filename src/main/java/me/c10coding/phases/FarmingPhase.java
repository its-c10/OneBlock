package me.c10coding.phases;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class FarmingPhase extends Phase{

    public FarmingPhase() {
        super(Phases.FARMING_PHASE);
    }

    /*
    Potential blocks
     */
    public enum Blocks{

        GRANITE(Material.STONE),
        POLISHED_GRANITE(Material.STONE),
        DIORITE(Material.STONE),
        POLISHED_DIORITE(Material.STONE),
        ANDESITE(Material.STONE),
        POLISHED_ANDESITE(Material.STONE),
        HAY(Material.HAY_BLOCK),
        MYCELIUM(Material.MYCEL);

        private Material mat;
        Blocks(Material mat){
            this.mat = mat;
        }

    }

    public enum Items{

        MELON_SEEDS(new ItemStack(Material.MELON_SEEDS)),
        MELON(new ItemStack(Material.MELON)),
        PUMPKIN_SEEDS(new ItemStack(Material.PUMPKIN_SEEDS)),
        SEEDS(new ItemStack(Material.SEEDS)),
        POTATO(new ItemStack(Material.POTATO)),
        CARROT(new ItemStack(Material.CARROT)),
        WATER_BUCKET(new ItemStack(Material.WATER_BUCKET)),
        LILY(new ItemStack(Material.WATER_LILY)),
        FEATHER(new ItemStack(Material.FEATHER)),
        CACTUS(new ItemStack(Material.CACTUS)),
        FISHING_ROD(new ItemStack(Material.FISHING_ROD));

        private ItemStack item;
        Items(ItemStack item) {
            this.item = item;
        }
    }


    public enum Mobs{

        SKELETON(EntityType.SKELETON),
        SPIDER(EntityType.SPIDER),
        SHEEP(EntityType.SHEEP),
        COW(EntityType.COW),
        MUSHROOM_COW(EntityType.MUSHROOM_COW),
        CHICKEN(EntityType.CHICKEN);

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
