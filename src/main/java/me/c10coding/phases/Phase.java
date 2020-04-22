package me.c10coding.phases;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Phase implements PhasePotientials, PhaseCharacteristics{

    final private Phases phaseKey;
    final private String phaseName;
    final private int threshold;

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

    public Phases getKey(){
        return phaseKey;
    }

    /*
    Each Phase will return there mobs, items, and blocks different because they are enums
     */
    @Override
    public abstract List<EntityType> getSpawnableMobs();

    @Override
    public abstract List<ItemStack> getPotentialItems();

    @Override
    public abstract List<Material> getPotentialBlocks();

    @Override
    public abstract boolean hasMobs();

    public enum Phases{

        STARTING_PHASE(0),
        TOOLS_PHASE(1),
        ORE_PHASE(2);

        private final int numPhase;
        Phases(int numPhase) {
            this.numPhase = numPhase;
        }

        public static List<Phases> getAreaPhases(Phases currentPhase){

            List<Phases> phasesArr = Arrays.asList(Phases.values());
            List<Phases> phasesBefore = new ArrayList<>();
            int numPhase = currentPhase.numPhase;

            if(numPhase > 0){
                for(int x = numPhase; x >= 0; x--){
                    phasesBefore.add(phasesArr.get(x));
                }
            }
            return phasesBefore;
        }
    }
}
