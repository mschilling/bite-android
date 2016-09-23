# Bite - Android

The Android app for Bite

-----

## `Requirements`

- **Gradle 2.14.0+**
- **JDK 1.8+**

------

#### Clone
Clone the repository

```sh
cd your/folder/
git clone https://github.com/mschilling/bite-android.git
```

-----

### Gradle

`Make sure you have Android SDK and JDK 8`

##### 2 ways to set your SDK location

1. **Set environment variable for the SDK if you haven't already**

```sh
set ANDROID_HOME=location/to/your/Sdk
```

2. **Create local.properties file**

```sh
echo sdk.dir=location/to/your/Sdk > local.properties
```

-----

##### Tasks
List all Gradle Tasks

```sh
gradlew tasks
```

-----

##### Clean project
Delete the build directory

```sh
gradlew clean
```

-----


##### Assemble and test
Build and test the project

```sh
gradlew build
```

-----


##### Building in Debug Mode
Build debug app

```sh
gradlew assembleDebug
```

-----


##### Building in Release Mode
Build release app

```sh
gradlew assembleRelease
```