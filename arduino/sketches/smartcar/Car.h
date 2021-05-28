// adapted from https://github.com/platisd/reusable-testable-arduino-tutorial
#pragma once

#include "StringUtil.h"

namespace arduino_car{

    struct Car{
        virtual ~Car() = default;

        virtual void setSpeed(float speed) = 0;
        virtual void setAngle(int angle) = 0;
        virtual float getSpeed() = 0;
    };

}

