#include "SerialImplementation.h"

namespace arduino_car{

    SerialImplementation::SerialImplementation(Serial& serial)
    : serial{serial}
    {}

    void SerialImplementation::println(std::string output) {
        serial.println(output);
    }

    void SerialImplementation::println(int output) {
        serial.println(output);
    }
}
