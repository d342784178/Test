package util.resume;


import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import static util.encryp.MD5Util.getBuferrSize;
import static util.encryp.MD5Util.getMD5ByFileIO;
import static util.encryp.MD5Util.getMd5ByFileNIO;

/**
 * Desc: 断点续传
 * Author: DLJ
 * Date: 2017-01-16
 * Time: 19:01
 */
public class ResumeUtil {
    private static final int DEFAULT_BUFFER_SIZE = 1024;

    /**
     * 断点下载
     *
     * @param file
     * @param startPosition
     * @param downSize
     * @param outputStream
     *
     * @exception Exception
     */
    public static void resumeDownload(File file, long startPosition, long downSize, OutputStream outputStream)
            throws Exception {
        if (!file.exists()) {
            throw new FileNotFoundException("文件不存在");
        }
        if (startPosition > file.length()) {
            throw new IllegalArgumentException("起始位置大于文件大小");
        }
        if (downSize > 0 && startPosition + downSize > file.length()) {
            throw new IllegalArgumentException("startPosition+downSize 大于文件大小");
        }

        int bufferSize = downSize < DEFAULT_BUFFER_SIZE && downSize > 0 ? Integer.valueOf(String.valueOf
                (downSize)) : DEFAULT_BUFFER_SIZE;
        long             totalSize = downSize;
        RandomAccessFile r         = new RandomAccessFile(file, "r");
        r.seek(startPosition);
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream, bufferSize);
        byte[]               buffer               = new byte[bufferSize];
        int                  read;
        while ((read = r.read(buffer)) > 0) {
            bufferedOutputStream.write(buffer, 0, read);
            bufferedOutputStream.flush();
            int buferrSize = getBuferrSize(totalSize, read, bufferSize);
            totalSize -= bufferSize;
            buffer = new byte[buferrSize];
        }
    }


    /**
     * 断点上传
     *
     * @param file          目标文件
     * @param startPosition 起始位置
     * @param uploadSize    上传长度
     * @param inputStream   输入流
     *
     * @exception Exception
     */
    public static void resumeUpload(File file, long startPosition, long uploadSize, InputStream inputStream) throws
            Exception {
        int bufferSize = uploadSize < DEFAULT_BUFFER_SIZE && uploadSize > 0 ? Integer.valueOf(String.valueOf
                (uploadSize)) : DEFAULT_BUFFER_SIZE;
        long             totalSize        = uploadSize;
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
        randomAccessFile.setLength(startPosition + uploadSize);
        randomAccessFile.seek(startPosition);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream, bufferSize);
        byte[]              buffer              = new byte[bufferSize];
        int                 read;
        while ((read = bufferedInputStream.read(buffer)) > 0) {
            randomAccessFile.write(buffer, 0, read);
            int buferrSize = getBuferrSize(uploadSize, read, bufferSize);
            uploadSize -= bufferSize;
            buffer = new byte[buferrSize];
        }
        bufferedInputStream.close();
        randomAccessFile.close();

    }

    @Test
    public void testUpload() throws Exception {
        final File file  = new File("/Users/mic/IdeaProjects/webTest/upload.txt");
        final File file1 = new File("/Users/mic/IdeaProjects/webTest/logging.properties");
        FutureTask<Void> target = new FutureTask<>(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                resumeUpload(file, 100L, file1.length(), new FileInputStream(file1));
                return null;
            }
        });
        FutureTask<Void> target1 = new FutureTask<>(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                resumeUpload(file, 1000L, file1.length(), new FileInputStream(file1));
                return null;
            }
        });
        new Thread(target).start();
        new Thread(target1).start();
        target.get();
        target1.get();
    }

    @Test
    public void testDownload() throws Exception {
        final File target = new File("/Users/mic/Movies/秘密.MP4");
        final File local  = new File("/Users/mic/IdeaProjects/webTest/upload.war");
        resumeDownload(target, 0, target.length() / 2, new FileOutputStream(local));
        resumeDownload(target, target.length() / 2, target.length() - target.length() / 2, new FileOutputStream(local));
//        download(target, 4, local);
    }

    /**
     * 模拟http下载文件
     * @param target
     * @param threadNum
     * @param local
     * @throws Exception
     */
    public void download(final File target, final int threadNum, final File local) throws Exception {
        final long            size            = target.length() / threadNum;
        ExecutorService       executorService = Executors.newFixedThreadPool(threadNum);
        ArrayList<FutureTask> arrayList       = new ArrayList();
        for (int i = 0; i < threadNum; i++) {
            final int finalI = i;
            FutureTask<Void> command = new FutureTask<>(new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    ByteArrayOutputStream outputStream  = new ByteArrayOutputStream();
                    long                  tempSize      = size;
                    long                  startPosition = tempSize * finalI;
                    long                  endPosition   = tempSize * (finalI + 1);
                    if (finalI != threadNum - 1) {
                        resumeDownload(target, startPosition,
                                tempSize, outputStream);
                        resumeUpload(local, startPosition, tempSize, new ByteArrayInputStream(outputStream
                                .toByteArray()));
                    } else {
                        tempSize = target.length() - startPosition;
                        endPosition = target.length();
                        resumeDownload(target, startPosition,
                                tempSize, outputStream);
                        resumeUpload(local, startPosition, tempSize, new ByteArrayInputStream(outputStream
                                .toByteArray()));
                    }
                    String s = target.getPath() + "\n   start:" + startPosition + "   end:" + endPosition
                            + "  " + getMd5ByFileNIO(target,
                            startPosition,
                            tempSize);
                    String s1 = local.getPath() + "\n   start:" + startPosition + "   end:" + endPosition
                            + "  " + getMd5ByFileNIO(local,
                            startPosition,
                            tempSize);
                    System.out.println(s + "\n" + s1 + "\n");
                    return null;
                }
            });
            arrayList.add(command);
            executorService.execute(command);
        }
        for (FutureTask futureTask : arrayList) {
            futureTask.get();
        }
    }

    @Test
    public void asdf() throws Exception {
        final File target = new File("/Users/mic/Movies/秘密.MP4");
        final File local  = new File("/Users/mic/IdeaProjects/webTest/upload.war");
        long       l      = System.currentTimeMillis();
        System.out.println(getMD5ByFileIO(target, 0, 0));
        long l1 = System.currentTimeMillis();
        System.out.println(getMd5ByFileNIO(target, 0, 0));
        long l2 = System.currentTimeMillis();
        System.out.println(l1 - l);
        System.out.println(l2 - l1);
//        System.out.println(getMd5ByFileNIO(local, 25391946, 12695973));
    }

}
