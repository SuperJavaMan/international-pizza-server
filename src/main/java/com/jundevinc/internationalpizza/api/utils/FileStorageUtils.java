package com.jundevinc.internationalpizza.api.utils;

/**
 * @author Oleg Pavlyukov
 * on 22.12.2019
 * cpabox777@gmail.com
 */
public class FileStorageUtils {
    private static final String BASE_ICON_URL = "http://localhost:8080/api/pizza/icon/";

    public static String extractFileNameFromUrl(String url) {
        return url.replace(BASE_ICON_URL, "");
    }

    public static String getUrlForIcon(String fileName) {
        return BASE_ICON_URL + fileName;
    }
}
