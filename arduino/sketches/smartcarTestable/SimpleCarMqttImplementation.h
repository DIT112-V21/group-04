#include <MQTTinterface.h>
#include <Arduino.h>
#include <MQTT.h>
#include <WiFi.h>
//corresponds to ESP32RestServer.h
namespace arduino_car{

    class SimpleCarMqttImplementation : public MQTTinterface{
    public:
        SimpleCarMqttImplementation(MQTTClient& mqtt);

        bool connect(String hostname, String id, String password) override;
        void subscribe(String topic, int qos) override;
        void publish(String topic, String message) override;
        void onMessage(std::function<void(std::string, std::string)> callback) override;
        void begin(WiFiClient client) override;
        void setHost(std::string ip_address, int portNumber) override;
        bool connected() override;
        void loop() override;

    private:
        MQTTClient& mMqtt;

    };

}


