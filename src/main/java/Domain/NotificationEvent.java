package Domain;

public class NotificationEvent {
    public final int pid;
    public final String processName;
    public final String className;
    public final String windowTitle;
    public final String possibleText;
    public final long hwnd;

    public NotificationEvent(int pid, String processName, String className, String windowTitle, String possibleText, long hwnd) {
        this.pid = pid;
        this.processName = processName;
        this.className = className;
        this.windowTitle = windowTitle;
        this.possibleText = possibleText;
        this.hwnd = hwnd;
    }
}
