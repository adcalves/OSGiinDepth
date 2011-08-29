package manning.osgi.helloworld.client;

import manning.osgi.helloworld.Printer;

public class RunnablePrinterClient implements Runnable {
    
    private static final int TWO_SECS = 2000;
    
    private boolean stop;

    private Printer printer;
    
    void start() {
        stop = false;
        new Thread(this).start();
    }
    
    void stop() {
        stop = true;
    }

    public void run() {
        while (!stop) {
            
            printer.print("Hello...");

            try {
                Thread.sleep(TWO_SECS);
            } catch (InterruptedException e) {
                // Someone else interrupted the thread, let's just stop.
                stop = true;
            }
        }
    }

    void setPrinterService(Printer printer) {
        this.printer = printer;
    }

}
