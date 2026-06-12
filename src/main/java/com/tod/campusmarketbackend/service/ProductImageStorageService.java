package com.tod.campusmarketbackend.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

/**
 * 商品图片存储服务
 * 负责校验图片并保存到代码目录之外的上传目录
 */
@Service
public class ProductImageStorageService {

    private static final long MAX_FILE_SIZE = 5L * 1024 * 1024;
    private static final Set<String> ALLOWED_EXTENSIONS = Set.of("jpg", "jpeg", "png", "webp");
    private static final Set<String> ALLOWED_CONTENT_TYPES =
            Set.of("image/jpeg", "image/png", "image/webp");
    private static final String SERVER_UPLOAD_DIRECTORY =
            "/home/ubuntu/campus-lifestyle-platform/uploads/product-images/";

    private final Path uploadDirectory;

    public ProductImageStorageService() {
        this.uploadDirectory = resolveUploadDirectory();
        createUploadDirectory();
    }

    /**
     * 保存单张商品图片并返回服务器生成的新文件名
     */
    public String store(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "请选择要上传的图片");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "图片大小不能超过 5MB");
        }

        String extension = getFileExtension(file.getOriginalFilename());
        String contentType = file.getContentType();
        if (!ALLOWED_EXTENSIONS.contains(extension)
                || contentType == null
                || !ALLOWED_CONTENT_TYPES.contains(contentType.toLowerCase(Locale.ROOT))) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "只允许上传 jpg、jpeg、png、webp 格式的图片"
            );
        }

        String savedFileName = UUID.randomUUID() + "." + extension;
        Path targetFile = uploadDirectory.resolve(savedFileName).normalize();

        if (!targetFile.getParent().equals(uploadDirectory)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "图片文件名不合法");
        }

        try {
            Files.copy(file.getInputStream(), targetFile, StandardCopyOption.REPLACE_EXISTING);
            return savedFileName;
        } catch (IOException exception) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "图片保存失败，请稍后重试",
                    exception
            );
        }
    }

    /**
     * WebMvcConfig 使用这个路径建立静态资源映射
     */
    public Path getUploadDirectory() {
        return uploadDirectory;
    }

    private Path resolveUploadDirectory() {
        // 服务器可以通过环境变量覆盖目录，便于以后调整部署位置
        String configuredDirectory = System.getenv("PRODUCT_IMAGE_UPLOAD_DIR");
        if (configuredDirectory != null && !configuredDirectory.isBlank()) {
            return Paths.get(configuredDirectory).toAbsolutePath().normalize();
        }

        // Windows 本地开发时保存到项目 uploads 目录，Ubuntu 服务器使用固定部署目录
        String osName = System.getProperty("os.name", "").toLowerCase(Locale.ROOT);
        if (osName.contains("win")) {
            return Paths.get(System.getProperty("user.dir"), "uploads", "product-images")
                    .toAbsolutePath()
                    .normalize();
        }

        return Paths.get(SERVER_UPLOAD_DIRECTORY).toAbsolutePath().normalize();
    }

    private void createUploadDirectory() {
        try {
            Files.createDirectories(uploadDirectory);
        } catch (IOException exception) {
            throw new IllegalStateException("无法创建商品图片上传目录：" + uploadDirectory, exception);
        }
    }

    private String getFileExtension(String originalFileName) {
        if (originalFileName == null || !originalFileName.contains(".")) {
            return "";
        }

        return originalFileName
                .substring(originalFileName.lastIndexOf('.') + 1)
                .toLowerCase(Locale.ROOT);
    }
}
