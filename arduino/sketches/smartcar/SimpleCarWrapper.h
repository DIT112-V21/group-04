#pragma once
#include "Car.h"
#include <Arduino.h>
#include <Smartcar.h>
//correspond to MagicCar.h

namespace arduino_car{

    class SimpleCarWrapper : public Car {
    public:
        SimpleCarWrapper(SimpleCar &car)
        : mCar{car} {}

        void setSpeed(float speed) override {
            mCar.setSpeed(speed);
        }

        void setAngle(int angle) override {
            mCar.setAngle(angle);
        }
        //void update() override;
        //int getHeading() override;

    private:
        SimpleCar& mCar;
    };

}
