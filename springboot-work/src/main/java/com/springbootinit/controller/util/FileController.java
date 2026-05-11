package com.springbootinit.controller.util;

import com.springbootinit.common.BaseResponse;
import com.springbootinit.common.ErrorCode;
import com.springbootinit.common.ResultUtils;
import com.springbootinit.model.domain.ImageHost;
import com.springbootinit.service.ImageHostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Api(tags = "文件管理接口")
@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private ImageHostService imageHostService;

    @Value("${app.upload.path}")
    private String uploadPath;

    @ApiOperation(value = "图片上传", notes = "上传图片到图床，返回图片URL和Markdown格式")
    @PostMapping("/upload")
    public BaseResponse<Map<String, Object>> upload(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        if (file.isEmpty()) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "文件不能为空");
        }

        String originalName = file.getOriginalFilename();
        if (originalName == null || originalName.trim().isEmpty()) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "文件名不能为空");
        }

        String suffix = "";
        int dotIndex = originalName.lastIndexOf(".");
        if (dotIndex > 0) {
            suffix = originalName.substring(dotIndex);
        }

        String storageName = UUID.randomUUID().toString().replace("-", "") + suffix;

        try {
            Path storagePath = Paths.get(uploadPath, storageName);
            Files.createDirectories(storagePath.getParent());
            file.transferTo(storagePath.toFile());

            ImageHost imageHost = new ImageHost();
            imageHost.setOriginalName(originalName);
            imageHost.setStoragePath(storagePath.toString());
            imageHost.setFileSize(file.getSize());
            imageHost.setCreateTime(new Date());
            imageHostService.save(imageHost);

            String baseUrl = getBaseUrl(request);
            String imageUrl = baseUrl + "/uploads/" + storageName;

            String markdown = String.format("![%s](%s)", originalName, imageUrl);

            Map<String, Object> result = new HashMap<>();
            result.put("id", imageHost.getId());
            result.put("url", imageUrl);
            result.put("markdown", markdown);
            result.put("fileName", storageName);
            result.put("originalName", originalName);

            return ResultUtils.success(result);
        } catch (IOException e) {
            return ResultUtils.error(ErrorCode.SYSTEM_ERROR, "文件保存失败: " + e.getMessage());
        }
    }

    @ApiOperation(value = "图片列表", notes = "获取所有已上传的图片")
    @GetMapping("/list")
    public BaseResponse<Map<String, Object>> list(HttpServletRequest request) {
        List<ImageHost> imageList = imageHostService.list();
        String baseUrl = getBaseUrl(request);

        List<Map<String, Object>> records = new ArrayList<>();
        for (ImageHost image : imageList) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", image.getId());
            item.put("delname", getFileName(image.getStoragePath()));
            item.put("url", baseUrl + "/uploads/" + getFileName(image.getStoragePath()));
            item.put("title", image.getOriginalName());
            item.put("size", image.getFileSize());
            item.put("createTime", image.getCreateTime());
            records.add(item);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("records", records);
        result.put("total", records.size());
        result.put("current", 1);
        result.put("pageSize", records.size());

        return ResultUtils.success(result);
    }

    @ApiOperation(value = "删除图片", notes = "根据文件名删除图片")
    @DeleteMapping("/delete")
    public BaseResponse<Boolean> delete(@RequestBody List<String> delnames) {
        if (delnames == null || delnames.isEmpty()) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "文件名不能为空");
        }

        List<ImageHost> allImages = imageHostService.list();
        List<Integer> deletedIds = new ArrayList<>();

        for (String delname : delnames) {
            for (ImageHost image : allImages) {
                if (getFileName(image.getStoragePath()).equals(delname)) {
                    try {
                        Path filePath = Paths.get(image.getStoragePath());
                        Files.deleteIfExists(filePath);
                    } catch (IOException e) {
                        // ignore delete error
                    }
                    deletedIds.add(image.getId());
                    break;
                }
            }
        }

        if (!deletedIds.isEmpty()) {
            imageHostService.removeByIds(deletedIds);
        }

        return ResultUtils.success(true);
    }

    private String getFileName(String storagePath) {
        if (storagePath == null) return "";
        int lastSepIndex = storagePath.lastIndexOf(File.separator);
        if (lastSepIndex >= 0) {
            return storagePath.substring(lastSepIndex + 1);
        }
        return storagePath;
    }

    private String getBaseUrl(HttpServletRequest request) {
        String scheme = request.getScheme();
        String host = request.getServerName();
        int port = request.getServerPort();
        String contextPath = request.getContextPath();

        if (port == 80 || port == 443) {
            return scheme + "://" + host + contextPath;
        }
        return scheme + "://" + host + ":" + port + contextPath;
    }
}