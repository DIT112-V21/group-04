#include <vector>

#include <MQTT.h>
#include <WiFi.h>
#ifdef __SMCE__
#include <OV767X.h>
#endif

#include <Smartcar.h>

#include "SimpleCarMqttImplementation.h"
#include "SimpleCarWrapper.h"
#include "SimpleCarController.h"
#include "SerialImplementation.h"


MQTTClient mqtt;

ArduinoRuntime arduinoRuntime;
BrushedMotor leftMotor(arduinoRuntime, smartcarlib::pins::v2::leftMotorPins);
BrushedMotor rightMotor(arduinoRuntime, smartcarlib::pins::v2::rightMotorPins);
DifferentialControl control(leftMotor, rightMotor);

SimpleCar car(control);

arduino_car::SimpleCarWrapper simpleCarWrapper(car);
arduino_car::SimpleCarMqttImplementation mqttWrapper(mqtt);
arduino_car::SerialImplementation serialWrapper;

arduino_car::SimpleCarController simpleCarController(simpleCarWrapper, mqttWrapper, serialWrapper);

/*const auto oneSecond = 1UL;
const auto triggerPin = 6;
const auto echoPin = 7;
const auto maxDistance = 400;
const int TRIGGER_PIN           = 6; // D6
const int ECHO_PIN              = 7; // D7
const unsigned int MAX_DISTANCE = 100;
const auto BACK_PIN = 3;
const auto stoppingSpeed = 0;
const auto stopDistanceFront = 80;
const auto stopDistanceBack = 100;
const int stopAngle = 0;
const int autoSpeed = 60;
const int autoAngle = 90;
int autoDriving = 0;
int carSpeed = 0;
boolean isObstacleDetectedPublished = false; *///keeps track of when an obstacle has been detected message is published to mqtt


SR04 frontSensorUS(arduinoRuntime, triggerPin, echoPin, maxDistance);
GP2D120 backSensorIR(arduinoRuntime, BACK_PIN);

std::vector<char> frameBuffer;

void setup() {
    serialWrapper.begin(9600);
#ifdef __SMCE__
    //mqtt.begin("3.138.188.190", 1883, WiFi);
  //mqtt.begin("aerostun.dev", 1883, WiFi);
  mqttWrapper.begin(); // Will connect to localhost
  Camera.begin(QQVGA, RGB888, 15);
  frameBuffer.resize(Camera.width() * Camera.height() * Camera.bytesPerPixel());
#else
    mqttWrapper.begin();
#endif
    simpleCarController.registerManualControl();
}

void loop() {
    if (mqttWrapper.connected()) {
        mqttWrapper.loop();
        const auto currentTime = millis();
#ifdef __SMCE__
        static auto previousTransmission = 0UL;
    if (currentTime - previousTransmission >= oneSecond) {
      previousTransmission = currentTime;
      Camera.readFrame(frameBuffer.data());
      mqttWrapper.publish("Camera_Stream", frameBuffer.data(), frameBuffer.size(), false, 0);
    }
#endif
    }
#ifdef __SMCE__
    // Avoid over-using the CPU if we are running in the emulator
  if (simpleCarController.registerObstacleAvoidance()) {
         if (autoDriving == 1){
              simpleCarController.registerAutonomousMoving();
          }
      }
  delay(35);
#endif
}




