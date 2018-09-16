package com.abc.iot;

public interface IoTConstants {
    // MQTT server settings
    public static final String   SERVER_URI  = "tcp://io.adafruit.com:1883";
    public static final String   CLIENT_ID   = "SmartHome-RPi-Client";
    public static final String   USER_NAME   = "your-adafruit-username-here";
    public static final char[]   PASSWORD    = "your-adafruit-password-here".toCharArray();

    // topics for feed from Adafruit MQTT
    public static final String   FEED_AC     = USER_NAME + "/feeds/ac";
    public static final String   FEED_TV     = USER_NAME + "/feeds/tv";
    public static final String   FEED_FAN1   = USER_NAME + "/feeds/fan1";
    public static final String   FEED_LIGHT1 = USER_NAME + "/feeds/light1";
    public static final String   FEED_MOTION = USER_NAME + "/feeds/motion";

    public static final String[] TOPICS      = { FEED_AC, FEED_TV, FEED_FAN1, FEED_LIGHT1 };

}
