/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.storage.repository.jdbc;

import com.gridnine.elsa.core.model.common.CallableWithExceptionAndArgument;
import com.gridnine.elsa.core.model.common.RunnableWithException;
import com.gridnine.elsa.core.utils.ExceptionUtils;
import com.gridnine.elsa.meta.config.Environment;
import com.gridnine.elsa.server.storage.repository.jdbc.model.JdbcUtils;
import com.gridnine.elsa.server.storage.transaction.TransactionContext;
import com.gridnine.elsa.server.storage.transaction.TransactionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;

public class JdbcTranscationManager implements TransactionManager {

    private final static ThreadLocal<TransactionContext> contexts = new ThreadLocal<>();

    private final static Logger log = LoggerFactory.getLogger(JdbcTranscationManager.class);
    @Override
    public <P> P withTransaction(CallableWithExceptionAndArgument<P, TransactionContext> func, boolean readonly) {
        return ExceptionUtils.wrapException(() ->{
            var context = contexts.get();
            var owner = context == null;
            Connection connection = null;
            if(context == null){
                context = new TransactionContext();
                connection = Environment.getPublished(DataSource.class).getConnection();
                connection.setAutoCommit(false);
                contexts.set(context);
                JdbcUtils.setConnection(connection);
            }
            try {
                var result = func.call(context);
                if (owner && !readonly) {
                    connection.commit();
                    for(RunnableWithException clb: context.getPostCommitCallbacks()){
                        try{
                            clb.run();
                        } catch (Throwable t){
                            log.error("error executing callback ", t);
                        }
                    }
                }
                return result;
            } catch (Throwable t) {
                log.error("error executing transaction ", t);
                if (owner) {
                    connection.rollback();
                }
                throw t;
            } finally {
                if (owner) {
                    contexts.remove();
                    connection.close();
                }
            }
        });

    }
}
