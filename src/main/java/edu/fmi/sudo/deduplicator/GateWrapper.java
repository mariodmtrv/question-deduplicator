package edu.fmi.sudo.deduplicator;

import gate.Gate;
import gate.util.GateException;

/**
 * Created by mateev on 18.1.2017 г..
 */
public class GateWrapper {
    private static boolean gateInitialized = false;
    private static Object lock = new Object();

    public static void init () throws GateException {
        if (!gateInitialized) {
            synchronized (lock) {
                if (!gateInitialized) {
                    Gate.init();
                    gateInitialized = true;
                }
            }
        }
    }
}
