#pragma once

#include "MQTTinterface.h"
#include "gmock/gmock.h"

namespace arduino_car{

    class MockMQTT : public MQTTinterface {

    public:
        MOCK_METHOD(bool, connect, (String hostname, String id, String password), (override));
        MOCK_METHOD(void, subscribe, (String topic, int qos), (override));
        MOCK_METHOD(void, publish,(String topic, String message), (override));
        MOCK_METHOD(void, publish, (String topic), (override));
        MOCK_METHOD(void, publish, (String topic, char* data,  std::vector<char>::size_type size, bool boolean, int qos), (override));
        MOCK_METHOD(void, onMessage, (std::function<void(String, String)> callback), (override));
        MOCK_METHOD(void, begin, (), (override));
        MOCK_METHOD(void, setHost, (String ip_address, int portNumber), (override));
        MOCK_METHOD(bool, connected, (), (override));
        MOCK_METHOD(void, loop, (), (override));
    };
}
