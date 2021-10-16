package de.vinogradoff.testdatabroker;

import org.springframework.beans.factory.annotation.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
public class TestDataController {

    @Autowired
    Map<String, Map<String, List<String>>> repo;

    @PostMapping("/write/{dictionary}/{key}")
    public void saveData(@PathVariable String dictionary, @PathVariable String key, @RequestParam String value) {
        var actualDict = repo.getOrDefault(dictionary, new HashMap<>());
        var actualValue = actualDict.getOrDefault(key, new ArrayList<>());
        actualValue.add(value);
        actualDict.put(key, actualValue);
        repo.put(dictionary, actualDict);
    }

    @PutMapping("/claim/{dictionary}/{key}")
    public String claimData(@PathVariable String dictionary, @PathVariable String key) {
        var actualDict = repo.get(dictionary);
        var actualData = actualDict.get(key);
        if (actualData == null && actualData.size() == 0) return null;
        var value = actualData.get(0);
        actualData.remove(0);
        if (actualData.size() == 0) actualDict.remove(key);
        actualDict.put("key", actualData);
        repo.put(dictionary, actualDict);
        return value;
    }

    @GetMapping("/read/{dictionary}/{key}")
    public String readData(@PathVariable String dictionary, @PathVariable String key) {
        var actualDict = repo.get(dictionary);
        var actualData = actualDict.get(key);
        if (actualData == null) return null;
        else return actualData.get(0);
    }
}
