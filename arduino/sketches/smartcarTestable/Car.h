// adapted from https://github.com/platisd/reusable-testable-arduino-tutorial
#pragma once

#if defined(ARDUINO)
#include <Arduino.h>
#else
#include <string>
using String = std::string;
#endif

namespace arduino_car{

    struct Car{
        virtual ~Car() = default;

        virtual void setSpeed(float speed) = 0;
        virtual void setAngle(int angle) = 0;
        //virtual void update() = 0;
        //virtual int getHeading() = 0;
    };

}

