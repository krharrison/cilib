package cilib.pso.selfadaptive.parametersetgenerator;

import cilib.math.random.generator.Rand;
import cilib.math.random.generator.quasi.Sobol;
import cilib.pso.selfadaptive.ParameterBounds;
import cilib.pso.selfadaptive.ParameterSet;

public class ConvergentSobolParameterSetGenerator extends SobolParameterSetGenerator {


    public ConvergentSobolParameterSetGenerator(){
        super();
    }

    public ConvergentSobolParameterSetGenerator(ConvergentSobolParameterSetGenerator copy){
        super(copy);
    }

    @Override
    public ParameterSet generate() {
        ParameterSet params;
        do{
            params = super.generate();
        } while (!params.isConvergent());

        return params;
    }

    @Override
    public ConvergentSobolParameterSetGenerator getClone() {
        return new ConvergentSobolParameterSetGenerator(this);
    }


}
