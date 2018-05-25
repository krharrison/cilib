package cilib.pso.iterationstrategies.selfadaptive.classifier;

import cilib.util.distancemeasure.EuclideanDistanceMeasure;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class FitnessDistanceCorrelation {
    //TODO: always assumes minimization - could perhaps use function.getFitness, but this would complicate matters quite a bit

    public static double calculate(List<Sample> samples){
        int numSamples = samples.size();

        EuclideanDistanceMeasure distanceMeasure = new EuclideanDistanceMeasure();

        if (samples.isEmpty()) return Double.NaN; //no data, return NaN

        Sample best = Sample.findBest(samples);

        //calculate average fitness and distance
        List<Double> distances = new ArrayList<>(numSamples);
        ListIterator<Sample> itr = samples.listIterator();
        double fitnessSum = 0;
        double distanceSum = 0;
        while (itr.hasNext()) {
            final Sample curr = itr.next();
            fitnessSum += curr.getFitness();
            double distance = distanceMeasure.distance(curr.getValues(), best.getValues());
            distanceSum += distance;
            distances.add(distance);
        }

        double avgFitness = fitnessSum / numSamples;
        double avgDistance = distanceSum / numSamples;

        //calculate the fdc
        itr = samples.listIterator();  //get new iterator for next traversal
        int index = 0;
        double numerator = 0;
        double lDenominator = 0;
        double rDenominator = 0;

        while (itr.hasNext()) {
            final Sample curr = itr.next();
            double distance = distances.get(index);

            double fitnessDiff = curr.getFitness() - avgFitness;
            double distanceDiff = distance - avgDistance;
            numerator += fitnessDiff * distanceDiff;
            lDenominator += fitnessDiff * fitnessDiff;
            rDenominator += distanceDiff * distanceDiff;

            index++;
        }


        return numerator / (Math.sqrt(lDenominator) * Math.sqrt(rDenominator));
    }
}
