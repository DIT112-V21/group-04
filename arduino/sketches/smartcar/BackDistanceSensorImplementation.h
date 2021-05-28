#pragma once

#include <Smartcar.h>
#include "DistanceSensorInterface.h"

namespace arduino_car{

    class BackDistanceSensorImplementation : public DistanceSensorInterface{

    public:
        BackDistanceSensorImplementation(GP2D120& backSensorIR)
        : mBackSensor{backSensorIR}
        {}

        unsigned int getDistance() override{
            mBackSensor.getDistance();
        }

    private:
        GP2D120& mBackSensor;
    };

}