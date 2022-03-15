/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.core.lock.standard;

import com.gridnine.elsa.common.core.lock.LockManager;
import com.gridnine.elsa.common.core.lock.NamedLock;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

@Component
@ConditionalOnProperty(prefix = "lock", name = "manager", havingValue = "standard", matchIfMissing = true)
public class StandardLockManager implements LockManager {

    private final ReentrantLock masterLock = new ReentrantLock();

    private final Map<String, LocalLock> locks = new ConcurrentHashMap<>();

    @Override
    public NamedLock getLock(String name) {
        masterLock.lock();
        try {
            var result = locks.computeIfAbsent(name, (it) -> new LocalLock(it, masterLock, locks));
            result.registerThread(Thread.currentThread());
            return result;
        } finally {
            masterLock.unlock();
        }
    }

}
