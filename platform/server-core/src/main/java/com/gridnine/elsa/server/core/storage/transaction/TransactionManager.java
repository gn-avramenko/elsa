/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.core.storage.transaction;

import com.gridnine.elsa.common.core.utils.CallableWithExceptionAndArgument;
import com.gridnine.elsa.common.core.utils.ExceptionUtils;
import com.gridnine.elsa.common.core.utils.RunnableWithExceptionAndArgument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;

@Component
public class TransactionManager {

    private final ThreadLocal<TransactionContext> contexts = new ThreadLocal<>();

    private TransactionTemplate readOnlyTemplate;

    private TransactionTemplate template;

    @Autowired
    public void setDataSource(DataSource ds) {
        var dstm = new DataSourceTransactionManager(ds);
        template = new TransactionTemplate(dstm);
        readOnlyTemplate = new TransactionTemplate(dstm);
        readOnlyTemplate.setReadOnly(true);
    }

    public void withTransaction(RunnableWithExceptionAndArgument<TransactionContext> func) {
        withTransaction((context) ->{
            func.run(context);
            return (Void) null;
        }, false);
    }

    public<P> P withTransaction(CallableWithExceptionAndArgument<P,TransactionContext> func, boolean readonly) {
        return ExceptionUtils.wrapException(() -> {
            var owner = contexts.get() == null;
            try {
                if (owner) {
                    var context = new TransactionContext();
                    contexts.set(context);
                    return (readonly? readOnlyTemplate : template).execute((status) ->
                            ExceptionUtils.wrapException(() -> func.call(context)));
                } else {
                    return func.call(contexts.get());
                }
            } finally {
                if (owner) {
                    contexts.remove();
                }
            }
        });
    }


}
