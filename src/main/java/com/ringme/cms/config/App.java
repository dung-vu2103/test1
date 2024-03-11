package com.ringme.cms.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
@Getter
@Setter
@Configurable
public class App {
    @Value("${cms.in.db.prefix}")
    private String fileInDBPrefix;

    @Value("${cms.root-path}")
    private String rootPath;
}
