import javax.swing.filechooser.FileSystemView;
import java.io.File;

public class DirUtils {
    public static String DesktopPath() {
        File desktopDir = FileSystemView.getFileSystemView().getHomeDirectory();
        return desktopDir.getAbsolutePath();
    }
}
