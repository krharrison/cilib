package cilib.pso.selfadaptive.parametersetgenerator;

import cilib.math.random.generator.Rand;
import cilib.pso.iterationstrategies.selfadaptive.classifier.ClassifierSingleton;
import cilib.pso.selfadaptive.ParameterSet;
import cilib.type.types.Bounds;
import weka.classifiers.Classifier;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ClassifierModelParameterSetGenerator implements ParameterSetGenerator {

    protected String modelPath;
    protected Bounds inertiaBounds;
    protected Bounds accelerationBounds;
    protected Classifier classifier;
    protected double fitnessDistanceCorrelation;
    protected double dispersion;

    String className;
    double inertia;
    double cognitive;
    double social;

    final Attribute fdc = new Attribute("fdc");
    final Attribute dm = new Attribute("dm");
    final Attribute w = new Attribute("w");
    final Attribute c1 = new Attribute("c1");
    final Attribute c2 = new Attribute("c2");
    final List<String> classes = new ArrayList<String>() {
        {
            add("E");
            add("VG");
            add("G");
            add("A");
            add("P");
            add("VP");
            add("T");
        }
    };
    final Attribute classification = new Attribute("output", classes);

    ArrayList<Attribute> attributeList = new ArrayList<Attribute>(2) {
        {
            add(fdc);
            add(dm);
            add(w);
            add(c1);
            add(c2);
            add(classification);
        }
    };

    Instances dataUnpredicted = new Instances("TestInstances", attributeList, 1);

    public ClassifierModelParameterSetGenerator(){
        inertiaBounds = new Bounds(-1.1, 1.1);
        accelerationBounds = new Bounds(0, 4.4);
    }

    @Override
    public ParameterSet generate() {

        try {

            classifier = ClassifierSingleton.getInstance(modelPath);

            // last feature is target variable
            dataUnpredicted.setClassIndex(dataUnpredicted.numAttributes() - 1);

            do {
                inertia = fixedRandom(inertiaBounds.getLowerBound(), inertiaBounds.getUpperBound());
                //System.out.println(inertia);

                double c = fixedRandom(accelerationBounds.getLowerBound(), accelerationBounds.getUpperBound());
                double rand = Rand.nextDouble();
                if(rand < 1.0/3.0) {
                    cognitive = c/2;
                    social =  cognitive;
                }
                else if (rand < 2.0/3.0){
                    cognitive = c/4;
                    social = c - cognitive;
                }
                else
                {
                    social = c/4;
                    cognitive = c - social;
                }

                final DecimalFormat df = new DecimalFormat("#.###");
                DenseInstance newInstance = new DenseInstance(dataUnpredicted.numAttributes()) {
                    {
                        setValue(fdc, fitnessDistanceCorrelation);
                        setValue(dm, dispersion);
                        setValue(w, inertia);
                        setValue(c1, Double.parseDouble(df.format(cognitive))); //hacky way to force only 3 decimals
                        setValue(c2, Double.parseDouble(df.format(social)));
                    }
                };

                newInstance.setDataset(dataUnpredicted);

                double result = classifier.classifyInstance(newInstance);
                className = classes.get(new Double(result).intValue());
            } while (className != "E");

            return ParameterSet.fromValues(inertia, cognitive, social);

        }catch (Exception e){
            System.out.println("Uh oh!");
        }

        return null;

    }

    @Override
    public ParameterSetGenerator getClone() {
        return null;
    }

    private double fixedRandom(double min, double max){
        return (Rand.nextInt((int)((max-min)*10+1))+min*10) / 10.0;
    }

    public String getModelPath() {
        return modelPath;
    }

    public void setModelPath(String modelPath) {
        this.modelPath = modelPath;
    }

    public Bounds getInertiaBounds() {
        return inertiaBounds;
    }

    public void setInertiaBounds(Bounds inertiaBounds) {
        this.inertiaBounds = inertiaBounds;
    }

    public Bounds getAccelerationBounds() {
        return accelerationBounds;
    }

    public void setAccelerationBounds(Bounds accelerationBounds) {
        this.accelerationBounds = accelerationBounds;
    }

    public double getFitnessDistanceCorrelation() {
        return fitnessDistanceCorrelation;
    }

    public void setFitnessDistanceCorrelation(double fitnessDistanceCorrelation) {
        this.fitnessDistanceCorrelation = fitnessDistanceCorrelation;
    }

    public double getDispersion() {
        return dispersion;
    }

    public void setDispersion(double dispersion) {
        this.dispersion = dispersion;
    }
}
