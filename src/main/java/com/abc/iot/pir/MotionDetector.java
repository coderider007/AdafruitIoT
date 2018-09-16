package com.abc.iot.pir;

import org.eclipse.paho.client.mqttv3.MqttClient;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

/*
 * PIR: PyroElectric Infra Red
 */
public class MotionDetector {
    final GpioController     gpio        = GpioFactory.getInstance();
    // RaspiPin.GPIO_29 => 40 Physical pin
    private static final Pin DEFAULT_PIN = RaspiPin.GPIO_29;
    private MqttClient       client;
    private Pin              pirPin      = null;

    public MotionDetector() {
        this(DEFAULT_PIN);
    }

    public MotionDetector(Pin p) {
        this.pirPin = p;
        // init();
    }

    public void setClient(MqttClient client) {
        this.client = client;
    }

    public void init() {
        GpioPinDigitalInput pirInput = gpio.provisionDigitalInputPin(pirPin, "Motion");
        pirInput.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
        pirInput.addListener(new SensorDataHandler().new PIRSensor(this.client));
    }

    public void shutdown() {
        gpio.shutdown();
    }

    public void motionDetected() {
        System.out.println("Something is moving!!!");
    }
}