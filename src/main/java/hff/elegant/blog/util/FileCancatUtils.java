package hff.elegant.blog.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class FileCancatUtils {
    private static final Logger logger = LoggerFactory.getLogger(FileCancatUtils.class);

    public static final int BUFSIZE = 1024 * 8;

    public static void mergeFiles(String outFile, String[] files) {
        FileChannel outChannel = null;
        try {
            outChannel = new FileOutputStream(outFile).getChannel();
            for(String f : files){
                @SuppressWarnings("resource")
                FileChannel fc = new FileInputStream(f).getChannel(); 
                ByteBuffer bb = ByteBuffer.allocate(BUFSIZE);
                while(fc.read(bb) != -1){
                    bb.flip();
                    outChannel.write(bb);
                    bb.clear();
                }
                fc.close();
            }
            logger.info("Merged!! " + Arrays.toString(files) + " into " + outFile);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {if (outChannel != null) {outChannel.close();}} catch (IOException ignore) {}
        }
    }
    
    public static void main(String[] args) {
        mergeFiles("D:/output.txt", new String[]{"D:/in_1.txt", "D:/in_2.txt", "D:/in_3.txt"});
    }
}