#include "MockCar.h"
#include "MockMQTT.h"
#include "SimpleCarController.h"
#include "gtest/gtest.h"

using namespace testing;

namespace arduino_car{

    struct SimpleCarControllerTest : public Test {
        MockCar mCar;
        MockMQTT mMQTT;
        SimpleCarController mSimpleCarController{mCar, mMQTT};
    };

    struct registerManualControlTest : public Test {

        void SetUp() override {
            EXPECT_CALL(mMQTT, connect("arduino", "public", "public"));
        }

        MockCar mCar;
        MockMQTT mMQTT;
        SimpleCarController mSimpleCarController{mCar, mMQTT};

    };

    TEST_F(SimpleCarControllerTest, registerManualControl_WhenCalled_WillSubscribeToCorrectChannels){ // _F means we dont need to repeat instatiation of all the mocks.
        EXPECT_CALL(mMQTT, subscribe("/smartcar/switchServer", _)); //Will make sure that the .subscribe method from the mMQTT object is called.
        EXPECT_CALL(mMQTT, subscribe("/smartcar/control/#", _)); // Underscore is wildcar - wont match particular argument. Test case just checks if endpoint is registered.

        mSimpleCarController.registerManualControl();
    }


}