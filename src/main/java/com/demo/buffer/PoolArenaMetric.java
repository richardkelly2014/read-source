package com.demo.buffer;

import java.util.List;

public interface PoolArenaMetric {

    int numThreadCaches();

    int numTinySubpages();

    int numSmallSubpages();

    int numChunkLists();

    List<PoolSubpageMetric> tinySubpages();

    List<PoolSubpageMetric> smallSubpages();

    List<PoolChunkListMetric> chunkLists();

    /**
     * Return the number of allocations done via the arena. This includes all sizes.
     */
    long numAllocations();

    /**
     * Return the number of tiny allocations done via the arena.
     */
    long numTinyAllocations();

    /**
     * Return the number of small allocations done via the arena.
     */
    long numSmallAllocations();

    /**
     * Return the number of normal allocations done via the arena.
     */
    long numNormalAllocations();

    /**
     * Return the number of huge allocations done via the arena.
     */
    long numHugeAllocations();

    /**
     * Return the number of deallocations done via the arena. This includes all sizes.
     */
    long numDeallocations();

    /**
     * Return the number of tiny deallocations done via the arena.
     */
    long numTinyDeallocations();

    /**
     * Return the number of small deallocations done via the arena.
     */
    long numSmallDeallocations();

    /**
     * Return the number of normal deallocations done via the arena.
     */
    long numNormalDeallocations();

    /**
     * Return the number of huge deallocations done via the arena.
     */
    long numHugeDeallocations();

    /**
     * Return the number of currently active allocations.
     */
    long numActiveAllocations();

    /**
     * Return the number of currently active tiny allocations.
     */
    long numActiveTinyAllocations();

    /**
     * Return the number of currently active small allocations.
     */
    long numActiveSmallAllocations();

    /**
     * Return the number of currently active normal allocations.
     */
    long numActiveNormalAllocations();

    /**
     * Return the number of currently active huge allocations.
     */
    long numActiveHugeAllocations();

    /**
     * Return the number of active bytes that are currently allocated by the arena.
     */
    long numActiveBytes();
}
