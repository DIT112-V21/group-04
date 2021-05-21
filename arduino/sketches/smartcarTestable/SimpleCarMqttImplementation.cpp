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

    void SimpleCarMqttImplementation::onMessage(std::function<void(std::string, std::string)> callback) {
        mMqtt.onMessage(callback);
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
    void SimpleCarMqttImplementation::setHost(std::string ip_address, int portNumber) {
        mMqtt.setHost(ip_address, portNumber);
    }


}
