# Group-04 - Medcar

![logo7](https://user-images.githubusercontent.com/68125677/116778243-6825bb00-aa79-11eb-83d6-5405cf059f2a.png)

## Contents
* <a href="https://github.com/DIT112-V21/group-04/tree/FinalizeReadme#what-are-you-going-to-make">What are you going to make</a>
* <a href="https://github.com/DIT112-V21/group-04/tree/FinalizeReadme#why-will-you-make-it">Why will you make it</a>
* <a href="https://github.com/DIT112-V21/group-04/tree/FinalizeReadme#how-are-you-going-to-make-it">How are you going to make it</a>
* <a href="https://github.com/DIT112-V21/group-04/tree/FinalizeReadme#technologies-we-used">Technologies we used</a>
* <a href="https://github.com/DIT112-V21/group-04/tree/FinalizeReadme#licensing">Licensing</a>
* <a href="https://github.com/DIT112-V21/group-04/tree/FinalizeReadme#feature-summary">Feature Summary</a>
* <a href="https://github.com/DIT112-V21/group-04/tree/FinalizeReadme#hardware-and-software-architecture">Hardware and Software Architecture</a>
* <a href="https://github.com/DIT112-V21/group-04/tree/FinalizeReadme#set-up-and-get-started-guide">Set-up and Get Started Guide</a>
* <a href="https://github.com/DIT112-V21/group-04/tree/FinalizeReadme#user-manual">User Manual</a>
* <a href="https://github.com/DIT112-V21/group-04/tree/FinalizeReadme#progress-log">Progress Log</a>
* <a href="https://github.com/DIT112-V21/group-04/tree/FinalizeReadme#team-overview">Team Overview</a>

## What are you going to make? 

For this project, we plan to create an Android app which will control Arduino cars. The cars will assist medical staff by performing certain tasks within a hospital, such as transporting food and medicine to patients/other medical staff.

The car will be traveling through corridors within a hospital, which we plan to take into consideration by providing features which would prevent damage to the car. These features would include but are not limited to: obstacle avoidance, notifications on certain situations that the car encounters, and a camera view.

Along with manually maneuvering the car, the car would also be able to drive autonomously, not requiring any input from the user unless necessary. This would make maneuvering the car more effortless as medical staff won't have to constantly provide input for the car to move. 


## Why will you make it?

Due to the current pandemic, the medical system is overwhelmed with the amount of cases. Contact between medical staff and patients put medical staff at higher risk of contracting the virus. In addition, medical care becomes difficult to deliver to immune deficient patients due to them being at high risk of contracting the virus. This is where our project comes in. By using Arduino cars, the contact between medical staff and patients can be limited to only when necessary. The cars will be able to carry out certain tasks, such as delivering medicine/doctor messages to patients.

Although, the question arises, will these cars still be useful after the pandemic? Yes! Due to these cars carrying out certain tasks for medical staff as stated previously, they may reduce the medical staff's work load, and allow them to focus on more advanced tasks where human intercation is necessary. Furthermore, features such as autonomous driving and live camera streaming enable the car to be adapted/reused to help other scenarios outside of the medical field, such as security monitoring in buildings and delivering food.

## How are you going to make it?
To allow medical staff to control the car, an Android app will be developed by using java for the back-end, and XML for the front-end. The app will allow medical staff to choose which car they connect to. Within the app, staff members will then be able to switch between manually maneuvering the car with a joystick, and letting the car drive autonomously.

The connection between the Android app and Arduino cars will be set up by using an MQTT broker and an AWS server. The Android app and Arduino car will both be able to publish instructions to the broker and receive content from subscribed channels. We plan to keep this connection secure by requiring a login from the user when starting the app.

We plan to add obstacle detection, which will stop the car from accelerating in the direction that the obstacle has been detected. This will be done by using ultrasonic sensors and infrared sensors detecting when obstacles are about 30cm away from the car. This will be implemented using C++. The car will also provide feedback and visualization to the app on its status. Feedback and visualization would include connection status notifications, obstacle detected notifications, the speed and angle the car is traveling at, and a live camera stream.

### Technologies we used:

* <a href="https://github.com/ItJustWorksTM/smce-gd">SMCE</a>
* <a href="https://github.com/platisd/smartcar_shield">Smartcar shield</a>
* <a href="https://github.com/controlwear/virtual-joystick-android">Joystick</a>
* <a href="https://github.com/eclipse/paho.mqtt.android/blob/master/README.md">Paho MQTT</a>
* <a href="https://github.com/DIT112-V21/smartcar-mqtt-controller">Smartcar code examples</a>
* <a href="https://github.com/google/googletest">Google test</a>
* <a href="https://github.com/anastr/SpeedView/blob/872293db92730d1e9c638db44eb83c05c502b05e/README.md">Speedometer</a>
* <a href="https://github.com/junit-team/junit4/blob/master/doc/ReleaseNotes4.13.md">JUnit</a>
* <a href="https://developer.android.com/training/testing/espresso">Espresso</a>
* <a href="https://github.com/DroidsOnRoids/android-gif-drawable">Gif drawable</a>
* <a href="https://www.arduino.cc/en/software">Arduino IDE</a>
* <a href="https://www.arduino.cc/pro/cli">Arduino CLI</a>
* C++
* Java
* Github
* Github actions
* Mosquitto
* Android Studio
* CLion
* Arduino
* Amazon AWS
* Gradle

### Licensing

This repository additionally contains adapted code components licensed under the following:

* MIT - https://mit-license.org
* Apache 2.0 - https://www.apache.org/licenses/LICENSE-2.0.html
* EPL 1.0 - https://www.eclipse.org/legal/epl-v10.html
* BSD 3-Clause - https://opensource.org/licenses/BSD-3-Clause

## Feature Summary


#### <a href="https://github.com/DIT112-V21/group-04/wiki/Build-Android-app-for-control-and-user-interface">Android app with UI for car control and more</a>

Android app with a UI for selecting an available car and controlling it. Also includes other features such as selecting online or offline mode, logging in to a remote server and getting feedback messages about the system. 

#### <a href="https://github.com/DIT112-V21/group-04/wiki/Autonomous-driving">Autonomous driving</a>

The autonomous driving feature keeps the MedCar moving and avoiding obstacles even without needing any input from the user. The user can later regain control of the MedCar when the user turns the auto driving mode off.

#### <a href="https://github.com/DIT112-V21/group-04/wiki/Connectivity-between-car-emulator-and-Android-app"> Connectivity between car emulator and Android app</a>

The connectivity feature is responsible for having the car emulator and the android app being able to connect to each other. When the car emulator and the android app are connected to each other via an AWS server or a local one they could communicate and send over different commands. Every piece of information that the app and car needs to send to each other is going to be through MQTT.

#### <a href="https://github.com/DIT112-V21/group-04/wiki/Continuous-integration">Continuous Integration</a>

The project implements continuous integration by utilizing Github actions. There are two automated tests, an arduino CI and android CI test that run each time a developer pushes or creates a pr in the github page. The tests are runned on a universal system ensuring that our product works on all various types of systems. Following the continuous integration principle, allows the developers to quickly understand errors.

#### <a href="https://github.com/DIT112-V21/group-04/wiki/Documentation">Documentation</a>

The project should include documentation about each milestone and feature in the app. This will give the users of our app a much easier understanding of how each component works. The documentation will include a description, functional requirements, non-functional requirements and an example use of the specific milestone.

#### <a href="https://github.com/DIT112-V21/group-04/wiki/Feedback-to-user-and-analysis-of-data">Feedback to user and analysis of data</a>

The feedback feature is responsible for giving the user of the app visual information about the status of the car and the app. It is going to notify the user if there's any information that the user should know, an example of this could be error messages.

#### <a href="https://github.com/DIT112-V21/group-04/wiki/Improvements">Improvements</a>

This milestone is about finalizing the software in sprint 4, and looking through the code and finding all bugs present ensuring that there are no bugs in the system. This will make sure that the application runs the way it was planned to.

#### <a href="https://github.com/DIT112-V21/group-04/wiki/Manual-maneuvering-via-Android-app">Manual maneuvering via Android app</a>

The car should be maneuvered manually by a user via a joystick in the app. The joystick should allow the user to easily adjust the speed and direction that the Medcar is travelling at. The joystick may also provide a better range of motion, allowing for more precise movements when necessary.


#### <a href="https://github.com/DIT112-V21/group-04/wiki/Obstacle-avoidance">Obstacle avoidance</a>

The Obstacle avoidance feature ensures that the Medcar will not collide with obstacles/people in a hospital environment. The Medcar will make use of an ultrasonic sensor and an infrared sensor in order to avoid obstacles that are in-front and behind it.

#### <a href="https://github.com/DIT112-V21/group-04/wiki/Security">Security</a>

The app will use an AWS external server so that the connection to an external server cannot be accessed by unauthorised users.

#### <a href="https://github.com/DIT112-V21/group-04/wiki/Unit-testing">Unit testing</a>

Unit testing will be implemented for both the Android app and Arduino parts of the project. Android app testing allows the developers to check newly added app functionality works as intended. Likewise, Arduino testing allows developers to test functions with the Medcar.


##  Hardware and Software Architecture

When designing the Android application, we decided to adhere to object oriented principles. During the development process the team decided to stay consistent with discussed/planned naming conventions and general code structure. For the Android app, the business logic was implemented via Java and the user interface was implemented by using XML files. The Medcar sketch was implemented via C++/Arduino code. The Arduino code also makes use of an ultrasonic and infrared sensor, which are mainly used to detect obstacles. Both, the app code and Arduino sketch code, encapsulate majority of their business logic in methods, which allows for code reuse, improved readability, and also makes the system more testable. Communication between the smartcar and android application is carried out via an MQTT broker, which resides on an AWS server. The Medcar and Android application can also communicate via an MQTT broker that is located on a local host instead of an external server if preferred. The sketch can also be run on the SMCE-GD emulator, allowing for remote testing, and hence not actually needing the physical car to implement new features. 

## Set-up and Get Started Guide

NOTE: This setup guide was written considering <a href="https://github.com/ItJustWorksTM/smce-gd/releases/tag/v1.3.0">SMCE-GD version 1.3.0</a>

### Pre-installation requirements

#### Before getting started with using the Medcar application, you will need to clone and install the following repository/software:

1. In order to get started with the Medcar application, clone the repository: https://github.com/DIT112-V21/group-04.git.
2. <a href="https://developer.android.com/studio">Install Android studio</a> 

#### In order to run the sketch on the SMCE-GD emulator, you will need to follow the install guide that corresponds to your operating system:

* <a href="https://github.com/ItJustWorksTM/smce-gd/wiki/Windows-setup">Windows install guide</a> 
* <a href="https://github.com/ItJustWorksTM/smce-gd/wiki/MacOS-setup">MacOS install guide</a> 
* <a href="https://github.com/ItJustWorksTM/smce-gd/wiki/Debian-based-Linux-setup">Debian based Linux setup</a> 
* <a href="https://github.com/ItJustWorksTM/smce-gd/wiki/Arch-based-Linux-setup">Arch based Linux setup</a> 

(NOTE: SMCE-GD install instructions were provided by the following <a href="https://github.com/ItJustWorksTM/smce-gd">repository</a>) 

### Set-up Arduino IDE
1. <a href="https://www.arduino.cc/en/software">Install Arduino IDE</a>.
2.  To set up the board in Arduino IDE:
    * <a href="https://github.com/espressif/arduino-esp32/blob/master/docs/arduino-ide/boards_manager.md">Follow these instructions</a>
    * In the Arduio IDE, navigate to Tools -> Board and select "DOIT ESP32 devkit v1"
3.  Next, <a href="https://github.com/platisd/smartcar_shield">install the Smartcar shield library</a>.
4.  In the Arduino IDE, navigate to: Sketch -> Include library -> Manage libraries and search for "Smartcar shield"
5.  Once found, press the install button.
6. Select File -> Open and then navigate to the group-04/arduino/sketches/smartcar/ directory, and select the smartcar.ino sketch. To compile the sketch in the IDE, press the tick button on the top of the IDE bar.
7. Lastly, you will also need to <a href="https://mosquitto.org/">install Eclipse Mosquitto</a>.

 (NOTE: Arduino board library install instructions were provided by the following <a href="https://github.com/espressif/arduino-esp32">repository</a>)<br />
 (NOTE: Smartcar shield library install instructions were provided by the following <a href="https://github.com/platisd/smartcar_shield">repository</a>)

### Set-up Android app

1. In order to use the Android application, use Android studio to open the android directory located in the cloned group-04 repository folder. 
2. After opening the project, select File -> "Sync Project with Gradle Files".
3. Then build the apk via: Build -> Build Bundle(s)/APK(s) -> Build APK(s) and install it on an Android device. To use the AWS server, select online mode when launching the app. If it is preferred to use the local host, select offline mode.


### Running the sketch

1. In order to run the sketch, open SMCE-GD and press fresh.
2. Select the + option in the left bar, and then choose to Add new.
3. Locate the group-04 cloned repository folder, and then navigate to the sketch which should be located in: group-04/arduino/sketches/smartcar/
4. Afterwards, select to compile and start. You can then select which server option (offline/online) in the Android app.

### Setting up Unit testing for developers

This section covers how to run the unit tests that have been created for both, the Android app and Arduino sketch. (Note, please follow the above set up before moving on with this section).

#### Android
For the Android Unit tests, Android Studio is required.
1. Using Android Studio, Open the Android folder within the cloned group-04 repository directory.
2. Next, sync the gradle project: File -> select File -> "Sync Project with Gradle Files".
3. Afterwards, navigate to the directory: app/java/com.example.medcarapp (androidTest).
4. To run the tests, navigate to the specific class you want to test (e.g: ManualControlTest contains the tests related to the manual control screen).
5. To the left of the class name, there will be a green arrow. When selected, all tests within the test file will run.
#### Arduino
For the Arduino Unit tests, CLion and CMake are required.
1. Using CLion, open the arduino folder within the cloned group-04 repository directory.
2. Next, load the CMake project. To do this, open the root CMake file, at the top of the text editor window, there should be a blue bar. Select "Load CMake project". 
3. Navigate to the test/ut directory and open SimpleCarController_test.cpp
4. There will be two structs: RegisterManualControlTest and SimpleCarControllerTest. To the left of those two, there will be a green arrow. Press those to run the tests related to the specific struct. Alternatively to run the tests, in the top bar, there should also be an option to run all tests.

NOTE: The Arduino unit testing was implemented by following and adapting files/code from the following repository: https://github.com/platisd/reusable-testable-arduino-tutorial

## User manual

<img align="right" src="https://github.com/DIT112-V21/group-04/blob/FinalizeReadme/Documentation_GIFS/Android-App-Vertical.gif"  height="800"/>

### Server selection screen

On the "select server" screen the medical worker is able to either choose to drive the car within their own department or drive another department's car. To drive a car within their own department the worker needs to select the "offline mode". If the worker needs to drive a car that is located in another department then they should select the "online mode".<br/><br/>
On the server selection screen the worker could also view a credits pop up where they could read which license the application is using and which library is used.<br/><br/>


### Select a car screen
On the "select a car" screen the medical worker is able to choose one of the four cars in use and connect to their selected car in the maneuvering screen. When the medical worker selects a car the application will notify the user via a pop up which car that have been selected. This allows multiple cars to be driven at the same time by different medical staff workers.<br/><br/>

### Maneuvering screen
On the "maneuvering screen" the medical worker can control the speed and direction of the car with the joystick. Video footage from the front of the car is also present in the screen to better maneuver the car. If an obstacle is detected while maneuvering the car the mobile device will vibrate and a pop up message will be displayed. The screen also displays both the speed (in percentage from 0 to 60 by a speedometer) and the angle of the input given by the joystick. Below the joystick is the autonomous driving button which turns the car’s auto-driving mode on and off. At the bottom left of the screen is the status of the connection to the MQTT broker so that the user can know if it is connected or not.<br/><br/>

## Progress Log

#### Sprint 1 progress
[![Sprint 1 progress video](https://img.youtube.com/vi/gAemX8FFedU/0.jpg)](https://www.youtube.com/watch?v=gAemX8FFedU)

#### Sprint 2 progress
[![Sprint 2 progress video](https://img.youtube.com/vi/H1MaOK4zTDE/0.jpg)](https://www.youtube.com/watch?v=H1MaOK4zTDE)

#### Sprint 3 progress
[![Sprint 3 progress video](https://img.youtube.com/vi/L3fd38SwvSY/0.jpg)](https://www.youtube.com/watch?v=L3fd38SwvSY)

## Team Overview
- [Nils Dunlop](https://github.com/NilsDunlop) - gusdunlni@student.gu.se
- [Markus Emilio Puerto Gutiérrez](https://github.com/puerto-hub) - guspuema@student.gu.se
- [Jihan Phanivong](https://github.com/JihanP) - jihan8497@gmail.com
- [Noor Ul Eeman Asim](https://github.com/gusasino) - gusasino@student.gu.se
- [Kevin Ayad](https://github.com/kevinayad) - Kevinayad@hotmail.com
- [Markus Järveläinen](https://github.com/MarkusJ299) - gusjarmap@student.gu.se
- [Younis Akel](https://github.com/Marble879) - younisakel879@gmail.com
