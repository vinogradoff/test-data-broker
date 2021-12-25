package io.github.vinogradoff.testdatabroker;

import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
public class CleanupController {

    @Autowired
    Map<String, Map<String, List<String>>> repo;

    @PutMapping("/clear/{dictionary}/{key}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void clearData(@PathVariable String dictionary, @PathVariable String key) {
        var actualDict = repo.getOrDefault(dictionary, new HashMap<>());
        actualDict.put(key, new ArrayList<>());
        System.out.println(dictionary + ": " + actualDict);
        repo.put(dictionary, actualDict);
    }

    @DeleteMapping("/delete/{dictionary}/{key}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteKey(@PathVariable String dictionary, @PathVariable String key) {
        var actualDict = repo.getOrDefault(dictionary, new HashMap<>());
        actualDict.remove(key);
        System.out.println(dictionary + ": " + actualDict);
        repo.put(dictionary, actualDict);
    }

    @DeleteMapping("/delete/{dictionary}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDictionary(@PathVariable String dictionary) {
        repo.remove(dictionary);
        System.out.println(dictionary + ": DELETED.");
    }
}
