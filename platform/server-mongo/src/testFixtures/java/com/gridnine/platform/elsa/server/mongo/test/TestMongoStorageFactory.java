package com.gridnine.platform.elsa.server.mongo.test;

import com.gridnine.platform.elsa.server.mongo.MongoStorageFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class TestMongoStorageFactory extends MongoStorageFactory {
    @Autowired
    private MongoTestServer mongoTestServer;

    @Override
    public String getId() {
        return "MONGO-TEST";
    }
}
