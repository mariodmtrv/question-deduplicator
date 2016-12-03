/*
 * $Id$
 *
 * Copyright 2016 Skrill Ltd. All Rights Reserved.
 * SKRILL PROPRIETARY/CONFIDENTIAL. For internal use only.
 */

package edu.fmi.sudo.deduplicator.dal;

import java.net.UnknownHostException;

import com.mongodb.MongoClient;

public class RemoteDataAccessFactory extends DataAccessFactory {
    private final String url ="";
    private int port;
    @Override
    public void prepareClient() {

        try {
            MongoClient mongo = new MongoClient(url, port);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
