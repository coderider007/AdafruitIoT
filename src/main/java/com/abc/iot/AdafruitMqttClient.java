package com.abc.iot;

import java.util.Arrays;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.abc.iot.pir.MotionDetector;

public class AdafruitMqttClient implements MqttCallback {

    // Switch handler to control pin state according to feed
    private SwitchHandler      switchHandler;
    private MqttConnectOptions options;
    private MqttClient         client;
    private MotionDetector     md;

    public AdafruitMqttClient(SwitchHandler switchHandler) {
        this.switchHandler = switchHandler;
        this.md = new MotionDetector();
        this.options = new MqttConnectOptions();
        this.options.setUserName(IoTConstants.USER_NAME);
        this.options.setPassword(IoTConstants.PASSWORD);
    }

    public void start() {
        System.out.println("Starting Mqtt Cilent...");
        try {
            this.client = new MqttClient(IoTConstants.SERVER_URI, IoTConstants.CLIENT_ID);
            this.client.connect(this.options);
            this.client.setCallback(this);
            this.md.setClient(this.client);
            this.md.init();

            IMqttToken tkn = this.client.subscribeWithResponse(IoTConstants.TOPICS);
            Arrays.asList(tkn.getTopics()).forEach(e -> System.out.println("Subscribed to topic : " + e));
        } catch (MqttException e1) {
            e1.printStackTrace();
        }
    }

    public void shutdown() {
        this.switchHandler.shutdown();
        this.md.shutdown();
    }

    @Override
    public void connectionLost(Throwable cause) {
        System.out.println("connectionLost : " + cause);
        try {
            if (this.client.isConnected()) {
                this.client.disconnect();
            }
            this.client.connect(this.options);
            this.client.setCallback(this);
            IMqttToken tkn = this.client.subscribeWithResponse(IoTConstants.TOPICS);
            Arrays.asList(tkn.getTopics()).forEach(e -> System.out.println("Subscribed to topic : " + e));
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        System.out.println("messageArrived :" + message);
        String input = new String(message.getPayload());
        this.switchHandler.handleSwitching(topic, input);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        System.out.println("deliveryComplete : " + token);
    }

}
