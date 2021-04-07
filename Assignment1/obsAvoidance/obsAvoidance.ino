#include <Smartcar.h>

const int forwardSpeed   = 70; 
const int stoppingSpeed = 0; 
const int stopDistance = 60;

ArduinoRuntime arduinoRuntime;

BrushedMotor leftMotor(arduinoRuntime, smartcarlib::pins::v2::leftMotorPins);
BrushedMotor rightMotor(arduinoRuntime, smartcarlib::pins::v2::rightMotorPins);
DifferentialControl control(leftMotor, rightMotor);

SimpleCar car(control);

const int TRIGGER_PIN           = 6; // D6
const int ECHO_PIN              = 7; // D7
const unsigned int MAX_DISTANCE = 100;

SR04 frontSensor(arduinoRuntime, TRIGGER_PIN, ECHO_PIN, MAX_DISTANCE);

void setup()
{
    Serial.begin(9600);
    car.setSpeed(forwardSpeed);
}

void loop()
{ 
     int distanceFromObject = frontSensor.getDistance();
     if (distanceFromObject < stopDistance && distanceFromObject > 0){
      car.setSpeed(stoppingSpeed);
     }
    Serial.println(distanceFromObject);
    delay(100);
}
