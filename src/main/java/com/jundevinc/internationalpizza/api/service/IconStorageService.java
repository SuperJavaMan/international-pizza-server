package com.jundevinc.internationalpizza.api.service;

import com.jundevinc.internationalpizza.api.utils.FileStorageUtils;
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
public class IconStorageService {
    private static final Path ROOT_DIR = Paths.get("src/main/resources/static/icon-pizza");

    public boolean storeIcon(MultipartFile file) {
        try {
            Files.copy(file.getInputStream(), ROOT_DIR.resolve(file.getOriginalFilename()));
            return true;
        } catch (IOException e) {
            throw new RuntimeException("Icon saving error");
        }
    }

    public Resource getIcon(String filename) {
        Path file = ROOT_DIR.resolve(filename);
        try {
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Resource is not exist or readable");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Resource creation ex. Msg: " + e.getMessage());
        }

    }

    public boolean deleteIcon(String url) {
        String fileName = FileStorageUtils.extractFileNameFromUrl(url);
        Path file = ROOT_DIR.resolve(fileName);
        try {
            return Files.deleteIfExists(file);
        } catch (IOException e) {
            throw new RuntimeException("Deletion files ex");
        }
    }
}
