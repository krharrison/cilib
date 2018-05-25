package cilib.pso.iterationstrategies.selfadaptive;

import cilib.algorithm.population.IterationStrategy;
import cilib.entity.Property;
import cilib.math.random.GaussianDistribution;
import cilib.problem.boundaryconstraint.BoundaryConstraint;
import cilib.pso.PSO;
import cilib.pso.iterationstrategies.SynchronousIterationStrategy;
import cilib.pso.particle.Particle;
import cilib.pso.particle.SelfAdaptiveParticle;
import cilib.pso.selfadaptive.ParameterSet;
import cilib.pso.selfadaptive.detectionstrategies.swarm.PeriodicDetectionStrategy;
import cilib.pso.selfadaptive.detectionstrategies.swarm.SwarmUpdateDetectionStrategy;
import cilib.pso.selfadaptive.parametersetgenerator.ParameterSetGenerator;
import cilib.pso.selfadaptive.parametersetgenerator.SobolParameterSetGenerator;
import cilib.util.selection.arrangement.Arrangement;
import cilib.util.selection.arrangement.ReverseArrangement;
import cilib.util.selection.arrangement.SortedArrangement;
import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.linear.SingularMatrixException;

import java.util.*;

public class MultivariateNormalDistributionIterationStrategy implements IterationStrategy<PSO> {
    protected List<ParameterSet> seedParameters;
    protected ArrayList<ParameterSet> successfulParameters;
    protected IterationStrategy<PSO> iterationStrategy;
    protected SwarmUpdateDetectionStrategy detectionStrategy;
    protected MultivariateNormalDistribution distribution;
    //protected GaussianDistribution distribution;
    protected ParameterSetGenerator parameterSetGenerator;

    protected Arrangement<Particle> arrangement;
    protected Arrangement<Particle> reverser;

    public MultivariateNormalDistributionIterationStrategy(){
        seedParameters = new ArrayList<>();
        successfulParameters = new ArrayList<>();
        iterationStrategy = new SynchronousIterationStrategy();
        detectionStrategy = new PeriodicDetectionStrategy();
        //distribution = new GaussianDistribution();
        parameterSetGenerator = new SobolParameterSetGenerator();

        arrangement = new SortedArrangement<>();
        reverser = new ReverseArrangement<>();
    }

     public MultivariateNormalDistributionIterationStrategy(MultivariateNormalDistributionIterationStrategy copy){
        this.seedParameters = new ArrayList<>(copy.seedParameters);
        this.successfulParameters = new ArrayList<>(copy.successfulParameters);
        this.detectionStrategy = copy.detectionStrategy.getClone();
        this.iterationStrategy = copy.iterationStrategy.getClone();
        //this.distribution = copy.distribution;
        this.parameterSetGenerator = copy.parameterSetGenerator.getClone();
     }

    @Override
    public MultivariateNormalDistributionIterationStrategy getClone() {
        return new MultivariateNormalDistributionIterationStrategy(this);
    }

    @Override
    public void performIteration(PSO algorithm) {

        if(algorithm.getIterations() == 0){
            //if(seedParameters.size() < 2) throw new IllegalArgumentException("At least 2 seed parameter sets needed");
            //assignParameters(algorithm, seedParameters);
            assignInitialParameters(algorithm);
        }
        else if(detectionStrategy.detect(algorithm)) {

            //hack check for at least 2 unique elements

            //TODO: must ensure observation matrix is not singular...
            //if(successfulParameters.size() < 2){
            //    successfulParameters.addAll(seedParameters);
            //}

            Iterable<Particle> ordering = orderParticles(algorithm);
            Iterator<Particle> iterator = ordering.iterator(); //ordered from worst to best
            if(!iterator.hasNext()) return;

            SelfAdaptiveParticle p;
            //p = (SelfAdaptiveParticle) iterator.next();

            int count = 0;

            while (count < 15 && iterator.hasNext()){
                p = (SelfAdaptiveParticle) iterator.next();
                successfulParameters.add(p.getParameterSet());
                count++;
            }


            assignParameters(algorithm, successfulParameters);
            successfulParameters.clear();
        }

        iterationStrategy.performIteration(algorithm);

        //add successful parameters to list of observations
        //for (Particle p : algorithm.getTopology()) {
        //    if(p.get(Property.PBEST_STAGNATION_COUNTER).intValue() == 0){
        //        SelfAdaptiveParticle sp = (SelfAdaptiveParticle) p;
        //        successfulParameters.add(sp.getParameterSet());
        //    }
        //}
    }

