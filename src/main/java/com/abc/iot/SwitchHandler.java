
package com.abc.iot;

import java.util.HashMap;
import java.util.Map;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

public class SwitchHandler {
    private static final GpioController                    GPIO            = GpioFactory.getInstance();
    private static final Map<String, GpioPinDigitalOutput> FEED_TO_PIN_MAP = new HashMap<>();

    public SwitchHandler() {
        // Pi4J/WiringPi GPIO => Physical pin number
        // RaspiPin.GPIO_24 => PIN 35
        // RaspiPin.GPIO_25 => PIN 37
        // RaspiPin.GPIO_27 => PIN 36
        // RaspiPin.GPIO_28 => PIN 38
        // RaspiPin.GPIO_29 => PIN 40
        // pi4j pin numbering is different than Broadcom GPIO pin numbering
        GpioPinDigitalOutput ac = GPIO.provisionDigitalOutputPin(RaspiPin.GPIO_24, "AC-Switch", PinState.LOW);
        GpioPinDigitalOutput tv = GPIO.provisionDigitalOutputPin(RaspiPin.GPIO_25, "TV-Switch", PinState.LOW);
        GpioPinDigitalOutput fan = GPIO.provisionDigitalOutputPin(RaspiPin.GPIO_27, "FAN-Switch", PinState.LOW);
        GpioPinDigitalOutput light = GPIO.provisionDigitalOutputPin(RaspiPin.GPIO_28, "LIGHT-Switch", PinState.LOW);

        // configure the pin shutdown behavior; these settings will be
        // automatically applied to the pin when the application is terminated
        ac.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
        tv.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
        fan.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
        light.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);

        FEED_TO_PIN_MAP.put(IoTConstants.FEED_AC, ac);
        FEED_TO_PIN_MAP.put(IoTConstants.FEED_TV, tv);
        FEED_TO_PIN_MAP.put(IoTConstants.FEED_FAN1, fan);
        FEED_TO_PIN_MAP.put(IoTConstants.FEED_LIGHT1, light);
    }

    public void handleSwitching(String topic, String input) {
        System.out.printf("handleSwitching input: %s, topic: %s \n", input, topic);
        GpioPinDigitalOutput pin = FEED_TO_PIN_MAP.get(topic);
        if (null != pin) {
            if ("1".equalsIgnoreCase(input) && pin.isLow()) {
                System.out.println("Turning ON");
                pin.high();
            } else if ("0".equalsIgnoreCase(input) && pin.isHigh()) {
                System.out.println("Turning OFF");
                pin.low();
            }
        } else {
            System.out.println("Cannot find mapped GPOIPIN");
        }
    }

    public void shutdown() {
        GPIO.shutdown();
    }
}
