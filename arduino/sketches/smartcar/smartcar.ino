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
#include "FrontDistanceSensorImplementation.h"
#include "BackDistanceSensorImplementation.h"

const auto oneSecond = 1UL;
const auto triggerPin = 6;
const auto echoPin = 7;
const auto maxDistance = 400;
const int TRIGGER_PIN           = 6; // D6
const int ECHO_PIN              = 7; // D7
const unsigned int MAX_DISTANCE = 100;
const auto BACK_PIN = 3;

MQTTClient mqtt;

ArduinoRuntime arduinoRuntime;
BrushedMotor leftMotor(arduinoRuntime, smartcarlib::pins::v2::leftMotorPins);
BrushedMotor rightMotor(arduinoRuntime, smartcarlib::pins::v2::rightMotorPins);
DifferentialControl control(leftMotor, rightMotor);

SimpleCar car(control);

SR04 frontSensorUS(arduinoRuntime, triggerPin, echoPin, maxDistance);
GP2D120 backSensorIR(arduinoRuntime, BACK_PIN);

arduino_car::SimpleCarWrapper simpleCarWrapper(car);
arduino_car::SimpleCarMqttImplementation mqttWrapper(mqtt);
arduino_car::SerialImplementation serialWrapper;
arduino_car::FrontDistanceSensorImplementation frontUsSensorWrapper(frontSensorUS);
arduino_car::BackDistanceSensorImplementation backIrSensorWrapper(backSensorIR);

arduino_car::SimpleCarController simpleCarController(simpleCarWrapper, mqttWrapper, serialWrapper, frontUsSensorWrapper, backIrSensorWrapper);


std::vector<char> frameBuffer;

void setup() {
    serialWrapper.begin(9600);
#ifdef __SMCE__
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




