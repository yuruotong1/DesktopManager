该项目可根据配置文件规则整理桌面，实现桌面目录自动分类功能。


## 配置文件说明



```yaml
targetBaseDir: D:\BaiduNetdiskWorkspace
sourceBaseDir: null
rule:
  - dirName: img
    regualr:
      - .*\.jpg$
      - .*\.png$
  - dirName: 
    regular: 
      - tmp
excludeRules:
  - ^BaiduNetdiskWorkspace.*
```

### targetBaseDir
目标目录，将源目录中的「文件或目录」移动到「目标目录或其子目录下」。

### sourceBaseDir
源目录，若为 null，表示桌面。

### rule 

配置移动规则，比如下例：
```yaml
rule:
  - dirName: img
    regualr:
      - .*\.jpg$
      - .*\.png$
```
如果源对象命中 regular 正则规则，则会将源对象移动到「目标目录/img」下。如果想直接移动到「目标目录」下，可以`$`：
```yaml
rule:
  - dirName: $
    regualr:
      - .*\.jpg$
      - .*\.png$
```


### excludeRules

不移动的文件，比如下面配置表示不移动符合规则`^BaiduNetdiskWorkspace.*`的文件或目录：

```yaml
excludeRules:
  - ^BaiduNetdiskWorkspace.*
```