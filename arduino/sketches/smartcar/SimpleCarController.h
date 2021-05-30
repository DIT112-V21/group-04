//code adapted from: https://github.com/platisd/reusable-testable-arduino-tutorial
#pragma once

#include "Car.h"
#include "MQTTinterface.h"
#include "Serialinterface.h"
#include "StringUtil.h"
#include "DistanceSensorInterface.h"
#include "StringToIntUtil.h"

#if defined(ARDUINO) || defined(__SMCE__)
    #include <Arduino.h>
#endif

namespace {
    const auto stoppingSpeed = 0;
    const auto stopDistanceFront = 80;
    const auto stopDistanceBack = 100;
    const int stopAngle = 0;
    const int autoSpeed = 65;
    const int autoAngle = 90;
    const int turnRight = 1;
    const int turnLeft = -1;
    int autoDriving = 0;
    bool isObstacleDetectedPublished = false; //keeps track of when an obstacle has been detected message is published to mqtt
}

namespace arduino_car{

    class SimpleCarController{
    public:
        SimpleCarController(Car& car, MQTTinterface& mqtt, Serialinterface& serial, DistanceSensorInterface& frontUSsensor, DistanceSensorInterface& backIRSensor)
        : mCar{car}
        , mMQTT{mqtt}
        , mSerial{serial}
        , mFrontSensor{frontUSsensor}
        , mBackSensor{backIRSensor}
        {}

        void registerManualControl() {
            if (mMQTT.connect("arduino", "public", "public")) {
                mSerial.println("GENERAL CONNECTION");
                mMQTT.subscribe("/smartcar/switchServer", 0);
                mMQTT.subscribe("/smartcar/control/#", 0);
                mMQTT.onMessage([&](String topic, String message) {
                    mSerial.println("Got initial message");
                    if (topic == "/smartcar/switchServer") {
                        mSerial.println("SWITCHED");
                        mMQTT.setHost("3.138.188.190", 1883);
                        mMQTT.connect("arduino", "public", "public");
                        mMQTT.subscribe("/smartcar/control/#", 0);
                        mMQTT.publish("test", "test");
                    }
                    if (topic == "/smartcar/control/speed" && autoDriving == 0) {
                        auto carSpeed = StringToIntUtil::stringToInt(message);
                        if (!(registerObstacleAvoidance(carSpeed))) {
                            mCar.setSpeed(static_cast<float>(carSpeed));
                        }
                    } else if (topic == "/smartcar/control/turning" && autoDriving == 0) {
                        mCar.setAngle(StringToIntUtil::stringToInt(message));
                    } else if (topic == "/smartcar/control/auto") {
                        autoDriving = StringToIntUtil::stringToInt(message);
                        mSerial.println(autoDriving);
                        if (autoDriving == 0) {
                            mCar.setSpeed(stoppingSpeed);
                            mCar.setAngle(stopAngle);
                        } else {
                            mCar.setSpeed(autoSpeed);
                        }
                    } else {
                        mSerial.println(topic + " " + message);
                    }
                });
            }

        }

        bool registerObstacleAvoidance(){
            bool isObstacleDetected = false;

            auto frontDistanceFromObject = mFrontSensor.getDistance();
            auto backDistanceFromObject = mBackSensor.getDistance();

            bool isFrontDetected = frontDistanceFromObject < stopDistanceFront && frontDistanceFromObject > 1 && (mCar.getSpeed() > 0);
            bool isBackDetected = backDistanceFromObject < stopDistanceBack && backDistanceFromObject > 1 && (mCar.getSpeed() < 0);


            if (isFrontDetected || isBackDetected) {
                registerSendObstacleDetectedNotification(true);
                mCar.setSpeed(stoppingSpeed);
                isObstacleDetected = true;
            } else {
                registerSendObstacleDetectedNotification(false);
            }

            return isObstacleDetected;
        }

        bool registerObstacleAvoidance(int speedFromMQTTMessage){
            bool isObstacleDetected = false;

            auto frontDistanceFromObject = mFrontSensor.getDistance();
            auto backDistanceFromObject = mBackSensor.getDistance();

            bool isFrontDetected = frontDistanceFromObject < stopDistanceFront && frontDistanceFromObject > 1 && ((mCar.getSpeed() > 0) || (speedFromMQTTMessage > 0));
            bool isBackDetected = backDistanceFromObject < stopDistanceBack && backDistanceFromObject > 1 && ((mCar.getSpeed() < 0) || (speedFromMQTTMessage < 0));


            if (isFrontDetected || isBackDetected) {
                registerSendObstacleDetectedNotification(true);
                mCar.setSpeed(stoppingSpeed);
                isObstacleDetected = true;
            } else {
                registerSendObstacleDetectedNotification(false);
            }

            return isObstacleDetected;
        }

        void registerSendObstacleDetectedNotification(bool shouldSend){
            if (shouldSend){
                if (!isObstacleDetectedPublished){
                    mMQTT.publish("/smartcar/obstacle");
                    isObstacleDetectedPublished = true;
                }
            } else {
                isObstacleDetectedPublished = false;
            }
        }

        void registerAutonomousMoving(){
            mCar.setSpeed(stoppingSpeed);
            #if defined(ARDUINO) || defined(__SMCE__)
                delay(500);
            #endif
            mCar.setSpeed(-autoSpeed);
            #if defined(ARDUINO) || defined(__SMCE__)
                delay(500);
            #endif
            mCar.setSpeed(stoppingSpeed);
            #if defined(ARDUINO) || defined(__SMCE__)
                delay(500);
            #endif
            registerTurning(turnLeft);
            if (registerObstacleAvoidance()) {
                mSerial.println("Checking if registered");
                registerTurning(turnRight);
                registerTurning(turnRight);
                if (registerObstacleAvoidance()) {
                    registerTurning(turnRight);
                }
            }
            mCar.setSpeed(autoSpeed);
        }

        void registerTurning(int direction){
            const auto registerTurningStopSpeed = 1; // ensures that obstacles will still be detected.
            mCar.setSpeed(autoSpeed);
            mCar.setAngle(direction*autoAngle);
            #if defined(ARDUINO) || defined(__SMCE__)
                delay(2000);
            #endif
            mCar.setSpeed(registerTurningStopSpeed);
            mCar.setAngle(stopAngle);
            #if defined(ARDUINO) || defined(__SMCE__)
                delay(500);
            #endif
        }

    private:
        Car& mCar;
        MQTTinterface& mMQTT;
        Serialinterface& mSerial;
        DistanceSensorInterface& mFrontSensor;
        DistanceSensorInterface& mBackSensor;
    };

}