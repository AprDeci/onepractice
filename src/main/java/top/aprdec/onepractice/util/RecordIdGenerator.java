package top.aprdec.onepractice.util;

import org.jetbrains.annotations.NotNull;

/**
 * Snowflake + Base62 record id generator.
 */
public final class RecordIdGenerator {

    private static final long EPOCH = 1704067200000L;
    private static final long WORKER_ID_BITS = 5L;
    private static final long DATACENTER_ID_BITS = 5L;
    private static final long SEQUENCE_BITS = 12L;

    private static final long MAX_WORKER_ID = ~(-1L << WORKER_ID_BITS);
    private static final long MAX_DATACENTER_ID = ~(-1L << DATACENTER_ID_BITS);
    private static final long SEQUENCE_MASK = ~(-1L << SEQUENCE_BITS);

    private static final long WORKER_ID_SHIFT = SEQUENCE_BITS;
    private static final long DATACENTER_ID_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;
    private static final long TIMESTAMP_LEFT_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS + DATACENTER_ID_BITS;

    private static final char[] BASE62 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();

    private static final RecordIdGenerator INSTANCE = new RecordIdGenerator();

    private final long workerId;
    private final long datacenterId;

    private long sequence = 0L;
    private long lastTimestamp = -1L;

    private RecordIdGenerator() {
        long machine = Math.abs(System.getProperty("user.name", "onepractice").hashCode());
        this.workerId = machine & MAX_WORKER_ID;
        this.datacenterId = (machine >> WORKER_ID_BITS) & MAX_DATACENTER_ID;
    }

    public static String nextRecordId() {
        return toBase62(INSTANCE.nextLongId());
    }

    private synchronized long nextLongId() {
        long timestamp = currentTime();
        if (timestamp < lastTimestamp) {
            timestamp = waitUntil(lastTimestamp);
        }

        if (timestamp == lastTimestamp) {
            sequence = (sequence + 1) & SEQUENCE_MASK;
            if (sequence == 0L) {
                timestamp = waitUntil(lastTimestamp);
            }
        } else {
            sequence = 0L;
        }

        lastTimestamp = timestamp;

        return ((timestamp - EPOCH) << TIMESTAMP_LEFT_SHIFT)
                | (datacenterId << DATACENTER_ID_SHIFT)
                | (workerId << WORKER_ID_SHIFT)
                | sequence;
    }

    private static long currentTime() {
        return System.currentTimeMillis();
    }

    private static long waitUntil(long lastTimestamp) {
        long timestamp = currentTime();
        while (timestamp <= lastTimestamp) {
            timestamp = currentTime();
        }
        return timestamp;
    }

    private static String toBase62(long value) {
        if (value == 0L) {
            return "0";
        }
        StringBuilder sb = new StringBuilder(12);
        long current = value;
        while (current > 0) {
            int index = (int) (current % 62);
            sb.append(BASE62[index]);
            current /= 62;
        }
        return sb.reverse().toString();
    }
}
