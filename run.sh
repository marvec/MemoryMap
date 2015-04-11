#!/bin/sh
LD_LIBRARY_PATH=native/target/.:$LD_LIBRARY_PATH java -cp mmap/target/memorymap-mmap-1.0-SNAPSHOT.jar io.silverspoon.mmap.JavaMmap
