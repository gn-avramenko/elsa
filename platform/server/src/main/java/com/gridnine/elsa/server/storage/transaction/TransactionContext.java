/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.storage.transaction;

import com.gridnine.elsa.common.model.common.RunnableWithException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionContext {

    private final List<RunnableWithException> postCommitCallbacks = new ArrayList<>();

    private final Map<String, Object> context = new HashMap<>();

    public List<RunnableWithException> getPostCommitCallbacks() {
        return postCommitCallbacks;
    }

    public Map<String, Object> getContext() {
        return context;
    }
}
