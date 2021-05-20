#include <Arduino.h>
#include "SimpleCarMqttImplementation.h"

//corresponds to ESP32RestServer.cpp

namespace arduino_car{

    SimpleCarMqttImplementation::SimpleCarMqttImplementation(MQTTClient& mqtt)
        : mMqtt{mqtt}
        {
        }

    void SimpleCarMqttImplementation::begin() {
        mMqtt.begin();
    }

    void SimpleCarMqttImplementation::onMessage(std::function<void(String, String)> callback) {
        mMqtt.on(callback);
    }

    void SimpleCarMqttImplementation::publish(int topic, int message) {
        mMqtt.publish(topic, message);
    }

    void SimpleCarMqttImplementation::subscribe(int topic, int qos) {
        mMqtt.subscribe(topic, qos);
    }

    void SimpleCarMqttImplementation::connect(int hostname, int id, int password) {
        mMqtt.connect(hostname, id, password);
    }


}
