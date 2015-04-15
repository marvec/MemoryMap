#!/bin/sh
#LD_LIBRARY_PATH=native/target/.:$LD_LIBRARY_PATH java -cp mmap/target/memorymap-mmap-1.0-SNAPSHOT.jar io.silverspoon.mmap.JavaMmap

# BeagleBoneBlack
LD_LIBRARY_PATH=native/target/.: java -cp mmap/target/memorymap-mmap-1.0-SNAPSHOT.jar:bbb/target/memorymap-bbb-1.0-SNAPSHOT.jar io.silverspoon.hw.bbb.BeagleBoneBlack

# Raspberry Pi 2
#LD_LIBRARY_PATH=native/target/.: java -cp mmap/target/memorymap-mmap-1.0-SNAPSHOT.jar:rpi2/target/memorymap-rpi2-1.0-SNAPSHOT.jar io.silverspoon.hw.rpi2.RaspberryPi2
