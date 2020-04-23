package me.c10coding.phases;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class DecorationPhase extends Phase{

    public DecorationPhase(){
        super(Phases.DECORATION_PHASE, 300);
    }

    /*
    Potential blocks
     */
    public enum Blocks{

        STAINED_CLAY(Material.STAINED_CLAY),
        WOOL(Material.WOOL);

        private Material mat;
        Blocks(Material mat){
            this.mat = mat;
        }

    }

    public enum Items{

        BANNER(new ItemStack(Material.BOOKSHELF)),
        ITEM_FRAME(new ItemStack(Material.ITEM_FRAME)),
        STAINED_GLASS_PANE(new ItemStack(Material.STAINED_GLASS_PANE)),
        STAINED_GLASS(new ItemStack(Material.STAINED_GLASS)),
        FLOWER_POT(new ItemStack(Material.FLOWER_POT_ITEM)),
        WHITE_CARPET(new ItemStack(Material.CARPET, 1, (byte) 0)),
        ORANGE_CARPET(new ItemStack(Material.CARPET, 1, (byte) 1)),
        MAGENTA_CARPET(new ItemStack(Material.CARPET, 1, (byte) 2)),
        LIGHT_BLUE_CARPET(new ItemStack(Material.CARPET, 1, (byte) 3)),
        YELLOW_CARPET(new ItemStack(Material.CARPET, 1, (byte) 4)),
        LIME_CARPET(new ItemStack(Material.CARPET, 1, (byte) 5)),
        PINK_CARPET(new ItemStack(Material.CARPET, 1, (byte) 6)),
        GRAY_CARPET(new ItemStack(Material.CARPET, 1, (byte) 7)),
        LIGHT_GRAY_CARPET(new ItemStack(Material.CARPET, 1, (byte) 8)),
        CYAN_CARPET(new ItemStack(Material.CARPET, 1, (byte) 9)),
        PURPLE_CARPET(new ItemStack(Material.CARPET, 1, (byte) 10)),
        BLUE_CARPET(new ItemStack(Material.CARPET, 1, (byte) 11)),
        BROWN_CARPET(new ItemStack(Material.CARPET, 1, (byte) 12)),
        GREEN_CARPET(new ItemStack(Material.CARPET, 1, (byte) 13)),
        RED_CARPET(new ItemStack(Material.CARPET, 1, (byte) 14)),
        BLACK_CARPET(new ItemStack(Material.CARPET, 1, (byte) 15));

        private ItemStack item;
        Items(ItemStack item) {
            this.item = item;
        }
    }


    public enum Mobs{

        CAT(EntityType.OCELOT);

        EntityType mobType;
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
