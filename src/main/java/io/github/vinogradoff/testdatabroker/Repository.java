package io.github.vinogradoff.testdatabroker;

import org.springframework.context.annotation.*;
import org.springframework.stereotype.*;

import java.util.*;

@Component
public class Repository {

    @Bean
    public Map<String, Map<String, List<String>>> getRepo() {
        return new HashMap<>();
    }
}

