package com.ringme.cms.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

@Service
@Getter
@Configuration
public class AppConfiguration {
    @Value("${cms.file.in.db.prefix}")
    private String fileInDBPrefix;

    @Value("${cms.file.store.root-path}")
    private String rootPath;

    @Value("${api-clear-cache}")
    private String apiClearCache;

    @Value("${server-directory-games}")
    private String gameDirectory;

    @Value("${api-top-game-event}")
    private String apiTopGameEvent;
}
