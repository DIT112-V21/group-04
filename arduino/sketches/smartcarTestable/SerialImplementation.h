#include "Serialinterface.h"

namespace arduino_car{

    class SerialImplementation : public Serialinterface {

    public:

        void println(String outputMessage) override;
        void begin(int beginNumber) override;

    };

}
