# 基于Spring Boot + Vue 3的图片托管系统技术文档

## 1. 项目概述

本项目是一个完整的图片托管系统（图床），采用前后端分离架构，支持图片上传、管理、AI识别和Markdown代码生成等功能。系统使用Spring Boot作为后端框架，Vue 3作为前端框架，实现本地图片存储和智能化管理。

## 2. 数据库设计

### 2.1 数据库表结构

```
CREATE TABLE `image_host` (
  `id` INT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
  `original_name` VARCHAR(255) NOT NULL COMMENT '原始文件名',
  `storage_path` VARCHAR(500) NOT NULL COMMENT '服务器本地存储路径',
  `file_size` BIGINT COMMENT '文件大小(字节)',
  `ai_tags` VARCHAR(500) COMMENT 'AI识别出的标签，逗号分隔',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '上传时间',
  UNIQUE KEY `idx_path` (`storage_path`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='图床图片信息表';
```

### 2.2 字段说明

| 字段名 | 数据类型 | 是否为空 | 说明 |
| ------ |------ |------ |------ |
| id | INT | NOT NULL | 主键，自增 |
| original_name | VARCHAR(255) | NOT NULL | 上传时的原始文件名 |
| storage_path | VARCHAR(500) | NOT NULL | 图片在服务器的存储路径 |
| file_size | BIGINT | YES | 文件大小，单位字节 |
| ai_tags | VARCHAR(500) | YES | AI识别的标签，逗号分隔 |
| create_time | DATETIME | YES | 记录创建时间 |

## 3. 后端实现 (Spring Boot)

### 3.1 项目配置

**application.yml**

```
server:
  port: 8080
spring:
  servlet:
    multipart:
      max-file-size: 10MB
      max-request-size: 100MB
app:
  upload:
    path: ${user.home}/my-image-host/ # 图片保存路径
```

### 3.2 核心代码实现

**实体类**

```
@Data
public class ImageHost {
    private Integer id;
    private String originalName;
    private String storagePath;
    private Long fileSize;
    private String aiTags;
    private LocalDateTime createTime;
}
```

**Mapper接口**

```
@Mapper
public interface ImageHostMapper {
    @Insert("INSERT INTO image_host(original_name, storage_path, file_size, ai_tags) VALUES(#{originalName}, #{storagePath}, #{fileSize}, #{aiTags})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(ImageHost imageHost);
    
    @Select("SELECT * FROM image_host ORDER BY create_time DESC")
    List<ImageHost> findAll();
}
```

**控制器**

```
@RestController
@RequestMapping("/api/images")
public class ImageController {

    @Value("${app.upload.path}")
    private String uploadPath;

    @Autowired
    private ImageHostMapper imageHostMapper;

    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> upload(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) return ResponseEntity.badRequest().build();

        // 生成唯一文件名
        String originalName = file.getOriginalFilename();
        String suffix = originalName.substring(originalName.lastIndexOf("."));
        String storageName = UUID.randomUUID() + suffix;
        
        // 保存到本地服务器
        Path storagePath = Paths.get(uploadPath, storageName);
        Files.createDirectories(storagePath.getParent());
        file.transferTo(storagePath);

        // 存入数据库
        ImageHost imageHost = new ImageHost();
        imageHost.setOriginalName(originalName);
        imageHost.setStoragePath(storagePath.toString());
        imageHost.setFileSize(file.getSize());
        imageHostMapper.insert(imageHost);

        // 返回可访问的URL和MD代码
        String imageUrl = "http://localhost:8080/uploads/" + storageName;
        String markdown = String.format("

![%s](%s)

", originalName, imageUrl);

        Map<String, Object> result = new HashMap<>();
        result.put("url", imageUrl);
        result.put("markdown", markdown);
        return ResponseEntity.ok(result);
    }

    @GetMapping
    public List<ImageHost> list() {
        return imageHostMapper.findAll();
    }
}
```

**静态资源映射**

```
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Value("${app.upload.path}")
    private String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploadPath + "/");
    }
}
```

## 4. 前端实现 (Vue 3)

### 4.1 组件代码

```
<template>
  <div class="image-host-container">
    <!-- 上传区域 -->
    <el-upload
      drag
      action="http://localhost:8080/api/images/upload"
      name="file"
      :on-success="handleSuccess"
      multiple
    >
      <el-icon class="el-icon--upload"><upload-filled /></el-icon>
      <div class="el-upload__text">拖拽文件到此处，或 <em>点击上传</em></div>
    </el-upload>

    <!-- 图片展示与MD复制 -->
    <div class="image-list" v-if="imageList.length">
      <el-card v-for="img in imageList" :key="img.id" class="image-card">
        <img :src="`http://localhost:8080/uploads/${getFileName(img.storagePath)}`" alt="图片" />
        <div class="md-box">
          <el-input :model-value="generateMD(img)" readonly size="small">
            <template #append>
              <el-button @click="copyMD(generateMD(img))">复制MD</el-button>
            </template>
          </el-input>
          <el-tag v-if="img.aiTags" type="success" size="small">AI: </el-tag>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import { ElMessage } from 'element-plus'
