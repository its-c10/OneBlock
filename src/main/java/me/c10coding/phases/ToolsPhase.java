package me.c10coding.phases;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ToolsPhase extends Phase{

    public ToolsPhase(){
        super(Phases.TOOLS_PHASE, 100);

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
        return null;
    }

    @Override
    public boolean hasMobs() {
        return false;
    }
}
