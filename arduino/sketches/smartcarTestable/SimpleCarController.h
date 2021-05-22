#pragma once

#include "Car.h"
#include "MQTTinterface.h"
#include "SerialImplementation.h"

//corresponds to MagicCarController.h
namespace arduino_car{

    class SimpleCarController{
    public:
        SimpleCarController(Car& car, MQTTinterface& mqtt /*Serial& mSerial*/);

        void registerManualControl();
        //void registerCameraPublishing();
        //void registerAutoDriving();
       // bool registerObstacleAvoidance();

    private:
        Car& mCar;
        MQTTinterface& mMQTT;
        /*Serial& mSerial;*/
    };

}