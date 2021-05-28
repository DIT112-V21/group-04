//code adapted from: https://github.com/platisd/reusable-testable-arduino-tutorial
#pragma once
#include "Car.h"
#include <Arduino.h>
#include <Smartcar.h>

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

    private:
        SimpleCar& mCar;
    };

}
