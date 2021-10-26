import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;


public class DeskManager {
    String targetBaseDir;
    String sourceBaseDir = DirUtils.DesktopPath();
    // 分类规则
    List<?> classifyRules;
    List<?> excludeRules;

    public DeskManager() {
        loadFromYaml();

    }

    private String getDirNameByRule(String fileName) {
        /*
        检测 file 是否命中某个 rule 规则，若命中返回其所属目录
        比如 a.png 会命中 .*\.png$，返回 img
        subDir: img
        regualr:
          - .*\.jpg$
          - .*\.png$
         */

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

    //判断是否命中了黑名单
    public boolean isHitExcluedeRules(String fileName) {
        for (Object excludeRule : excludeRules) {
            if (Pattern.matches((String) excludeRule, fileName))
                return true;
        }
        return false;

    }

    public void DesktopFiles() {
        // 检查每一个文件（目录），根据配置文件中的 rule 规则进行分类放置
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