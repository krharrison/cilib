/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.pso.iterationstrategies.selfadaptive;

import cilib.algorithm.population.AbstractIterationStrategy;
import cilib.algorithm.population.IterationStrategy;
import cilib.controlparameter.ConstantControlParameter;
import cilib.entity.Property;
import cilib.math.random.ProbabilityDistributionFunction;
import cilib.math.random.UniformDistribution;
import cilib.problem.solution.Fitness;
import cilib.problem.solution.InferiorFitness;
import cilib.pso.PSO;
import cilib.pso.iterationstrategies.SynchronousIterationStrategy;
import cilib.pso.particle.Particle;
import cilib.pso.particle.SelfAdaptiveParticle;
import cilib.type.types.Type;
import cilib.type.types.container.StructuredType;
import cilib.util.distancemeasure.DistanceMeasure;
import cilib.util.distancemeasure.EuclideanDistanceMeasure;

/**
 * APSO-ZZLC
 */
public class FuzzyClassificationIterationStrategy extends AbstractIterationStrategy<PSO>{

    protected enum State{
        EXPLORATION,
        EXPLOITATION,
        CONVERGENCE,
        JUMPINGOUT
    }

    protected IterationStrategy<PSO> delegate;
    protected DistanceMeasure distanceMeasure;
    protected double delta;
    protected State state;
    protected ProbabilityDistributionFunction deltaDistribution;

    public FuzzyClassificationIterationStrategy(){
        delegate = new SynchronousIterationStrategy();
        distanceMeasure = new EuclideanDistanceMeasure();
        delta = 0.1;
        state = State.EXPLORATION;
        deltaDistribution = new UniformDistribution(ConstantControlParameter.of(0.05), ConstantControlParameter.of(0.1));
    }

    public FuzzyClassificationIterationStrategy(FuzzyClassificationIterationStrategy copy){
        this.delegate = copy.delegate.getClone();
        this.distanceMeasure = copy.distanceMeasure;
        this.delta = copy.delta;
        this.state = copy.state;
        this.deltaDistribution = copy.deltaDistribution;
    }

    @Override
    public FuzzyClassificationIterationStrategy getClone() {
        return new FuzzyClassificationIterationStrategy(this);
    }

    @Override
    public void performIteration(PSO algorithm) {
        double dMin = Double.MAX_VALUE;
        double dMax = -Double.MIN_VALUE;
        for (Particle p : algorithm.getTopology()) {
            double d = meanDistance(p.getPosition(), algorithm);
            dMin = Math.min(d, dMin);
            dMax = Math.max(d, dMax);
        }

        //TODO: dg is usually always the dmin, so algorithm is always 'converging'
        double dg = meanDistance((StructuredType) algorithm.getBestSolution().getPosition(), algorithm);
        dMin = Math.min(dMin, dg);
        dMax = Math.max(dMax, dg);

        double f = calculateF(dg, dMin, dMax);

        double memExplore = membershipExploration(f);
        double memExploit = membershipExploitation(f);
        double memConv = membershipConvergence(f);
        double memJump = membershipJumpingOut(f);

        changeState(memExplore, memExploit, memConv, memJump);

        for (Particle p : algorithm.getTopology()) {
            SelfAdaptiveParticle sp = (SelfAdaptiveParticle) p;
            sp.put(Property.PREVIOUS_PARAMETERS, sp.getParameterSet().asVector());
            adapt(sp, f);
        }

        delegate.performIteration(algorithm);

    }

    protected void changeState(double memExplore, double memExploit, double memConv, double memJump){
        //TODO: this could probably be simplified and cleaned up
        switch(state){
            case EXPLORATION:
                if(memExplore > 0){   //resist change
                    state = State.EXPLORATION;
                }
                else if(memExploit > 0){         //favor change to exploitation
                    state = state.EXPLOITATION;
                }
                else if(memConv > memJump){
                    state = State.CONVERGENCE;
                }
                else{
                    state = State.JUMPINGOUT;
                }
                break;
            case EXPLOITATION:
                if(memExploit > 0){   //resist change
                    state = State.EXPLOITATION;
                }
                else if(memConv > 0){         //favor change to convergence
                    state = state.CONVERGENCE;
                }
                else if(memExplore > memJump){
                    state = State.EXPLORATION;
                }
                else{
                    state = State.JUMPINGOUT;
                }
                break;
            case CONVERGENCE:
                if(memConv > 0){   //resist change
                    state = State.CONVERGENCE;
                }
                else if(memJump > 0){         //favor change to jumping out
                    state = state.JUMPINGOUT;
                }
                else if(memExplore > memExploit){
                    state = State.EXPLORATION;
                }
                else{
                    state = State.EXPLOITATION;
                }
                break;
            case JUMPINGOUT:
                if(memJump > 0){       //resist change
                    state = State.JUMPINGOUT;
                }
                else if(memExplore > 0){         //favor change to exploration
                    state = state.EXPLORATION;
                }
                else if(memExploit > memConv){
                    state = State.EXPLOITATION;
                }
                else{
                    state = State.CONVERGENCE;
                }
                break;
            default:
                state = State.EXPLORATION;
                break;
        }
    }

