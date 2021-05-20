#include <MQTTinterface.h>
#include <Arduino.h>
#include <MQTT.h>
//corresponds to ESP32RestServer.h
namespace arduino_car{

    class SimpleCarMqttImplementation : public MQTTinterface{
    public:
        SimpleCarMqttImplementation(MQTTClient& mqtt);

        bool connect(String hostname, String id, String password) override;
        void subscribe(String topic, int qos) override;
        void publish(String topic, String message) override;
        void onMessage(std::function<void(String, String)> callback) override;
        void begin() override;

    private:
        MQTTClient& mqtt

    };

}


