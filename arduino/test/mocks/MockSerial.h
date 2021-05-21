#pragma once

#include "Serial.h"
#include "gmock/gmock.h"

namespace arduino_car{

    class MockSerial : public Serial{

    public:
        MOCK_METHOD(void, println, (std::string output), (override));
        MOCK_METHOD(void, println, (int output), (override));
    };
}
