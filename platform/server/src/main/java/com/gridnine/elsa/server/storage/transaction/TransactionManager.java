/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.storage.transaction;

import com.gridnine.elsa.common.model.common.CallableWithExceptionAndArgument;
import com.gridnine.elsa.common.model.common.RunnableWithExceptionAndArgument;
import com.gridnine.elsa.meta.config.Environment;

public interface TransactionManager {

    default void withTransaction(RunnableWithExceptionAndArgument<TransactionContext> func) {
        withTransaction((context) ->{
            func.run(context);
            return (Void) null;
        }, false);
    }


    <P> P withTransaction(CallableWithExceptionAndArgument<P, TransactionContext> func, boolean readonly);


    static TransactionManager get(){
        return Environment.getPublished(TransactionManager.class);
    }

}
