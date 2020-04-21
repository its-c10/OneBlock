package me.c10coding.phases;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ToolsPhase extends Phase{

    public ToolsPhase(){
        super(Phases.TOOLS_PHASE, 100);
    }

    /*
    Potential blocks
     */
    public enum Blocks{

        LOG(Material.LOG),
        OTHER_LOG(Material.LOG_2);

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
        RED_MUSHROOM(new ItemStack(Material.RED_MUSHROOM));

        ItemStack item;
        Items(ItemStack item) {
            this.item = item;
        }
    }

    @Override
    public List<Entity> getSpawnableMobs() {
        return null;
    }

    @Override
    public List<ItemStack> getPotentialItems() {
        return null;
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
