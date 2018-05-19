package org.edraens.monitoring;

/**
 * Hello world!
 */
public final class App {
    private App() {
    }

    /**
     * Says hello to the world.
     * @param args The arguments of the program.
     */
    public static void main(String[] args) {
        System.out.println("EdMon 1.0 - Edraens 2018");
        new BatchCheck();
        System.out.println("Scanning complexe. Exiting...");
    }
}
