// adapted from https://github.com/platisd/reusable-testable-arduino-tutorial
#include "StringUtil.h"
#pragma once

namespace arduino_car{

    struct Car{
        virtual ~Car() = default;

        virtual void setSpeed(float speed) = 0;
        virtual void setAngle(int angle) = 0;
        //virtual void update() = 0;
        //virtual int getHeading() = 0;
    };

}

