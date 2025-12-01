package Domain;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.*;
import com.sun.jna.ptr.IntByReference;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class NotificationWatcher {

    private static final int EVENT_OBJECT_CREATE = 0x8000;
    private static final int EVENT_OBJECT_SHOW   = 0x8002;

    private static final int WINEVENT_OUTOFCONTEXT = 0x0000;

    private User32.WinEventProc hookCallback;
    private WinNT.HANDLE hHook;

    private volatile boolean running = false;
    private Consumer<NotificationEvent> listener;

    public void setListener(Consumer<NotificationEvent> listener) {
        this.listener = listener;
    }

    public void start() {
        if (running) return;
        running = true;

        hookCallback = new User32.WinEventProc() {

            @Override
            public void callback(WinNT.HANDLE hWinEventHook, WinDef.DWORD event, WinDef.HWND hwnd, WinDef.LONG idObject, WinDef.LONG idChild, WinDef.DWORD dwEventThread, WinDef.DWORD dwmsEventTime) {

            }

            public void callback(WinDef.HWINEVENTHOOK hWinEventHook,
                                 int event,
                                 WinDef.HWND hwnd,
                                 int idObject,
                                 int idChild,
                                 int dwEventThread,
                                 int dwmsEventTime) {

                if (hwnd == null) return;

                // Only window events
                if (idObject != 0 || idChild != 0) return;

                String className = getClassName(hwnd);
                String windowText = getWindowText(hwnd);

                IntByReference pidRef = new IntByReference();
                User32.INSTANCE.GetWindowThreadProcessId(hwnd, pidRef);
                int pid = pidRef.getValue();

                String processName = getProcessName(pid);

                boolean likely =
                        (processName != null && processName.toLowerCase().contains("shellexperiencehost"))
                                || (className != null && className.toLowerCase().contains("toast"))
                                || (className != null && className.toLowerCase().contains("notification"))
                                || (windowText != null && !windowText.isEmpty());

                if (likely) {
                    NotificationEvent n = new NotificationEvent(
                            pid,
                            processName,
                            className,
                            windowText,
                            gatherChildTexts(hwnd),
                            Pointer.nativeValue(hwnd.getPointer())
                    );

                    if (listener != null) listener.accept(n);
                }
            }
        };

        hHook = User32.INSTANCE.SetWinEventHook(
                EVENT_OBJECT_CREATE,
                EVENT_OBJECT_SHOW,
                null,
                hookCallback,
                0,
                0,
                WINEVENT_OUTOFCONTEXT
        );

        if (hHook == null) {
            System.err.println("Failed to install WinEvent hook.");
        } else {
            Thread pump = new Thread(this::messagePump, "WinEventPump");
            pump.setDaemon(true);
            pump.start();
        }
    }

    public void stop() {
        running = false;
        if (hHook != null) {
            User32.INSTANCE.UnhookWinEvent(hHook);
            hHook = null;
        }
    }

    private void messagePump() {
        while (running) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException ignored) {}
        }
    }

    private String getClassName(WinDef.HWND hwnd) {
        char[] buf = new char[512];
        int len = User32.INSTANCE.GetClassName(hwnd, buf, buf.length);
        return len > 0 ? Native.toString(buf) : "";
    }

    private String getWindowText(WinDef.HWND hwnd) {
        char[] buf = new char[512];
        int len = User32.INSTANCE.GetWindowText(hwnd, buf, buf.length);
        return len > 0 ? Native.toString(buf) : "";
    }

    private String gatherChildTexts(WinDef.HWND parent) {
        List<String> texts = new ArrayList<>();

        User32.INSTANCE.EnumChildWindows(parent, (hwnd, data) -> {
            char[] buf = new char[512];
            int len = User32.INSTANCE.GetWindowText(hwnd, buf, buf.length);
            if (len > 0) {
                String s = Native.toString(buf).trim();
                if (!s.isEmpty()) texts.add(s);
            }
            return true;
        }, null);

        return String.join(" | ", texts);
    }

    private String getProcessName(int pid) {

        WinNT.HANDLE snapshot = Kernel32.INSTANCE.CreateToolhelp32Snapshot(
                Tlhelp32.TH32CS_SNAPPROCESS, new WinDef.DWORD(0));

        if (snapshot == null || WinBase.INVALID_HANDLE_VALUE.equals(snapshot)) {
            return "";
        }

        Tlhelp32.PROCESSENTRY32.ByReference entry = new Tlhelp32.PROCESSENTRY32.ByReference();
        try {
            boolean hasProcess = Kernel32.INSTANCE.Process32First(snapshot, entry);
            while (hasProcess) {
                if (entry.th32ProcessID.intValue() == pid) {
                    return Native.toString(entry.szExeFile);
                }
                hasProcess = Kernel32.INSTANCE.Process32Next(snapshot, entry);
            }
        } finally {
            Kernel32.INSTANCE.CloseHandle(snapshot);
        }

        return "";
    }
}
