package com.mongohua.etl.schd.common;

import com.mongohua.etl.model.JobLockObj;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * 实现对象的读写锁机制
 * @author xiaohf
 */
public class JobReadWriterLock implements ReadWriterLock{

    public final Logger logger = LoggerFactory.getLogger(JobReadWriterLock.class);

    private static JobReadWriterLock instance;

    public static JobReadWriterLock getInstance() {
        if (instance == null) {
            synchronized (JobReadWriterLock.class) {
                if (instance == null) {
                    instance = new JobReadWriterLock();
                }
            }
        }
        return instance;
    }

    private static Map<String, Integer> readLock = new HashMap<String, Integer>();
    private static Map<String, Integer> writerLock=new HashMap<String, Integer>();

    private static Map<String, Integer> queues = new HashMap<String, Integer>();

    /**
     * 获取读锁
     * @param lockStr
     * @throws InterruptedException
     */
    public synchronized void lockRead(int jobId, String vDate,String lockStr) throws InterruptedException {
        Integer writerLockCnt = getWriterLockObjCnt(lockStr);
        while (writerLockCnt > 0) {
            logger.info("get lock[{}] for read fail, the [{}] has writer locked, lockCnt={}", lockStr, lockStr,writerLockCnt);
            putQueue(jobId, vDate);
            wait();
            writerLockCnt = getWriterLockObjCnt(lockStr);
        }
        Integer readLockCnt = readLock.get(lockStr);
        if (readLockCnt == null) {
            readLock.put(lockStr, 1);
            logger.debug("get lock for read, lock object={},lockCnt={}", lockStr,1);
        } else {
            readLock.put(lockStr, readLockCnt + 1);
            logger.debug("get lock for read, lock object={},lockCnt={}", lockStr,readLockCnt+1);
        }
    }

    /**
     * 获取写锁
     * @param lockStr
     * @throws InterruptedException
     */
    public synchronized void lockWriter(int jobId, String vDate,String lockStr) throws InterruptedException {
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
        writerLock.put(lockStr, writerLockCnt + 1);
    }

    /**
     * 将获取锁失败的作业插入队列中
     * @param jobId
     * @param vDate
     */
    public synchronized void putQueue(int jobId, String vDate) {
        String key = jobId + "~" + vDate;
        if (!queues.containsKey(key)) {
            queues.put(key,1);
        }
    }

    /**
     * 将需要释放的锁移除
     * @param jobId
     * @param vDate
     */
    public   synchronized  void clearQueue(int jobId, String vDate) {
        String key = jobId + "~" + vDate;
        if (queues.containsKey(key)) {
            queues.remove(key);
        }
    }

    /**
     * 解除读锁
     * @param lockStr
     */
    public synchronized void unLockRead(String lockStr) {
        Integer readLockCnt = readLock.get(lockStr);
        if (readLockCnt != null && readLockCnt > 0) {
            logger.debug("unlock for read, unlock object={}", lockStr);
            if (readLockCnt - 1 > 0) {
                readLock.put(lockStr, readLockCnt - 1);
            }else {
                readLock.remove(lockStr);
            }
            notifyAll();
        }
    }

    /**
     * 解除写锁
     * @param lockStr
     */
    public synchronized void unLockWriter(String lockStr) {
        Integer writerLockCnt = writerLock.get(lockStr);
        if (writerLockCnt != null && writerLockCnt > 0) {
            logger.debug("unlock for writer, unlock object={}", lockStr);
            if (writerLockCnt - 1 > 0) {
                writerLock.put(lockStr, writerLockCnt - 1);
            }else {
                writerLock.remove(lockStr);
            }
            notifyAll();
        }
    }

    /**
     * 获取锁
     * @param jobId
     * @param vDate
     * @throws InterruptedException
     */
    public synchronized void lock(int jobId, String vDate) throws InterruptedException {

        logger.debug("lock jobId={} vData={}",jobId, vDate);

        // 1、 获取写锁
        List<JobLockObj> writerLockObjs = InitDataBase.jobWriterLock.get(jobId);
        if (writerLockObjs != null) {
            for(JobLockObj writerLockObj : writerLockObjs) {
                lockWriter(jobId, vDate,ParamPaser.parse(writerLockObj.getLockObj(),vDate).toUpperCase());
            }
        }

        // 2. 获取读锁
        List<JobLockObj> readLockObjs = InitDataBase.jobReadLock.get(jobId);
        if (readLockObjs != null) {
            for (JobLockObj readLockObj : readLockObjs) {
                lockRead(jobId, vDate,ParamPaser.parse(readLockObj.getLockObj(),vDate).toUpperCase());
            }
        }
    }

    public synchronized  void unlock(int jobId, String vDate) {

        logger.debug("unlock jobId={} vData={}",jobId, vDate);

        // 1、 释放写锁
        List<JobLockObj> writerLockObjs = InitDataBase.jobWriterLock.get(jobId);
        if (writerLockObjs != null) {
            for(JobLockObj writerLockObj : writerLockObjs) {
                unLockWriter(ParamPaser.parse(writerLockObj.getLockObj(),vDate).toUpperCase());
            }
        }

        // 2. 释放读锁
        List<JobLockObj> readLockObjs = InitDataBase.jobReadLock.get(jobId);
        if (readLockObjs != null) {
            for (JobLockObj readLockObj : readLockObjs) {
                unLockRead(ParamPaser.parse(readLockObj.getLockObj(),vDate).toUpperCase());
            }
        }

        // 3. 清除queues里面的对象
        clearQueue(jobId, vDate);
    }

    public Map<String, Integer> getReadLock() {
        return readLock;
    }

    public Map<String, Integer> getWriterLock() {
        return writerLock;
    }

    /**
     * 清除对象锁，包括读锁和写锁
     * @param lockStr
     */
    public synchronized void clearLock(String lockStr) {
        readLock.remove(lockStr);
        writerLock.remove(lockStr);
        notifyAll();
    }

    /**
     * 获取对象的读锁次数
     * @param lockObj
     * @return
     */
    public synchronized int getReadLockObjCnt(String lockObj) {
        Integer readLockCnt = readLock.get(lockObj);
        if (readLockCnt == null) {
            readLockCnt=0;
        }
        return readLockCnt;
    }

    public synchronized int getWriterLockObjCnt(String lockObj) {
        Integer writerLockCnt = writerLock.get(lockObj);
        if (writerLockCnt == null) {
            writerLockCnt = 0;
        }
        return writerLockCnt;
    }
}
