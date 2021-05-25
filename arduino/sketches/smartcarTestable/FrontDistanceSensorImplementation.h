#pragma once

#include <Smartcar.h>
#include "DistanceSensorInterface.h"

namespace arduino_car{

    class FrontDistanceSensorImplementation : public DistanceSensorInterface{

    public:
        FrontDistanceSensorImplementation(SRO4& frontSensorUS)
        : mFrontSensor{frontSensorUS}
        {}

        unsigned int getDistance() override{
            mFrontSensor.getDistance()
        }

    private:
        SR04& mFrontSensor;
    };

}