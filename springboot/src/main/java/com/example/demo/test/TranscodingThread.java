package com.example.demo.test;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TranscodingThread extends Thread {
    private String saveFileName;

    private String transcodingUrl;


    public TranscodingThread(String saveFileName, String transcodingUrl) {
        this.saveFileName = saveFileName;
        this.transcodingUrl = transcodingUrl;
    }

    @Override
    public void run() {
        String videothumbnailcommand = "cmd /c start /b ffmpeg  -i " + saveFileName
                + " -y -ab 35200  -ar 22050  " + transcodingUrl;
        try {
            Process process = Runtime.getRuntime().exec(videothumbnailcommand);
            new PrintStream(process.getErrorStream()).start();
            new PrintStream(process.getInputStream()).start();
            process.waitFor();
            process.destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    static List<String> list = new ArrayList<>();
    String[] type  ={"mp4","avi","wmv"};

    public static void main(String[] args) {
//        TranscodingThread thumbnailThread = new TranscodingThread("E:\\3.avi", "E:\\4.mp4");
//        thumbnailThread.start();
        traverse(new File("D:\\..m3u8download"));
        for (String l : list) {
            System.out.println(l);
        }
    }

    static class PrintStream extends Thread {
        InputStream __is = null;

        public PrintStream(InputStream is) {
            __is = is;
        }

        public void run() {
            try {
                while (this != null) {
                    int _ch = __is.read();
                    if (_ch != -1)
                        System.out.print((char) _ch);
                    else break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public static void traverse(File file) {
        File[] files = file.listFiles();
        if (files == null) {
            return;
        }
        for (File file1 : files) {
            if (file1.isFile()) {
                list.add(file1.getPath());
            } else {
                traverse(file1);
            }
        }
    }


}
