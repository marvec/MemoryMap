/*
 * -----------------------------------------------------------------------\
 * SilverWare
 *  
 * Copyright (C) 2010 - 2013 the original author or authors.
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * -----------------------------------------------------------------------/
 */
package io.silverspoon.hw.rpi2;

import java.io.IOException;

import io.silverspoon.mmap.JavaMmap;

public class RaspberryPi2 {

   public static final long BCM2709_PERI_BASE = 0x3F000000;
   public static final long GPIO_START = BCM2709_PERI_BASE + 0x200000;
   public static final long GPIO_SIZE = 4 * 1024;
   public static final long GPIO_SET = 4 * 7;
   public static final long GPIO_CLEAR = 4 * 10;
   public static final long PIN = 5;
   public static final long PIN_SHIFT = (PIN % 10) * 3;
   public static final long PIN_MODE_OFFSET = (PIN / 10) * 4;

   public static void main(String[] args) throws IOException, InterruptedException {
      final JavaMmap mmap = new JavaMmap("/dev/mem", GPIO_START, GPIO_SIZE);

      long mode = mmap.getLong(PIN_MODE_OFFSET);
      mode = (mode & (0xFFFFFFFF - (7 << PIN_SHIFT))) | (1 << PIN_SHIFT);
      mmap.setLong(PIN_MODE_OFFSET, mode);

      final long pinBit = 1 << PIN;
      while(true){
         mmap.setLong(GPIO_SET, pinBit);
         //Thread.sleep(1);
         mmap.setLong(GPIO_CLEAR, pinBit);
         //Thread.sleep(1);
      }
   }
}
