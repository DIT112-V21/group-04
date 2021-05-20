#pragma once

namespace arduino_car{

    struct Serial{
        virtual ~Serial() = default;

        virtual void println(String output) = 0;
    };

}