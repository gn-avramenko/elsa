/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.core.test.lock;

import com.gridnine.elsa.common.core.common.TestBase;
import com.gridnine.elsa.common.core.lock.LockTemplate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class LockTest extends TestBase {

    @Autowired
    private LockTemplate lockTemplate;

    @Test
    public void testReflectionFactory(){
        var result = lockTemplate.withLock("lock", ()->{
            System.out.println("inside lock");
            return 2;
        });
        System.out.println(result);
    }
}
