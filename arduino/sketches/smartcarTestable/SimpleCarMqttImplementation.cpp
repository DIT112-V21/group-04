#include "SimpleCarMqttImplementation.h"

//corresponds to ESP32RestServer.cpp

namespace arduino_car{

    SimpleCarMqttImplementation::SimpleCarMqttImplementation(MQTTClient& mqtt)
        : mMqtt{mqtt}
        {
        }

    void SimpleCarMqttImplementation::begin() {
        mMqtt.begin(net);
    }

    void SimpleCarMqttImplementation::onMessage(std::function<void(String, String)> callBack) {
        mMqtt.onMessage(callBack);
    }

    void SimpleCarMqttImplementation::publish(String topic, String message) {
        mMqtt.publish(topic.c_str(), message.c_str());
    }

    void SimpleCarMqttImplementation::publish(String topic) {
        mMqtt.publish(topic.c_str());
    }

    void SimpleCarMqttImplementation::subscribe(String topic, int qos) {
        mMqtt.subscribe(topic, qos);
    }

    bool SimpleCarMqttImplementation::connect(String hostname, String id, String password) {
        return mMqtt.connect(hostname.c_str(), id.c_str(), password.c_str());
    }

    void SimpleCarMqttImplementation::setHost(String ip_address, int portNumber) {
        mMqtt.setHost(ip_address.c_str(), portNumber);
    }

    bool SimpleCarMqttImplementation::connected() {
        return mMqtt.connected();
    }

    void SimpleCarMqttImplementation::loop() {
        mMqtt.loop();
    }


}
