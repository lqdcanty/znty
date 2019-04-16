package com.efida.sports.app.Util;


import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;

public class FileUtils {

    public static MultipartFile base64ToMultipart(String base64) {
            try {

                String[] baseStrs = base64.split(",");

                BASE64Decoder decoder = new BASE64Decoder();
                byte[] b = new byte[0];
                b = decoder.decodeBuffer(baseStrs[1]);

                for (int i = 0; i < b.length; ++i) {
                    if (b[i] < 0) {
                        b[i] += 256;
                    }
                }
                return new BASE64DecodedMultipartFile(b, baseStrs[0]);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }catch (ArrayIndexOutOfBoundsException e){
                e.printStackTrace();
                return null;
            }
        }

    /* 根据url下载图片*/
    public static File downloadPic(String _url) throws Exception{
        try {
            _url= URLDecoder.decode(_url,"utf-8");
        }catch (Exception e){}
        File file = null;
        String fileName = _url.substring(_url.lastIndexOf("/")+1,_url.length());
        String fileTempUrl = "../tempFile/otherPic/"+fileName;
        try{
            URL url = new URL(_url);
            URLConnection con = url.openConnection();
            con.setConnectTimeout(5000);
            InputStream is = con.getInputStream();
            byte[] bs = new byte[1024];
            int len;
            file = new File(fileTempUrl);
            if (!file.getParentFile().exists()) { //不存在，创建图片
                file.getParentFile().mkdirs();
            }
            OutputStream os = new FileOutputStream(file);
            while ((len = is.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
            os.close();
            is.close();

        } catch (IOException e) {
        }
        return  file;
    }

    public static byte[] getBytes(File file) {
        byte[] buffer = null;

        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
            byte[] b = new byte[1024];

            int n;
            while((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }

            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException var6) {
            var6.printStackTrace();
        } catch (IOException var7) {
            var7.printStackTrace();
        }

        return buffer;
    }

    /*
     URL --->base64
     */
    public static String getBase64(String imgFile) {
        byte[] bytes = null;
        try {
            bytes= urlTobyte(imgFile);
            //bytes= getBytes(FileUtils.downloadPic(imgFile));
            // 对字节数组Base64编码

        } catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        BASE64Encoder encoder = new BASE64Encoder();
        // 返回Base64编码过的字节数组字符串
        return  encoder.encode(bytes);
    }

    public static byte[] urlTobyte(String url) throws MalformedURLException {
        URL ur = new URL(url);
        BufferedInputStream in = null;
        ByteArrayOutputStream out = null;

        byte[] content;
        try {
            in = new BufferedInputStream(ur.openStream());
            out = new ByteArrayOutputStream(1024);
            content = new byte[1024];

            int size;
            while((size = in.read(content)) != -1) {
                out.write(content, 0, size);
            }
        } catch (Exception var14) {
            var14.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException var13) {
                var13.printStackTrace();
            }

        }

        content = out.toByteArray();
        return content;
    }
    public static void main(String[] args) {
        String url = "http://zntyfdfs.efida.com.cn/group1/M00/00/05/rBADF1tpTZaASrSIAAGcADuJJ3w450.jpg";
        String url1 = "http://tva2.sinaimg.cn/crop.0.0.600.600.1024/006a2MW5jw8etpqwkeptqj30go0gogmr.jpg";
        String s = "data:image/png;base64,"+ FileUtils.getBase64(url1);
       /* try {
            getBytes(FileUtils.downloadPic(url));
        }catch (Exception e){

        }*/

        System.out.println(s);
    }
    }
