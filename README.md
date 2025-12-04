# Notification Manager & Analyser

Windows application that captures and analyzes system notifications in real-time, allowing classification by importance.

> **Note:** This project was migrated from Java to C# for better native integration with the Windows API.  

## ✨ Features

- 🔔 Real-time Windows notification capture
- 🎯 Filtering by process, window class, and content
- 📊 Modern GUI with Windows Forms
- ⚡ Direct Windows API access (no external dependencies)
- 💾 Captured notification history

## 🛠️ Technologies

- C# / .NET 8.0
- Windows Forms
- Windows API (User32.dll, Kernel32.dll)
- Windows Event Hooks

## 📋 Requirements

- Windows 10/11
- .NET 8.0 SDK or higher
- Visual Studio 2022 (recommended)

## 🚀 How to Run

### Via Visual Studio:
1. Clone the repository
2. Open `NotificationManager.sln` in Visual Studio
3. Press `F5` to build and run

### Via command line:
```bash
dotnet build
dotnet run --project NotificationManager
```

## 📁 Project Structure

```
NotificationManager/
├── Program.cs              # Entry point
├── MainForm.cs             # Main graphical interface
├── NotificationWatcher.cs  # Capture logic (Windows hooks)
└── NotificationEvent.cs    # Data model
```

## 🎯 How It Works

The application uses **Windows Event Hooks** to intercept window creation events:
- `EVENT_OBJECT_CREATE` (0x8000)
- `EVENT_OBJECT_SHOW` (0x8002)

It filters notification-related windows (ShellExperienceHost, Toast notifications) and captures:
- Process name
- Window title
- Notification text
- Process PID

## 📝 License

MIT License - feel free to use and modify!

## 👤 Author

Developed as a personal Windows notification management project.