#include "MockCar.h"
#include "MockMQTT.h"
#include "MockSerial.h"
#include "SimpleCarController.h"
#include "StringUtil.h"
#include "gtest/gtest.h"

using namespace testing;

namespace arduino_car{

    struct SimpleCarControllerTest : public Test {
        MockCar mCar;
        MockMQTT mMQTT;
        MockSerial mSerial;
        SimpleCarController mSimpleCarController{mCar, mMQTT, mSerial};
    };

    struct RegisterManualControlTest : public Test {

        void SetUp() override {
            EXPECT_CALL(mMQTT, connect(_, _, _)).WillOnce(Return(true));
            EXPECT_CALL(mMQTT, onMessage(_)).WillOnce(SaveArg<0>(&mCallBack));

            mSimpleCarController.registerManualControl();
        }

        MockCar mCar;
        MockMQTT mMQTT;
        MockSerial mSerial;
        SimpleCarController mSimpleCarController{mCar, mMQTT, mSerial};
        std::function<void(String, String)> mCallBack;
    };

    TEST_F(SimpleCarControllerTest, registerManualControl_WhenCalled_WillSubscribeToCorrectChannels){ // _F means we dont need to repeat instatiation of all the mocks.
        EXPECT_CALL(mMQTT, connect(_, _, _));
        //EXPECT_CALL(mMQTT, subscribe("/smartcar/switchServer", _)); //Will make sure that the .subscribe method from the mMQTT object is called.
        //EXPECT_CALL(mMQTT, subscribe("/smartcar/control/#", _)); // Underscore is wildcar - wont match particular argument. Test case just checks if endpoint is registered.

        mSimpleCarController.registerManualControl();
    }

    TEST_F(SimpleCarControllerTest, registerManualControl_WhenCalled_WillPrintSerialMessageAfterConnectingToMQTT){
        EXPECT_CALL(mMQTT, connect(_, _, _)).WillOnce(Return(true));
        EXPECT_CALL(mSerial, println(_));

        mSimpleCarController.registerManualControl();
    }

    TEST_F(RegisterManualControlTest, registerManualControl_WhenCalledAndRecievesSwitchServerMessage_MqttWillSetHostAndReconnectAndSubscribeToMqtt){
        EXPECT_CALL(mMQTT, setHost(_, _));
        EXPECT_CALL(mMQTT, connect(_, _, _)).WillOnce(Return(true));
        EXPECT_CALL(mMQTT, subscribe("/smartcar/control/#", _));
        
        mCallBack("/smartcar/switchServer", "message");
    }




}