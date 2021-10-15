package de.vinogradoff.testdatabroker;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class TestDataControllerTest {

    @Autowired
    TestDataController controller;

    @Test
    void shouldPutDataInRepo() {
        controller.saveData("dict", "key", "value");
        assertThat(controller.repo).containsKey("dict");
        assertThat(controller.repo.get("dict").get("key")).isEqualTo("value");
    }


    @Test
    void shouldGetDataFromRepo() {
        controller.saveData("dict", "key1", "value1");
        assertThat(controller.readData("dict", "key1")).isEqualTo("value1");
        assertThat(controller.readData("dict", "key1")).isEqualTo("value1");
    }

    @Test
    void shouldClaimDataFromRepo() {
        controller.saveData("dict", "key2", "value2");
        assertThat(controller.claimData("dict", "key2")).isEqualTo("value2");
        assertThat(controller.readData("dict", "key2")).isEqualTo(null);
    }
}
