#!/bin/sh

gradle_compile_release()
{
    echo "build release start!"
    ./gradlew :app:clean
    ./gradlew :app:assembleRelease

    rm -rf out/
    mkdir -p out/apk
    cp app/build/outputs/apk/release/*.apk out/apk/MemoryLeakDemo-release.apk
    adb install -r out/apk/MemoryLeakDemo-release.apk
    adb shell am start -n com.ljz.rxjava/com.ljz.rxjava.activity.MainActivity
    echo "build release end!"
}

gradle_compile_debug()
{
    echo "build debug start!"
    ./gradlew :app:clean
    ./gradlew :app:assembleDebug

    rm -rf out/
    mkdir -p out/apk
    cp app/build/outputs/apk/debug/*.apk out/apk/MemoryLeakDemo-debug.apk
    adb install -r out/apk/MemoryLeakDemo-debug.apk
    adb shell am start -n com.ljz.rxjava/com.ljz.rxjava.activity.MainActivity
    echo "build debug end!"
}

Main()
{
    if [ "$1" == "0" ]; then
       gradle_compile_debug
    elif [ "$1" == "1" ]; then
       gradle_compile_release
    fi

    return $?
}

Main "$@"
