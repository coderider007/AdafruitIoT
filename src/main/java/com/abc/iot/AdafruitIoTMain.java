package com.abc.iot;

/**
 * Run the jar using: <br/>
 * 192.168.43.84 - VNC
 * <code>sudo java -Dpi4j.linking=dynamic -jar AdafruitIoT.jar</code>
 */
public class AdafruitIoTMain {

    public static void main(String[] args) {
        new AdafruitIoTMain().init();
    }

    private void init() {
        SwitchHandler switchHandler = new SwitchHandler();
        AdafruitMqttClient adafruitMqttClient = new AdafruitMqttClient(switchHandler);
        adafruitMqttClient.start();

        final Thread coreThread = Thread.currentThread();

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                System.out.println("\nUser interrupted.");
                synchronized (coreThread) {
                    coreThread.notify();
                }
            }
        });

        System.out.println("...On watch.");
        try {
            synchronized (coreThread) {
                coreThread.wait();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        adafruitMqttClient.shutdown();
        System.out.println("Done.");
    }
}