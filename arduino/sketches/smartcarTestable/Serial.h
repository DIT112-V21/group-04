#pragma once

namespace arduino_car{

    struct Serial{
        virtual ~Serial() = default;

        virtual void println(std::string output) = 0;
        virtual void println(int output) = 0;
        virtual void begin(int beginNumber) = 0;
    };

}