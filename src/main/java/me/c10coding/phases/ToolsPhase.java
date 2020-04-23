package me.c10coding.phases;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ToolsPhase extends Phase{

    public ToolsPhase(){
        super(Phases.TOOLS_PHASE, 50);
    }

    /*
    Potential blocks
     */
    public enum Blocks{

        LOG(Material.LOG),
        OTHER_LOG(Material.LOG_2),
        GRAVEL(Material.GRAVEL);

        Material mat;
        Blocks(Material mat){
            this.mat = mat;
        }

    }

    /*
    Potential Items
     */
    public enum Items{

        OAK_PLANK(new ItemStack(Material.WOOD, 1, (byte) 0)),
        SPRUCE_PLANK(new ItemStack(Material.WOOD, 1, (byte) 1)),
        BIRCH_PLANK(new ItemStack(Material.WOOD, 1, (byte) 2)),
        JUNGLE_PLANK(new ItemStack(Material.WOOD, 1, (byte) 3)),
        ACACIA_PLANK(new ItemStack(Material.WOOD, 1, (byte) 4)),
        DARK_OAK_PLANK(new ItemStack(Material.WOOD, 1, (byte) 5)),

        OAK_SAPLING(new ItemStack(Material.SAPLING, 1, (byte)0)),
        SPRUCE_SAPLING(new ItemStack(Material.SAPLING, 1, (byte)1)),
        BIRCH_SAPLING(new ItemStack(Material.SAPLING, 1, (byte)2)),
        JUNGLE_SAPLING(new ItemStack(Material.SAPLING, 1, (byte)3)),
        ACACIA_SAPLING(new ItemStack(Material.SAPLING, 1, (byte)4)),
        DARK_OAK_SAPLING(new ItemStack(Material.SAPLING, 1, (byte)5)),

        FLINT(new ItemStack(Material.FLINT)),
        GLASS(new ItemStack(Material.GLASS));

        private ItemStack item;
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