    protected void adapt(SelfAdaptiveParticle sp, double f){
        double delta = deltaDistribution.getRandomNumber();

        double soc = sp.getSocialAcceleration().getParameter();
        double cog = sp.getCognitiveAcceleration().getParameter();

        switch (state){
            case EXPLORATION:
                cog += delta;
                soc -= delta;
                break;
            case EXPLOITATION:
                cog += 0.5 * delta;
                soc -= 0.5 * delta ;
                break;
            case CONVERGENCE:
                cog += 0.5 * delta; //TODO: why increase both?
                soc += 0.5 * delta ;
                break;
            case JUMPINGOUT:
                cog -= delta;
                soc += delta;
                break;
            default:
                cog = 2;
                soc = 2;
                break;
        }

        //clamp c1 and c2
        soc = Math.min(soc, 2.5);
        soc = Math.max(soc, 1.5);
        cog = Math.min(cog, 2.5);
        cog = Math.max(cog, 1.5);

        double sum = soc + cog;
        if(sum > 4){
            soc = (soc * 4) / sum;
            cog = (cog * 4) / sum;
        }

        double inertia = calculateInertia(f);

        sp.setSocialAcceleration(ConstantControlParameter.of(soc));
        sp.setCognitiveAcceleration(ConstantControlParameter.of(cog));
        sp.setInertiaWeight(ConstantControlParameter.of(inertia));
    }

    protected double calculateInertia(double f){
        return 1 / (1 + 1.5 * Math.exp(-2.6 * f));
    }

    protected double calculateF(double dg, double dmin, double dmax){
        return (dg - dmin) / (dmax - dmin);
    }

    protected double meanDistance(StructuredType pos, PSO algorithm){
        double sum = 0.0;

        for(Particle p : algorithm.getTopology()){
            sum += distanceMeasure.distance(pos, p.getPosition());
        }

        return sum /= (double)(algorithm.getTopology().length() - 1);
    }

    protected double membershipExploration(double f){
        if(f <= 0.4) return 0;                 // 0.0 < f <= 0.4
        else if (f <= 0.6) return 5 * f - 2;   // 0.4 < f <= 0.6
        else if (f <= 0.7) return 1;           // 0.6 < f <= 0.7
        else if (f <= 0.8) return -10 * f + 8; // 0.7 < f <= 0.8
        else return 0;                         // 0.8 < f <= 1.0
    }

    protected double membershipExploitation(double f){
        if(f <= 0.2) return 0;                 // 0.0 < f <= 0.2
        else if (f <= 0.3) return 10 * f - 2;  // 0.2 < f <= 0.3
        else if (f <= 0.4) return 1;           // 0.3 < f <= 0.4
        else if (f <= 0.6) return -5 * f + 3;  // 0.4 < f <= 0.6
        else return 0;                         // 0.6 < f <= 1.0
    }

    protected double membershipConvergence(double f){
        if(f <= 0.1) return 1;                 // 0.0 < f <= 0.1
        else if (f <= 0.3) return -5 * f + 1.5;// 0.1 < f <= 0.3
        else return 0;                         // 0.3 < f <= 1.0
    }

    protected double membershipJumpingOut(double f){
        if(f <= 0.7) return 0;                 // 0.0 < f <= 0.7
        else if (f <= 0.9) return 5 * f - 3.5; // 0.7 < f <= 0.9
        else return 1;                         // 0.9 < f <= 1.0
    }

    public void setDelegate(IterationStrategy<PSO> delegate){
        this.delegate = delegate;
    }

    public void setDistanceMeasure(DistanceMeasure measure){
        this.distanceMeasure = measure;
    }

    public void setDelta(double delta){
        this.delta = delta;
    }
}
