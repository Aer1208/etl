package com.mongohua.etl.schd.common;

import com.mongohua.etl.utils.RedisUtil;
import groovy.util.logging.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;

/**
 * 使用Redis实现分布式读写锁机制
 */
@Service("distributedReadWriterLock")
@Slf4j
public class DistributedReadWriterLock extends JobReadWriterLock {

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public synchronized void lockRead(int jobId, String vDate, String lockStr) throws InterruptedException {
        Integer writerLockCnt = getWriterLockObjCnt(lockStr);
        while (writerLockCnt > 0) {
            logger.info("get lock[{}] for read fail, the [{}] has writer locked, lockCnt={}", lockStr, lockStr,writerLockCnt);
            putQueue(jobId, vDate);
            wait();
            writerLockCnt = getWriterLockObjCnt(lockStr);
        }
        Integer readLockCnt = getReadLockObjCnt(lockStr);
        if (readLockCnt == null) {
            redisUtil.hset(lockStr, LockType.READ.name(), 1);
            logger.debug("get lock for read, lock object={},lockCnt={}", lockStr,1);
        } else {
            redisUtil.hincr(lockStr, LockType.READ.name(), 1);
            logger.debug("get lock for read, lock object={},lockCnt={}", lockStr,readLockCnt+1);
        }
    }

    @Override
    public synchronized void lockWriter(int jobId, String vDate, String lockStr) throws InterruptedException {
        Integer writerLockCnt = getWriterLockObjCnt(lockStr);
        Integer readLockCnt = getReadLockObjCnt(lockStr);

        while (readLockCnt > 0) {
            logger.info("get lock[{}] for writer fail, the [{}] has read locked, lockCnt={}", lockStr, lockStr,readLockCnt);
            putQueue(jobId, vDate);
            wait();
            readLockCnt = getReadLockObjCnt(lockStr);
        }

        while (writerLockCnt > 0) {
            logger.info("get lock[{}] for writer fail, the [{}] has writer locked, lockCnt={}", lockStr, lockStr,writerLockCnt);
            putQueue(jobId, vDate);
            wait();
            writerLockCnt = getWriterLockObjCnt(lockStr);
        }
        logger.debug("get lock for writer, lock object={}", lockStr);
        redisUtil.hincr(lockStr, LockType.WRITER.name(), 1);
    }

    @Override
    public synchronized void unLockRead(String lockStr) {
        Integer readLockCnt = getReadLockObjCnt(lockStr);
        if (readLockCnt != null && readLockCnt > 0) {
            logger.debug("unlock for read, unlock object={}", lockStr);
            if (readLockCnt - 1 > 0) {
                redisUtil.hdecr(lockStr, LockType.READ.name(),1);
            }else {
                redisUtil.hdel(lockStr, LockType.READ.name());
            }
            notifyAll();
        }
    }

    @Override
    public synchronized void unLockWriter(String lockStr) {
        Integer writerLockCnt = getWriterLockObjCnt(lockStr);
        if (writerLockCnt != null && writerLockCnt > 0) {
            logger.debug("unlock for writer, unlock object={}", lockStr);
            if (writerLockCnt - 1 > 0) {
                redisUtil.hdecr(lockStr, LockType.WRITER.name(), 1);
            }else {
                redisUtil.hdel(lockStr, LockType.WRITER.name());
            }
            notifyAll();
        }
    }

    @Override
    public Map<String, Integer> getReadLock() {
        return getLockByType(LockType.READ);
    }

    @Override
    public Map<String, Integer> getWriterLock() {
        return getLockByType(LockType.WRITER);
    }

    /**
     * 根据锁对象类型获取所有锁当前锁定的次数
     * @param lockType
     * @return
     */
    private Map<String, Integer> getLockByType(LockType lockType) {
        Set<String> keys = redisUtil.getAllKeys();
        Map<String, Integer> readMaps = new HashMap<String, Integer>();
        for (String key : keys) {
            if (redisUtil.hHasKey(key, lockType.name())){
                readMaps.put(key, (Integer) redisUtil.hget(key, lockType.name()));
            }
        }
        return readMaps;
    }

    @Override
    public synchronized int getReadLockObjCnt(String lockObj) {
        Object writeObject = redisUtil.hget(lockObj, LockType.READ.name());

        if (writeObject == null ) {
            return  0;
        }
        return (Integer)writeObject;
    }

    @Override
    public synchronized int getWriterLockObjCnt(String lockObj) {
        Object readObject = redisUtil.hget(lockObj, LockType.WRITER.name());
        if (readObject == null) {
            return 0;
        }
        return (Integer)readObject;
    }
}

enum LockType {
    READ,
    WRITER
}
