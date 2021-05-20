#include "SimpleCarController.h"
//corresponds to MagicCarController.cpp
namespace arduino_car{

    SimpleCarController::SimpleCarController(Car& car, MQTTinterface& mqtt, Serial& serial)
    : mCar{car}
    , mMQTT{mqtt}
    , mSerial{serial}
    {}

    void SimpleCarController::registerManualControl() {
        if (mMQTT.connect("arduino", "public", "public")) {
            mSerial.println("GENERAL CONNECTION");
            mMQTT.subscribe("/smartcar/switchServer", 0);
            mMQTT.subscribe("/smartcar/control/#", 0);
            mMQTT.onMessage(+[](std::string& topic, std::string& message) {mSerial.println("Got initial message");
                if (topic == "/smartcar/switchServer"){
                    mSerial.println("SWITCHED");
                    mMQTT.setHost("3.138.188.190", 1883);
                    mMQTT.connect("arduino", "public", "public");
                    mMQTT.subscribe("/smartcar/control/#", 0);
                    mMQTT.publish("test", "test");
                }
                if (topic == "/smartcar/control/speed" && autoDriving == 0) {
                    carSpeed = message.toInt();
                    if (!(obstacleAvoidance())){
                        mCar.setSpeed(carSpeed);
                    }
                } else if (topic == "/smartcar/control/turning" && autoDriving == 0) {
                    mCar.setAngle(message.toInt());
                }
            }
        }
    }

}



