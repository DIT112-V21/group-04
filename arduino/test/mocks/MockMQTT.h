#pragma once

#include "MQTTwrapper.h"
#include "gmock/gmock.h"
namespace arduino_car{

    Class MockMQTT : public MQTTwrapper {

    public:
        MOCK_METHOD(bool, connect, (String hostname, String id, String password), (override));
        MOCK_METHOD(void, subscribe, (String topic, int qos), (override));
        MOCK_METHOD(void, publish,(String topic, String message), (override));
        MOCK_METHOD(void, onMessage, (std::function<void(String, String)> callback), (override));
        MOCK_METHOD(void, begin, (), (override));
    };
}