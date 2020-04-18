package me.c10coding.phases;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public abstract class Phase implements PhasePotientials{

    final protected Phases phaseKey;
    final protected String phaseName;
    final protected int threshold;

    public Phase(Phases phase, int threshold){
        this.phaseName = phase.name();
        this.phaseKey = phase;
        this.threshold = threshold;
    }

    public String getName(){
        return phaseName;
    }

    public int getThreshold() {
        return threshold;
    }

    @Override
    public abstract ArrayList<Entity> getSpawnableMobs();

    @Override
    public abstract ArrayList<ItemStack> getPotentialItems();

    @Override
    public abstract ArrayList<Material> getPotentialBlocks();

    public enum Phases{

        STARTING_PHASE(1),
        TOOLS_PHASE(2);

        private final int numPhase;
        Phases(int numPhase) {
            this.numPhase = numPhase;
        }
    }
}
