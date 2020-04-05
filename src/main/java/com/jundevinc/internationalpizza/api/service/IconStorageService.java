package com.jundevinc.internationalpizza.api.service;

import com.jundevinc.internationalpizza.api.utils.FileStorageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Oleg Pavlyukov
 * on 22.12.2019
 * cpabox777@gmail.com
 */
@Service
@Slf4j
public class IconStorageService {
    private static final Path ROOT_DIR = Paths.get("src/main/resources/static/icon-pizza");

    public boolean storeIcon(MultipartFile file) {
        log.info("storeIcon() is invoked");
        log.debug("MultipartFile with fileName -> " + file.getOriginalFilename());
        try {
            Files.copy(file.getInputStream(), ROOT_DIR.resolve(file.getOriginalFilename()));
            log.debug("Icon saved successfully");
            return true;
        } catch (IOException e) {
            log.error("Icon saving error", e);
            throw new RuntimeException("Icon saving error");
        }
    }

    public Resource getIcon(String filename) {
        log.info("getIcon() is invoked");
        log.debug("FileName -> " + filename);
        Path file = ROOT_DIR.resolve(filename);
        try {
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                log.error("Resource is not exist or readable");
                throw new RuntimeException("Resource is not exist or readable");
            }
        } catch (MalformedURLException e) {
            log.error("Resource creation error", e);
            throw new RuntimeException("Resource creation ex. Msg: " + e.getMessage());
        }

    }

    public boolean deleteIcon(String url) {
        log.info("deleteIcon() is invoked");
        log.debug("Icon url -> " + url);
        String fileName = FileStorageUtils.extractFileNameFromUrl(url);
        Path file = ROOT_DIR.resolve(fileName);
        try {
            return Files.deleteIfExists(file);
        } catch (IOException e) {
            log.error("Deletion files error", e);
            throw new RuntimeException("Deletion files ex");
        }
    }
}
