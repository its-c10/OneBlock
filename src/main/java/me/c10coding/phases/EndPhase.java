package me.c10coding.phases;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class EndPhase extends Phase{

    public EndPhase(){
        super(Phases.END_PHASE, 450);
    }

    /*
        Potential blocks
     */
    public enum Blocks{

        SPONGE(Material.SPONGE),
        ENDSTONE(Material.ENDER_STONE);

        private Material mat;
        Blocks(Material mat){
            this.mat = mat;
        }

    }

    public enum Items{

        NAME_TAG(new ItemStack(Material.NAME_TAG)),
        MAP(new ItemStack(Material.MAP)),
        PAINTING(new ItemStack(Material.PAINTING));

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
