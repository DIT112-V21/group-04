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
