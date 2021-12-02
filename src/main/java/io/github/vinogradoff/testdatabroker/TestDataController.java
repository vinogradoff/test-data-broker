package io.github.vinogradoff.testdatabroker;

import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.*;

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
        System.out.println(dictionary + ": " + actualDict);
        repo.put(dictionary, actualDict);
    }

    @PutMapping("/claim/{dictionary}/{key}")
    public String claimData(@PathVariable String dictionary, @PathVariable String key) {
        var actualDict = repo.get(dictionary);
        if (actualDict == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Dictionary " + dictionary + " doesn't exist yet.");
        var actualData = actualDict.get(key);
        if (actualData == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Key " + key + " doesn't exist in the " + dictionary);
        if (actualData.size() == 0) throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        var value = actualData.get(0);
        actualData.remove(0);
        actualDict.put(key, actualData);
        repo.put(dictionary, actualDict);
        System.out.println(dictionary + ": " + actualDict);
        return value;
    }

    @GetMapping("/read/{dictionary}/{key}")
    public String readData(@PathVariable String dictionary, @PathVariable String key) {
        var actualDict = repo.get(dictionary);
        if (actualDict == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Dictionary " + dictionary + " doesn't exist yet.");
        var actualData = actualDict.get(key);
        System.out.println(dictionary + ": " + actualDict);
        if (actualData == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Key " + key + " doesn't exist in the " + dictionary);
        if (actualData.size() == 0) throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        else return actualData.get(0);
    }
}
