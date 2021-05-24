#pragma once

#include "Serialinterface.h"
#include "gmock/gmock.h"


namespace arduino_car{

    class MockSerial : public Serialinterface {

    public:
        MOCK_METHOD(void, println, (String outputMessage), (override));
        MOCK_METHOD(void, begin, (int beginNumber), (override));
    };


}