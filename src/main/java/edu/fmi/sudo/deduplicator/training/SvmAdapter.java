/**
 * Copyright 2017 (C) Endrotech
 * Created on :  1/14/2017
 * Author     :  Mario Dimitrov
 */

package edu.fmi.sudo.deduplicator.training;

import java.io.File;
import java.io.IOException;

public abstract class SvmAdapter {
    public void execute(String executablePath) {
        try {
            Runtime.getRuntime().exec(executablePath, null, new File("c:\\program files\\test\\"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
