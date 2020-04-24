package me.c10coding.phases;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class OrePhase extends Phase{

    public OrePhase() {
        super(Phases.ORE_PHASE);
    }

    /*
    Potential blocks
     */
    public enum Blocks{

        STONE(Material.STONE),
        COBBLESTONE(Material.COBBLESTONE),
        COAL_ORE(Material.COAL_ORE),
        COAL_BLOCK(Material.COAL_BLOCK),
        IRON_ORE(Material.IRON_ORE),
        LEAVES(Material.LEAVES),
        LEAVES_2(Material.LEAVES_2),
        BRICKS(Material.BRICK);

        private Material mat;
        Blocks(Material mat){
            this.mat = mat;
        }

    }

    public enum Items{

        COAL(new ItemStack(Material.COAL)),
        IRON(new ItemStack(Material.IRON_INGOT)),
        WHEAT(new ItemStack(Material.WHEAT)),
        STICK(new ItemStack(Material.STICK)),
        PAPER(new ItemStack(Material.PAPER)),
        FLOWER(new ItemStack(Material.DOUBLE_PLANT, 1, (byte) 0)),
        FLOWER_2(new ItemStack(Material.DOUBLE_PLANT, 1, (byte) 1)),
        FLOWER_3(new ItemStack(Material.DOUBLE_PLANT, 1, (byte) 2)),
        FLOWER_4(new ItemStack(Material.DOUBLE_PLANT, 1, (byte) 3)),
        FLOWER_5(new ItemStack(Material.DOUBLE_PLANT, 1, (byte) 4)),
        FLOWER_6(new ItemStack(Material.DOUBLE_PLANT, 1, (byte) 5));

        private ItemStack item;
        Items(ItemStack item) {
            this.item = item;
        }
    }


    public enum Mobs{

        ZOMBIE(EntityType.SILVERFISH),
        SLIME(EntityType.SLIME),
        PIG(EntityType.PIG);

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
