#pragma once

namespace arduino_car {

    struct DistanceSensorInterface{
        virtual ~DistanceSensorInterface() = default;

        virtual unsigned int getDistance() = 0;
    };
}