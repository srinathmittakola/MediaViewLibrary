# MediaView Library

A lightweight Android custom view that automatically handles:

* Image loading with Glide
* Lottie animation loading
* Loading indicator
* Automatic media type detection
* Reusable clean UI component

---

# Features

✅ Load Images from URL
✅ Load Lottie JSON animations from URL
✅ Automatic media type detection
✅ Built-in loading indicator
✅ RecyclerView friendly
✅ Lightweight and reusable
✅ Easy integration

---

# Installation

## Step 1 — Add JitPack Repository

Add this inside your `settings.gradle`

```gradle
dependencyResolutionManagement {

    repositoriesMode.set(
        RepositoriesMode.FAIL_ON_PROJECT_REPOS
    )

    repositories {

        google()
        mavenCentral()

        maven {
            url 'https://jitpack.io'
        }
    }
}
```

---

## Step 2 — Add Dependency

```gradle
implementation("com.github.srinathmittakola:MediaViewLibrary:1.0.0")
```

---

# Usage

## XML

```xml
<com.yourname.mediaview.MediaView
    android:id="@+id/mediaView"
    android:layout_width="80dp"
    android:layout_height="80dp"/>
```

---

## Kotlin

```kotlin
binding.mediaView.load(
    "https://your-image-url.com/image.png"
)
```

---

# Lottie Support

Simply pass a `.json` URL.

```kotlin
binding.mediaView.load(
    "https://your-domain.com/animation.json"
)
```

The view automatically detects:

* Image
* Lottie animation

---

# Dependencies

* Glide
* Lottie

---

# Requirements

* Android SDK 24+
* Kotlin

---

# Author

Srinath Mittakola

GitHub:
https://github.com/srinathmittakola
