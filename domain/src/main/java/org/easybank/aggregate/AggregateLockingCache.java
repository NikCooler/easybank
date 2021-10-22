package org.easybank.aggregate;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * When a Command is executed
 * an aggregate is locked to prevent concurrent modification.
 *
 * @author Nikolay Smirnov
 */
public final class AggregateLockingCache {

    private final ConcurrentHashMap<AggregateIdBase, ReentrantLock> lockCache = new ConcurrentHashMap<>();

    private AggregateLockingCache() {}

    public synchronized ReentrantLock getLock(AggregateIdBase id) {
        return lockCache.computeIfAbsent(id, x -> new ReentrantLock());
    }

    public void unlockAll(AggregateIdBase id) {
        while (Objects.nonNull(id.getLock()) && id.getLock().getHoldCount() > 0) {
            id.getLock().unlock();
        }
    }

    public static AggregateLockingCache getInstance() {
        return LazyAggregateCacheHolder.AGGREGATE_CACHE;
    }

    private static class LazyAggregateCacheHolder {
        private static final AggregateLockingCache AGGREGATE_CACHE = new AggregateLockingCache();
    }
}
