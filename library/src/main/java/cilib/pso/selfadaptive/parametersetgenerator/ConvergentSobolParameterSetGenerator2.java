package cilib.pso.selfadaptive.parametersetgenerator;

import cilib.pso.selfadaptive.ParameterSet;

public class ConvergentSobolParameterSetGenerator2 extends SobolParameterSetGenerator {


    public ConvergentSobolParameterSetGenerator2(){
        super();
    }

    public ConvergentSobolParameterSetGenerator2(ConvergentSobolParameterSetGenerator2 copy){
        super(copy);
    }

    @Override
    public ParameterSet generate() {
        ParameterSet params;
        do{
            params = super.generate();
        } while (!check(params));

        return params;
    }

    private boolean check(ParameterSet params)
    {
        double w = params.getInertiaWeight().getParameter();
        double c1 = params.getCognitiveAcceleration().getParameter();
        double c2 = params.getSocialAcceleration().getParameter();

        double upper = (24 * (1 - w * w)) / (7 - 5 * w);
        double lower = (22 - 30 * w * w) / (7 - 5 * w);
        double c = c1 + c2;

        return c > lower && c < upper;
    }

    @Override
    public ConvergentSobolParameterSetGenerator2 getClone() {
        return new ConvergentSobolParameterSetGenerator2(this);
    }


}
