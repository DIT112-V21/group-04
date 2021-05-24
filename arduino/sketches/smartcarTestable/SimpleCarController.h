#pragma once

#include "Car.h"
#include "MQTTinterface.h"
#include "Serialinterface.h"
#include "StringUtil.h"

//corresponds to MagicCarController.h
namespace arduino_car{

    class SimpleCarController{
    public:
        SimpleCarController(Car& car, MQTTinterface& mqtt, Serialinterface& serial);

        void registerManualControl();
        //void registerCameraPublishing();
        //void registerAutoDriving();
       // bool registerObstacleAvoidance();

    private:
        Car& mCar;
        MQTTinterface& mMQTT;
        Serialinterface& mSerial;
    };

}