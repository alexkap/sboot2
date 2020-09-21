package ak.challenge.cities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.FileNotFoundException;

/**
 * Bean definitions go here.
 * There may be various ways to init the bean, e.g. not from the file, but from the DB, etc.
 * Also the environment param could be used to switch between configurations or even param in application.yml file
 */
@Configuration
@ComponentScan(basePackages = "ak.challenge.cities") // excessive in our simple flat case
public class CitiesConfiguration {
    private static Logger LOG = LoggerFactory.getLogger(CitiesConfiguration.class);

    @Bean(name="navigator")
    public CustomMap getMap() {
        CustomMap map = new CustomMap();
        String fName  = System.getProperty("CITIES");
        if (fName == null)
            fName = "config/city.txt";
        try {
            map.loadFromFile(fName);
        } catch (Exception e) {
            // will load empty map. The API technically can provide other methods to populate - e.g. command line, expose URL...
            LOG.error("Unable to load using current default method", e);
            LOG.info("Loading an empty map");
        } finally {
            LOG.info("Total number of cities loaded: " + map.getNumberOfCities() );
        }

        return map;
    }
 }
