//: DeskManager.java
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;


/** 入口类
 * 桌面整理工具，可以一键将文件按规则分类放置
 * @author 年叶
 * @version 1.0
 * */
public class DeskManager {
    String targetBaseDir;
    String sourceBaseDir = DirUtils.DesktopPath();
    // 分类规则
    List<?> classifyRules;
    List<?> excludeRules;

    public DeskManager() {
        loadFromYaml();
    }

    /** <p>检测 file 是否命中某个 rule 规则，若命中返回其所属目录
     * <p>比如 a.png 会命中 .*\.png$，返回 img，配置内容如下：
     * <p><pre class="code">
     * subDir: img
     * regualr:
     * - .*\.jpg$
     * - .*\.png$
     * </pre>
     * @param fileName 文件名
     * */
    public String getDirNameByRule(String fileName) {

        for (Object ruleObject : classifyRules) {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> ruleMap = objectMapper.convertValue(ruleObject, new TypeReference<Map<String, Object>>() {
            });
            List<?> rules = (List<?>) ruleMap.get("regular");
            for (Object rule : rules) {
                boolean matchResult = Pattern.matches((String) rule, fileName);
                if (matchResult) {
                    return (String) ruleMap.get("dirName");
                }
            }
        }
        return null;
    }

    /** 判断文件名是否命中了黑名单（excludeRules）
     * @param fileName 文件名
     */
    public boolean isHitExcluedeRules(String fileName) {
        for (Object excludeRule : excludeRules) {
            if (Pattern.matches((String) excludeRule, fileName))
                return true;
        }
        return false;

    }
    /** 检查每一个文件（目录），根据配置文件中的 rule 规则进行分类放置
     * */
    public void DesktopFiles() {
        File[] files = new File(sourceBaseDir).listFiles();
        if (files == null) return;
        for (File file : files) {
            if (isHitExcluedeRules(file.getName())) continue;
            String dirName = getDirNameByRule(file.getName());
            String tarPath = targetBaseDir + File.separator + dirName;
            if (dirName == null) continue;
            else if (dirName.equals("$")) tarPath = targetBaseDir;
            String sourcePath = sourceBaseDir + File.separator + file.getName();
            HandleFile handleFile = new HandleFile();
            handleFile.moveFolder(sourcePath, tarPath);
        }
    }

    /** 从 config.yaml 中加载初始配置文件 */
    public void loadFromYaml() {
        Yaml yaml = new Yaml();
        Map<String, Object> data = yaml.load(this.getClass().getClassLoader().getResourceAsStream("config.yaml"));
        targetBaseDir = (String) data.get("targetBaseDir");
        if (data.get("sourceBaseDir") != null) {
            sourceBaseDir = (String) data.get("sourceBaseDir");
        }
        if (data.get("rule") != null) {
            classifyRules = (List<?>) data.get("rule");
        }
        if (data.get("excludeRules") != null) {
            excludeRules = (List<?>) data.get("excludeRules");
        }
    }

    public static void main(String[] args) {
        DeskManager deskManager = new DeskManager();
        deskManager.DesktopFiles();

    }
}