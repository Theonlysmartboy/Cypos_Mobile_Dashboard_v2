# Cypos Dashboard

Cypos Dashboard is an Android application designed to provide real-time business analytics and management insights for the Cypos ecosystem. It allows users to monitor sales, branch performance, and other key metrics through an intuitive mobile interface.

## Features

- **Business Analytics Dashboard**: Comprehensive view of business performance with visual data representation.
- **Detailed Reports**: Granular view of dashboard items with support for date-range filtering (From/To) and branch-specific data.
- **Data Visualization**: Integrated charts and graphs for easy trend analysis using MPAndroidChart.
- **User Authentication**: Secure Login, Registration, and Password Recovery/Reset workflows.
- **Modern UI/UX**: Built with Material Design components, Lottie animations for better engagement, and a responsive navigation drawer.

## Technology Stack

- **Language**: Java / Kotlin
- **Architecture**: Fragment-based UI with Navigation Component.
- **Networking**: Volley & Retrofit for robust API communication and JSON parsing.
- **UI Components**:
    - `MPAndroidChart` for data visualization.
    - `Lottie` for interactive animations.
    - `ViewBinding` for type-safe view access.
- **Data Handling**: `Gson` for JSON serialization/deserialization.

## Current Status

The project is currently in active development. 

- [x] Authentication module (Login, Register, Recovery).
- [x] Navigation structure and Main Menu.
- [x] Dashboard UI with Card-based layout.
- [x] Date filtering logic in Detail views.
- [ ] Finalizing data parsing for specific dashboard detail types.
- [ ] Optimization of network retry policies.

## Getting Started

1. Clone the repository.
2. Open the project in Android Studio (Giraffe or newer recommended).
3. Ensure you have the latest Android SDK and Build Tools installed.
4. Sync Gradle and run the `:app` module.

---
© 2024 Cybene Dashboard
