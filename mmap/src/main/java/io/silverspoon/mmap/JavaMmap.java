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
   private final long maddress;
   private final long size;

   public JavaMmap(final String fileName, final long offset, final long size) {
      fileDescriptor = NativeTools.open(fileName, NativeTools.OPEN_READ_WRITE);
      System.out.println("File descriptor: " + fileDescriptor);
      address = unsafe.allocateMemory(size);
      System.out.println("Allocated native memory: " + address);
      maddress = NativeMmap.createMap(address, size, NativeMmap.PROT_READ | NativeMmap.PROT_WRITE, NativeMmap.FLAGS_SHARED, fileDescriptor, offset);
      System.out.println("Obtained file at address: " + maddress);
      this.size = size;
   }

   public void setLong(final long offset, final long value) {
      unsafe.putLong(maddress + offset, value);
   }

   public long getLong(final long offset) {
      return unsafe.getAddress(maddress + offset);
   }

   @Override
   public void close() throws IOException {
      NativeMmap.deleteMap(maddress, size);
      unsafe.freeMemory(address);
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
      System.loadLibrary("javammap");
      JavaMmap map = new JavaMmap("/home/mvecera/mapa", 10, 16);
      map.setLong(2, 0xFFFFFFFF);
      map.close();
   }
}