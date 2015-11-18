/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.io.transform;

import cilib.io.DataTable;
import cilib.io.exception.CIlibIOException;
import cilib.util.Cloneable;

/**
 * Interface for classes that perform an operation on data in a DataTable.
 */
public interface DataOperator extends Cloneable {

    /**
     * {@inheritDoc}
     */
    @Override
    DataOperator getClone();

    /**
     * Apply an operation to the given DataTable.
     * @param dataTable the DataTable to operate on.
     * @return the resulting DataTable.
     * @throws cilib.io.exception.CIlibIOException A wrapper exception
     * that occurred during the operation.
     */
    DataTable operate(DataTable dataTable) throws CIlibIOException;
}
