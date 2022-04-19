/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.demo.activator;

import com.gridnine.elsa.common.core.boot.ElsaActivator;

public class ElsaDemoActivator implements ElsaActivator {

    @Override
    public void activate() {
        System.out.println("elsa activated");
    }

    @Override
    public double getPriority() {
        return 10;
    }
}
