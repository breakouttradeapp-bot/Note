# Notepad++ – Smart Notes
### A Production-Ready Android Notes App

---

## 📱 Features
- **Splash Screen** – Clean fade-in animation, rotation-safe, no memory leaks
- **Notes List** – RecyclerView with search, empty state, delete on long-press
- **Add/Edit Notes** – Title + content fields, validation, unsaved changes warning
- **Search** – Real-time debounced filtering, case-insensitive
- **Privacy Policy & Terms** – Fully offline, no data collection
- **Material 3 Design** – Purple (#6D28D9) theme, rounded cards, smooth UI

## 🏗 Architecture
- **MVVM** – ViewModel + LiveData/StateFlow
- **Clean Architecture** – Data / Domain / UI layers
- **Room Database** – NoteEntity, NoteDao, NoteDatabase
- **Coroutines** – All DB ops on Dispatchers.IO, lifecycleScope for UI
- **Sealed UiState** – Loading / Success / Error for all states

## 🛠 Setup Instructions

### Prerequisites
- Android Studio Hedgehog (2023.1.1) or newer
- JDK 17
- Android SDK 34
- Minimum SDK 24 (Android 7.0)

### Steps
1. **Open Project** in Android Studio: `File > Open > NotepadPlusPlus`
2. **Sync Gradle**: Click "Sync Now" when prompted
3. **Run App**: Select a device/emulator, click Run ▶

### Build Release APK
```bash
./gradlew assembleRelease
```

### Run Unit Tests
```bash
./gradlew test
```

### Run Instrumented Tests
```bash
./gradlew connectedAndroidTest
```

## 📂 Project Structure
```
app/src/main/java/com/smartnotes/notepadplusplus/
├── NoteApplication.kt          # App class, dependency initialization
├── data/
│   ├── database/
│   │   ├── NoteEntity.kt       # Room entity
│   │   ├── NoteDao.kt          # Database queries
│   │   └── NoteDatabase.kt     # Room database singleton
│   └── repository/
│       └── NoteRepository.kt   # Data access layer
├── ui/
│   ├── splash/
│   │   └── SplashActivity.kt
│   ├── noteslist/
│   │   ├── NotesListActivity.kt
│   │   ├── NotesViewModel.kt
│   │   └── NotesAdapter.kt
│   ├── addedit/
│   │   ├── AddEditNoteActivity.kt
│   │   └── AddEditViewModel.kt
│   └── privacy/
│       ├── PrivacyPolicyActivity.kt
│       └── TermsActivity.kt
└── utils/
    ├── UiState.kt
    ├── DateUtils.kt
    └── Extensions.kt
```

## 🔒 Privacy
- **No permissions required** – no internet, no camera, no storage
- **Fully offline** – all notes stored in local Room database
- **No analytics** – no tracking or data collection of any kind

## 📦 Dependencies
| Library | Version | Purpose |
|---------|---------|---------|
| Room | 2.6.1 | Local database |
| Coroutines | 1.7.3 | Async operations |
| ViewModel/LiveData | 2.7.0 | MVVM architecture |
| Material 3 | 1.11.0 | UI components |
| KSP | 1.9.22 | Annotation processing |

## ✅ Production Checklist
- [x] R8/ProGuard enabled in release builds
- [x] No GlobalScope usage
- [x] lifecycleScope used throughout
- [x] No Handler memory leaks
- [x] Configuration change safe (ViewModel)
- [x] Null-safe throughout
- [x] Sealed UiState for all states
- [x] Min SDK 24, Target SDK 34
- [x] Adaptive app icon
- [x] No unnecessary permissions
- [x] Privacy Policy & Terms screens

## 🚀 Play Store Ready
- Version: 1.0.0 (code: 1)
- Package: com.smartnotes.notepadplusplus
- Min SDK: 24 (Android 7.0 Nougat)
- Target SDK: 34 (Android 14)