import { UploadFilled } from '@element-plus/icons-vue'

const imageList = ref([])

// 获取图片列表
const fetchImages = async () => {
  const res = await axios.get('http://localhost:8080/api/images')
  imageList.value = res.data
}

// 上传成功回调
const handleSuccess = (response) => {
  ElMessage.success('上传成功！')
  fetchImages() // 刷新列表
}

// 生成Markdown
const generateMD = (img) => {
  const fileName = getFileName(img.storagePath)
  const url = `http://localhost:8080/uploads/${fileName}`
  return `

![${img.originalName}](${url})

`
}

// 辅助：从路径中提取文件名
const getFileName = (path) => path.split(/[\\/]/).pop()

// 复制MD到剪贴板
const copyMD = (text) => {
  navigator.clipboard.writeText(text)
  ElMessage.success('Markdown已复制！')
}

onMounted(() => {
  fetchImages()
})
</script>

<style scoped>
.image-list { 
  display: flex; 
  flex-wrap: wrap; 
  gap: 20px; 
  margin-top: 20px; 
}
.image-card { 
  width: 250px; 
}
.image-card img { 
  width: 100%; 
  height: 180px; 
  object-fit: cover; 
  border-radius: 4px; 
}
.md-box { 
  margin-top: 10px; 
}
</style>
```

## 5. AI功能集成方案

### 5.1 云端API方案

**推荐服务**：

- 百度文心一言图像理解API
- 阿里通义千问视觉API
- 火山引擎图像识别

**集成方式**：

1. 在后端`callAIService`方法中调用云端API
2. 将图片转换为Base64或提供临时URL
3. AI平台返回JSON格式的标签信息
4. 存储到数据库的`ai_tags`字段

### 5.2 本地Python服务方案

**技术栈**：

- Python FastAPI框架
- 开源CLIP模型
- Stable Diffusion

**部署方式**：

1. 独立部署Python微服务
2. Spring Boot通过HTTP内网调用
3. 实现完全离线的AI识别

## 6. 系统架构总结

### 6.1 技术架构图

```
+----------------+       +----------------+       +----------------+
|   Vue 3前端    |<----->|  Spring Boot   |<----->|    MySQL数据库  |
|   (图片展示)   | HTTP  |   (业务逻辑)   | JDBC  |   (数据存储)   |
+----------------+       +----------------+       +----------------+
                             |    ^
                             |    |
                      +------------------+
                      |   AI服务 (可选)  |
                      +------------------+
```

### 6.2 核心功能流程

1. **图片上传流程**：
前端上传 → 后端接收 → 保存本地 → 存入数据库 → 返回URL/MD
2. **AI识别流程**：
图片上传 → 调用AI服务 → 获取标签 → 存储标签 → 前端展示
3. **图片访问流程**：
静态资源映射 → 直接访问本地文件 → 浏览器显示图片

## 7. 部署建议

### 7.1 环境要求

**后端**：

- Java 11+
- Maven 3.6+
- MySQL 5.7+

**前端**：

- Node.js 14+
- Vue 3
- Element Plus

### 7.2 部署步骤

1. 创建数据库并执行建表SQL
2. 配置Spring Boot的上传路径
3. 启动后端服务
4. 安装前端依赖并启动
5. 访问前端页面使用系统

## 8. 扩展功能建议

1. **用户系统**：增加用户注册登录，实现个人图床
2. **图片分类**：支持文件夹分类管理
3. **权限控制**：不同用户权限管理
4. **CDN加速**：对接云存储和CDN服务
5. **批量操作**：支持图片批量删除、导出
6. **统计分析**：图片数量、存储空间统计

## 9. 安全注意事项

1. **文件类型校验**：严格限制上传文件类型
2. **文件大小限制**：防止大文件攻击
3. **XSS防护**：防止恶意脚本注入
4. **权限验证**：确保操作合法性
5. **日志监控**：记录关键操作日志

## 10. 总结

本技术文档详细描述了一个基于Spring Boot + Vue 3的图片托管系统的完整实现方案，包括数据库设计、后端代码、前端界面和AI集成方案。系统具有良好的扩展性和实用性，可作为个人或企业图床使用，并可根据需求进一步扩展功能。

通过本方案的实施，可以快速搭建一个功能完善的图片托管平台，满足日常图片管理和智能化处理的需求。

