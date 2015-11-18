/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.nn.architecture;

import cilib.math.Maths;
import cilib.nn.components.BiasNeuron;
import cilib.nn.components.Neuron;
import cilib.type.types.container.Vector;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 */
public class LayerTest {

    private Layer layer;
    private Vector refActivations;

    @Before
    public void setup() {
        layer = new Layer();
        layer.setBias(false);
        Neuron neuron = new Neuron();
        neuron.setDomain("R(-1:3.3)^4");
        refActivations = Vector.of(-0.1, 0.7, 0.3, -0.5);
        for (int i = 0; i < refActivations.size(); i++) {
            neuron.setActivation(refActivations.doubleValueOf(i));
            layer.add((Neuron) neuron.getClone());
        }
    }

    @Test
    public void testGetNeuralInput() {
        for (int i = 0; i < refActivations.size(); i++) {
            Assert.assertEquals(refActivations.get(i).doubleValue(), layer.getNeuralInput(i), Maths.EPSILON);
        }
    }

    @Test
    public void testGetActivations() {
         Assert.assertEquals(refActivations, layer.getActivations());
    }

    @Test
    public void testIsBias() {
        Assert.assertEquals(false, layer.isBias());
    }
	
    @Test
    public void testDomain() {
        Assert.assertEquals("R(-1:3.3)^4,R(-1:3.3)^4,R(-1:3.3)^4,R(-1:3.3)^4", layer.getDomain().getDomainString());
        Assert.assertEquals(16, ((Vector) layer.getDomain().getBuiltRepresentation()).size());

        Layer layer2 = new Layer();
        layer2.setBias(true);
        Neuron neuron = new Neuron();
        neuron.setDomain("R(-1:3.3)^4");
        refActivations = Vector.of(-0.1, 0.7, 0.3, -0.5);
        for (int i = 0; i < refActivations.size(); i++) {
            neuron.setActivation(refActivations.doubleValueOf(i));
            layer2.add((Neuron) neuron.getClone());
        }
        layer2.add(new BiasNeuron());
        Assert.assertEquals("R(-1:3.3)^4,R(-1:3.3)^4,R(-1:3.3)^4,R(-1:3.3)^4", layer2.getDomain().getDomainString());
        Assert.assertEquals(16, ((Vector) layer2.getDomain().getBuiltRepresentation()).size());
    }
	
    @Test
    public void testGetClone() {
        Layer newLayer = layer.getClone();
        for (int i = 0; i < refActivations.size(); i++) {
            Assert.assertEquals(refActivations.get(i).doubleValue(), newLayer.getNeuralInput(i), Maths.EPSILON);
        }
    }
}
