package com.abc.iot.pir;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.abc.iot.IoTConstants;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

public class SensorDataHandler {

    public class PIRSensor implements GpioPinListenerDigital {
        private MqttClient client;

        public PIRSensor(MqttClient client) {
            this.client = client;
        }

        @Override
        public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
            if ("true".equals(System.getProperty("pir.verbose", "false")))
                System.out.println(" >>> GPIO pin state changed: time=" + System.currentTimeMillis() + ", " + event.getPin() + " = " + event.getState());
            MqttMessage message = null;
            if (event.getState().isHigh()) {
                message = new MqttMessage("1".getBytes());
            }
            if (event.getState().isLow()) {
                message = new MqttMessage("0".getBytes());
            }
            try {
                message.setQos(0);
                this.client.publish(IoTConstants.FEED_MOTION, message);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }
}