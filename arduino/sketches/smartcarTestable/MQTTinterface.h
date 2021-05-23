// adapted from https://github.com/platisd/reusable-testable-arduino-tutorial
#pragma once
#include <functional>
#if defined(ARDUINO)
#include <Arduino.h>
#else
#include <string>
using String = std::string;
#endif

//corresponds to RestServer.h
namespace arduino_car {

    struct MQTTinterface{
        virtual ~MQTTinterface() = default;


        virtual bool connect(String hostname, String id, String password) = 0;
        virtual void subscribe(String topic, int qos) = 0;
        virtual void publish(String topic, String message) = 0;
        virtual void publish(String topic) = 0;
        virtual void onMessage(std::function<void(String, String)> callback) = 0;
        virtual void begin() = 0;
        virtual void setHost(String ip_address, int portNumber) = 0;
        virtual bool connected() = 0;
        virtual void loop()  = 0;
    };

}
