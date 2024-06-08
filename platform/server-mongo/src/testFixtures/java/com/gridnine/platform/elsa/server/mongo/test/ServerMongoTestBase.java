/*****************************************************************
 * Gridnine AB http://www.gridnine.com
 * Project: LDocs
 *****************************************************************/

package com.gridnine.platform.elsa.server.mongo.test;

import com.gridnine.platform.elsa.config.ElsaServerMongoConfiguration;
import com.gridnine.platform.elsa.server.core.common.ServerCoreTestBase;
import com.gridnine.platform.elsa.server.mongo.MongoFacade;
import de.bwaldvogel.mongo.MongoServer;
import de.bwaldvogel.mongo.backend.memory.MemoryBackend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {ElsaServerMongoConfiguration.class, ElsaServerMongoTestConfiguration.class}, initializers = {ServerMongoTestPropertyOverrideContextInitializer.class})
public class ServerMongoTestBase extends ServerCoreTestBase {

    private MongoServer server;

    @Override
    protected void setup() throws Exception {
        server = new MongoServer(new MemoryBackend());
        server.bind("localhost", 47017);
        super.setup();
    }

    @Override
    protected void dispose() throws Exception {
        try {
            super.dispose();
        } finally {
            server.shutdown();
        }
    }

    @Autowired
    protected MongoFacade mongoFacade;
}
