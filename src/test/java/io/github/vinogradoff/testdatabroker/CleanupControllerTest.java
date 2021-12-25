package io.github.vinogradoff.testdatabroker;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.web.server.*;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class CleanupControllerTest {

    @Autowired
    TestDataController dataController;
    @Autowired
    CleanupController cleanUpController;

    @BeforeEach
    void initRepo() {
        dataController.repo.clear();
    }

    @Test
    void shouldClearDataForKey() {
        // arrange
        dataController.saveData("dict", "key", "value");
        // act
        cleanUpController.clearData("dict", "key");
        // assert
        assertThat(dataController.repo.get("dict").get("key")).hasSize(0);
    }

    @Test
    void shouldDeleteKey() {
        // arrange
        dataController.saveData("dict", "key2", "value");
        // act
        cleanUpController.deleteKey("dict", "key2");
        //assert
        assertThatThrownBy(() ->
                dataController.readData("dict", "key2"))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContainingAll("404 NOT_FOUND", "key2 doesn't exist", "dict");
    }


    @Test
    void shouldDeleteDictionary() {
        // arrange
        dataController.saveData("dict2", "key", "value");
        // act
        cleanUpController.deleteDictionary("dict2");
        //assert
        assertThatThrownBy(() ->
                dataController.readData("dict2", "key2"))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContainingAll("404 NOT_FOUND", "dict2 doesn't exist");
    }

}
