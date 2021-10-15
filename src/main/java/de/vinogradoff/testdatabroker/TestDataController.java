package de.vinogradoff.testdatabroker;

import org.springframework.beans.factory.annotation.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
public class TestDataController {

    @Autowired
    Map<String, Map<String, String>> repo;

    @PostMapping("/write/{dictionary}/{key}")
    public void saveData(@PathVariable String dictionary, @PathVariable String key, @RequestParam String value) {
        var actualDict = repo.getOrDefault(dictionary, new HashMap<>());
        actualDict.put(key, value);
        repo.put(dictionary, actualDict);
    }

    @GetMapping("/claim/{dictionary}/{key}")
    public String claimData(@PathVariable String dictionary, @PathVariable String key) {
        var actialDict = repo.get(dictionary);
        var data=actialDict.get(key);
        actialDict.remove(key);
        repo.put(dictionary,actialDict);
        return data;
    }

    @GetMapping("/read/{dictionary}/{key}")
    public String readData(@PathVariable String dictionary, @PathVariable String key){
        var actualDict=repo.get(dictionary);
        return actualDict.get(key);
    }
}
