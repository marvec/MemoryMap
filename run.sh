#!/bin/sh
#LD_LIBRARY_PATH=native/target/.:$LD_LIBRARY_PATH java -cp mmap/target/memorymap-mmap-1.0-SNAPSHOT.jar io.silverspoon.mmap.JavaMmap
LD_LIBRARY_PATH=native/target/.: java -cp mmap/target/memorymap-mmap-1.0-SNAPSHOT.jar:bbb/target/memorymap-bbb-1.0-SNAPSHOT.jar io.silverspoon.hw.bbb.BeagleBoneBlack
