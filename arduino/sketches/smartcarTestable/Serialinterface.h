#pragma once

#include <functional>
#include "StringUtil.h"

namespace arduino_car {

    struct Serialinterface{

        virtual ~Serialinterface() = default;

        virtual void println(String outputMessage) = 0;
        virtual void begin(int beginNumber) = 0;

    };

}

