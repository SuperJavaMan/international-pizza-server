package com.jundevinc.internationalpizza.security.service;

/**
 * @author Oleg Pavlyukov
 * on 03.12.2019
 * cpabox777@gmail.com
 */
public class UploadFileUtil {

    public static String getStoragePath(String fileName) {
        String path = "http://localhost:8080/files/";
        return path + fileName;
    }
}
