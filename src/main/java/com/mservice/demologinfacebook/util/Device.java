package com.mservice.demologinfacebook.util;

import net.sf.uadetector.*;
import net.sf.uadetector.service.UADetectorServiceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;

public class Device {

    private static final Logger LOGGER = LoggerFactory.getLogger(Device.class);
    // User of browers
    public String userAgent;
    public static UserAgentStringParser parser = UADetectorServiceFactory.getResourceModuleParser();

    public Device(String userAgent){
        this.userAgent = userAgent;
    }

    public void getAllInformation(String clientId) {
        printUserAgentInfo(parser.parse(userAgent), clientId);
    }

    public boolean isMobile(){
        return isAndroidDevice() || isIOsDevice();
    }
    public boolean isIOsDevice(){
        Pattern p = Pattern.compile("(i[PSa-z\\s]+);.*?CPU\\s([OSPa-z\\s]+(?:([\\d_]+)|;))");
        return p.matcher(userAgent).find();
    }
    public boolean isIphone(){
        return userAgent.indexOf("iPhone") > 0;
    }
    public  boolean isAndroidDevice(){
        Pattern p = Pattern.compile("(Android);?[\\s\\/]+([\\d.]+)?");
        return p.matcher(userAgent).find();
    };

    public  boolean isIpod(){
        return userAgent.matches("(iPod)(.*OS\\s([\\d_]+))?");
    };

    public boolean isIpad(){
        return userAgent.matches("(iPad).*OS\\s([\\d_]+)");
    };

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public static void printUserAgentInfo(ReadableUserAgent agent, String clientId){
        LOGGER.info("[" + clientId + "]- - - - - - - - - - - - - - - - -");
        // type
        LOGGER.info("[" + clientId + "] Browser type: " + agent.getType().getName());
        LOGGER.info("[" + clientId + "] Browser name: " + agent.getName());
        VersionNumber browserVersion = agent.getVersionNumber();
        LOGGER.info("[" + clientId + "] Browser version: " + browserVersion.toVersionString());
        LOGGER.info("[" + clientId + "] Browser version major: " + browserVersion.getMajor());
        LOGGER.info("[" + clientId + "] Browser version minor: " + browserVersion.getMinor());
        LOGGER.info("[" + clientId + "] Browser version bug fix: " + browserVersion.getBugfix());
        LOGGER.info("[" + clientId + "] Browser version extension: " + browserVersion.getExtension());
        LOGGER.info("[" + clientId + "] Browser producer: " + agent.getProducer());

        // operating system
        OperatingSystem os = agent.getOperatingSystem();
        LOGGER.info("[" + clientId + "]- - - - - - - - - - - - - - - - -");
        LOGGER.info("[" + clientId + "] OS Name: " + os.getName());
        LOGGER.info("[" + clientId + "] OS Producer: " + os.getProducer());
        VersionNumber osVersion = os.getVersionNumber();
        LOGGER.info("[" + clientId + "] OS version: " + osVersion.toVersionString());
        LOGGER.info("[" + clientId + "] OS version major: " + osVersion.getMajor());
        LOGGER.info("[" + clientId + "] OS version minor: " + osVersion.getMinor());
        LOGGER.info("[" + clientId + "] OS version bug fix: " + osVersion.getBugfix());
        LOGGER.info("[" + clientId + "] OS version extension: " + osVersion.getExtension());

        // device category
        ReadableDeviceCategory device = agent.getDeviceCategory();
        LOGGER.info("[" + clientId + "]- - - - - - - - - - - - - - - - -");
        LOGGER.info("[" + clientId + "] Device: " + device.getName());
    }

    public static void main(String[] args){

    }

}
