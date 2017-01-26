package edu.fmi.sudo.deduplicator;

import edu.fmi.sudo.deduplicator.training.SvmClassifierAdapter;
import edu.fmi.sudo.deduplicator.training.SvmLearnerAdapter;
import org.junit.Test;

public class SvmTest {
    @Test
    public void testSvm() {
        SvmLearnerAdapter learnerAdapter = new SvmLearnerAdapter(123L);
        learnerAdapter.execute();
        SvmClassifierAdapter classifierAdapter = new SvmClassifierAdapter(123L);
        classifierAdapter.execute();
    }
}
