package cilib.tuning;

import cilib.algorithm.AbstractAlgorithm;
import cilib.algorithm.Algorithm;
import cilib.math.random.generator.Rand;
import cilib.problem.Problem;

public class IFRaceSimulation implements Runnable {

    private final AbstractAlgorithm algorithm;
    private final Problem problem;

    public IFRaceSimulation(AbstractAlgorithm algorithm, Problem problem){
        this.algorithm = algorithm;
        this.problem = problem;
    }

    @Override
    public void run() {
        Rand.reset();
        init();
    }

    private void init() {
        algorithm.setOptimisationProblem(problem);
        algorithm.performInitialisation();
        algorithm.runAlgorithm();
    }

    public Algorithm getAlgorithm() {
        return algorithm;
    }

    public Problem getProblem() {
        return problem;
    }
}
