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

set(calculator_dir ${CMAKE_CURRENT_SOURCE_DIR}/smartcar/smartcar.ino)

include_directories(.)
include_directories(smartcar)

add_executable(arduino
        smartcar/smartcar.ino)

enable_testing()
add_subdirectory(test)