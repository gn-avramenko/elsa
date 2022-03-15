/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.core.test.reflection;

import com.gridnine.elsa.common.core.common.TestBase;
import com.gridnine.elsa.common.core.reflection.ReflectionFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class ReflectionFactoryTest extends TestBase {

    @Autowired
    private ReflectionFactory reflectionFactory;

    @Test
    public void testReflectionFactory(){
        println(reflectionFactory.getClass(ReflectionFactory.class.getName()));
    }
}
