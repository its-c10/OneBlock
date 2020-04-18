package me.c10coding.phases;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public interface PhasePotientials {

    ArrayList<Entity> getSpawnableMobs();
    ArrayList<ItemStack> getPotentialItems();
    ArrayList<Material> getPotentialBlocks();

}
