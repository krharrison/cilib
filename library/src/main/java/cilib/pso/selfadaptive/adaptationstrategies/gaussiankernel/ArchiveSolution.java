/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.pso.selfadaptive.adaptationstrategies.gaussiankernel;

import cilib.pso.selfadaptive.ParameterSet;

public class ArchiveSolution implements Comparable<ArchiveSolution>{
    protected double weight;
    protected ParameterSet parameterSet;

    public ArchiveSolution(ParameterSet parameterSet){
        this.parameterSet = parameterSet;
    }

    @Override
    public int compareTo(ArchiveSolution archiveSolution) {
        return this.parameterSet.compareTo(archiveSolution.parameterSet);
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public ParameterSet getParameterSet() {
        return parameterSet;
    }

}
