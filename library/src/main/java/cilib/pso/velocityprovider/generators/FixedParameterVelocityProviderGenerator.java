/**
 * __  __
 * _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 * / ___/ / / / __ \   (c) CIRG @ UP
 * / /__/ / / / /_/ /   http://cilib.net
 * \___/_/_/_/_.___/
 */
package cilib.pso.velocityprovider.generators;

import cilib.controlparameter.ConstantControlParameter;
import cilib.controlparameter.ControlParameter;
import cilib.pso.guideprovider.GuideProvider;
import cilib.pso.guideprovider.NBestGuideProvider;
import cilib.pso.guideprovider.PBestGuideProvider;
import cilib.pso.velocityprovider.SelfAdaptiveVelocityProvider;
import cilib.pso.velocityprovider.VelocityProvider;

public class FixedParameterVelocityProviderGenerator implements VelocityProviderGenerator {
    private GuideProvider globalGuideProvider;
    private GuideProvider localGuideProvider;
    private ControlParameter inertia;
    private ControlParameter social;
    private ControlParameter cognitive;

    public FixedParameterVelocityProviderGenerator(){
        this.globalGuideProvider = new NBestGuideProvider();
        this.localGuideProvider = new PBestGuideProvider();

        inertia = ConstantControlParameter.of(0.729844);
        social = ConstantControlParameter.of(1.496180);
        cognitive = ConstantControlParameter.of(1.496180);
    }

    public FixedParameterVelocityProviderGenerator(FixedParameterVelocityProviderGenerator copy){
        this.inertia = copy.inertia.getClone();
        this.social = copy.social.getClone();
        this.cognitive = copy.cognitive.getClone();
        this.globalGuideProvider = copy.globalGuideProvider.getClone();
        this.localGuideProvider = copy.localGuideProvider.getClone();
    }

    @Override
    public FixedParameterVelocityProviderGenerator getClone() {
        return new FixedParameterVelocityProviderGenerator(this);
    }

    @Override
    public VelocityProvider generate() {
        SelfAdaptiveVelocityProvider provider = new SelfAdaptiveVelocityProvider();
        provider.setLocalGuideProvider(localGuideProvider);
        provider.setGlobalGuideProvider(globalGuideProvider);

        provider.setCognitiveAcceleration(cognitive);
        provider.setSocialAcceleration(social);
        provider.setInertiaWeight(inertia);

        return provider;
    }

    /**
     * Sets the GuideProvider responsible for retrieving a particle's global guide.
     * @param globalGuideProvider The guide provider to set.
     */
    public void setGlobalGuideProvider(GuideProvider globalGuideProvider) {
        this.globalGuideProvider = globalGuideProvider;
    }

    /**
     * Sets the GuideProvider responsible for retrieving a particle's local guide.
     * @param localGuideProvider The guide provider to set.
     */
    public void setLocalGuideProvider(GuideProvider localGuideProvider) {
        this.localGuideProvider = localGuideProvider;
    }

    /**
     * Sets the inertia coefficient.
     * @param inertia The inertia coefficient.
     */
    public void setInertia(ControlParameter inertia){
        this.inertia = inertia;
    }

    public ControlParameter getInertia(){
        return this.inertia;
    }

    /**
     * Sets the social coefficient.
     * @param social The social coefficient.
     */
    public void setSocial(ControlParameter social){
        this.social = social;
    }

    public ControlParameter getSocial(){
        return this.social;
    }

    /**
     * Sets the cognitive coefficient.
     * @param cognitive The cognitive coefficient.
     */
    public void setCognitive(ControlParameter cognitive){
        this.cognitive = cognitive;
    }

    public ControlParameter getCognitive(){
        return this.cognitive;
    }
}
