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

        void TearDown() override {
            carSpeed = 0;
            isObstacleDetectedPublished = false;
            autoDriving = 0;
        }

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

        void TearDown() override {
            carSpeed = 0;
            isObstacleDetectedPublished = false;
            autoDriving = 0;
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
        EXPECT_CALL(mSerial, println(Matcher<String>(_)));

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
        auto sensorReadingFront = 110;
        auto sensorReadingBack = 200;
        EXPECT_CALL(mFrontSensor, getDistance()).WillOnce(Return(sensorReadingFront)); //This and the line below ensures that obstacleAvoidance returns false!
        EXPECT_CALL(mBackSensor, getDistance()).WillOnce(Return(sensorReadingBack)); // Test still passes when this value is set to 10. This is because the speed is set to a positive value, hence an obstacle avoidance for the back would not trigger.
        EXPECT_CALL(mCar, setSpeed(static_cast<float>(carSpeed)));


        mCallBack("/smartcar/control/speed", "40");
    }


    TEST_F(RegisterManualControlTest, registerManualControl_WhenCalledAndReceivesTurningTopic_WillAdjustTheCarTurningAngle){
        const auto turningAngle = 80;
        EXPECT_CALL(mCar, setAngle(turningAngle));

        mCallBack("/smartcar/control/turning", "80");
    }

    TEST_F(RegisterManualControlTest, registerManualControl_WhenCalledAndReceivesAutoTopicAndMessageIs1_WillSwitchToAutonomousDriving){
        const auto speed = 65;
        EXPECT_CALL(mCar, setSpeed(speed));

        mCallBack("/smartcar/control/auto", "1");
    }

    TEST_F(RegisterManualControlTest, registerManualControl_WhenCalledAndReceivesAutoTopicAndMessageIs0_WillStopAndSwitchToManualDriving){

        const auto speed = 0;
        const auto angle = 0;
        EXPECT_CALL(mCar, setSpeed(speed));
        EXPECT_CALL(mCar, setAngle(angle));

        mCallBack("/smartcar/control/auto", "0");
    }


    TEST_F(SimpleCarControllerTest, registerObstacleAvoidance_WhenCalledAndCarDrivingForwardAndObstacleDetectedWithinStopDistance_WillReturnTrue){
        carSpeed = 40; // Car will move forward. If speed was not set, obstacle avoidance would not be registered
        auto sensorReadingFront = 60;
        auto sensorReadingBack = 120;
        EXPECT_CALL(mFrontSensor, getDistance()).WillOnce(Return(sensorReadingFront));
        EXPECT_CALL(mBackSensor, getDistance()).WillOnce(Return(sensorReadingBack));
        EXPECT_TRUE(mSimpleCarController.registerObstacleAvoidance());
    }

    TEST_F(SimpleCarControllerTest, registerObstacleAvoidance_WhenCalledAndCarDrivingBackwardsAndObstacleDetectedWithinStopDistance_WillReturnTrue){
        carSpeed = -40; // Car will move backwards. If speed was not set, obstacle avoidance would not be registered
        auto sensorReadingFront = 120;
        auto sensorReadingBack = 40;
        EXPECT_CALL(mFrontSensor, getDistance()).WillOnce(Return(sensorReadingFront));
        EXPECT_CALL(mBackSensor, getDistance()).WillOnce(Return(sensorReadingBack));
        EXPECT_TRUE(mSimpleCarController.registerObstacleAvoidance());
    }


    TEST_F(SimpleCarControllerTest, registerObstacleAvoidance_WhenCalledAndCarStationaryAndObstacleDetectedWithinStopDistance_WillReturnFalse){
        carSpeed = 0;
        auto sensorReadingFront = 10;
        auto sensorReadingBack = 20;
        EXPECT_CALL(mFrontSensor, getDistance()).WillOnce(Return(sensorReadingFront));
        EXPECT_CALL(mBackSensor, getDistance()).WillOnce(Return(sensorReadingBack));
        EXPECT_FALSE(mSimpleCarController.registerObstacleAvoidance());
    }


    TEST_F(SimpleCarControllerTest, registerObstacleAvoidance_WhenCalledAndObstacleDetectedInFrontOfCar_WillStopCar){
        carSpeed = 50;
        auto sensorReadingFront = 10;
        auto sensorReadingBack = 300;
        EXPECT_CALL(mFrontSensor, getDistance()).WillOnce(Return(sensorReadingFront));
        EXPECT_CALL(mBackSensor, getDistance()).WillOnce(Return(sensorReadingBack));
        EXPECT_CALL(mCar, setSpeed(stoppingSpeed));

        mSimpleCarController.registerObstacleAvoidance();
    }

    TEST_F(SimpleCarControllerTest, registerObstacleAvoidance_WhenCalledAndObstacleDetectedBehindCar_WillStopCar){

        carSpeed = -50;
        auto sensorReadingFront = 300;
        auto sensorReadingBack = 10;
        EXPECT_CALL(mFrontSensor, getDistance()).WillOnce(Return(sensorReadingFront));
        EXPECT_CALL(mBackSensor, getDistance()).WillOnce(Return(sensorReadingBack));
        EXPECT_CALL(mCar, setSpeed(stoppingSpeed));

        mSimpleCarController.registerObstacleAvoidance();

    }


    TEST_F(RegisterManualControlTest, registerManualControl_WhenCalledAndMoveBackwardsMessageReceivedWhenObstacleDetectedInFrontOfCar_WillMoveCarBackwards){
        carSpeed = -40;
        auto sensorReadingFront = 30;
        auto sensorReadingBack = 3000;
        EXPECT_CALL(mFrontSensor, getDistance()).WillOnce(Return(sensorReadingFront));
        EXPECT_CALL(mBackSensor, getDistance()).WillOnce(Return(sensorReadingBack));
        EXPECT_CALL(mCar, setSpeed(static_cast<float>(carSpeed)));

        mCallBack("/smartcar/control/speed", "-40");
    }

    TEST_F(RegisterManualControlTest, registerManualControl_WhenCalledAndMoveForwardMessageReceivedWhenObstacleDetectedBehindCar_WillMoveCarForwards){
        carSpeed = 40;
        auto sensorReadingFront = 4000;
        auto sensorReadingBack = 20;
        EXPECT_CALL(mFrontSensor, getDistance()).WillOnce(Return(sensorReadingFront));
        EXPECT_CALL(mBackSensor, getDistance()).WillOnce(Return(sensorReadingBack));
        EXPECT_CALL(mCar, setSpeed(static_cast<float>(carSpeed)));

        mCallBack("/smartcar/control/speed", "40");
    }

    TEST_F(SimpleCarControllerTest, registerObstacleAvoidance_WhenCalledAndObstacleDetectedInFrontOfCar_WillSendObstacleDetectedNotification){
        carSpeed = 50;
        const auto obstacleTopic = "/smartcar/obstacle";
        const auto sensorReadingFront = 10;
        const auto sensorReadingBack = 200;
        EXPECT_CALL(mFrontSensor, getDistance()).WillOnce(Return(sensorReadingFront));
        EXPECT_CALL(mBackSensor, getDistance()).WillOnce(Return(sensorReadingBack));
        EXPECT_CALL(mMQTT, publish(obstacleTopic));

        mSimpleCarController.registerObstacleAvoidance();
    }

    TEST_F(SimpleCarControllerTest, registerObstacleAvoidance_WhenCalledAndObstacleDetectedBehindCar_WillSendObstacleDetectedNotification){
        carSpeed = -50;
        const auto obstacleTopic = "/smartcar/obstacle";
        const auto sensorReadingFront = 200;
        const auto sensorReadingBack = 10;
        EXPECT_CALL(mFrontSensor, getDistance()).WillOnce(Return(sensorReadingFront));
        EXPECT_CALL(mBackSensor, getDistance()).WillOnce(Return(sensorReadingBack));
        EXPECT_CALL(mMQTT, publish(obstacleTopic));

        mSimpleCarController.registerObstacleAvoidance();
    }

    TEST_F(SimpleCarControllerTest, registerObstacleAvoidance_WhenCalledAndObstacleNotDetectedInFrontOfCar_WillNotSendObstacleDetectedNotification){
        carSpeed = 50;
        const auto obstacleTopic = "/smartcar/obstacle";
        const auto sensorReadingFront = 300;
        const auto sensorReadingBack = 200;
        EXPECT_CALL(mFrontSensor, getDistance()).WillOnce(Return(sensorReadingFront));
        EXPECT_CALL(mBackSensor, getDistance()).WillOnce(Return(sensorReadingBack));
        EXPECT_CALL(mMQTT, publish(obstacleTopic)).Times(0);

        mSimpleCarController.registerObstacleAvoidance();
    }

    TEST_F(SimpleCarControllerTest, registerObstacleAvoidance_WhenCalledAndObstacleNotDetectedBehindCar_WillNotSendObstacleDetectedNotification){
        carSpeed = -50;
        const auto obstacleTopic = "/smartcar/obstacle";
        const auto sensorReadingFront = 300;
        const auto sensorReadingBack = 200;
        EXPECT_CALL(mFrontSensor, getDistance()).WillOnce(Return(sensorReadingFront));
        EXPECT_CALL(mBackSensor, getDistance()).WillOnce(Return(sensorReadingBack));
        EXPECT_CALL(mMQTT, publish(obstacleTopic)).Times(0);

        mSimpleCarController.registerObstacleAvoidance();
    }

    TEST_F(SimpleCarControllerTest, registerObstacleAvoidance_WhenCalledAndObstacleDetectedAndCarStationary_WillNotSendObstacleDetectedNotification){
        carSpeed = 0;
        const auto obstacleTopic = "/smartcar/obstacle";
        const auto sensorReadingFront = 13;
        const auto sensorReadingBack = 10;
        EXPECT_CALL(mFrontSensor, getDistance()).WillOnce(Return(sensorReadingFront));
        EXPECT_CALL(mBackSensor, getDistance()).WillOnce(Return(sensorReadingBack));
        EXPECT_CALL(mMQTT, publish(obstacleTopic)).Times(0);

        mSimpleCarController.registerObstacleAvoidance();
    }

    TEST_F(SimpleCarControllerTest, registerSendObstacleDetectedNotification_WhenCalledAndParameterIsTrueAndHasNotYetPublished_WillPublishObstacleDetectedNotification){
        const auto obstacleTopic = "/smartcar/obstacle";
        isObstacleDetectedPublished = false;
        EXPECT_CALL(mMQTT, publish(obstacleTopic));

        mSimpleCarController.registerSendObstacleDetectedNotification(true);
    }

    TEST_F(SimpleCarControllerTest, registerSendObstacleDetectedNotification_WhenCalledAndParameterIsFalseAndHasNotYetPublished_WillNotPublishObstacleDetectedNotification){
        const auto obstacleTopic = "/smartcar/obstacle";
        isObstacleDetectedPublished = false;
        EXPECT_CALL(mMQTT, publish(obstacleTopic)).Times(0);

        mSimpleCarController.registerSendObstacleDetectedNotification(false);
    }

    TEST_F(SimpleCarControllerTest, registerSendObstacleDetectedNotification_WhenCalledAndParameterIsTrueAndHasPublished_WillNotPublishObstacleDetectedNotification){
        const auto obstacleTopic = "/smartcar/obstacle";
        isObstacleDetectedPublished = true;
        EXPECT_CALL(mMQTT, publish(obstacleTopic)).Times(0);

        mSimpleCarController.registerSendObstacleDetectedNotification(true);
    }

    TEST_F(SimpleCarControllerTest, registerSendObstacleDetectedNotification_WhenCalledAndParameterIsFalseAndHasPublished_WillNotPublishObstacleDetectedNotification){
        const auto obstacleTopic = "/smartcar/obstacle";
        isObstacleDetectedPublished = true;
        EXPECT_CALL(mMQTT, publish(obstacleTopic)).Times(0);

        mSimpleCarController.registerSendObstacleDetectedNotification(false);
    }



//TODO check if autodriving on, should not let manual speed control
//TODO check if autodrive on, should not let manual angle control

}