/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.test.encrypt;

import com.gridnine.elsa.server.codec.DesCodec;
import com.gridnine.elsa.server.test.ServerTestBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DesCodecTest extends ServerTestBase {

    @Test
    public void testCode(){
        final var str = "hello мир";
        var encrypted = DesCodec.get().encrypt(str);
        Assertions.assertEquals(str, DesCodec.get().decrypt(encrypted));
    }
}
