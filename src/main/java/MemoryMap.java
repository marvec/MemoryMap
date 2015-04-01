import java.io.File;
import java.nio.ByteBuffer;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Field;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.*;

import sun.misc.Unsafe;

public class MemoryMap {

    public static final long GPIO_START = 0x4804C000;
    public static final long GPIO_END = 0x4804DFFF;
    public static final long GPIO_SIZE = GPIO_END - GPIO_START;
    public static final long GPIO_OE = 0x134;
    public static final long GPIO_SET = 0x194;
    public static final long GPIO_CLEAR = 0x190;
    public static final int USR1_LED = 1 << 22;


    public static Unsafe getUnsafe() throws Exception {
        Field f = Unsafe.class.getDeclaredField("theUnsafe");
        f.setAccessible(true);
        return (Unsafe) f.get(null);
    }

    public static void main(String[] args) throws Exception {
        Unsafe u = getUnsafe();

        long l = u.getAddress(GPIO_START + GPIO_OE);
        u.putAddress(GPIO_START + GPIO_OE, l & (0xFFFFFFFF - USR1_LED));

        u.putAddress(GPIO_START + GPIO_SET, USR1_LED);
    }
}