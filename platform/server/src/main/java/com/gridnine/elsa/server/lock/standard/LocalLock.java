/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.lock.standard;

import com.gridnine.elsa.server.lock.NamedLock;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class LocalLock extends ReentrantLock implements NamedLock {

    private final Map<Thread, Boolean> threadsMap = new ConcurrentHashMap<>();

    private final String name;

    private final ReentrantLock masterLock;

    private final Map<String, LocalLock> locks;

    public LocalLock(String name, ReentrantLock masterLock, Map<String, LocalLock> locks) {
        this.name = name;
        this.masterLock = masterLock;
        this.locks = locks;
    }

    @Override
    public String getName() {
        return name;
    }

    void registerThread(Thread thread) {
        threadsMap.put(thread, true);
    }

    @Override
    public void close() {
        if (getHoldCount() == 0) {
            masterLock.lock();
            try {
                threadsMap.remove(Thread.currentThread());
                if (threadsMap.isEmpty()) {
                    locks.remove(name);
                }
            } finally {
                masterLock.unlock();
            }
        }
    }
}
