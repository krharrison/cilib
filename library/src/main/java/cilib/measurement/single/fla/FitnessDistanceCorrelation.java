package cilib.measurement.single.fla;

import cilib.algorithm.Algorithm;
import cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import cilib.entity.Entity;
import cilib.measurement.Measurement;
import cilib.type.types.Real;
import cilib.util.distancemeasure.EuclideanDistanceMeasure;
import cilib.type.types.container.Vector;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FitnessDistanceCorrelation implements Measurement<Real> {

    protected EuclideanDistanceMeasure distanceMeasure;

    public FitnessDistanceCorrelation(){
        distanceMeasure = new EuclideanDistanceMeasure();
    }

    @Override
    public Measurement<Real> getClone() {
        return this;
    }

    @Override
    public Real getValue(Algorithm algorithm) {

        SinglePopulationBasedAlgorithm populationBasedAlgorithm = (SinglePopulationBasedAlgorithm) algorithm;

        int popSize = populationBasedAlgorithm.getTopology().length();

        Iterator<Entity> populationIterator = populationBasedAlgorithm.getTopology().iterator();
        //calculate overall dispersion

        //double distanceSum = 0;

        //first add the global best
        populationBasedAlgorithm.getBestSolution();
        double bestFitness = populationBasedAlgorithm.getBestSolution().getFitness().getValue();
        //distanceSum += 0; //distance between best and best is 0!

        Vector bestPosition = (Vector)populationBasedAlgorithm.getBestSolution().getPosition();

        List<Double> fitnesses = new ArrayList<>();
        List<Double> distances = new ArrayList<>();

        fitnesses.add(bestFitness);
        distances.add(0.0); //distance from best to best is 0

        double fitnessSum = bestFitness;
        double distanceSum = 0;

        while(populationIterator.hasNext()){
            Entity entity = populationIterator.next();
            double fitness = entity.getFitness().getValue(); //fitness of this solution
            double distance = distanceMeasure.distance(entity.getPosition(), bestPosition); //distance to best
            fitnesses.add(fitness);
            distances.add(distance);

            fitnessSum += fitness;
            distanceSum += distance;
        }

        double avgFitness = fitnessSum / (popSize + 1); //add 1 for best solution
        double avgDistance = distanceSum / (popSize + 1);

        double numerator = 0;
        double lDemoninator = 0;
        double rDemoninator = 0;

        for(int i = 0; i < fitnesses.size(); i++){
            double fitnessDiff = (fitnesses.get(i) - avgFitness);
            double distanceDiff = (distances.get(i) - avgDistance);
            numerator +=  fitnessDiff * distanceDiff;

            lDemoninator += fitnessDiff * fitnessDiff;
            rDemoninator += distanceDiff * distanceDiff;
        }

        double result = numerator / (Math.sqrt(lDemoninator) * Math.sqrt(rDemoninator));

        return Real.valueOf(result);

    }
}
