/**
 * Copyright 2017 (C) Endrotech
 * Created on :  1/14/2017
 * Author     :  Mario Dimitrov
 */

package edu.fmi.sudo.deduplicator;

import edu.fmi.sudo.deduplicator.training.SvmLearnerAdapter;
import org.junit.Test;

public class SvmTest {
    @Test
    public void testSvm(){
        SvmLearnerAdapter learnerAdapter = new SvmLearnerAdapter();
        learnerAdapter.execute(null);
    }
}
