package me.c10coding.phases;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class StartingPhase extends Phase{

    public StartingPhase(Phases phase, int threshold) {
        super(Phases.STARTING_PHASE, 10);
    }

    @Override
    public ArrayList<Entity> getSpawnableMobs() {
        return null;
    }

    @Override
    public ArrayList<ItemStack> getPotentialItems() {
        return null;
    }

    @Override
    public ArrayList<Material> getPotentialBlocks() {
        return null;
    }
}
