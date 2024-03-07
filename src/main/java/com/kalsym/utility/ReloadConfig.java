package com.kalsym.utility;

//import com.kalsym.mqServer.JeraRequest;
import org.jera.JeraRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Reloads the config without restarting
 *
 * @author Zeeshan Ali
 */
public class ReloadConfig extends JeraRequest {

    static Logger logger = LoggerFactory.getLogger("com.kalsym.utility");

    @Override
    public void run() {
        try {
            ConfigReader conf = new ConfigReader();
            conf.reload();
            //LogProperties.WriteLog("Configurations Reloaded at:" + System.currentTimeMillis());
            logger.info("Configurations Reloaded at:" + System.currentTimeMillis());
        } catch (Exception ex) {
            //LogProperties.WriteLog("[ReloadConfig]" + ex);
        }
    }

    @Override
    public void decode() {
    }
}
