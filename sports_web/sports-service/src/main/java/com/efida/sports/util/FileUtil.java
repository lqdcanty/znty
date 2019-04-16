package com.efida.sports.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.lang3.StringUtils;

public class FileUtil {

    /**
     * 
     * @param absolutelPath
     * @return
     */
    public static boolean existFile(String absolutelPath) {
        File file = new File(absolutelPath);
        if (file.exists()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 
     * @param dir
     * @param path
     * @return
     */
    public static boolean existFile(String dir, String path) {
        File file = new File(dir, path);
        return existFile(file.getAbsolutePath());
    }

    /**
     * 判断文件是否过期
     * @param absolutePath
     * @param delay
     * @return
     */
    public static boolean isOutOfDate(String absolutePath, long delay) {
        File file = new File(absolutePath);
        if (!file.exists()) {
            return true;
        }

        long lastModified = file.lastModified();

        if (lastModified + delay > System.currentTimeMillis()) {
            return false;
        } else {
            return true;
        }

    }

    /**
     * 获取项目下的相对路径
     * @return
     * @throws Exception
     */
    public static File getWebAppPath() throws Exception {
        final File webappPath = new File(getSystemPath("")).getParentFile().getParentFile();
        return webappPath;
    }

    public static String getFileExtensionName(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    /**
     * @param String
     * @return String etc:D:/workspace/my_depms/WebRoot/WEB-INF/doc
     */
    public static String getSystemPath(String resource) throws Exception {
        String path = getExtendResource(resource).toString();
        String temp = path;
        if (path.startsWith("file:/")) {
            temp = temp.substring(7);
            if (!temp.startsWith(":"))
                return path.substring(path.indexOf("/"));
        }

        return path.substring(path.indexOf("/") + 1);
    }

    /**
     * 必须传递资源的相对路径。是相对于CLASSPATH的路径。如果需要查找CLASSPATH外部的资源，需要使 用../来查找
     * @param relativePath
     * @return资源的绝对URL 
     * @throwsMalformedURLException
     */
    public static URL getExtendResource(String relativePath) throws MalformedURLException {
        if (!relativePath.contains("../")) {
            return getResource(relativePath);
        }
        String classPathAbsolutePath = getAbsolutePathOfClassLoaderClassPath();

        if (relativePath.substring(0, 1).equals("/")) {
            relativePath = relativePath.substring(1);
        }
        String wildcardString = relativePath.substring(0, relativePath.lastIndexOf("../") + 3);
        relativePath = relativePath.substring(relativePath.lastIndexOf("../") + 3);

        int containSum = containSum(wildcardString, "../");
        classPathAbsolutePath = FileUtil.cutLastString(classPathAbsolutePath, "/", containSum);
        String resourceAbsolutePath = classPathAbsolutePath + relativePath;
        URL resourceAbsoluteURL = new URL(resourceAbsolutePath);
        return resourceAbsoluteURL;
    }

    /**
     * @param String
     * @return URL etc: file:/D:/workspace/my_depms/WebRoot/WEB-INF/doc
     */
    public static URL getResource(String resource) {
        return FileUtil.getClassLoader().getResource(resource);
    }

    /**
     * 得到本Class所在的ClassLoader的CLASSPATH的绝对路径。URL形式的 
     * @return String
     */
    public static String getAbsolutePathOfClassLoaderClassPath() {
        return FileUtil.getClassLoader().getResource("").toString();
    }

    public static ClassLoader getClassLoader() {
        return FileUtil.class.getClassLoader();
    }

    /**
     * @param String
     * @param String
     * @return int
     */
    private static int containSum(String source, String dest) {
        int containSum = 0;
        int destLength = dest.length();
        while (source.contains(dest)) {
            containSum = containSum + 1;
            source = source.substring(destLength);
        }
        return containSum;
    }

    /**
     * @param String
     * @param String
     * @param int
     * @return String
     */
    private static String cutLastString(String source, String dest, int num) {
        // String cutSource=null;
        for (int i = 0; i < num; i++) {
            source = source.substring(0, source.lastIndexOf(dest, source.length() - 2) + 1);
        }
        return source;
    }

    /**
     * 获取uuid
     * @return
     */
    public static String getUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    /**
     * 获取文件总长度；
     * 
     * 不检测文件是否存在，失败直接抛出异常
     * @param fileFullName 文件全路径
     * @return 返回文件总长度
     */
    public static long getFileLength(String fileFullName) {

        File file = new File(fileFullName);
        if (!file.exists()) {
            throw new RuntimeException("文件不存在， fileFullName:" + fileFullName);
        }

        if (!file.isFile()) {
            throw new RuntimeException("非文件不能获取文件长度, fileFullName:" + fileFullName);
        }

        return file.length();
    }

    //	public static String saveTempFile(MultipartFile multipartFile,String type) throws Exception{
    //		String sourceFileName  = multipartFile.getOriginalFilename();
    //		//获取后缀名
    //		String suffix =  sourceFileName.substring(sourceFileName.lastIndexOf("."), sourceFileName.length());
    //		String datePrefix = DateUtil.formateSimpleDate(System.currentTimeMillis())+"/"+type;
    //		String url = getRandomFileName(suffix);
    //		File file = new File(getTempFile(), url);
    //		multipartFile.transferTo(file);
    //		return getFullPath(url);
    //	}
    //	

    /**
     * 复制文件
     * @param sourceFile
     * @param targetFile
     * @throws IOException
     */
    public static void copyFile(File sourceFile, File targetFile) throws IOException {
        BufferedInputStream inBuff = null;
        BufferedOutputStream outBuff = null;
        try {
            // 新建文件输入流并对它进行缓冲
            inBuff = new BufferedInputStream(new FileInputStream(sourceFile));
            if (!targetFile.exists()) {
                if (!targetFile.getParentFile().exists()) {
                    targetFile.getParentFile().mkdirs();
                }
                targetFile.createNewFile();
            }
            // 新建文件输出流并对它进行缓冲
            outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));

            // 缓冲数组
            byte[] b = new byte[1024 * 5];
            int len;
            while ((len = inBuff.read(b)) != -1) {
                outBuff.write(b, 0, len);
            }
            // 刷新此缓冲的输出流
            outBuff.flush();
        } finally {
            // 关闭流
            if (inBuff != null)
                inBuff.close();
            if (outBuff != null)
                outBuff.close();
        }
    }

    public static void removeFile(String filePath) {
        File targetFile = new File(filePath);
        if (targetFile.exists() && targetFile.isFile()) {
            targetFile.delete();
        }
    }

    public static void removeFile(File targetFile) {
        if (targetFile.exists() && targetFile.isFile()) {
            targetFile.delete();
        }
    }

    /**
     * 给定根目录，返回另一个文件名的相对路径，用于zip文件中的路径.
     * @param baseDir java.lang.String 根目录
     * @param realFileName java.io.File 实际的文件名
     * @return 相对文件名
     */
    public static String getZippedFileName(File baseDir, File file) {
        File temp = file;
        String zippedName = temp.getName();
        while (true) {
            temp = temp.getParentFile();
            if (temp == null)
                break;
            if (temp.equals(baseDir))
                break;
            else {
                zippedName = temp.getName() + "/" + zippedName;
            }
        }
        return zippedName;
    }

    /**
     * 取得指定目录下的所有文件列表，包括子目录.
     * @param baseDir File 指定的目录
     * @return 包含java.io.File的List
     */
    public static List<File> getSubFiles(File baseDir) {
        List<File> result = new ArrayList<File>();
        File[] tmp = baseDir.listFiles();
        for (int i = 0; i < tmp.length; i++) {
            if (tmp[i].isFile()) {
                result.add(tmp[i]);
            }
            if (tmp[i].isDirectory()) {
                result.addAll(getSubFiles(tmp[i]));
            }
        }
        return result;
    }

    public static void mkdirs(String parentDir, String childDir) {
        File dir = new File(parentDir, childDir);
        if (dir.isFile()) {
            throw new RuntimeException(dir.getAbsolutePath() + "不是一个目录");
        }
        //如果不存在
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public static void mkdirs(String dirPath) {
        File dir = new File(dirPath);
        if (dir.isFile()) {
            throw new RuntimeException(dirPath + "不是一个目录");
        }
        //如果不存在
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    /**
     * 获取文件的后缀名
     * @param fileName
     * @return
     */
    public static String getFileSuffix(String fileName) {
        if (StringUtils.isBlank(fileName)) {
            return null;
        }
        if (fileName.lastIndexOf(".") == -1) {
            return null;
        }
        String suffix = fileName.substring(fileName.lastIndexOf("."), fileName.length());
        return suffix;
    }

    /**
     * 压缩多个文件为zip
     * @param zipFile
     * @param files
     * @throws IOException
     */
    public void zipCompress(File zipFile, File[] files) throws IOException {
        byte[] buffer = new byte[1024];
        //生成的ZIP文件名
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFile));
        for (int i = 0; i < files.length; i++) {

            FileInputStream fis = new FileInputStream(files[i]);

            out.putNextEntry(new ZipEntry(files[i].getName()));

            int len;

            //读入需要下载的文件的内容，打包到zip文件   

            while ((len = fis.read(buffer)) > 0) {

                out.write(buffer, 0, len);

            }

            out.closeEntry();

            fis.close();

        }
        out.close();
    }

    /**
     * 将字符串追加到文件内容
     * @param str
     * @param file
     */
    public static void appendFileContentByString(String str, File file) {
        FileWriter writer = null;
        try {
            // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件     
            writer = new FileWriter(file, true);
            writer.write(str);
        } catch (IOException e) {
            throw new RuntimeException("写入文本内容到文件失败", e);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e1) {
                }
            }
        }
    }

    /**
     * 将字符串追加到文件内容
     * @param str
     * @param filePath
     */
    public static void appendFileContentByString(String str, String filePath) {
        appendFileContentByString(str, new File(filePath));
    }

    public static String readFile(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        try {
            StringBuffer contentBuffer = new StringBuffer();
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            // 一次读入一行，直到读入null为文件结束  
            while ((tempString = reader.readLine()) != null) {
                // 显示行号  
                contentBuffer.append(tempString);
            }
            reader.close();
            return contentBuffer.toString();
        } catch (IOException e) {
            return null;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
    }

    /**
     * 读取文件到byte数组
     * 
     * @param fileName
     * @return
     * @throws IOException
     */
    public static byte[] readFileBytes(String fileName) throws IOException {
        File file = new File(fileName);
        long fileSize = file.length();
        if (fileSize > Integer.MAX_VALUE) {
            return null;
        }
        FileInputStream fi = new FileInputStream(file);
        byte[] buffer = new byte[(int) fileSize];
        int offset = 0;
        int numRead = 0;
        while (offset < buffer.length
               && (numRead = fi.read(buffer, offset, buffer.length - offset)) >= 0) {
            offset += numRead;
        }
        // 确保所有数据均被读取  
        if (offset != buffer.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }
        fi.close();
        return buffer;
    }

}
