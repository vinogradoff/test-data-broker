package io.github.vinogradoff.testdatabroker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
    void shouldDeleteDataset() {
        // arrange
        dataController.saveData("dict2", "key", "value");
        // act
        cleanUpController.deleteDataset("dict2");
        //assert
        assertThatThrownBy(() ->
                dataController.readData("dict2", "key2"))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContainingAll("404 NOT_FOUND", "dict2 doesn't exist");
    }

}
