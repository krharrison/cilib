package cilib.pso.iterationstrategies.selfadaptive.classifier;

import weka.classifiers.Classifier;

public class ClassifierSingleton {

    private static Classifier instance;

    private ClassifierSingleton(){}

    /**
     * Implements double checked locking to handle singleton creation.
     * @param path
     * @return
     */
    public static Classifier getInstance(String path){
        if(instance == null){
            synchronized (ClassifierSingleton.class) {
                if(instance == null) {
                    try {
                        instance = (Classifier) weka.core.SerializationHelper.read(path);
                    }
                    catch (Exception e){ return null; }
                }
            }
        }

        return instance;
    }
}
