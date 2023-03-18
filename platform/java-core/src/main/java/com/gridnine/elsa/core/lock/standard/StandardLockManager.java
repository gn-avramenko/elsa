/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.core.lock.standard;

import com.gridnine.elsa.core.lock.LockManager;
import com.gridnine.elsa.core.lock.NamedLock;
import com.gridnine.elsa.core.model.common.BaseIdentity;
import com.gridnine.elsa.core.model.common.Xeption;
import com.gridnine.elsa.core.utils.ExceptionUtils;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class StandardLockManager extends LockManager {

    private final ReentrantLock masterLock = new ReentrantLock();

    private final Map<String, LocalLock> locks = new ConcurrentHashMap<>();

    @Override
    public <T> T withLock(Object obj, long tryTime, TimeUnit timeUnit, Callable<T> func) {
        return ExceptionUtils.wrapException(() ->{
            var lockName = getLockName(obj);
            try (var loc = getLock(lockName)) {
                if (!loc.tryLock(tryTime, timeUnit)) {
                    throw Xeption.forDeveloper("unable to get lock %s during %s %s".formatted(lockName, tryTime, timeUnit));
                }
                try {
                    return func.call();
                } finally {
                    loc.unlock();
                }
            }
        });

    }
    private String getLockName(Object obj){
        if(obj instanceof BaseIdentity bi){
            return "%s-%s".formatted(bi.getClass().getName(), bi.getId());
        }
        if(obj instanceof String st){
            return st;
        }
        return "%s-%s".formatted(obj.getClass().getName(), obj.hashCode());
    }

    private NamedLock getLock(String name) {
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
