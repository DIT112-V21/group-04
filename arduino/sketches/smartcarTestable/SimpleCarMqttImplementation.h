#include "MQTTinterface.h"
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
        void publish(String topic) override;
        void publish(String topic, char* data,  std::vector<char>::size_type size, bool boolean, int qos) override;
        void onMessage(std::function<void(String, String)> callback) override;
        void begin() override;
        void setHost(String ip_address, int portNumber) override;
        bool connected() override;
        void loop() override;

    private:
        MQTTClient& mMqtt;

    };

}


