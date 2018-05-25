/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.tuning;

import static fj.data.List.range;
import static fj.function.Doubles.sum;
import cilib.algorithm.AbstractAlgorithm;
import cilib.algorithm.AlgorithmEvent;
import cilib.algorithm.MeasuringListener;
import cilib.measurement.Measurement;
import cilib.problem.*;
import cilib.problem.objective.Objective;
import cilib.problem.solution.Fitness;
import cilib.tuning.problem.ProblemGenerator;
import cilib.type.types.Real;
import cilib.type.types.Type;
import fj.F;

import java.util.List;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TuningProblem extends AbstractProblem {
    
    private Problem currentProblem;    
    private AbstractAlgorithm targetAlgorithm;
    private ProblemGenerator problemsProvider;
    private MeasuringListener measuringListener;
    private Measurement<Real> measurement;
    private int samples;
    
    public TuningProblem() {
        this.measurement = new cilib.measurement.single.Fitness();
        this.samples = 1;
        this.measuringListener = new MeasuringListener();
        this.measuringListener.setResolution(5000);
    }
    
    public TuningProblem(TuningProblem copy) {
        this.measurement = copy.measurement.getClone();
        this.samples = copy.samples;
        this.targetAlgorithm = copy.targetAlgorithm.getClone();
        this.problemsProvider = copy.problemsProvider;
        this.measuringListener = copy.measuringListener.getClone();
    }

    @Override
    public TuningProblem getClone() {
        return new TuningProblem(this);
    }

    @Override
    protected Fitness calculateFitness(Type solution) {
        double f = sum(range(0, samples).map(new F<Integer, Double>(){
            @Override
            public Double f(Integer a) {
                measuringListener.setMeasurement(measurement.getClone());
                targetAlgorithm.setOptimisationProblem(currentProblem);
                targetAlgorithm.performInitialisation();
                targetAlgorithm.runAlgorithm();
                measuringListener.algorithmFinished(new AlgorithmEvent(targetAlgorithm));
                return ((Real)measuringListener.getLastMeasurement()).doubleValue();
            }                    
        })) / samples;

        /*List<IFRaceSimulation> simulations = new ArrayList<>();
        for(int i = 0; i < samples; i++){
            simulations.add(new IFRaceSimulation(targetAlgorithm.getClone(), currentProblem));
        }

        final IFRaceSimulation[] completedSimulations = execute(simulations);

        double f = 0;

        for(int i = 0; i < completedSimulations.length; i++){
            f += measurement.getValue(completedSimulations[i].getAlgorithm()).doubleValue();
        }

        f /= samples;*/

        return objective.evaluate(f);
    }


    private IFRaceSimulation[] execute(List<IFRaceSimulation> simulations){
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        CompletionService<IFRaceSimulation> completionService = new ExecutorCompletionService<>(executor);

        //add each simulation to the queue
        for (IFRaceSimulation simulation : simulations) {
            completionService.submit(simulation, simulation);
        }

        final IFRaceSimulation[] completedSimulations = new IFRaceSimulation[samples];
        try {
            //take each simulation from the queue as it completes
            for (int i = 0; i < samples; i++) {
                Future<IFRaceSimulation> simulation = completionService.take();
                completedSimulations[i] = simulation.get();
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(TuningProblem.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            executor.shutdownNow(); // Shutdown now - time to explode
            throw new RuntimeException(ex);
        }

        executor.shutdown();

        return completedSimulations;
    }

    public void nextProblem() {
        currentProblem = problemsProvider._1();
    }
    
    public void setMeasurement(Measurement<Real> measurement) {
        this.measurement = measurement;
    }

    public Measurement<Real> getMeasurement() {
        return measurement;
    }
    
    public void setProblemsProvider(ProblemGenerator problemProvider) {
        this.problemsProvider = problemProvider;
    }
    
    public ProblemGenerator getProblemsProvider() {
        return problemsProvider;
    }
    
    public void setTargetAlgorithm(AbstractAlgorithm targetAlgorithm) {
        this.targetAlgorithm = targetAlgorithm;
        //this.targetAlgorithm.addAlgorithmListener(measuringListener);
    }

    public AbstractAlgorithm getTargetAlgorithm() {
        return targetAlgorithm;
    }
    
    public void setSamples(int samples) {
        this.samples = samples;
    }

    public int getSamples() {
        return samples;
    }

    public Problem getCurrentProblem() {
        return currentProblem;
    }

    @Override
    public Objective getObjective() {
        return ((AbstractProblem) currentProblem).getObjective();
    }

    public void setMeasuringListener(MeasuringListener measuringListener) {
        this.measuringListener = measuringListener;
    }
}
