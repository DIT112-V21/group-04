// adapted from https://github.com/platisd/reusable-testable-arduino-tutorial

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

        void SetUp() override {
            isObstacleDetectedPublished = false;
            autoDriving = 0;
        }

        void TearDown() override {
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
            isObstacleDetectedPublished = false;
            autoDriving = 0;
            EXPECT_CALL(mMQTT, connect(_, _, _)).WillOnce(Return(true));
            EXPECT_CALL(mMQTT, onMessage(_)).WillOnce(SaveArg<0>(&mCallBack));

            mSimpleCarController.registerManualControl();
        }

        void TearDown() override {
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


    /* Test(s) relating to adjusting car speed */


    TEST_F(RegisterManualControlTest, registerManualControl_WhenCalledAndReceivesSpeedTopic_WillAdjustTheCarSpeed){
        const auto carSpeed = 40;
        autoDriving = 0;
        auto sensorReadingFront = 110;
        auto sensorReadingBack = 200;
        EXPECT_CALL(mFrontSensor, getDistance()).WillOnce(Return(sensorReadingFront)); // This and the line below ensures that obstacleAvoidance returns false.
        EXPECT_CALL(mBackSensor, getDistance()).WillOnce(Return(sensorReadingBack)); // Test still passes when this value is set to 10. This is because the speed is set to a positive value, hence an obstacle avoidance for the back would not trigger.
        EXPECT_CALL(mCar, setSpeed(static_cast<float>(carSpeed)));


        mCallBack("/smartcar/control/speed", "40");
    }

    TEST_F(RegisterManualControlTest, registerManualControl_WhenCalledAndDoesNotReceiveSpeedTopic_WillNotAdjustCarSpeed){
        const auto carSpeed = 40;
        autoDriving = 0;
        EXPECT_CALL(mCar, setSpeed(static_cast<float>(carSpeed))).Times(0);

        mCallBack("/smartcar/control/turning", "40");
    }

    TEST_F(RegisterManualControlTest, registerManualControl_WhenCalledAndReceivesSpeedTopicAndAutoDrivingIsEnabled_WillNotAdjustCarSpeed){
        const auto carSpeed = 40;
        autoDriving = 1;
        EXPECT_CALL(mCar, setSpeed(static_cast<float>(carSpeed))).Times(0);

        mCallBack("/smartcar/control/speed", "40");
    }


    /* Test(s) relating to adjusting car direction */


    TEST_F(RegisterManualControlTest, registerManualControl_WhenCalledAndReceivesTurningTopic_WillNotAdjustTheCarTurningAngle){
        const auto turningAngle = 80;
        autoDriving = 0;
        EXPECT_CALL(mCar, setAngle(turningAngle));

        mCallBack("/smartcar/control/turning", "80");
    }

    TEST_F(RegisterManualControlTest, registerManualControl_WhenCalledAndDoesNotReceiveTurningTopic_WillNotAdjustTheCarTurningAngle){
        const auto turningAngle = 80;
        autoDriving = 0;
        EXPECT_CALL(mCar, setAngle(turningAngle)).Times(0);

        mCallBack("/smartcar/control/speed", "80");
    }

    TEST_F(RegisterManualControlTest, registerManualControl_WhenCalledAndDoesNotReceiveTurningTopicAndAutoDrivingIsEnabled_WillNotAdjustTheCarTurningAngle){
        const auto turningAngle = 80;
        autoDriving = 1;
        EXPECT_CALL(mCar, setAngle(turningAngle)).Times(0);

        mCallBack("/smartcar/control/turning", "80");
    }


    /* Test(s) relating to MQTT connection */


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
        const auto noMessage = "No message";
        EXPECT_CALL(mMQTT, setHost(_, _));
        EXPECT_CALL(mMQTT, connect(_, _, _)).WillOnce(Return(true));
        EXPECT_CALL(mMQTT, subscribe("/smartcar/control/#", _));

        mCallBack("/smartcar/switchServer", noMessage);
    }


    /* Test(s) relating to Obstacle Avoidance */


    TEST_F(SimpleCarControllerTest, registerObstacleAvoidance_WhenCalledAndCarDrivingForwardAndObstacleDetectedWithinStopDistance_WillReturnTrue){
        EXPECT_CALL(mCar, getSpeed()).WillOnce(Return(40)); // Car will move forward. If speed was not set, obstacle avoidance would not be registered
        const auto sensorReadingFront = 60;
        const auto sensorReadingBack = 120;
        EXPECT_CALL(mFrontSensor, getDistance()).WillOnce(Return(sensorReadingFront));
        EXPECT_CALL(mBackSensor, getDistance()).WillOnce(Return(sensorReadingBack));
        EXPECT_TRUE(mSimpleCarController.registerObstacleAvoidance());
    }

    TEST_F(SimpleCarControllerTest, registerObstacleAvoidance_WhenCalledAndCarDrivingBackwardsAndObstacleDetectedWithinStopDistance_WillReturnTrue){
        EXPECT_CALL(mCar, getSpeed()).WillOnce(Return(-40)); // Car will move backwards. If speed was not set, obstacle avoidance would not be registered
        const auto sensorReadingFront = 120;
        const auto sensorReadingBack = 40;
        EXPECT_CALL(mFrontSensor, getDistance()).WillOnce(Return(sensorReadingFront));
        EXPECT_CALL(mBackSensor, getDistance()).WillOnce(Return(sensorReadingBack));
        EXPECT_TRUE(mSimpleCarController.registerObstacleAvoidance());
    }


    TEST_F(SimpleCarControllerTest, registerObstacleAvoidance_WhenCalledAndCarStationaryAndObstacleDetectedWithinStopDistance_WillReturnFalse){
        mCar.setSpeed(0);
        const auto sensorReadingFront = 10;
        const auto sensorReadingBack = 20;
        EXPECT_CALL(mFrontSensor, getDistance()).WillOnce(Return(sensorReadingFront));
        EXPECT_CALL(mBackSensor, getDistance()).WillOnce(Return(sensorReadingBack));
        EXPECT_FALSE(mSimpleCarController.registerObstacleAvoidance());
    }


    TEST_F(SimpleCarControllerTest, registerObstacleAvoidance_WhenCalledAndObstacleDetectedInFrontOfCar_WillStopCar){
        EXPECT_CALL(mCar, getSpeed()).WillOnce(Return(50));
        const auto stoppingSpeed = 0;
        const auto sensorReadingFront = 10;
        const auto sensorReadingBack = 300;
        EXPECT_CALL(mFrontSensor, getDistance()).WillOnce(Return(sensorReadingFront));
        EXPECT_CALL(mBackSensor, getDistance()).WillOnce(Return(sensorReadingBack));
        EXPECT_CALL(mCar, setSpeed(stoppingSpeed));

        mSimpleCarController.registerObstacleAvoidance();
    }

    TEST_F(SimpleCarControllerTest, registerObstacleAvoidance_WhenCalledAndObstacleDetectedBehindCar_WillStopCar){

        EXPECT_CALL(mCar, getSpeed()).WillOnce(Return(-50));;
        const auto sensorReadingFront = 300;
        const auto sensorReadingBack = 10;
        const auto stoppingSpeed = 0;
        EXPECT_CALL(mFrontSensor, getDistance()).WillOnce(Return(sensorReadingFront));
        EXPECT_CALL(mBackSensor, getDistance()).WillOnce(Return(sensorReadingBack));
        EXPECT_CALL(mCar, setSpeed(stoppingSpeed));

        mSimpleCarController.registerObstacleAvoidance();

    }


    TEST_F(RegisterManualControlTest, registerManualControl_WhenCalledAndMoveBackwardsMessageReceivedWhenObstacleDetectedInFrontOfCar_WillMoveCarBackwards){
        const auto carSpeed = -40;
        const auto sensorReadingFront = 30;
        const auto sensorReadingBack = 3000;
        EXPECT_CALL(mFrontSensor, getDistance()).WillOnce(Return(sensorReadingFront));
        EXPECT_CALL(mBackSensor, getDistance()).WillOnce(Return(sensorReadingBack));
        EXPECT_CALL(mCar, setSpeed(static_cast<float>(carSpeed)));

        mCallBack("/smartcar/control/speed", "-40");
    }

    TEST_F(RegisterManualControlTest, registerManualControl_WhenCalledAndMoveForwardMessageReceivedWhenObstacleDetectedBehindCar_WillMoveCarForwards){
        const auto carSpeed = 40;
        const auto sensorReadingFront = 4000;
        const auto sensorReadingBack = 20;
        EXPECT_CALL(mFrontSensor, getDistance()).WillOnce(Return(sensorReadingFront));
        EXPECT_CALL(mBackSensor, getDistance()).WillOnce(Return(sensorReadingBack));
        EXPECT_CALL(mCar, setSpeed(static_cast<float>(carSpeed)));

        mCallBack("/smartcar/control/speed", "40");
    }


    /* Test(s) relating to Obstacle Avoidance Notification */


    TEST_F(SimpleCarControllerTest, registerObstacleAvoidance_WhenCalledAndObstacleDetectedInFrontOfCar_WillSendObstacleDetectedNotification){
        EXPECT_CALL(mCar, getSpeed()).WillOnce(Return(50));
        const auto obstacleTopic = "/smartcar/obstacle";
        const auto sensorReadingFront = 10;
        const auto sensorReadingBack = 200;
        EXPECT_CALL(mFrontSensor, getDistance()).WillOnce(Return(sensorReadingFront));
        EXPECT_CALL(mBackSensor, getDistance()).WillOnce(Return(sensorReadingBack));
        EXPECT_CALL(mMQTT, publish(obstacleTopic));

        mSimpleCarController.registerObstacleAvoidance();
    }

    TEST_F(SimpleCarControllerTest, registerObstacleAvoidance_WhenCalledAndObstacleDetectedBehindCar_WillSendObstacleDetectedNotification){
        EXPECT_CALL(mCar, getSpeed()).WillOnce(Return(-50));
        const auto obstacleTopic = "/smartcar/obstacle";
        const auto sensorReadingFront = 200;
        const auto sensorReadingBack = 10;
        EXPECT_CALL(mFrontSensor, getDistance()).WillOnce(Return(sensorReadingFront));
        EXPECT_CALL(mBackSensor, getDistance()).WillOnce(Return(sensorReadingBack));
        EXPECT_CALL(mMQTT, publish(obstacleTopic));

        mSimpleCarController.registerObstacleAvoidance();
    }

    TEST_F(SimpleCarControllerTest, registerObstacleAvoidance_WhenCalledAndObstacleNotDetectedInFrontOfCar_WillNotSendObstacleDetectedNotification){
        mCar.setSpeed(50);
        const auto obstacleTopic = "/smartcar/obstacle";
        const auto sensorReadingFront = 300;
        const auto sensorReadingBack = 200;
        EXPECT_CALL(mFrontSensor, getDistance()).WillOnce(Return(sensorReadingFront));
        EXPECT_CALL(mBackSensor, getDistance()).WillOnce(Return(sensorReadingBack));
        EXPECT_CALL(mMQTT, publish(obstacleTopic)).Times(0);

        mSimpleCarController.registerObstacleAvoidance();
    }

    TEST_F(SimpleCarControllerTest, registerObstacleAvoidance_WhenCalledAndObstacleNotDetectedBehindCar_WillNotSendObstacleDetectedNotification){
        mCar.setSpeed(-50);
        const auto obstacleTopic = "/smartcar/obstacle";
        const auto sensorReadingFront = 300;
        const auto sensorReadingBack = 200;
        EXPECT_CALL(mFrontSensor, getDistance()).WillOnce(Return(sensorReadingFront));
        EXPECT_CALL(mBackSensor, getDistance()).WillOnce(Return(sensorReadingBack));
        EXPECT_CALL(mMQTT, publish(obstacleTopic)).Times(0);

        mSimpleCarController.registerObstacleAvoidance();
    }

    TEST_F(SimpleCarControllerTest, registerObstacleAvoidance_WhenCalledAndObstacleDetectedAndCarStationary_WillNotSendObstacleDetectedNotification){
        mCar.setSpeed(0);
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


    /* Test(s) relating to autonomous moving */


    TEST_F(SimpleCarControllerTest, registerAutonomousMoving_WhenCalled_WillProvideMovementInstructionsToTheCar){
        const auto stoppingSpeedRegisterTurning = 1;
        const auto stoppingSpeedAutonomous = 0;
        const auto autoSpeed = 65;
        EXPECT_CALL(mCar, setSpeed(stoppingSpeedAutonomous)).Times(2);
        EXPECT_CALL(mCar, setSpeed(stoppingSpeedRegisterTurning)).Times(1);
        EXPECT_CALL(mCar, setSpeed(autoSpeed)).Times(2);
        EXPECT_CALL(mCar, setSpeed(-autoSpeed)).Times(1);


        mSimpleCarController.registerAutonomousMoving();
    }

    TEST_F(SimpleCarControllerTest, registerTurning_WhenCalled_WillSetTheProperAngles){
        const auto angle = 10;
        const auto autonomousAngle = 90;
        const auto expectedAngle = autonomousAngle * angle;
        EXPECT_CALL(mCar, setAngle(expectedAngle));
        EXPECT_CALL(mCar, setAngle(0));

        mSimpleCarController.registerTurning(10);
    }

    TEST_F(SimpleCarControllerTest, registerTurning_WhenCalled_WillSetTheProperSpeeds){
        const auto autoSpeed = 65;
        const auto stoppingSpeed = 1;
        EXPECT_CALL(mCar, setSpeed(autoSpeed));
        EXPECT_CALL(mCar, setSpeed(stoppingSpeed));

        mSimpleCarController.registerTurning(-1000);
    }
}