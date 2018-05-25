package cilib.measurement.single.fla;

import cilib.algorithm.Algorithm;
import cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import cilib.entity.Entity;
import cilib.measurement.Measurement;
import cilib.type.types.Real;
import cilib.util.distancemeasure.EuclideanDistanceMeasure;
import cilib.util.selection.Samples;
import cilib.util.selection.Selection;
import cilib.util.selection.arrangement.ReverseArrangement;
import cilib.util.selection.arrangement.SortedArrangement;
import com.google.common.collect.Ordering;

import java.util.List;

public class DispersionMetric implements Measurement<Real> {

    protected EuclideanDistanceMeasure distanceMeasure;
    protected double sampleSize;

    public DispersionMetric(){
        distanceMeasure = new EuclideanDistanceMeasure();
        sampleSize = 0.1;
    }

    @Override
    public Measurement<Real> getClone() {
        return this;
    }

    @Override
    public Real getValue(Algorithm algorithm) {

        SinglePopulationBasedAlgorithm populationBasedAlgorithm = (SinglePopulationBasedAlgorithm) algorithm;

        //Iterator<Entity> populationIterator = populationBasedAlgorithm.getTopology().iterator();
        //order by fitness
        Selection<Entity> sorted =  Selection.copyOf(populationBasedAlgorithm.getTopology()).orderBy(new SortedArrangement(Ordering.natural()))
                .orderBy(new ReverseArrangement());


        int popSize = populationBasedAlgorithm.getTopology().length();
        int subsetSize = (int) (sampleSize * popSize);

        List<Entity> entities = sorted.select(Samples.all());
        List<Entity> subset = sorted.select(Samples.first(subsetSize));

        double totalDispersion = 0;
        double subsetDispersion = 0;

        double totalDistance = 0;

        //calculate overall dispersion
        for(Entity e: entities){
            double currentSum = 0;
            for(Entity e2 : entities){
                currentSum += distanceMeasure.distance(e.getPosition(), e2.getPosition());
            }
            totalDistance += currentSum / popSize;
        }

        totalDispersion = totalDistance / popSize;

        //calculate subset dispersion
        totalDistance = 0;
        for (Entity e : subset){
            double currentSum = 0;
            for(Entity e2 : subset){
                currentSum += distanceMeasure.distance(e.getPosition(), e2.getPosition());
            }
            totalDistance += currentSum / subsetSize;
        }

        subsetDispersion = totalDistance / subsetSize;

        return Real.valueOf(subsetDispersion - totalDispersion);

    }
}
