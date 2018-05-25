package cilib.pso.selfadaptive.parametersetgenerator;

import Jama.EigenvalueDecomposition;
import Jama.Matrix;
import cilib.controlparameter.ConstantControlParameter;
import cilib.math.random.ProbabilityDistributionFunction;
import cilib.math.random.UniformDistribution;
import cilib.pso.selfadaptive.ParameterBounds;
import cilib.pso.selfadaptive.ParameterSet;
import cilib.type.types.Bounds;
import cilib.type.types.Real;

public class SpectralRadiusParameterSetGenerator implements ParameterSetGenerator {

    protected ProbabilityDistributionFunction inertiaDistribution;
    protected ProbabilityDistributionFunction socialDistribution;
    protected ProbabilityDistributionFunction cognitiveDistribution;
    protected ParameterBounds spectralRadiusBounds;


    public SpectralRadiusParameterSetGenerator(){
        inertiaDistribution = new UniformDistribution(ConstantControlParameter.of(-1), ConstantControlParameter.of(1));
        socialDistribution = new UniformDistribution(ConstantControlParameter.of(0), ConstantControlParameter.of(4.4));
        cognitiveDistribution = new UniformDistribution(ConstantControlParameter.of(0), ConstantControlParameter.of(4.4));
        spectralRadiusBounds = new ParameterBounds(0.9, 1.0);
    }

    @Override
    public ParameterSet generate() {
        double w, c1, c2, sr;

        do{
            w = inertiaDistribution.getRandomNumber();
            c1 = cognitiveDistribution.getRandomNumber();
            c2 = socialDistribution.getRandomNumber();
            sr = spectralRadius(w, c1, c2);
        } while(!spectralRadiusBounds.isInsideBounds(sr));

        return ParameterSet.fromValues(w, c1, c2);
    }


    private double spectralRadius(double w, double c1, double c2){

        double ec1 = c1/2;
        double ec2 = c2/2;

        double ec12 = c1*c1 / 3;
        double ec22 = c2*c2 / 3;

        double el = 1 + w - ec1 - ec2;
        double el2 = 1 + w*w + ec12 + ec22 + 2*w - 2*(ec1 + ec2) - 2 * w * (ec1+ec2) + 2*ec1*ec2;
        double elw = w + w*w - w*(ec1 + ec2);

        double[][] vals = {
                {el, -w, 0, 0, 0},
                {1, 0, 0, 0, 0},
                {0, 0, el2, w*w, -2*elw},
                {0, 0, 1, 0, 0},
                {0, 0, el, 0, -w}
        };
        Matrix B = new Matrix(vals);

        EigenvalueDecomposition eig = B.eig();
        double max = Double.MIN_VALUE;
        for(double d : eig.getRealEigenvalues()){
            double val = Math.abs(d);
            if(val > max) max = val;
        }

        return max;
    }

    @Override
    public ParameterSetGenerator getClone() {
        return null;
    }

    public ProbabilityDistributionFunction getInertiaDistribution() {
        return inertiaDistribution;
    }

    public void setInertiaDistribution(ProbabilityDistributionFunction inertiaDistribution) {
        this.inertiaDistribution = inertiaDistribution;
    }

    public ProbabilityDistributionFunction getSocialDistribution() {
        return socialDistribution;
    }

    public void setSocialDistribution(ProbabilityDistributionFunction socialDistribution) {
        this.socialDistribution = socialDistribution;
    }

    public ProbabilityDistributionFunction getCognitiveDistribution() {
        return cognitiveDistribution;
    }

    public void setCognitiveDistribution(ProbabilityDistributionFunction cognitiveDistribution) {
        this.cognitiveDistribution = cognitiveDistribution;
    }

    public ParameterBounds getSpectralRadiusBounds() {
        return spectralRadiusBounds;
    }

    public void setSpectralRadiusBounds(ParameterBounds spectralRadiusBounds) {
        this.spectralRadiusBounds = spectralRadiusBounds;
    }
}
