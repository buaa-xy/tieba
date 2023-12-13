package com.buaa.markpp.util;

import com.buaa.markpp.model.ExceptionType;
import com.buaa.markpp.model.MarkppException;

import java.io.File;
import java.io.IOException;

public class FileHelper {
    public static void removeFolder(File folder) throws IOException {
        if (folder == null)
            throw new IllegalArgumentException("folder is null");
        if (folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files == null)
                throw new IOException("old file list is null");
            for (File child : files) {
                removeFolder(child);
            }
        }
        if (!folder.delete()) {
            throw new IOException("cannot delete old file");
        }
    }

    public static String getExtension(String filename) {
        return filename.substring(filename.lastIndexOf('.') + 1);
    }

    public static String getImageContentType(String filename) {
        filename = getExtension(filename.toLowerCase());
        return switch (filename) {
            case "bmp" -> "image/bmp";
            case "gif" -> "image/gif";
            case "jpg", "jpeg", "jpe", "jfif" -> "image/jpeg";
            case "png" -> "image/png";
            case "tiff", "tif" -> "image/tiff";
            case "ico" -> "image/x-icon";
            default -> throw new MarkppException(ExceptionType.INVALID_PARAM);
        };
    }
}
