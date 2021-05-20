#pragma once

#include "Car.h"
#include "MQTTinterface.h"
#include "Serial.h"

//corresponds to MagicCarController.h
namespace arduino_car{

    class SimpleCarController{
    public:
        SimpleCarController(Car& car, MQTTinterface& mqtt, Serial& serial);

        void registerManualControl();
        void registerCameraPublishing();
        void registerAutoDriving();
        bool registerObstacleAvoidance();

    private:
        Car& mCar;
        MQTTinterface& mMQTT;
        Serial& mSerial;
    };

}