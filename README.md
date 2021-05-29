# Group-04 - Medcar

![logo7](https://user-images.githubusercontent.com/68125677/116778243-6825bb00-aa79-11eb-83d6-5405cf059f2a.png)

## What are you going to make? 

For this project, we plan to create an Android app which will control Arduino cars. The cars will assist medical staff by performing certain tasks within a hospital, such as transporting food and medicine to patients/other medical staff.

The car will be traveling through corridors within a hospital, which we plan to take into consideration by providing features which would prevent damage to the car. These features would include but are not limited to: obstacle avoidance, notifications on certain situations that the car encounters, and a camera view.

Along with manually maneuvering the car, the car would also be able to drive autonomously, not requiring any input from the user unless necessary. This would make manuvering the car more effortless as medical staff won't have to constantly provide input for the car to move. 


## Why will you make it?

Due to the current pandemic, the medical system is overwhelmed with the amount of cases. Contact between medical staff and patients put medical staff at higher risk of contracting the virus. In addition, medical care becomes difficult to deliver to immune deficient patients due to them being at high risk of contracting the virus. This is where our project comes in. By using Arduino cars, the contact between medical staff and patients can be limited to only when necessary. The cars will be able to carry out certain tasks, such as delivering medicine/doctor messages to patients.

Although, the question arises, will these cars still be useful after the pandemic? Yes! Due to these cars carrying out certain tasks for medical staff as stated previously, they may reduce the medical staff's work load, and allow them to focus on more advanced tasks where human intercation is necessary. Furthermore, features such as autonomous driving and live camera streaming enable the car to be adapted/reused to help other scenarios outside of the medical field, such as security montioring in buildings and delivering food.

## How are you going to make it?
To allow medical staff to control the car, an Android app will be developed by using java for the back-end, and XML for the front-end. The app will allow medical staff to choose which car they connect to. Within the app, staff members will then be able to switch between manually maneuvering the car with a joystick, and letting the car drive autonomously.

The connection between the Android app and Arduino cars will be set up by using an MQTT broker and an AWS server. The Android app and Arduino car will both be able to publish instructions to the broker and receive content from subscribed channels. We plan to keep this connection secure by requiring a login from the user when starting the app.

We plan to add obstacle detection, which will stop the car from accelerating in the direction that the obstacle has been detected. This will be done by using ultrasonic sensors and infrared sensors detecting when obstacles are about 30cm away from the car. This will be implemented using C++. The car will also provide feedback and visualization to the app on its status. Feedback and visualization would include connection status notifications, obstacle detected notifications, the speed and angle the car is traveling at, and a live camera stream.

### Technologies that we will use:

* <a href="https://github.com/ItJustWorksTM/smce-gd">SMCE</a>
* <a href="https://github.com/platisd/smartcar_shield">Smartcar shield</a>
* <a href="https://github.com/controlwear/virtual-joystick-android">Joystick</a>
* <a href="https://github.com/eclipse/paho.mqtt.android/blob/master/README.md">Paho MQTT</a>
* <a href="https://github.com/DIT112-V21/smartcar-mqtt-controller">Smartcar code examples</a>
* C++
* Java
* Github
* Github actions
* Mosquitto
* Android studio
* Arduino
* Amazon AWS
* Gradle

## Feature summary


#### <a href="https://github.com/DIT112-V21/group-04/wiki/Obstacle-avoidance">Obstacle avoidance</a>

Using an ultrasonic sensor, the vehicle should avoid obstacles that are 30cm away from it. The car should be able to move in the opposite direction from the object. This would prevent the car from colliding and potentially being damaged.

#### <a href="https://github.com/DIT112-V21/group-04/wiki/Build-Android-app-for-control-and-user-interface">Build Android app for control and user interface</a>

Android app with user interface to control car emulator, Interact with other features while also providing a medium for connectivity.

#### <a href="https://github.com/DIT112-V21/group-04/wiki/Connectivity-between-car-emulator-and-Android-app"> Connectivity between car emulator and Android app</a>

Communication between the car and Android app is handled using the MQTT protocol and an AWS server.

#### <a href="https://github.com/DIT112-V21/group-04/wiki/Manual-maneuvering-via-Android-app">Manual maneuvering via Android app</a>

The car should be maneuvered manually by a user via a joystick in the app. The joystick allows the user to control the speed depending on how far out it is moved, which makes adjusting the speed simple and also would assist in situations where precise movements are necessary. Along with speed controls, the user is also able to change the direction of the car by using the joystick.

#### <a href="https://github.com/DIT112-V21/group-04/wiki/Continuous-integration">Continuous Integration</a>

The project implements continuous integration by utilizing Github actions, ensuring that our product works on all various types of systems. Github actions would also be used to run automated tests. Using this principle, the code and quality of the product would be kept at the highest standards.

#### <a href="https://github.com/DIT112-V21/group-04/wiki/Feedback-to-user-and-analysis-of-data">Feedback to user and analysis of data</a>

The feedback feature gives the user visual information about the status of both the car and the app. It is responsible for information messages, error messages and to monitor and analyze the data that could produce such message.

#### <a href="https://github.com/DIT112-V21/group-04/wiki/Autonomous-driving">Autonomous driving</a>

The car should be able to enter a auto driving mode where it will move forward while considering any obstacles.

#### <a href="https://github.com/DIT112-V21/group-04/wiki/Security">Security</a>
The app will use an AWS external server so that the connection to an external server cannot be accessed by unauthorised users.

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

### Setup Arduino IDE
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

### Set up Android app

1. In order to use the Android application, use Android studio to open the android directory located in the cloned group-04 reposoitroy folder. 
2. After opening the project, select File -> "Sync Project with Gradle Files".
3. Then build the apk via: Build -> Build Bundle(s)/APK(s) -> Build APK(s) and install it on an Android device. To use the AWS server, select online mode when launching the app. If it is preferred to use the local host, select offline mode.


### Running the sketch

1. In order to run the sketch, open SMCE-GD and press fresh.
2. Select the + option in the left bar, and then choose to Add new.
3. Locate the group-04 cloned repository folder, and then navigate to the sketch which should be located in: group-04/arduino/sketches/smartcar/
4. Afterwards, select to compile and start. You can then select which server option (offline/online) in the Android app.

## User manual

<img align="right" src="https://github.com/DIT112-V21/group-04/blob/FinalizeReadme/Documentation_GIFS/Android-App-Vertical.gif"  height="800"/>

### Server selection screen

On the "select server" screen the medical worker is able to either choose to drive the car within their own department or drive another department's car. To drive a car within their own department the worker needs to select the "offline mode". If the worker needs to drive a car that is located in another department then they should select the "online mode".<br/><br/>
On the server selection screen the worker could also view a credits pop up where they could read which license the application is using and which library is used.<br/><br/>


### Select a car screen
On the "select a car" screen the medical worker is able to choose one of the four cars in use and connect to their selected car in the maneuvering screen. When the medical worker selects a car the application will notify the user via a pop up which car that have been selected. This allows multiple cars to be driven at the same time by different medical staff workers.<br/><br/>

### Maneuvering screen
On the "maneuvering screen" the medical worker can control the speed and direction of the car with the joystick. Video footage from the front of the car is also present in the screen to better maneuver the car. If an obstacle is detected while maneuvering the car the mobile device will vibrate and a pop up message will be displayed. The screen also displays both the speed (in percentage from 0 to 60 by a speedometer) and the angle of the input given by the joystick. Below the joystick is the autonomous driving button which turns the car’s auto-driving mode on and off. At the bottom left of the screen is the status of the connection to the MQTT broker so that the user can know if it is connected or not.<br/><br/>

## Progress log

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
