package com.redphase.framework.util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IdUtil {

    /**
     * <p>
     * 生成UUID主键
     *
     * @param len 位数
     * @return 主键
     */
    public static String createUUID(int len) {
        String uuid = java.util.UUID.randomUUID().toString().replaceAll("-", "");
        return uuid.substring(0, len);
    }

    /**
     * https://github.com/twitter/snowflake
     */
    public static long nextId() throws Exception {
        return IdWorker.getFlowIdWorkerInstance().nextId();
    }

    public static class IdWorker {

        private final long workerId;
        private final long epoch = 1403854494756L;   // 时间起始标记点，作为基准，一般取系统的最近时间
        private final long workerIdBits = 10L;      // 机器标识位数
        private final long maxWorkerId = -1L ^ -1L << this.workerIdBits;// 机器ID最大值: 1023
        private long sequence = 0L;                   // 0，并发控制
        private final long sequenceBits = 12L;      //毫秒内自增位

        private final long workerIdShift = this.sequenceBits;                             // 12
        private final long timestampLeftShift = this.sequenceBits + this.workerIdBits;// 22
        private final long sequenceMask = -1L ^ -1L << this.sequenceBits;                 // 4095,111111111111,12位
        private long lastTimestamp = -1L;

        private IdWorker(long workerId) {
            if (workerId > this.maxWorkerId || workerId < 0) {
                throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", this.maxWorkerId));
            }
            this.workerId = workerId;
        }

        public synchronized long nextId() throws Exception {
            long timestamp = timeGen();
            if (this.lastTimestamp == timestamp) { // 如果上一个timestamp与新产生的相等，则sequence加一(0-4095循环); 对新的timestamp，sequence从0开始
                this.sequence = this.sequence + 1 & this.sequenceMask;
                if (this.sequence == 0) {
                    timestamp = this.tilNextMillis(this.lastTimestamp);// 重新生成timestamp
                }
            } else {
                this.sequence = 0;
            }

            if (timestamp < this.lastTimestamp) {
                throw new Exception(String.format("clock moved backwards.Refusing to generate id for %d milliseconds", (this.lastTimestamp - timestamp)));
            }

            this.lastTimestamp = timestamp;
            return timestamp - this.epoch << this.timestampLeftShift | this.workerId << this.workerIdShift | this.sequence;
        }

        private static IdWorker flowIdWorker = new IdWorker(1);

        public static IdWorker getFlowIdWorkerInstance() {
            return flowIdWorker;
        }


        /**
         * 等待下一个毫秒的到来, 保证返回的毫秒数在参数lastTimestamp之后
         */
        private long tilNextMillis(long lastTimestamp) {
            long timestamp = timeGen();
            while (timestamp <= lastTimestamp) {
                timestamp = timeGen();
            }
            return timestamp;
        }

        /**
         * 获得系统当前毫秒数
         */
        private static long timeGen() {
            return System.currentTimeMillis();
        }

        public static void main(String[] args) throws Exception {
            System.out.println(timeGen());

            IdWorker idWorker = IdWorker.getFlowIdWorkerInstance();
            // System.out.println(Long.toBinaryString(idWorker.nextId()));
//        System.out.println(idWorker.nextId());
//        System.out.println(idWorker.nextId());
            for (int i = 0; i < 1000; i++) {
                System.out.println(idWorker.nextId());
            }
        }

    }

}
