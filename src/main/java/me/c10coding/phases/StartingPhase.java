package me.c10coding.phases;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class StartingPhase extends Phase{

    public StartingPhase() {
        super(Phases.STARTING_PHASE);
    }

    /*
    Potential blocks
     */
    public enum Blocks{

        DIRT(Material.DIRT),
        GRASS(Material.GRASS),
        CLAY(Material.CLAY),
        SAND(Material.SAND),
        MELON_BLOCK(Material.MELON_BLOCK);

        Material mat;
        Blocks(Material mat){
            this.mat = mat;
        }

    }

    /*
    Potential Items
     */
    public enum Items{

        GRASS(new ItemStack(Material.GRASS)),
        SUGAR_CANE(new ItemStack(Material.SUGAR_CANE)),
        SUGAR(new ItemStack(Material.SUGAR)),
        ROSE(new ItemStack(Material.RED_ROSE)),
        WEB(new ItemStack(Material.WEB)),
        BROWN_MUSHROOM(new ItemStack(Material.BROWN_MUSHROOM)),
        RED_MUSHROOM(new ItemStack(Material.RED_MUSHROOM)),
        VINE(new ItemStack(Material.VINE)),
        COOKIE(new ItemStack(Material.COOKIE));

        ItemStack item;
        Items(ItemStack item) {
            this.item = item;
        }
    }

    @Override
    public List<EntityType> getSpawnableMobs() {
        return null;
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
        return false;
    }
}