    private Iterable<Particle> orderParticles(PSO algorithm){
        return reverser.arrange(arrangement.arrange(algorithm.getTopology()));
    }

    private void assignInitialParameters(PSO algorithm){
        for (Particle p : algorithm.getTopology()) {
            SelfAdaptiveParticle sp = (SelfAdaptiveParticle) p;
            sp.setParameterSet(parameterSetGenerator.generate());
        }
    }

    private void assignParameters(PSO algorithm, Collection<ParameterSet> parameters){

        double[][] observations = paramsAsArray(parameters);
        double[] means = means(observations);
        //double[] deviations = deviations(observations, means);
        double[][] covariance = covariance(observations, means);

        //System.out.println(Arrays.toString(deviations));

        try {
            distribution = new MultivariateNormalDistribution(means, covariance);
        } catch (SingularMatrixException e){
            System.out.println(Arrays.deepToString(covariance));
            //System.exit(1);
        }

        for (Particle p : algorithm.getTopology()) {
            SelfAdaptiveParticle sp = (SelfAdaptiveParticle) p;
            //double w = distribution.getRandomNumber(means[0], deviations[0]);
            //double c1 = distribution.getRandomNumber(means[1], deviations[1]);
            //double c2 = distribution.getRandomNumber(means[2], deviations[2]);
            //sp.setParameterSet(ParameterSet.fromValues(w, c1, c2));
            sp.setParameterSet(ParameterSet.fromArray(distribution.sample()));
        }

    }

    private double[] means(double[][] values){
        double rows = values.length;
        if (rows == 0) return new double[0];
        double[] result = new double[values[0].length];
        for (int i = 0; i < result.length; i++)
        {
            for (int j = 0; j < rows; j++)
            {
                result[i] += values[j][i];
            }

            result[i] /= rows;
        }

        return result;
    }

    private double[] deviations(double[][]values, double[] means){
        int rows = values.length;
        if(rows == 0) return new double[3];
        int cols = values[0].length;
        double divisor = values.length - 1;
        if (means.length != cols)
            throw new IllegalArgumentException("Length of the mean vector should equal the number of columns");

        double[] vars = new double[cols];

        for(int i = 0; i < cols; i++){
            double s = 0;
            for(int j = 0; j < rows; j++){
                double d = values[j][i] - means[i];
                s += d*d;
            }
            s /= divisor;
            vars[i] = Math.sqrt(s);
        }

        return vars;
    }

    private double[][] covariance(double[][] values, double[] means){
        int rows = values.length;
        if (rows == 0) return new double[0][];
        int cols = values[0].length;
        double divisor = values.length - 1;

        if (means.length != cols)
            throw new IllegalArgumentException("Length of the mean vector should equal the number of columns");

        double[][] cov = new double[cols][cols];

        for (int i = 0; i < cols; i++)
        {
            for (int j = i; j < cols; j++)
            {
                double s = 0.0;
                for (int k = 0; k < rows; k++)
                    s += (values[k][j] - means[j]) * (values[k][i] - means[i]);
                s /= divisor;
                cov[i][j] = s;
                cov[j][i] = s;
            }
        }

        return cov;

    }

    private double[][] paramsAsArray(Collection<ParameterSet> parameters){
        double[][] result = new double[parameters.size()][];

        int index = 0;
        for(ParameterSet parameterSet : parameters){
            result[index++] = parameterSet.asArray();
        }

        return result;
    }

    @Override
    public BoundaryConstraint getBoundaryConstraint() {
        return iterationStrategy.getBoundaryConstraint();
    }

    @Override
    public void setBoundaryConstraint(BoundaryConstraint boundaryConstraint) {
        iterationStrategy.setBoundaryConstraint(boundaryConstraint);
    }

    public void addSeedParameters(ParameterSet params){
        seedParameters.add(params);
    }

    public void setIterationStrategy(IterationStrategy<PSO> iterationStrategy) {
        this.iterationStrategy = iterationStrategy;
    }

    public void setDetectionStrategy(SwarmUpdateDetectionStrategy detectionStrategy) {
        this.detectionStrategy = detectionStrategy;
    }

    public void setParameterSetGenerator(ParameterSetGenerator parameterSetGenerator) {
        this.parameterSetGenerator = parameterSetGenerator;
    }
}
