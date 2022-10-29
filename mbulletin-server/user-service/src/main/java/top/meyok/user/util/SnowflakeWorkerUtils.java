package top.meyok.user.util;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author meyok@mbulletin.meyok.top
 * @date 2022/8/30 0:30
 * 雪花算法生成id类
 */
@Component
public class SnowflakeWorkerUtils {

    /**
     * 机房id
     */
    @Value("${snowflake.worker}")
    private static long workerId;
    /**
     * 机器id
     */
    @Value("${snowflake.datacenter}")
    private static long datacenterId;

    private static short sequence = 1;
    private final static byte SEQUENCE_BIT_LENGTH = 12;
    private final static long MAX_SEQUENCE = ~(-1L << SEQUENCE_BIT_LENGTH);

    private final static byte WORKER_ID_BIT_LENGTH = 5;
    private final static long MAX_WORKER_ID = ~(-1L << WORKER_ID_BIT_LENGTH);
    private final static byte WORKER_ID_SHIFT = SEQUENCE_BIT_LENGTH;

    private final static byte DATACENTER_ID_BIT_LENGTH = 5;
    private final static long MAX_DATACENTER_ID = ~(-1L << DATACENTER_ID_BIT_LENGTH);
    private final static byte DATACENTER_ID_SHIFT = WORKER_ID_SHIFT + WORKER_ID_BIT_LENGTH;

    private static long lastTimestamp = -1L;
    private final static long INITIAL_TIME_STAMP = 1288834974657L;
    private final static byte TIMESTAMP_SHIFT = DATACENTER_ID_SHIFT + DATACENTER_ID_BIT_LENGTH;

    /**
     * 生成雪花id
     * @return 雪花id
     */
    public static synchronized Long getSnowflakeId() {
        if (workerId > MAX_WORKER_ID || workerId < 0 || datacenterId > MAX_DATACENTER_ID || datacenterId < 0) {
            // TODO: MeYok 2022/08/30 机房、机器id不符合规范
            return null;
        }
        long timestamp = System.currentTimeMillis();
        if (timestamp > lastTimestamp) {
            sequence = 1;
            lastTimestamp = timestamp;
        } else if (timestamp == lastTimestamp) {
            ++sequence;
            if (sequence > MAX_SEQUENCE) {
                while (timestamp == System.currentTimeMillis()) ;
                return getSnowflakeId();
            }
        } else {
            // TODO: MeYok 2022/08/30 时间获取出错
            return null;
        }

        return ((timestamp - INITIAL_TIME_STAMP) << TIMESTAMP_SHIFT) |
                (datacenterId << DATACENTER_ID_SHIFT) |
                (workerId << WORKER_ID_SHIFT) |
                sequence;

    }

}
