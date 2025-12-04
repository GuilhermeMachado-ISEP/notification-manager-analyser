namespace NotificationManager
{
    ///<summary>
    /// Representation of a notification event captured on a Windows System.
    ///</summary>
    
    public class NotificationEvent{
        ///<summary>
        /// Components of the notification event representation and its constructor
        /// </summary>
        public int processId { get; set; }
        public string processName { get; set; }
        public string className { get; set; }
        public string windowTitle { get; set; }
        public string possibleText { get; set; }
        public IntPtr windowHandle { get; set; }
        public DateTime timestamp { get; set; }

        public NotificationEvent(int pid, string nameP, string nameC, string window, string text, IntPtr hwnd)
        {
            processId = pid;
            processName = nameP ?? string.Empty;
            className = nameC ?? string.Empty;
            windowTitle = window ?? string.Empty;
            possibleText = text ?? string.Empty;
            timestamp = DateTime.Now;
        }

        /// <summary>
        /// Default toString of the representation
        /// </summary>

        public string toString()
        {
            return $"[{timestamp:HH:mm:ss}] {processName} - {windowTitle}";
        }

        /// <summary>
        /// Returns a detailed version of the notification
        /// </summary>
    
        public string toStringDefined()
        {
            return $"=== Notification Details ===\n" + 
            $"Time: {timestamp:yyyy-MM-dd HH:mm:ss}\n" +
            $"Process: {processName} (PID: {processId})\n" +
            $"Class: {className}\n" +
            $"Window Title: {windowTitle}\n" +
            $"Content: {possibleText}\n" +
            $"Handle: 0x{windowHandle.ToString("X")}\n" +
            $"============================";
        }
    }
}