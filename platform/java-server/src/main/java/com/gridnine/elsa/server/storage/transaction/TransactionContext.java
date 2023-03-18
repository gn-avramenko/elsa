/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.storage.transaction;

import com.gridnine.elsa.core.model.common.RunnableWithException;

import java.util.ArrayList;
import java.util.List;

public class TransactionContext {

    private final List<RunnableWithException> postCommitCallbacks = new ArrayList<>();

    public List<RunnableWithException> getPostCommitCallbacks() {
        return postCommitCallbacks;
    }
}
