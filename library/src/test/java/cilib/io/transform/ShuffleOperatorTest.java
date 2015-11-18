/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package cilib.io.transform;

import java.util.List;
import cilib.io.DataTable;
import cilib.io.DataTableBuilder;
import cilib.io.DelimitedTextFileReader;
import cilib.io.exception.CIlibIOException;
import cilib.type.types.StringType;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 */
public class ShuffleOperatorTest {

    private static String testFilePath;

    @BeforeClass
    public static void setTestFilePath() {
        testFilePath = "library/src/test/resources/datasets/iris.data";
    }

    @Test
    public void TestShuffle() throws CIlibIOException {
        DataTableBuilder dataTableBuilder = new DataTableBuilder(new DelimitedTextFileReader());
        dataTableBuilder.getDataReader().setSourceURL(testFilePath);
        dataTableBuilder.buildDataTable();
        DataTable<List<StringType>, List<StringType>> dataTable = dataTableBuilder.getDataTable();
        DataTable<List<StringType>, List<StringType>> reference = (DataTable<List<StringType>, List<StringType>>) dataTable.getClone();
        
        ShuffleOperator operator = new ShuffleOperator();
        operator.operate(dataTable);

        for (int i = 0; i < dataTable.size(); i++) {
            Assert.assertNotSame(reference.getRow(i), dataTable.getRow(i));
        }
    }

}
