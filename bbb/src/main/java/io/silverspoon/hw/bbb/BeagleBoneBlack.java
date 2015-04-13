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
package io.silverspoon.hw.bbb;

import java.io.IOException;

import io.silverspoon.mmap.JavaMmap;

public class BeagleBoneBlack {

   public static final long GPIO_START = 0x4804C000;
   public static final long GPIO_END = 0x4804DFFF;
   public static final long GPIO_SIZE = GPIO_END - GPIO_START;
   public static final long GPIO_OE = 0x134;
   public static final long GPIO_SET = 0x194;
   public static final long GPIO_CLEAR = 0x190;
   public static final int USR1_LED = 1 << 19; // 22

   public static void main(String[] args) throws IOException, InterruptedException {
      JavaMmap mmap = new JavaMmap("/dev/mem", GPIO_START, GPIO_SIZE);

      long l = mmap.getLong(GPIO_OE);
      mmap.setLong(GPIO_OE, l & (0xFFFFFFFF - USR1_LED));

      while (true) {
         mmap.setLong(GPIO_SET, USR1_LED);

//         Thread.sleep(1);

         mmap.setLong(GPIO_CLEAR, USR1_LED);
         
//         Thread.sleep(1);
      }
   }
}
