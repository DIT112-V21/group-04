#include "SerialImplementation.h"

namespace arduino_car{

    void SerialImplementation::println(String outputMessage) {
        Serial.println(outputMessage);
    }

    void SerialImplementation::begin(int beginNumber) {
        Serial.begin(beginNumber);
    }
}
