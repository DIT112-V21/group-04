#include <Arduino.h>
#include <Serial.h>

#pragma once

namespace arduino_car{

    class SerialImplementation : public Serial{
    public:
        SerialImplementation(Serial& serial);

        void println(std::string output) override;

    private:
        Serial& serial;
    };

}


