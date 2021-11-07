package com.deskmanager.dongle;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;


public class HandleFile {

    /**
     * 递归删除指定目录下的文件或目录
     */
    public boolean deleteDir(File dir) {
        // 如果是文件夹
        if (dir.isDirectory()) {
            String[] children = dir.list();
            // 递归删除目录中的子目录下
            for (String child : children != null ? children : new String[0]) {
                boolean isDelete = deleteDir(new File(dir, child));
                // 如果删完了，没东西删，isDelete==false的时候，则跳出此时递归
                if (!isDelete) {
                    return false;
                }
            }
        }
        // 读到的是一个文件或者是一个空目录，则可以直接删除
        return dir.delete();
    }

    public void delNotUpdateDir(String path, long delDate) {
        File newFile = new File(path);
        if (newFile.isDirectory()) {
            long lastModified = newFile.lastModified();
            Date lastModifiedDate = new Date(lastModified);
            Date nowDate = new Date();
            long diffDateLong = nowDate.getTime() - lastModifiedDate.getTime();
            long diffDay = diffDateLong / 1000 * 24 * 60 * 60;
            if (diffDay >= delDate) {


            }

        }

    }

    public void showDialogDeleteFile(File[] files) {
        StringBuilder fileNames = new StringBuilder();
        for (File file : files) {
            fileNames.append("\n").append(file.getName());
        }
        String message = "确定要删除以下内容吗？\n" + fileNames;
        int options = JOptionPane.showConfirmDialog(null, message, "删除提示", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
        if (options == JOptionPane.YES_OPTION) {
            for (File file : files) {
                boolean res = file.delete();
                System.out.println(file.getName() + " delete " + res);
            }

        }

    }

    /**
     * 从源路径复制文件到新路径
     */
    public void copyFolder(String oldPath, String newPath) {
        // 如果文件夹不存在，则建立新文件夹
        File newFile = new File(newPath);
        if (!newFile.exists()) {
            boolean mkdirsResult = newFile.mkdirs();
            if (!mkdirsResult) {
                System.out.println("create dir '" + newFile.getName() + "'not successful.");
            }
        }

        // 递归遍历目录
        File oldFile = new File(oldPath);
        if (oldFile.isFile()) movFile(oldFile, newPath);
        else if (oldFile.isDirectory()) {
            File[] files = oldFile.listFiles();
            if (files != null) {
                for (File file : files) {
                    copyFolder(oldPath + File.separator + file.getName(), newPath + File.separator + oldFile.getName());
                }
            }

        }

    }

    /**
     * 从源路径移动文件或目录到新路径
     */
    public void movFile(File oldFile, String targetPath) {
        FileInputStream input;
        try {
            input = new FileInputStream(oldFile);
            // 复制并且改名
            FileOutputStream output = new FileOutputStream(targetPath
                    + File.separator + "rename_" + (oldFile.getName()));
            byte[] bufferarray = new byte[1024 * 64];
            int prereadlength;
            while ((prereadlength = input.read(bufferarray)) != -1) {
                output.write(bufferarray, 0, prereadlength);
            }
            output.flush();
            output.close();
            input.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void moveFolder(String oldPath, String newPath) {
        // 先复制文件
        copyFolder(oldPath, newPath);
        // 则删除源文件，以免复制的时候错乱
        deleteDir(new File(oldPath));
    }

}