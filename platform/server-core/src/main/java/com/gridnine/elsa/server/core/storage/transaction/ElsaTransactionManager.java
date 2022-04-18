/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.core.storage.transaction;

import com.gridnine.elsa.common.core.model.common.CallableWithExceptionAndArgument;
import com.gridnine.elsa.common.core.model.common.RunnableWithExceptionAndArgument;
import com.gridnine.elsa.common.core.utils.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

public class ElsaTransactionManager {

    private final ThreadLocal<ElsaTransactionContext> contexts = new ThreadLocal<>();

    private TransactionTemplate readOnlyTemplate;

    private TransactionTemplate template;

    @Autowired
    public void setTransactionManager(PlatformTransactionManager dstm) {
        template = new TransactionTemplate(dstm);
        readOnlyTemplate = new TransactionTemplate(dstm);
        readOnlyTemplate.setReadOnly(true);
    }

    public void withTransaction(RunnableWithExceptionAndArgument<ElsaTransactionContext> func) {
        withTransaction((context) ->{
            func.run(context);
            return (Void) null;
        }, false);
    }

    public<P> P withTransaction(CallableWithExceptionAndArgument<P, ElsaTransactionContext> func, boolean readonly) {
        return ExceptionUtils.wrapException(() -> {
            var owner = contexts.get() == null;
            try {
                if (owner) {
                    var context = new ElsaTransactionContext();
                    contexts.set(context);
                    return (readonly? readOnlyTemplate : template).execute((status) ->
                            ExceptionUtils.wrapException(() -> func.call(context)));
                } else {
                    return func.call(contexts.get());
                }
            } finally {
                if (owner) {
                    var callbacks = contexts.get().getPostCommitCallbacks();
                    contexts.remove();
                    callbacks.forEach(ExceptionUtils::wrapException);
                }
            }
        });
    }


}
