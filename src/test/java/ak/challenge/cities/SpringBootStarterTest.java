package ak.challenge.cities;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SpringBootStarterTest {
    @LocalServerPort
    @Value("${local.server.port}")
    protected int localPort;

    @BeforeEach
    void setUp() throws MalformedURLException {
        SpringBootStarter.main(new String[] {});
    }

    @Test
    void testHostPort() {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress("localhost", localPort), 15_000);
            // OK
        } catch (IOException e) {
            fail();
        }
    }
}