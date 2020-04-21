package me.c10coding.phases;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
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

    protected String getName(){
        return phaseName;
    }

    protected int getThreshold() {
        return threshold;
    }

    /*
    Each Phase will return there mobs, items, and blocks different because they are enums
     */
    @Override
    public abstract List<Entity> getSpawnableMobs();

    @Override
    public abstract List<ItemStack> getPotentialItems();

    @Override
    public abstract List<Material> getPotentialBlocks();

    @Override
    public abstract boolean hasMobs();

    public enum Phases{

        STARTING_PHASE(1),
        TOOLS_PHASE(2),
        TESTING_PHASE(3);

        private final int numPhase;
        Phases(int numPhase) {
            this.numPhase = numPhase;
        }

        public static List<Phases> getPhasesBefore(Phases currentPhase){

            Phases[] phasesArr = Phases.values();
            List<Phases> phasesBefore = new ArrayList<>();
            int numPhase = currentPhase.numPhase;

            if(numPhase != 1){
                for(int x = numPhase; x > 1; x--){
                    phasesBefore.add(phasesArr[x]);
                }
            }
            return phasesBefore;
        }
    }
}
