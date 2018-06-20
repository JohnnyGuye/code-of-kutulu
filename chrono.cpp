#include <chrono>

using namespace std::chrono;

class Stopwatch {
    high_resolution_clock::time_point _starttime;
public:
    
    static inline int now() {
        return high_resolution_clock::now().time_since_epoch().count();
    }
    
    Stopwatch() {   start();    }
    inline void start() {
        _starttime = high_resolution_clock::now();
    }
    
    template<typename T>
    inline float currentTime() {
        return duration_cast<T>(high_resolution_clock::now() - _starttime).count();
    }
};
