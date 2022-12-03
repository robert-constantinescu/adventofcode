package commons;

import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.nio.file.WatchEvent;

public class WebClientConfig {



    public static WebClient adventOfCodeClient() {
        String urlBase = "https://adventofcode.com";
        return WebClient.builder()
                .baseUrl(urlBase)
                .build();
    }
}
