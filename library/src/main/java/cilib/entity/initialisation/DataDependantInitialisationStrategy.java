/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.entity.initialisation;

import java.util.ArrayList;
import cilib.controlparameter.ControlParameter;
import cilib.entity.Entity;
import cilib.entity.Property;
import cilib.io.ARFFFileReader;
import cilib.io.DataTable;
import cilib.io.DataTableBuilder;
import cilib.io.StandardDataTable;


public abstract class DataDependantInitialisationStrategy<E extends Entity> implements InitialisationStrategy<E> {
    protected DataTableBuilder tableBuilder;
    protected InitialisationStrategy<E> initialisationStrategy;
    protected DataTable dataset;
    protected int windowSize;
    protected ArrayList<ControlParameter[]> bounds;

    public DataDependantInitialisationStrategy() {
        initialisationStrategy = new RandomBoundedInitialisationStrategy();
        tableBuilder = new DataTableBuilder(new ARFFFileReader());
        dataset = new StandardDataTable();
        windowSize = 0;
        bounds = new ArrayList<>();
    }

    public DataDependantInitialisationStrategy(DataDependantInitialisationStrategy copy) {
        initialisationStrategy = copy.initialisationStrategy;
        tableBuilder = copy.tableBuilder;
        dataset = copy.dataset;
        windowSize = copy.windowSize;
        bounds = copy.bounds;
    }

    @Override
    public abstract void initialise(Property key, E entity);

    public void setBounds(ArrayList<ControlParameter[]> newBounds) {
        bounds = newBounds;
    }

    public void setInitialisationStrategy(InitialisationStrategy strategy) {
        initialisationStrategy = strategy;
    }

    public InitialisationStrategy getInitialisationStrategy() {
        return initialisationStrategy;
    }

    public void setDataTableBuilder(DataTableBuilder builder) {
        tableBuilder = builder;
    }

    public DataTableBuilder getDataTableBuilder() {
        return tableBuilder;
    }

}
