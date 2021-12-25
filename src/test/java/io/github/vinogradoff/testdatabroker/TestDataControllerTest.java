package io.github.vinogradoff.testdatabroker;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.web.server.*;

import java.util.*;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class TestDataControllerTest {

    @Autowired
    TestDataController controller;

    @BeforeEach
    void initRepo() {
        controller.repo.clear();
    }

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

        // assert
        assertThat(value1).isEqualTo("value2");
        assertThatThrownBy(() -> controller.readData("dict", "key2"))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessage("204 NO_CONTENT");
    }

    @Test
    void shouldClaimDataFromRepoUntilNoDataLeft() {
        // arrange
        controller.repo.put("dict", new HashMap<>(Map.of("key2", new ArrayList<>(List.of("value1", "value2")))));

        // act
        var value1 = controller.claimData("dict", "key2");
        var value2 = controller.claimData("dict", "key2");

        // assert
        assertThat(value1).isEqualTo("value1");
        assertThat(value2).isEqualTo("value2");

        assertThatThrownBy(() -> controller.readData("dict", "key2"))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessage("204 NO_CONTENT");

    }

    @Test
    void shouldReturn204IfNoValuesForKey() {
        // arrange
        controller.repo.put("dict", new HashMap<>(Map.of("key1", new ArrayList<>())));
        // act & assert
        assertThatThrownBy(() -> controller.readData("dict", "key1"))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessage("204 NO_CONTENT");

        assertThatThrownBy(() -> controller.claimData("dict", "key1"))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessage("204 NO_CONTENT");
    }

    @Test
    void shouldReturn404IfDictionaryDoesntExist() {
        // act & assert
        assertThatThrownBy(() -> controller.readData("dict", "key1"))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContainingAll("404 NOT_FOUND", "dict doesn't exist");

        assertThatThrownBy(() -> controller.claimData("dict", "key1"))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContainingAll("404 NOT_FOUND", "dict doesn't exist");

    }

    @Test
    void shouldReturn404IfKeyDoesntExist() {
        //arrange
        controller.repo.put("dict1", new HashMap<>(Map.of("key1", new ArrayList<>())));

        // act & assert
        assertThatThrownBy(() -> controller.readData("dict1", "key2"))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContainingAll("404 NOT_FOUND", "key2 doesn't exist", "dict1");

        assertThatThrownBy(() -> controller.claimData("dict1", "key2"))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContainingAll("404 NOT_FOUND", "key2 doesn't exist", "dict1");

    }
}
