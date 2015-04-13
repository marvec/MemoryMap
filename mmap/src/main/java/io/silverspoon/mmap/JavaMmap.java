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
package io.silverspoon.mmap;

import sun.misc.Unsafe;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.Field;

import io.silverspoon.mmap.jni.NativeMmap;
import io.silverspoon.mmap.jni.NativeTools;

public class JavaMmap implements Closeable {

   private final Unsafe unsafe = JavaMmap.getUnsafe();
   private final int fileDescriptor;
   private final long address;
   private final long correctedAddress;
   private final long size;

   public JavaMmap(final String fileName, final long offset, final long size) {
      System.loadLibrary("javammap");

      long page = offset / getPageSize();
      long pageAddress = page * getPageSize();
      long offsetCorrection = offset - pageAddress;

      fileDescriptor = NativeTools.open(fileName, NativeTools.OPEN_READ_WRITE);
      address = NativeMmap.createMap(0, size, NativeMmap.PROT_READ | NativeMmap.PROT_WRITE, NativeMmap.MAP_SHARED, fileDescriptor, pageAddress);
      correctedAddress = address + offsetCorrection;
      System.out.println("Obtained file at address: " + address);

      this.size = size;
   }

   public void setLong(final long offset, final long value) {
      unsafe.putLong(correctedAddress + offset, value);
   }

   public long getLong(final long offset) {
      return unsafe.getAddress(correctedAddress + offset);
   }

   public int getPageSize() {
      return unsafe.pageSize();
   }

   @Override
   public void close() throws IOException {
      NativeMmap.deleteMap(address, size);
      NativeTools.close(fileDescriptor);
   }

   private static Unsafe getUnsafe() {
      try {
         Field f = Unsafe.class.getDeclaredField("theUnsafe");
         f.setAccessible(true);
         return (Unsafe) f.get(null);
      } catch (IllegalAccessException | NoSuchFieldException e) {
         throw new IllegalStateException("Unsafe passed away...");
      }
   }

   public static void main(String[] args) throws IOException {
      JavaMmap map = new JavaMmap("/home/mvecera/mapa", 2, 4);
      map.setLong(2, 0xFFFFFFFF);
      map.close();
   }
}