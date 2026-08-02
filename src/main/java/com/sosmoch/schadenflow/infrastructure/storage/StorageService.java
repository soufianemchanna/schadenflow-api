package com.sosmoch.schadenflow.infrastructure.storage;

import java.net.URL;

public interface StorageService {
    URL generateUploadUrl(String key, String contentType);
    URL generateDownloadUrl(String key);
    void delete(String key);
    boolean exists(String key);
}
