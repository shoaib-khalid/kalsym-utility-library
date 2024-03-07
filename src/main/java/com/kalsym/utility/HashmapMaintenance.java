package com.kalsym.utility;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * << Insert description here >>
 *
 *
 * @author fadzlan
 */
public class HashmapMaintenance extends Thread implements Runnable {

    static Logger logger = LoggerFactory.getLogger("com.kalsym.utility");

    String msisdn;
    String rowId;
    String tbl;

    public static Map<String, String> connMap = new ConcurrentHashMap<String, String>();

    public HashmapMaintenance() {
    }

    /**
     *
     * @param max_thread_alive_before_restart
     */
    public void run(int max_thread_alive_before_restart) {
        super.run();
        logger.info("Hashmap maintenance running");
        long connTimestamp = 0;
        long currentTimestamp = 0;
        long differentTimestamp = 0;
        long differentTimestampMs = 0;
        int connSize = 0;
        long keepAliveTime = 0;
        try {
            keepAliveTime = Long.parseLong(ConfigReader.getProperty("max_thread_alive_time"));
            max_thread_alive_before_restart = Integer.parseInt(ConfigReader.getProperty("max_thread_alive_before_restart"));
        } catch (Exception ex) {
            //LogProperties.WriteLog(ex.getMessage());
        }

        while (true) {
            Iterator<Entry<String, String>> itr = connMap.entrySet().iterator();
            connSize = connMap.size();
            //LogProperties.WriteLog("Hashmap for OpenConn size:" + connSize);
            logger.debug("Hashmap for OpenConn size:" + connSize);
            if (connSize > max_thread_alive_before_restart) {
                //LogProperties.WriteLog("Too many open OpenConn:" + connSize + ". Possible application problem. Application will restart now!");
                logger.info("Too many open OpenConn:" + connSize + ". Possible application problem. Application will restart now!");
                System.exit(1);
            }
            while (itr.hasNext()) {
                Entry<String, String> entry = itr.next();
                connTimestamp = Long.valueOf(entry.getValue());
                currentTimestamp = System.nanoTime();
                differentTimestamp = (currentTimestamp - connTimestamp) / 1000000000;
                differentTimestampMs = (currentTimestamp - connTimestamp) / 1000000;
                //LogProperties.WriteLog("[" + entry.getKey() + "][" + entry.getValue() + "][" + currentTimestamp + "] OpenConn period:" + differentTimestampMs + " MS");
                logger.debug("[" + entry.getKey() + "][" + entry.getValue() + "][" + currentTimestamp + "] OpenConn period:" + differentTimestampMs + " MS");
                if (differentTimestamp > keepAliveTime) {
                    connMap.remove(entry.getKey());
                    //LogProperties.WriteLog("[" + entry.getKey() + "][" + entry.getValue() + "][" + currentTimestamp + "] OpenConn period:" + differentTimestampMs + " MS Purge Connection");
                    logger.debug("[" + entry.getKey() + "][" + entry.getValue() + "][" + currentTimestamp + "] OpenConn period:" + differentTimestampMs + " MS Purge Connection");
                }
            }
            try {
                Thread.sleep(Long.parseLong(ConfigReader.getProperty("check_thread_timeout_interval")));
            } catch (NumberFormatException ex) {
                //LogProperties.WriteLog("Hashmap Error " + ex);
            } catch (InterruptedException ex) {
                //LogProperties.WriteLog("Hashmap Error " + ex);
            }
        }
    }

}
