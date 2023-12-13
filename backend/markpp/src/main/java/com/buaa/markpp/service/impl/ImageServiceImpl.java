package com.buaa.markpp.service.impl;

import com.buaa.markpp.service.ImageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Service
public class ImageServiceImpl implements ImageService {

    private static Set<String> set;

    public static final Object TOKEN_LOCK = new Object();

    @Value("${resource.storage-directory}")
    private String directory;

    @PostConstruct
    public void init() throws IOException {
        synchronized (TOKEN_LOCK) {
            if (set == null) {
                File dir = new File(directory).getAbsoluteFile();
                if (!dir.exists() && !dir.mkdirs())
                    throw new IOException("cannot create directory");
                File[] folders = dir.listFiles();
                if (folders == null)
                    throw new IOException("folder list is null");
                set = new HashSet<>();
                for (File folder : folders) {
                    if (folder.isDirectory())
                        set.add(folder.getName());
                }
            }
        }
    }

    public boolean existsToken(String token) {
        synchronized (TOKEN_LOCK) {
            return set.contains(token);
        }
    }

    public void addToken(String token) {
        synchronized (TOKEN_LOCK) {
            set.add(token);
        }
    }

    public void removeToken(String token) {
        synchronized (TOKEN_LOCK) {
            set.remove(token);
        }
    }
}
