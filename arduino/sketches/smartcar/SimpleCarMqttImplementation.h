#pragma once
#include "MQTTinterface.h"
#include <MQTT.h>
#include <WiFi.h>

#ifndef __SMCE__
WiFiClient net;
#endif
//corresponds to ESP32RestServer.h



namespace arduino_car{

    class SimpleCarMqttImplementation : public MQTTinterface{

    public:
        SimpleCarMqttImplementation(MQTTClient& mqtt)
        : mMqtt{mqtt}
        {
        }

        bool connect(String hostname, String id, String password) override {
            return mMqtt.connect(hostname.c_str(), id.c_str(), password.c_str());
        }

        void subscribe(String topic, int qos) override {
            mMqtt.subscribe(topic, qos);
        }

        void publish(String topic, String message) override {
            mMqtt.publish(topic.c_str(), message.c_str());
        }

        void publish(String topic) override {
            mMqtt.publish(topic.c_str());
        }

        void publish(String topic, char* data,  std::vector<char>::size_type size, bool boolean, int qos) override {
            mMqtt.publish(topic.c_str(), data, size, boolean, qos);
        }

        void onMessage(std::function<void(String, String)> callback) override {
            mMqtt.onMessage(callback);
        }

        void begin() override {
            #ifdef __SMCE__
                mMqtt.begin(WiFi); // Will connect to localhost
            #else
                mMqtt.begin(net);
            #endif
        }

        void setHost(String ip_address, int portNumber) override {
            mMqtt.setHost(ip_address.c_str(), portNumber);
        }

        bool connected() override {
            return mMqtt.connected();
        }

        void loop() override {
            mMqtt.loop();
        }

    private:
        MQTTClient& mMqtt;

    };

}


