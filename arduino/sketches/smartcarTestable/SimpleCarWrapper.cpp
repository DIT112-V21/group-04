#include "SimpleCarWrapper.h"
//corresponds to MagicCar.cpp
namespace arduino_car{
    SimpleCarWrapper::SimpleCarWrapper(SimpleCar& car)
        : mCar{car}
    {}

    void SimpleCarWrapper::setSpeed(float speed) {
        mCar.setSpeed(speed);
    }

    void MagicCar::setAngle(int angle)
    {
        mCar.setAngle(angle);
    }

    /*void MagicCar::update()
    {
        mCar.update();
    }

    int MagicCar::getHeading()
    {
        return mCar.getHeading();
    }*/
}