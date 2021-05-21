// adapted from https://github.com/platisd/reusable-testable-arduino-tutorial
#pragma once
#include <string>
#include <MockMQTT.h>
//corresponds to RestServer.h
namespace arduino_car {

    struct MQTTinterface{
        virtual ~MQTTinterface() = default;


        virtual bool connect(std::string hostname, std::string id, std::string password) = 0;
        virtual void subscribe(std::string topic, int qos) = 0;
        virtual void publish(std::string topic, std::string message) = 0;
        virtual void onMessage(std::function<void(std::string, std::string)> callback) = 0;
        virtual void begin() = 0;
        virtual void setHost() = 0;
    };

}
