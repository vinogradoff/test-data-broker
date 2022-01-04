package io.github.vinogradoff.testdatabroker;

import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
@Api(value = "Clearing up operations", description = "Clearing up operations")
public class CleanupController {

    @Autowired
    Map<String, Map<String, List<String>>> repo;

    @PutMapping("/clear/{dictionary}/{key}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "clears data for given key", notes = "deletes all data values in dataset under given key")
    public void clearData(@PathVariable String dictionary, @PathVariable String key) {
        var actualDict = repo.getOrDefault(dictionary, new HashMap<>());
        actualDict.put(key, new ArrayList<>());
        System.out.println(dictionary + ": " + actualDict);
        repo.put(dictionary, actualDict);
    }

    @DeleteMapping("/delete/{dictionary}/{key}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "deletes given key", notes = "deletes given key from dataset")
    public void deleteKey(@PathVariable String dictionary, @PathVariable String key) {
        var actualDict = repo.getOrDefault(dictionary, new HashMap<>());
        actualDict.remove(key);
        System.out.println(dictionary + ": " + actualDict);
        repo.put(dictionary, actualDict);
    }

    @DeleteMapping("/delete/{dictionary}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "deletes given dataset", notes = "deletes dataset including all its keys")
    public void deleteDictionary(@PathVariable String dictionary) {
        repo.remove(dictionary);
        System.out.println(dictionary + ": DELETED.");
    }
}
