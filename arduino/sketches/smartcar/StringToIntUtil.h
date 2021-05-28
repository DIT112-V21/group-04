#pragma once
#include "StringUtil.h"

#if defined(ARDUINO) || defined(__SMCE__)
    #include <Arduino.h>
#else
    #include <string>
#endif

namespace StringToIntUtil{

    int stringToInt(String message){

        #if defined(ARDUINO) || defined(__SMCE__)
            return message.toInt();
        #else
            return std::stoi(message);
        #endif

    }
}