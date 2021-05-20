#pragma once

namespace arduino_car{

    struct Serial{
        virtual ~Serial() = default;

        virtual void println(std::string output) = 0;
    };

}