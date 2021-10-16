package de.vinogradoff.testdatabroker;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;

import java.util.*;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class TestDataControllerTest {

    @Autowired
    TestDataController controller;

    @Test
    void shouldPutDataInRepo() {
        // act
        controller.saveData("dict", "key", "value");
        // assert
        assertThat(controller.repo).containsKey("dict");
        assertThat(controller.repo.get("dict").get("key")).isEqualTo(List.of("value"));
    }

    @Test
    void shouldPutMultipleTestDataInRepo() {
        // act
        controller.saveData("dict", "key", "value");
        controller.saveData("dict", "key", "value2");
        // assert
        assertThat(controller.repo).containsKey("dict");
        assertThat(controller.repo.get("dict").get("key")).isEqualTo(List.of("value", "value2"));
    }

    @Test
    void shouldGetDataFromRepoMultipleTimes() {
        // arrange
        controller.repo.put("dict", new HashMap<>(Map.of("key1", new ArrayList<>(List.of("value1")))));

        // act
        var value1 = controller.readData("dict", "key1");
        var value2 = controller.readData("dict", "key1");

        // assert
        assertThat(value1).isEqualTo("value1");
        assertThat(value2).isEqualTo("value1");
    }

    @Test
    void shouldClaimDataFromRepoJustOnce() {
        // arrange
        controller.repo.put("dict", new HashMap<>(Map.of("key2", new ArrayList<>(List.of("value2")))));

        // act
        var value1 = controller.claimData("dict", "key2");
        var value2 = controller.readData("dict", "key2");

        // assert
        assertThat(value1).isEqualTo("value2");
        assertThat(value2).isNull();
    }

    @Test
    void shouldClaimDataFromRepoUntilNoDataLeft() {
        // arrange
        controller.repo.put("dict", new HashMap<>(Map.of("key2", new ArrayList<>(List.of("value1", "value2")))));

        // act
        var value1 = controller.claimData("dict", "key2");
        var value2 = controller.claimData("dict", "key2");
        var value3 = controller.readData("dict", "key2");

        // assert
        assertThat(value1).isEqualTo("value1");
        assertThat(value2).isEqualTo("value2");
        assertThat(value3).isNull();
    }
}
