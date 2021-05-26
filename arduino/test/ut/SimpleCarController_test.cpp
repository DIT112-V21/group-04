#include "MockCar.h"
#include "MockMQTT.h"
#include "MockSerial.h"
#include "MockDistanceSensors.h"
#include "SimpleCarController.h"
#include "StringUtil.h"
#include "gtest/gtest.h"

using namespace testing;

namespace arduino_car{

    struct SimpleCarControllerTest : public Test {
        MockCar mCar;
        MockMQTT mMQTT;
        MockSerial mSerial;
        MockDistanceSensors mFrontSensor;
        MockDistanceSensors mBackSensor;
        SimpleCarController mSimpleCarController{mCar, mMQTT, mSerial, mFrontSensor, mBackSensor};
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
        MockDistanceSensors mFrontSensor;
        MockDistanceSensors mBackSensor;
        SimpleCarController mSimpleCarController{mCar, mMQTT, mSerial, mFrontSensor, mBackSensor};
        std::function<void(String, String)> mCallBack;
    };

    TEST_F(SimpleCarControllerTest, registerManualControl_WhenCalled_WillSubscribeToCorrectChannels){ // _F means we dont need to repeat instatiation of all the mocks.
        EXPECT_CALL(mMQTT, connect(_, _, _)).WillOnce(Return(true));
        EXPECT_CALL(mMQTT, subscribe("/smartcar/switchServer", _)); //Will make sure that the .subscribe method from the mMQTT object is called.
        EXPECT_CALL(mMQTT, subscribe("/smartcar/control/#", _)); // Underscore is wildcar - wont match particular argument. Test case just checks if endpoint is registered.

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

    TEST_F(RegisterManualControlTest, registerManualControl_WhenCalledAndReceivesSpeedTopic_WillAdjustTheCarSpeed){
        carSpeed = 40;
        autoDriving = 0;
        EXPECT_CALL(mFrontSensor, getDistance()).WillOnce(Return(110)); //This and the line below ensures that obstacleAvoidance returns false!
        EXPECT_CALL(mBackSensor, getDistance()).WillOnce(Return(10)); // Test still passes when this value is set to 10. This is because the speed is set to a positive value, hence an obstacle avoidance for the back would not trigger.
        EXPECT_CALL(mCar, setSpeed(static_cast<float>(carSpeed)));


        mCallBack("/smartcar/control/speed", "40");
    }



    //TODO: Add test to check that the car's angle can be adjusted

    //TODO: Add test to check that car stops if obstacle detected in front


    TEST_F(SimpleCarControllerTest, registerObstacleAvoidance_WhenDrivingForwardAndDetectingObstacle_WillReturnTrue){
        carSpeed = 40; // Car will move forward. If speed was not set, obstacle avoidance would not be registered
        EXPECT_CALL(mFrontSensor, getDistance()).WillOnce(Return(30));
        EXPECT_CALL(mBackSensor, getDistance()).WillOnce(Return(120));
        EXPECT_TRUE(mSimpleCarController.registerObstacleAvoidance());
    }

    TEST_F(SimpleCarControllerTest, registerObstacleAvoidance_WhenDrivingBackwardsAndDetectingObstacle_WillReturnTrue){
        carSpeed = -40; // Car will move backwards. If speed was not set, obstacle avoidance would not be registered
        EXPECT_CALL(mFrontSensor, getDistance()).WillOnce(Return(120));
        EXPECT_CALL(mBackSensor, getDistance()).WillOnce(Return(10));
        EXPECT_TRUE(mSimpleCarController.registerObstacleAvoidance());
    }

    TEST_F(SimpleCarControllerTest, registerObstacleAvoidance_WhenStationaryAndDetectObstacleBackAndFront_WillReturnFalse){
        carSpeed = 0;
        EXPECT_CALL(mFrontSensor, getDistance()).WillOnce(Return(10));
        EXPECT_CALL(mBackSensor, getDistance()).WillOnce(Return(10));
        EXPECT_FALSE(mSimpleCarController.registerObstacleAvoidance());
    }




    //TODO: Add test to check that car stops if obstacle detected in back
    //TODO: Add test to check that car can move back if obstacle detected in front
    //TODO: Add test to check that car can move forward if obstacle detected in back




}