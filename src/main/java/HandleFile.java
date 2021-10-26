import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

public class HandleFile {
    public boolean deleteDir(File dir) {
        // 如果是文件夹
        if (dir.isDirectory()) {
            String[] children = dir.list();
            // 递归删除目录中的子目录下
            for (int i = 0; i < Objects.requireNonNull(children).length; i++) {
                boolean isDelete = deleteDir(new File(dir, children[i]));
                // 如果删完了，没东西删，isDelete==false的时候，则跳出此时递归
                if (!isDelete) {
                    return false;
                }
            }
        }
        // 读到的是一个文件或者是一个空目录，则可以直接删除
        return dir.delete();
    }

    public void copyFolder(String oldPath, String newPath) {
        // 如果文件夹不存在，则建立新文件夹
        File newFile = new File(newPath);
        if (!newFile.exists()) {
            boolean mkdirsResult = newFile.mkdirs();
            if (!mkdirsResult) {
                System.out.println("create dir '" + newFile.getName() + "'not successful.");
            }
        }

        // 递归去遍历目录
        File oldFile = new File(oldPath);
        if (oldFile.isFile()) movFile(oldFile, newPath);
        else if (oldFile.isDirectory()) {
            for (File file : Objects.requireNonNull(oldFile.listFiles())) {
                copyFolder(oldPath + File.separator + file.getName(), newPath + File.separator + oldFile.getName());
            }
        }

    }

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