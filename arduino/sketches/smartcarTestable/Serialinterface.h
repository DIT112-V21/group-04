#pragma once

#include <functional>
#if defined(ARDUINO)
#include <Arduino.h>
#else
#include <string>
using String = std::string;
#endif

namespace arduino_car {

    struct Serialinterface{

        virtual ~Serialinterface() = default;

        virtual void println(String outputMessage) = 0;
        virtual void begin(int beginNumber) = 0;

    };

}

