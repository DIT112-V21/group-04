#include "SimpleCarController.h"

//corresponds to MagicCarController.cpp

namespace {
    const auto oneSecond = 1UL;
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
    bool isObstacleDetectedPublished = false; //keeps track of when an obstacle has been detected message is published to mqtt
}

namespace arduino_car{

    SimpleCarController::SimpleCarController(Car& car, MQTTinterface& mqtt /*Serial& serial*/)
    : mCar{car}
    , mMQTT{mqtt}
   // , mSerial{serial}
    {}

    void SimpleCarController::registerManualControl() {
        if (mMQTT.connect("arduino", "public", "public")) {
            //SimpleCarController::mSerial.println("GENERAL CONNECTION");
            mMQTT.subscribe("/smartcar/switchServer", 0);
            mMQTT.subscribe("/smartcar/control/#", 0);
            mMQTT.onMessage([&](String topic, String message) {
                //mSerial.println("Got initial message");
                if (topic == "/smartcar/switchServer") {
                    //mSerial.println("SWITCHED");
                    mMQTT.setHost("3.138.188.190", 1883);
                    mMQTT.connect("arduino", "public", "public");
                    mMQTT.subscribe("/smartcar/control/#", 0);
                    mMQTT.publish("test", "test");
                }
                if (topic == "/smartcar/control/speed" && autoDriving == 0) {
                    carSpeed = message.toInt().
                    mCar.setSpeed(static_cast<float>(carSpeed));
                   /* if (!(obstacleAvoidance())) {
                        mCar.setSpeed(static_cast<float>(carSpeed));
                    }*/
                } else if (topic == "/smartcar/control/turning" && autoDriving == 0) {
                    mCar.setAngle(message.toInt());
                } else if (topic == "/smartcar/control/auto") {
                    autoDriving = message.toInt();
                    //mSerial.println(autoDriving);
                    if (autoDriving == 0) {
                        mCar.setSpeed(stoppingSpeed);
                        mCar.setAngle(stopAngle);
                        carSpeed = stoppingSpeed;
                    } else {
                        mCar.setSpeed(autoSpeed);
                        carSpeed = autoSpeed;
                    }
                } else {
                    //mSerial.println(topic + " " + message);
                }
            });
        }

    }


}




