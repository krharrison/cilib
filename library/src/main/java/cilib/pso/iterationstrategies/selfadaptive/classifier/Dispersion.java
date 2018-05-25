package cilib.pso.iterationstrategies.selfadaptive.classifier;

import cilib.type.types.Bounds;
import cilib.type.types.Numeric;
import cilib.type.types.Real;
import cilib.type.types.container.Vector;
import cilib.util.distancemeasure.EuclideanDistanceMeasure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Dispersion {
    public static double calculate(List<Sample> samples, double sampleSize) {

        EuclideanDistanceMeasure distanceMeasure = new EuclideanDistanceMeasure();

        //normalize samples
        List<Sample> normalized = normalizeSamples(samples);
        //sort the normalized collection
        Collections.sort(normalized);

        //calculateDispersion of all points
        double distance = 0;
        int count = 0;
        for(Sample sample : normalized){
            for(Sample other : normalized){
                if(sample != other) {
                    distance += distanceMeasure.distance(sample.getValues(), other.getValues());
                    count ++;
                }
            }
        }

        double totalDispersion = distance / count;

        //calculate dispersion of a subset of the best
        int subsetSize = (int)(sampleSize * normalized.size());
        List<Sample> s = normalized.subList(0, subsetSize);
        distance = 0;
        count = 0;
        for(Sample sample : s){
            for(Sample other : s){
                if(sample != other) {
                    distance += distanceMeasure.distance(sample.getValues(), other.getValues());
                    count ++;
                }
            }
        }

        double subsetDispersion = distance / count;

        return subsetDispersion - totalDispersion;
    }

    /**
     * Return a new list of samples where each dimension of the samples has been normalized to the range [0, 1]
     * @param samples
     * @return
     */
    private static List<Sample> normalizeSamples(List<Sample> samples){

        List<Sample> result = new ArrayList<>(samples.size());
        for(Sample sample : samples){
            Vector.Builder builder = Vector.newBuilder();

            //normalize and update bounds to [0,1]
            for(Numeric n : sample.getValues()){
                double normalized = (n.doubleValue() - n.getBounds().getLowerBound()) / n.getBounds().getRange();
                builder.add(Real.valueOf(normalized, new Bounds(0, 1)));
            }

            result.add(new Sample(builder.build(), sample.getFitness()));
        }

        return result;
    }
}
