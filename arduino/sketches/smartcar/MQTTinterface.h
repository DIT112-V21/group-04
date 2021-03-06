#pragma once

#include <functional>
#include <vector>
#include "StringUtil.h"

namespace arduino_car {

    struct MQTTinterface{
        virtual ~MQTTinterface() = default;

        virtual bool connect(String hostname, String id, String password) = 0;
        virtual void subscribe(String topic, int qos) = 0;
        virtual void publish(String topic, String message) = 0;
        virtual void publish(String topic) = 0;
        virtual void publish (String topic, char* data,  std::vector<char>::size_type size, bool boolean, int qos) = 0;
        virtual void onMessage(std::function<void(String, String)> callback) = 0;
        virtual void begin() = 0;
        virtual void setHost(String ip_address, int portNumber) = 0;
        virtual bool connected() = 0;
        virtual void loop()  = 0;
    };

}
