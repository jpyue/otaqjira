package gov.epa.otaq.fuel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
//public class CrSync  {
public class CrSync extends SpringBootServletInitializer {

    private static final Logger log = LoggerFactory.getLogger(CrSync.class);

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {

        log.info("Cr Sync Application web started.");
        return application.sources(CrSync.class);
    }



    public static void main(String[] args) {
        SpringApplication.run(CrSync.class, args);
        log.info("Cr Sync Application started.");
	}

}
