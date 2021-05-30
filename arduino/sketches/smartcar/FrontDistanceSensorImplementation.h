#pragma once

#include <Smartcar.h>
#include "DistanceSensorInterface.h"

namespace arduino_car{

    class FrontDistanceSensorImplementation : public DistanceSensorInterface{

    public:
        FrontDistanceSensorImplementation(SR04& frontSensorUS)
        : mFrontSensor{frontSensorUS}
        {}

        unsigned int getDistance() override{
            return mFrontSensor.getDistance();
        }

    private:
        SR04& mFrontSensor;
    };

}