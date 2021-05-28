#pragma once

#if defined(ARDUINO) || defined(__SMCE__)
#include <Arduino.h>
#else
#include <string>
using String = std::string;
#endif