package com.example.demo.utils;

import com.example.demo.config.FilePathConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.*;


@Component
public class FileUtil {





    public void downloadFile(String filePath, HttpServletResponse response) throws UnsupportedEncodingException {

        File file = new File(filePath);
        if (file.exists()) { //判断文件父目录是否存在
            response.setContentType("application/octet-stream");
            response.setCharacterEncoding("UTF-8");
            String fileName = filePath.substring(filePath.lastIndexOf("\\"));
            response.setHeader("Content-Disposition", "attachment;fileName=" + java.net.URLEncoder.encode(fileName, "UTF-8"));
            byte[] buffer = new byte[1024];
            FileInputStream fis = null; //文件输入流
            BufferedInputStream bis = null;

            OutputStream os = null; //输出流
            try {
                os = response.getOutputStream();
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer);
                    i = bis.read(buffer);
                }

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                bis.close();
                fis.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * 源文件流复制给目标文件流
     */
    public void copyFile(InputStream in, String targetPath) {

        File dest = new File(targetPath);
        if (!dest.getParentFile().exists()) {
            // 判断文件父目录是否存在
            dest.getParentFile().mkdirs();
        }

        byte[] buff = new byte[1024];
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(new File(targetPath));
            int len = in.read(buff);
            while (len != -1) {
                out.write(buff, 0, len);
                len = in.read(buff);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != out) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public boolean isExist(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 删除文件夹
     */
    public void deleteAllFilesOfDir(File file) {
        if (null != file) {
            if (!file.exists())
                return;
            if (file.isFile()) {
                boolean result = file.delete();
                int tryCount = 0;
                while (!result && tryCount++ < 10) {
                    System.gc(); // 回收资源
                    result = file.delete();
                }
            }
            File[] files = file.listFiles();
            if (null != files) {
                for (int i = 0; i < files.length; i++) {
                    deleteAllFilesOfDir(files[i]);
                }
            }
            file.delete();
        }
    }

    public File getFile(String path) {
        return new File(path);
    }

}
