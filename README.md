# Testing Android Mobile Application through Android Accessibility Service
The goal of this project is to use accessibility service in testing UI elements by automating interaction with an existing application running on an Android device.
## How to Build or Deploy
* Firstly, download and install the latest Android Studio from here https://developer.android.com/studio/install#64bit-libs
* Clone or download the project and then open the project directly from Android Studio.
* At this point, we can either choose to;
    * Build with project using an Android Emulator
    * Build through a physical device.
    * Generate a .APK file within Android studio which can be installed on a Physical Android device, Android Emulator or on a       server.
    
## How to Use
After successful installation, Accessibilbity Service can be used by;
* Launching the service. After this, a list of all installed application installed on the device or emulator is being displayed.
* Select from the list of all installed apps the application that needs to be tested. (**com.haringeymobile.ukweather** , **com.dougkeen.bart** , **ch.bailu.aat** can be tested). If you want to test something else, please add packagename to *android:packageNames* in the app.src.main.res.xml.servicefonfig.xml file.
* After this, Accessibility Service automatically starts testing
