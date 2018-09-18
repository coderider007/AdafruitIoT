package com.abc.iot.pir;

import org.eclipse.paho.client.mqttv3.MqttClient;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
//import com.pi4j.io.gpio.trigger.GpioPulseStateTrigger;
import com.pi4j.io.gpio.trigger.GpioPulseStateTrigger;

/*
 * PIR: PyroElectric Infra Red
 */
public class MotionDetector {
    final GpioController GPIO = GpioFactory.getInstance();
    private MqttClient   client;

    public MotionDetector() {
    }

    public void setClient(MqttClient client) {
        this.client = client;
    }

    public void init() {
        // RaspiPin.GPIO_29 => 40 Physical pin
        GpioPinDigitalInput pirInput = GPIO.provisionDigitalInputPin(RaspiPin.GPIO_29, "Motion");
        // RaspiPin.GPIO_23 => 33 Physical pin
        GpioPinDigitalOutput buzzer = GPIO.provisionDigitalOutputPin(RaspiPin.GPIO_23, "buzzer", PinState.HIGH);

        // buzz for 2 seconds if motion is detected
        pirInput.addTrigger(new GpioPulseStateTrigger(PinState.LOW, buzzer, 2000));
        pirInput.addListener(new SensorDataHandler().new PIRSensor(this.client));
        GPIO.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF, pirInput, buzzer);
    }

    public void shutdown() {
        GPIO.shutdown();
    }

}