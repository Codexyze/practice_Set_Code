#include <jni.h>
#include <string>

std::string getData(int x) { // Some function to get the key depends on the parameter
    std::string app_secret = "Null";

    if (x == 1) app_secret = "123456789";
    if (x == 2) app_secret = "abcdefg";

    // The number of parameters to be protected can be increased.

    return app_secret;
}

extern "C" jstring
Java_com_example_apisecurendk_MainActivity_getApiKey(
        JNIEnv *env,
        jobject /* this */,
        jint id
) {
    std::string app_secret = "Null";
    app_secret = getData(id);
    return env->NewStringUTF(app_secret.c_str());
}
