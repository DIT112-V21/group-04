#pragma once

#include "MQTTinterface.h"
#include "gmock/gmock.h"

namespace arduino_car{

    class MockMQTT : public MQTTinterface {

    public:
        MOCK_METHOD(bool, connect, (std::string hostname, std::string id, std::string password), (override));
        MOCK_METHOD(void, subscribe, (std::string topic, int qos), (override));
        MOCK_METHOD(void, publish,(std::string topic, std::string message), (override));
        MOCK_METHOD(void, onMessage, (std::function<void(std::string, std::string)> callback), (override));
        MOCK_METHOD(void, begin, (), (override));
        MOCK_METHOD(void, setHost, (std::string ip_address, int portNumber), (override));
        MOCK_METHOD(bool, connected, (), (override));
        MOCK_METHOD(void, loop, (), (override));
    };
}
