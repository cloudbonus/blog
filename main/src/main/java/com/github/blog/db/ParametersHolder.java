package com.github.blog.db;

import com.github.framework.annotation.Component;
import com.github.framework.annotation.Value;

@Component
public class ParametersHolder {
    @Value("my.param.db")
    private String someText;

    public String getSomeText() {
        return someText;
    }
}
