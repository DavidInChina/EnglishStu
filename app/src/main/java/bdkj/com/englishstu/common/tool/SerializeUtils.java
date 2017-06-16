/*
 * Copyright (c) 2015.
 * All Rights Reserved.
 */

package bdkj.com.englishstu.common.tool;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 序列号操作的工具类
 */
public class SerializeUtils {

    /**
     * 禁止构造
     */
    private SerializeUtils() {
        throw new AssertionError();
    }

    /**
     * 从文件中读取一个实现了{@link java.io.Serializable}接口的Object对象
     *
     * @param file 文件路径
     * @return
     */
    public static Object readObject(File file) {
        Object obj = null;
        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(new FileInputStream(file));
            obj = in.readObject();
            in.close();
        } catch (FileNotFoundException | ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return obj;
    }

    /**
     * 向文件中写入实现了{@link java.io.Serializable}接口的Object对象
     *
     * @param file 文件的路径
     * @param obj  实现了{@link java.io.Serializable}接口的Object对象
     * @return
     */
    public static boolean writeObject(File file, Object obj) {
        ObjectOutputStream out = null;
        boolean isSuccess = false;
        try {
            out = new ObjectOutputStream(new FileOutputStream(file));
            out.writeObject(obj);
            out.close();
            isSuccess = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return isSuccess;
    }

    /**
     * 删除文件{@link java.io.Serializable}接口的Object对象
     *
     * @param file 文件的路径
     * @return
     */
    public static boolean deleteObject(File file) {
        boolean isSuccess = false;
        if (file.exists()) { // 判断文件是否存在
            if (file.isFile()) { // 判断是否是文件
                isSuccess = file.delete(); // delete()方法 你应该知道 是删除的意思;
            } else if (file.isDirectory()) { // 否则如果它是一个目录
                File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
                for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
                    deleteObject(files[i]); // 把每个文件 用这个方法进行迭代
                }
            }
        }
        return isSuccess;
    }
}
