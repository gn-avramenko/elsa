/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.core.storage.transaction;

import com.gridnine.elsa.common.core.model.common.RunnableWithException;

import java.util.ArrayList;
import java.util.List;

public class ElsaTransactionContext {

    private final List<RunnableWithException> postCommitCallbacks = new ArrayList<>();

    public List<RunnableWithException> getPostCommitCallbacks() {
        return postCommitCallbacks;
    }
}
