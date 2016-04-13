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
import cilib.math.random.ProbabilityDistributionFunction;
import cilib.math.random.UniformDistribution;
import cilib.pso.PSO;
import cilib.pso.iterationstrategies.SynchronousIterationStrategy;
import cilib.pso.particle.Particle;
import cilib.pso.particle.SelfAdaptiveParticle;
import cilib.type.types.container.StructuredType;
import cilib.util.distancemeasure.DistanceMeasure;
import cilib.util.distancemeasure.EuclideanDistanceMeasure;

/**
 * Personal-parameter modification of APSO-ZZLC
 */
public class PersonalFuzzyClassificationIterationStrategy extends AbstractIterationStrategy<PSO>{

    protected enum State{
        EXPLORATION,
        EXPLOITATION,
        CONVERGENCE,
        JUMPINGOUT
    }

    protected IterationStrategy<PSO> delegate;
    protected DistanceMeasure distanceMeasure;
    //protected double delta;
    protected ProbabilityDistributionFunction deltaDistribution;
    protected State[] state;


    public PersonalFuzzyClassificationIterationStrategy(){
        delegate = new SynchronousIterationStrategy();
        distanceMeasure = new EuclideanDistanceMeasure();
        deltaDistribution = new UniformDistribution(ConstantControlParameter.of(0.05), ConstantControlParameter.of(0.1));
        //delta = 0.1;
        //state = State.EXPLORATION;
    }

    public PersonalFuzzyClassificationIterationStrategy(PersonalFuzzyClassificationIterationStrategy copy){
        this.delegate = copy.delegate.getClone();
        this.distanceMeasure = copy.distanceMeasure;
        this.deltaDistribution = copy.deltaDistribution;
        this.state = copy.state;
    }

    @Override
    public PersonalFuzzyClassificationIterationStrategy getClone() {
        return new PersonalFuzzyClassificationIterationStrategy(this);
    }

    @Override
    public void performIteration(PSO algorithm) {

        if(algorithm.getIterations() == 0){
            state = new State[algorithm.getTopology().length()];
            for(int i = 0; i < state.length; i++){
                state[i] = State.EXPLORATION;
            }
        }

        double dValues[] = new double[algorithm.getTopology().length()];
        double dMin = Double.MAX_VALUE;
        double dMax = -Double.MIN_VALUE;

        for(int i = 0; i < algorithm.getTopology().length(); i++){
            Particle p = algorithm.getTopology().index(i);
            dValues[i] = distanceMeasure.distance(p.getPosition(), (StructuredType)algorithm.getBestSolution().getPosition());  //meanDistance(p.getPosition(), algorithm);
            dMin = Math.min(dValues[i], dMin);
            dMax = Math.max(dValues[i], dMax);
        }

        for(int i = 0; i < algorithm.getTopology().length(); i++){
            SelfAdaptiveParticle p = (SelfAdaptiveParticle)algorithm.getTopology().index(i);

            double f = calculateF(dValues[i], dMin, dMax);

            double memExplore = membershipExploration(f);
            double memExploit = membershipExploitation(f);
            double memConv = membershipConvergence(f);
            double memJump = membershipJumpingOut(f);

            changeState(i, memExplore, memExploit, memConv, memJump);
            adapt(p, f, state[i]);
        }

        delegate.performIteration(algorithm);

    }

    protected void changeState(int index, double memExplore, double memExploit, double memConv, double memJump){
        //TODO: this could probably be simplified and cleaned up
        switch(state[index]){
            case EXPLORATION:
                if(memExplore > 0){   //resist change
                    state[index] = State.EXPLORATION;
                }
                else if(memExploit > 0){         //favor change to exploitation
                    state[index] = State.EXPLOITATION;
                }
                else if(memConv > memJump){
                    state[index] = State.CONVERGENCE;
                }
                else{
                    state[index] = State.JUMPINGOUT;
                }
                break;
            case EXPLOITATION:
                if(memExploit > 0){   //resist change
                    state[index] = State.EXPLOITATION;
                }
                else if(memConv > 0){         //favor change to convergence
                    state[index] = State.CONVERGENCE;
                }
                else if(memExplore > memJump){
                    state[index] = State.EXPLORATION;
                }
                else{
                    state[index] = State.JUMPINGOUT;
                }
                break;
            case CONVERGENCE:
                if(memConv > 0){   //resist change
                    state[index] = State.CONVERGENCE;
                }
                else if(memJump > 0){         //favor change to jumping out
                    state[index] = State.JUMPINGOUT;
                }
                else if(memExplore > memExploit){
                    state[index] = State.EXPLORATION;
                }
                else{
                    state[index] = State.EXPLOITATION;
                }
                break;
            case JUMPINGOUT:
                if(memJump > 0){       //resist change
                    state[index] = State.JUMPINGOUT;
                }
                else if(memExplore > 0){         //favor change to exploration
                    state[index] = State.EXPLORATION;
                }
                else if(memExploit > memConv){
                    state[index] = State.EXPLOITATION;
                }
                else{
                    state[index] = State.CONVERGENCE;
                }
                break;
            default:
                state[index] = State.EXPLORATION;
                break;
        }
    }

    protected void adapt(SelfAdaptiveParticle sp, double f, State state){
        double soc = sp.getSocialAcceleration().getParameter();
        double cog = sp.getCognitiveAcceleration().getParameter();

        double delta = deltaDistribution.getRandomNumber();

        switch (state){
            case EXPLORATION:
                cog += delta;
                soc -= delta;
                break;
            case EXPLOITATION:
                cog += 0.5 * delta;
                soc -= 0.5 * delta;
                break;
            case CONVERGENCE:
                cog += 0.5 * delta; //TODO: this is defined as both increasing, but doesn't match the diagram
                soc += 0.5 * delta;
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
        soc = Math.max(soc, 0.0);
       // soc = Math.min(soc, 2.0);
        cog = Math.max(cog, 0.0);
       // cog = Math.min(cog, 2.0);

        double sum = soc + cog; //TODO define more sensible upper limit
        double maxSum = 2.99236; // 2 * 1.49618
        if(sum > maxSum){
            soc = (soc * maxSum) / sum;
            cog = (cog * maxSum) / sum;
        }

        //double inertia = calculateInertia(f);

        sp.setSocialAcceleration(ConstantControlParameter.of(soc));
        sp.setCognitiveAcceleration(ConstantControlParameter.of(cog));
        //sp.setInertiaWeight(ConstantControlParameter.of(inertia));
    }

   // protected double calculateInertia(double f){
   //     return 1 / (1 + 1.5 * Math.exp(-2.6 * f));
   // }

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

    public void setDeltaDistribution(ProbabilityDistributionFunction dist){
        this.deltaDistribution = dist;
    }
}
