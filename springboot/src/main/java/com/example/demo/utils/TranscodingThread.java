package com.example.demo.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;
@Slf4j
public class TranscodingThread extends Thread{
    private String saveFileName;

    private String transcodingUrl;




    public TranscodingThread(String saveFileName, String transcodingUrl) {
        this.saveFileName = saveFileName;
        this.transcodingUrl = transcodingUrl;
    }
    @Override
    public void run()  {
        String videothumbnailcommand = "cmd /c start /b D:\\software\\ffmpeg-4.4-essentials_build\\bin\\ffmpeg  -i " + saveFileName
                + " -y -ab 35200  -ar 22050  " + transcodingUrl;
        try {
            Process process = Runtime.getRuntime().exec(videothumbnailcommand);
            //防止阻塞
            new TranscodingThread.PrintStream(process.getErrorStream(),"-1",saveFileName).start();
            new TranscodingThread.PrintStream(process.getInputStream(),"1",saveFileName).start();
            process.waitFor();
            process.destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static void main(String[] args) {
        TranscodingThread thumbnailThread = new TranscodingThread("E:\\3.avi", "E:\\4.mp4");
        thumbnailThread.start();
    }

    static class PrintStream extends Thread {
        java.io.InputStream is = null;
        String code = "";
        String delPath="";

        public PrintStream(java.io.InputStream is,String code,String delPath) {
            this.is = is;
            this.code = code;
            this.delPath = delPath;
        }

        public void run() {
            long start = System.currentTimeMillis();
            try {
                while (this != null) {
                    int _ch = is.read();
                    if (_ch != -1){
//                        System.out.print((char) _ch);
                    }else{
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                if(code.equals("1")){
                    long end = System.currentTimeMillis();
                    log.info("视频转码完成，耗时：" + (end - start) + " ms");
                    new FileUtil().deleteAllFilesOfDir(new File(delPath));
                }
            }
        }
    }
}
