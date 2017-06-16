package bdkj.com.englishstu.common.tool;

import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;

/**
 * Created by younminx on 2017/2/21.
 * 文件工具类
 */

public class FileUtil {
    /**
     * The constant FILE_SUFFIX_SEPARATOR.
     */
    public final static String FILE_SUFFIX_SEPARATOR = ".";

    /**
     * Read file
     *
     * @param filePath    the file path
     * @param charsetName the charset name
     * @return string builder
     */
    public static StringBuilder readFile(String filePath, String charsetName) {
        File file = new File(filePath);
        StringBuilder fileContent = new StringBuilder("");
        if (file == null || !file.isFile()) {
            return null;
        }

        BufferedReader reader = null;
        try {
            InputStreamReader is = new InputStreamReader(new FileInputStream(file), charsetName);
            reader = new BufferedReader(is);
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (!fileContent.toString().equals("")) {
                    fileContent.append("\r\n");
                }
                fileContent.append(line);
            }
            return fileContent;
        } catch (IOException e) {
            throw new RuntimeException("IOException", e);
        } finally {
            closeQuietly(reader);
        }
    }

    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                // Ignored
            }
        }
    }

    /**
     * Write file
     *
     * @param filePath the file path
     * @param content  the content
     * @param append   the append
     * @return boolean
     */
    public static boolean writeFile(String filePath, String content, boolean append) {
        if (TextUtils.isEmpty(content)) {
            return false;
        }
        FileWriter fileWriter = null;
        try {
            makeDirs(filePath);
            fileWriter = new FileWriter(filePath, append);
            fileWriter.write(content);
            return true;
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            closeQuietly(fileWriter);
        }
    }

    /**
     * write file, the string will be written to the begin of the file
     *
     * @param filePath the file path
     * @param content  the content
     * @return boolean
     */
    public static boolean writeFile(String filePath, String content) {
        return writeFile(filePath, content, false);
    }

    /**
     * Write file
     *
     * @param filePath the file path
     * @param is       the is
     * @return boolean
     */
    public static boolean writeFile(String filePath, InputStream is) {
        return writeFile(filePath, is, false);
    }

    /**
     * Write file
     *
     * @param filePath the file path
     * @param is       the is
     * @param append   the append
     * @return boolean
     */
    public static boolean writeFile(String filePath, InputStream is, boolean append) {
        return writeFile(filePath != null ? new File(filePath) : null, is, append);
    }

    /**
     * Write file
     *
     * @param file the file
     * @param is   the is
     * @return boolean
     */
    public static boolean writeFile(File file, InputStream is) {
        return writeFile(file, is, false);
    }

    /**
     * Write file
     *
     * @param file   the file
     * @param is     the is
     * @param append the append
     * @return boolean
     */
    public static boolean writeFile(File file, InputStream is, boolean append) {
        OutputStream o = null;
        try {
            makeDirs(file.getAbsolutePath());
            o = new FileOutputStream(file, append);
            byte data[] = new byte[1024];
            int length = -1;
            while ((length = is.read(data)) != -1) {
                o.write(data, 0, length);
            }
            o.flush();
            return true;
        } catch (FileNotFoundException e) {
            throw new RuntimeException("FileNotFoundException", e);
        } catch (IOException e) {
            throw new RuntimeException("IOException", e);
        } finally {
            closeQuietly(o);
            closeQuietly(is);
        }
    }

    /**
     * Move file
     *
     * @param srcFilePath  the src file path
     * @param destFilePath the dest file path
     * @throws FileNotFoundException the file not found exception
     */
    public static void moveFile(String srcFilePath, String destFilePath) throws FileNotFoundException {
        if (TextUtils.isEmpty(srcFilePath) || TextUtils.isEmpty(destFilePath)) {
            throw new RuntimeException("Both srcFilePath and destFilePath cannot be null.");
        }
        moveFile(new File(srcFilePath), new File(destFilePath));
    }

    /**
     * Move file
     *
     * @param srcFile  the src file
     * @param destFile the dest file
     * @throws FileNotFoundException the file not found exception
     */
    public static void moveFile(File srcFile, File destFile) throws FileNotFoundException {
        boolean rename = srcFile.renameTo(destFile);
        if (!rename) {
            copyFile(srcFile.getAbsolutePath(), destFile.getAbsolutePath());
            deleteFile(srcFile.getAbsolutePath());
        }
    }

    /**
     * Copy file
     *
     * @param srcFilePath  the src file path
     * @param destFilePath the dest file path
     * @return boolean
     * @throws FileNotFoundException the file not found exception
     */
    public static boolean copyFile(String srcFilePath, String destFilePath) throws FileNotFoundException {
        InputStream inputStream = new FileInputStream(srcFilePath);
        return writeFile(destFilePath, inputStream);
    }

    /**
     * rename file
     *
     * @param file        the file
     * @param newFileName the new file name
     * @return boolean
     */
    public static boolean renameFile(File file, String newFileName) {
        File newFile = null;
        if (file.isDirectory()) {
            newFile = new File(file.getParentFile(), newFileName);
        } else {
            String temp = newFileName + file.getName().substring(file.getName().lastIndexOf('.'));
            newFile = new File(file.getParentFile(), temp);
        }
        if (file.renameTo(newFile)) {
            return true;
        }
        return false;
    }

    /**
     * Get file name without suffix
     *
     * @param filePath the file path
     * @return file name without suffix
     */
    public static String getFileNameWithoutSuffix(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return filePath;
        }
        int suffix = filePath.lastIndexOf(FILE_SUFFIX_SEPARATOR);
        int fp = filePath.lastIndexOf(File.separator);
        if (fp == -1) {
            return (suffix == -1 ? filePath : filePath.substring(0, suffix));
        }
        if (suffix == -1) {
            return filePath.substring(fp + 1);
        }
        return (fp < suffix ? filePath.substring(fp + 1, suffix) : filePath.substring(fp + 1));
    }

    /**
     * Get file name
     *
     * @param filePath the file path
     * @return file name
     */
    public static String getFileName(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return filePath;
        }
        int fp = filePath.lastIndexOf(File.separator);
        return (fp == -1) ? filePath : filePath.substring(fp + 1);
    }

    /**
     * Get folder name
     *
     * @param filePath the file path
     * @return folder name
     */
    public static String getFolderName(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return filePath;
        }
        int fp = filePath.lastIndexOf(File.separator);
        return (fp == -1) ? "" : filePath.substring(0, fp);
    }

    /**
     * Get suffix of file
     *
     * @param filePath the file path
     * @return file suffix
     */
    public static String getFileSuffix(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return filePath;
        }
        int suffix = filePath.lastIndexOf(FILE_SUFFIX_SEPARATOR);
        int fp = filePath.lastIndexOf(File.separator);
        if (suffix == -1) {
            return "";
        }
        return (fp >= suffix) ? "" : filePath.substring(suffix + 1);
    }

    /**
     * Create the directory
     *
     * @param filePath the file path
     * @return boolean
     */
    public static boolean makeDirs(String filePath) {
        String folderName = getFolderName(filePath);
        if (TextUtils.isEmpty(folderName)) {
            return false;
        }
        File folder = new File(folderName);
        return (folder.exists() && folder.isDirectory()) ? true : folder.mkdirs();
    }

    /**
     * Judge whether a file is exist
     *
     * @param filePath the file path
     * @return boolean
     */
    public static boolean isFileExist(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return false;
        }
        File file = new File(filePath);
        return (file.exists() && file.isFile());
    }

    /**
     * Judge whether a folder is exist
     *
     * @param directoryPath the directory path
     * @return boolean
     */
    public static boolean isFolderExist(String directoryPath) {
        if (TextUtils.isEmpty(directoryPath)) {
            return false;
        }
        File dire = new File(directoryPath);
        return (dire.exists() && dire.isDirectory());
    }

    /**
     * Delete file or folder
     *
     * @param path the path
     * @return boolean
     */
    public static boolean deleteFile(String path) {
        if (TextUtils.isEmpty(path)) {
            return true;
        }

        File file = new File(path);
        if (!file.exists()) {
            return true;
        }
        if (file.isFile()) {
            return file.delete();
        }
        if (!file.isDirectory()) {
            return false;
        }
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                if (f.isFile()) {
                    f.delete();
                } else if (f.isDirectory()) {
                    deleteFile(f.getAbsolutePath());
                }
            }
        }
        return file.delete();
    }

    /**
     * Delete file or folder
     *
     * @param file the file
     * @return boolean
     */
    public static boolean deleteFile(File file) {
        if (!file.exists()) {
            return true;
        }
        if (file.isFile()) {
            return file.delete();
        }
        if (!file.isDirectory()) {
            return false;
        }
        if (file.isDirectory()) {
            File[] childFile = file.listFiles();
            if (childFile == null || childFile.length == 0) {
                return file.delete();
            }
            for (File f : childFile) {
                deleteFile(f);
            }
        }
        return file.delete();
    }

    /**
     * Get file size
     *
     * @param path the path
     * @return file size
     */
    public static long getFileSize(String path) {
        if (TextUtils.isEmpty(path)) {
            return -1;
        }
        File file = new File(path);
        return (file.exists() && file.isFile() ? file.length() : -1);
    }

    /**
     * Get folder size
     *
     * @param file the file
     * @return folder size
     * @throws Exception the exception
     */
    public static long getFolderSize(File file) throws Exception {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);
                } else {
                    size = size + fileList[i].length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * byte(字节)根据长度转成kb(千字节)和mb(兆字节)
     *
     * @param bytes 文件长度
     * @return 文件大小
     */
    public static String bytes2kb(double bytes) {
        BigDecimal filesize = new BigDecimal(bytes);
        BigDecimal megabyte = new BigDecimal(1024 * 1024);
        // 参数1分割单位，参数2保留位数，参数3保留方式ROUND_HALF_UP四舍五入
        float returnValue = filesize.divide(megabyte, 2, BigDecimal.ROUND_HALF_UP).floatValue();
        if (returnValue > 1)
            return (returnValue + "MB");
        BigDecimal kilobyte = new BigDecimal(1024);
        returnValue = filesize.divide(kilobyte, 2, BigDecimal.ROUND_HALF_UP).floatValue();
        return (returnValue + "KB");
    }

    /**
     * 保存文件至本地
     *
     * @param inputStream 输入流
     * @param file        文件
     * @return 保存结果
     */
    public static boolean write2Disk(InputStream inputStream, File file) {
        try {
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];
                outputStream = new FileOutputStream(file);

                while (true) {
                    int read = inputStream.read(fileReader);
                    if (read == -1) {
                        break;
                    }
                    outputStream.write(fileReader, 0, read);
                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }
}
