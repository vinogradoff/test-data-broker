package io.github.vinogradoff.testdatabroker;

import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Api(value = "Managing test data", description = "Managing test data: a dataset can contain multiple values under a given key.")
public class TestDataController {

    @Autowired
    Map<String, Map<String, List<String>>> repo;

    @PostMapping("/write/{dataset}/{key}")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "writes data to Broker", notes = "writes data value in dataset under given key, prevoisly written data values are not overwritten")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Success")
    })
    public void saveData(@PathVariable @ApiParam(value = "dataset", example = "myService-prod", required = true)
                                 String dataset,
                         @PathVariable @ApiParam(value = "key", example = "active user", required = true)
                                 String key,
                         @RequestParam @ApiParam(value = "value to be added under the key", example = "demo@acme.com", required = true)
                                 String value) {
        var actualDict = repo.getOrDefault(dataset, new HashMap<>());
        var actualValue = actualDict.getOrDefault(key, new ArrayList<>());
        actualValue.add(value);
        actualDict.put(key, actualValue);
        System.out.println(dataset + ": " + actualDict);
        repo.put(dataset, actualDict);
    }

    @PutMapping("/claim/{dataset}/{key}")
    @ApiOperation(value = "reads data from Broker and deletes it", notes = "reads data value in dataset under given key, " +
            "this data value is deleted and not available for next calls. 204 returned, if no more data values exist in Broker")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 204, message = "No data found for given key", response = void.class),
            @ApiResponse(code = 404, message = "Either key or dataset not found")
    })
    public String claimData(@PathVariable @ApiParam(value = "dataset", example = "myService-prod", required = true)
                                    String dataset,
                            @PathVariable @ApiParam(value = "key", example = "active user", required = true)
                                    String key) {
        var actualDict = repo.get(dataset);
        if (actualDict == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Dataset " + dataset + " doesn't exist yet.");
        var actualData = actualDict.get(key);
        if (actualData == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Key " + key + " doesn't exist in the " + dataset);
        if (actualData.size() == 0) throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        var value = actualData.get(0);
        actualData.remove(0);
        actualDict.put(key, actualData);
        repo.put(dataset, actualDict);
        System.out.println(dataset + ": " + actualDict);
        return value;
    }

    @GetMapping("/read/{dataset}/{key}")
    @ApiOperation(value = "reads data from Broker", notes = "reads data value in dataset under given key, " +
            "this data value remains in Broker and is available for next calls. 204 returned, if no more data values exist in Broker")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 204, message = "No data found for given key", response = void.class),
            @ApiResponse(code = 404, message = "Either key or dataset not found")
    })
    public String readData(@PathVariable @ApiParam(value = "dataset", example = "myService-prod", required = true)
                                   String dataset,
                           @PathVariable @ApiParam(value = "key", example = "active user", required = true)
                                   String key) {
        var actualDict = repo.get(dataset);
        if (actualDict == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Dataset " + dataset + " doesn't exist yet.");
        var actualData = actualDict.get(key);
        System.out.println(dataset + ": " + actualDict);
        if (actualData == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Key " + key + " doesn't exist in the " + dataset);
        if (actualData.size() == 0) throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        else return actualData.get(0);
    }
}
