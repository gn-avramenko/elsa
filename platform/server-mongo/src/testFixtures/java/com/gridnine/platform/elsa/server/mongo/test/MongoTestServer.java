package com.gridnine.platform.elsa.server.mongo.test;

import de.bwaldvogel.mongo.MongoServer;
import de.bwaldvogel.mongo.backend.memory.MemoryBackend;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

public class MongoTestServer {

    private MongoServer server;

    @PostConstruct
    public void init() {
        server = new MongoServer(new MemoryBackend());
        server.bind("localhost", 47017);
    }

    @PreDestroy
    public void destroy() {
        server.shutdown();
    }

}
