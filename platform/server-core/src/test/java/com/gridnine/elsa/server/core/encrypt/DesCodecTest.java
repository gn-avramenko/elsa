/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.core.encrypt;

import com.gridnine.elsa.server.core.common.ServerCoreTestBase;
import com.gridnine.elsa.server.core.encrypt.DesCodec;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class DesCodecTest extends ServerCoreTestBase {

    @Autowired
    private DesCodec desCodec;

    @Test
    public void testCode(){
        final var str = "hello мир";
        var encrypted = desCodec.encrypt(str);
        Assertions.assertEquals(str, desCodec.decrypt(encrypted));
    }
}
