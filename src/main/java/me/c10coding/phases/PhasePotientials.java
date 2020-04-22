package me.c10coding.phases;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.util.List;


public interface PhasePotientials {

    List<EntityType> getSpawnableMobs();
    List<ItemStack> getPotentialItems();
    List<Material> getPotentialBlocks();

}
