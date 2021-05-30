# Adapted from https://github.com/platisd/reusable-testable-arduino-tutorial
cmake_minimum_required(VERSION 3.15)
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
           
    )
else()
    add_compile_options(
            /W4
            /WX
    )
endif()


#include_directories(.)
#include_directories(sketches/smartcar sketches/smartcar)
set(simplecar_dir ${CMAKE_CURRENT_SOURCE_DIR}/sketches/smartcar)

#add_executable(arduino/sketches/smartcar/smartcar.ino sketches/smartcar/Car.h sketches/smartcar/MQTTinterface.h sketches/smartcar/SimpleCarController.h sketches/smartcar/SimpleCarWrapper.h sketches/smartcar/SimpleCarMqttImplementation.h test/ut/SimpleCarController_test.cpp test/mocks/MockCar.h test/mocks/MockMQTT.h)

enable_testing()
add_subdirectory(test)
