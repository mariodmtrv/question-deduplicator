package edu.fmi.sudo.deduplicator.models;

import edu.fmi.sudo.deduplicator.training.DataSetGenerator;
import edu.fmi.sudo.deduplicator.training.DataSetType;
import org.junit.Test;

import java.util.Arrays;

public class DataSetGeneratorTest {

    @Test
    public void testDataSetGeneration(){
        DataSetGenerator dataSetGenerator = new DataSetGenerator(DataSetType.TRAIN, 11L);
        dataSetGenerator.writeEntry(Arrays.asList("h e l l o", "r e a d m e"));
    }
}
