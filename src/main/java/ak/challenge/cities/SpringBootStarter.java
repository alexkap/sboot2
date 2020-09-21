package ak.challenge.cities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@EnableAutoConfiguration
public class SpringBootStarter {

    private static Logger LOG = LoggerFactory.getLogger(SpringBootStarter.class);

    public static void main(String[] args) {
        SpringApplication.run(SpringBootStarter.class, args);
    }
}
