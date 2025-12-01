package Interface;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class notificationDisplayUI implements Runnable{
    public void run(){
        start();
    }

    public void start(){
        System.out.println("Starting Notification Listener (console)...");
        NotificationWatcher watcher = new NotificationWatcher();

        // attach a simple handler that prints notification-like events
        watcher.setListener(event -> {
            System.out.println("=== Notification Candidate ===");
            System.out.println("Time: " + java.time.ZonedDateTime.now());
            System.out.println("Process: " + event.processName + " (pid:" + event.pid + ")");
            System.out.println("Class: " + event.className);
            System.out.println("WindowTitle: " + event.windowTitle);
            System.out.println("PossibleText: " + event.possibleText);
            System.out.println("-------------------------------");
        });

        watcher.start();

        // keep running until user presses ENTER
        System.out.println("Listening. Press ENTER to quit.");
        try { System.in.read(); } catch (Exception ignored) {}

        System.out.println("Stopping...");
        watcher.stop();
        System.out.println("Stopped.");
    }

}
