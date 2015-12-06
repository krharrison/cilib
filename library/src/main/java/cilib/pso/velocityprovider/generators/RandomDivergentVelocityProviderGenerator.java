/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 *//*

package cilib.pso.velocityprovider.generators;

import cilib.controlparameter.ConstantControlParameter;
import cilib.math.random.generator.Rand;
import cilib.pso.guideprovider.GuideProvider;
import cilib.pso.guideprovider.NBestGuideProvider;
import cilib.pso.guideprovider.PBestGuideProvider;
import cilib.pso.velocityprovider.SelfAdaptiveVelocityProvider;
import cilib.pso.velocityprovider.VelocityProvider;
import cilib.tuning.parameters.TuningBounds;

public class RandomDivergentVelocityProviderGenerator implements VelocityProviderGenerator {

    private GuideProvider globalGuideProvider;
    private GuideProvider localGuideProvider;
    private TuningBounds inertiaBounds;
    private TuningBounds socialBounds;
    private TuningBounds cognitiveBounds;

    public RandomDivergentVelocityProviderGenerator(){
        this.globalGuideProvider = new NBestGuideProvider();
        this.localGuideProvider = new PBestGuideProvider();

        inertiaBounds = new TuningBounds(0, 1);
        socialBounds = new TuningBounds(0, 4);
        cognitiveBounds = new TuningBounds(0, 4);
    }

    public RandomDivergentVelocityProviderGenerator(RandomDivergentVelocityProviderGenerator clone){
        this.inertiaBounds = clone.inertiaBounds;
        this.socialBounds = clone.socialBounds;
        this.cognitiveBounds = clone.cognitiveBounds;
        this.globalGuideProvider = clone.globalGuideProvider.getClone();
        this.localGuideProvider = clone.localGuideProvider.getClone();
    }

    @Override
    public RandomDivergentVelocityProviderGenerator getClone() {
        return new RandomDivergentVelocityProviderGenerator(this);
    }

    */
/**
     * Generate random, divergent parameters based on the convergence criteria of Poli
     *//*

    @Override
    public VelocityProvider generate() {
        SelfAdaptiveVelocityProvider provider = new SelfAdaptiveVelocityProvider();
        provider.setLocalGuideProvider(localGuideProvider);
        provider.setGlobalGuideProvider(globalGuideProvider);

        double cognitive;
        double social;
        double inertia;

        double check; //used to check convergence

        do{
            inertia = Rand.nextDouble() * inertiaBounds.getRange() + inertiaBounds.getLowerBound();
            cognitive = Rand.nextDouble() * cognitiveBounds.getRange() + cognitiveBounds.getLowerBound();
            social = Rand.nextDouble() * socialBounds.getRange() + socialBounds.getLowerBound();

            check = (24 * (1 - inertia * inertia)) / (7 - 5 * inertia);
        } while(check > social + cognitive);

        provider.setCognitiveAcceleration(ConstantControlParameter.of(cognitive));
        provider.setSocialAcceleration(ConstantControlParameter.of(social));
        provider.setInertiaWeight(ConstantControlParameter.of(inertia));

        return provider;
    }

    */
/**
     * Sets the GuideProvider responsible for retrieving a particle's global guide.
     * @param globalGuideProvider The guide provider to set.
     *//*

    public void setGlobalGuideProvider(GuideProvider globalGuideProvider) {
        this.globalGuideProvider = globalGuideProvider;
    }

    */
/**
     * Sets the GuideProvider responsible for retrieving a particle's local guide.
     * @param localGuideProvider The guide provider to set.
     *//*

    public void setLocalGuideProvider(GuideProvider localGuideProvider) {
        this.localGuideProvider = localGuideProvider;
    }

    */
/**
     * Sets the range for the inertia coefficient.
     * @param inertiaBounds The bounds for the inertia coefficient.
     *//*

    public void setInertiaBounds(TuningBounds inertiaBounds){
        this.inertiaBounds = inertiaBounds;
    }

    public TuningBounds getInertiaBounds(){
        return this.inertiaBounds;
    }

    */
/**
     * Sets the range for the social coefficient.
     * @param socialBounds The bounds for the social coefficient.
     *//*

    public void setSocialBounds(TuningBounds socialBounds){
        this.socialBounds = socialBounds;
    }

    public TuningBounds getSocialBounds(){
        return this.socialBounds;
    }

    */
/**
     * Sets the range for the cognitive coefficient.
     * @param cognitiveBounds The bounds for the cognitive coefficient.
     *//*

    public void setCognitiveBounds(TuningBounds cognitiveBounds){
        this.cognitiveBounds = cognitiveBounds;
    }

    public TuningBounds getCognitiveBounds(){
        return this.cognitiveBounds;
    }

}*/
