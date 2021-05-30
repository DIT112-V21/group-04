#pragma once
#include "Serialinterface.h"

#include <Arduino.h>

namespace arduino_car{

    class SerialImplementation : public Serialinterface {
    public:
        void println(String outputMessage) override {
            Serial.println(outputMessage);
        }

        void println(int outputMessage) override{
            Serial.println(outputMessage);
        }

        void begin(int beginNumber) override {
            Serial.begin(beginNumber);
        }

    };

}
