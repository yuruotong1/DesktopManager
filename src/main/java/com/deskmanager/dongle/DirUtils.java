package com.deskmanager.dongle;

import javax.swing.filechooser.FileSystemView;
import java.io.File;

public class DirUtils {
    /**
     * 获取桌面路径
     */
    public static String DesktopPath() {
        File desktopDir = FileSystemView.getFileSystemView().getHomeDirectory();
        return desktopDir.getAbsolutePath();
    }


}
