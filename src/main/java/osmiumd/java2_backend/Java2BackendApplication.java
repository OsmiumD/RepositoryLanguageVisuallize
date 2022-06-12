package osmiumd.java2_backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import osmiumd.java2_backend.dto.Repository;
import osmiumd.java2_backend.scraper.RepositoryScraper;

@SpringBootApplication
public class Java2BackendApplication {

    @Autowired
    RepositoryScraper scraper;

    public static void main(String[] args) {
        SpringApplication.run(Java2BackendApplication.class, args);
    }

}
