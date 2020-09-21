package ak.challenge.cities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import java.net.MalformedURLException;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CitiesControllerTest {

    @LocalServerPort
    private int port;
    private URL baseURL;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    void setUp() throws MalformedURLException {
        baseURL = new URL("http://localhost:" + port + "/");
    }

    @Test
    void index() {
        ResponseEntity<String> response = restTemplate.getForEntity(baseURL.toString(), String.class);
        assertThat(response.getBody()).isEqualTo(CitiesConstants.ROOT_TEXT);
    }

    @Test
    void areConnected() {

    }


}