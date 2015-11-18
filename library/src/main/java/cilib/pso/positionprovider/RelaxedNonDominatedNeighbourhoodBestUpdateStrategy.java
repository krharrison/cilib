/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.pso.positionprovider;

import cilib.algorithm.AbstractAlgorithm;
import cilib.entity.Entity;
import cilib.entity.Topologies;
import cilib.entity.comparator.RelaxedNonDominatedFitnessComparator;
import cilib.problem.solution.Fitness;
import cilib.pso.PSO;
import cilib.pso.particle.Particle;


public class RelaxedNonDominatedNeighbourhoodBestUpdateStrategy implements NeighbourhoodBestUpdateStrategy {
    
    @Override
    public NeighbourhoodBestUpdateStrategy getClone() {
        return this;
    }

    @Override
    public Fitness getSocialBestFitness(Entity entity) { 
        PSO pso = (PSO) AbstractAlgorithm.get();              
        Particle nBest = Topologies.getNeighbourhoodBest(pso.getTopology(), (Particle)entity, pso.getNeighbourhood(), new RelaxedNonDominatedFitnessComparator());
        return nBest.getBestFitness();
    }        
}
