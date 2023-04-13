/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.atomikos;

import com.atomikos.icatch.config.UserTransactionService;
import com.atomikos.icatch.config.UserTransactionServiceImp;
import com.atomikos.icatch.jta.TransactionManagerImp;
import com.atomikos.icatch.jta.UserTransactionImp;
import com.gridnine.elsa.common.model.common.CallableWithExceptionAndArgument;
import com.gridnine.elsa.common.model.common.RunnableWithException;
import com.gridnine.elsa.common.utils.ExceptionUtils;
import com.gridnine.elsa.meta.config.Environment;
import com.gridnine.elsa.server.storage.repository.jdbc.model.JdbcUtils;
import com.gridnine.elsa.server.storage.transaction.TransactionContext;
import com.gridnine.elsa.server.storage.transaction.TransactionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;

public class AtomikosTransactionManager implements TransactionManager {

    private final Logger log = LoggerFactory.getLogger(getClass());

    public static final String USER_TRANSACTION_KEY = "USER_TRANSACTION_KEY";

    private final static ThreadLocal<TransactionContext> contexts = new ThreadLocal<>();

    @Override
    public TransactionContext getCurrentContext() {
        return contexts.get();
    }

    public AtomikosTransactionManager(){
        if ( TransactionManagerImp.getTransactionManager () == null ) {
            UserTransactionService uts = new UserTransactionServiceImp();
            uts.init();
        }
    }

    @Override
    public <P> P withTransaction(CallableWithExceptionAndArgument<P, TransactionContext> func, boolean readonly) {
        return ExceptionUtils.wrapException(() ->{
            var context = contexts.get();
            var owner = context == null;
            Connection connection = null;
            UserTransactionImp utx = null;
            if(context == null){
                context = new TransactionContext();
                utx = new UserTransactionImp();
                utx.begin();
                context.getAttributes().put(USER_TRANSACTION_KEY, utx);
                connection = Environment.getPublished(DataSource.class).getConnection();
                connection.setAutoCommit(false);
                contexts.set(context);
                JdbcUtils.setConnection(connection);
            }
            try {
                var result = func.call(context);
                if (owner && !readonly) {
                    utx.commit();
                    for(RunnableWithException clb: context.getPostCommitCallbacks()){
                        try{
                            clb.run();
                        } catch (Throwable t){
                            log.error("error executing callback ", t);
                        }
                    }
                } else if(owner){
                    utx.rollback();
                }
                return result;
            } catch (Throwable t) {
                log.error("error executing transaction ", t);
                if (owner) {
                    utx.rollback();
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
