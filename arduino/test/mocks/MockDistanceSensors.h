#pragma once

#include "DistanceSensorInterface.h"
#include "gmock/gmock.h"


namespace arduino_car{

    class MockDistanceSensors : public DistanceSensorInterface {

    public:
        MOCK_METHOD(unsigned int, getDistance, (), (override));
    };


}