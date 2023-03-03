/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.utils;

public class BuildExceptionUtils {
    public static void wrapException(BuildRunnableWithException body){
        try {
            body.run();
        } catch (Exception e) {
            throw new Error(e);
        }
    }
}
