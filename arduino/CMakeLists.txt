# Adapted from https://github.com/platisd/reusable-testable-arduino-tutorial
cmake_minimum_required(VERSION 3.20)
project(arduino)

set(CMAKE_CXX_STANDARD 14)

if(NOT MSVC)
    add_compile_options(
            -Wcast-align
            -Wpedantic
            -Wcast-qual
            -Wconversion
            -Wctor-dtor-privacy
            -Wnon-virtual-dtor
            -Wmissing-include-dirs
            -Wdisabled-optimization
            -Winit-self
            -Wnon-virtual-dtor
            -Wold-style-cast
            -Woverloaded-virtual
            -Wparentheses
            -Wredundant-decls
            -Wshadow
            -Wsign-promo
            -Wstrict-aliasing
            -Wall
            -Wextra
            -Werror
    )
else()
    add_compile_options(
            /W4
            /WX
    )
endif()


include_directories(.)
include_directories(sketches/smartcar)
set(simplecar_dir ${CMAKE_CURRENT_SOURCE_DIR}/sketches/smartcarTestable)

add_executable(arduino/sketches/smartcar/smartcar.ino sketches/smartcarTestable/Car.h   sketches/smartcarTestable/SerialImplementation.h sketches/smartcarTestable/SerialImplementation.cpp sketches/smartcarTestable/MQTTinterface.h sketches/smartcarTestable/SimpleCarController.h sketches/smartcarTestable/SimpleCarController.cpp sketches/smartcarTestable/Serial.h sketches/smartcarTestable/SimpleCarWrapper.cpp sketches/smartcarTestable/SimpleCarWrapper.h sketches/smartcarTestable/SimpleCarMQTTwrapper.cpp sketches/smartcarTestable/SimpleCarMQTTwrapper.h test/ut/SimpleCarController_test.cpp test/mocks/MockCar.h test/mocks/MockMQTT.h test/mocks/MockSerial.h)

enable_testing()
add_subdirectory(test)