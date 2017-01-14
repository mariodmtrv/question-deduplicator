/**
 * Copyright 2017 (C) Endrotech
 * Created on :  1/14/2017
 * Author     :  Mario Dimitrov
 */

package edu.fmi.sudo.deduplicator.training;

import java.io.File;
import java.io.IOException;

public abstract class SvmAdapter {
    protected String executablePath;
    public void execute() {
        try {
            Runtime.getRuntime().exec(this.executablePath, null, new File(new File(this.executablePath).getParent()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
