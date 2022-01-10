package io.github.vinogradoff.testdatabroker;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Api(value = "Clearing up operations", description = "Clearing up operations")
public class CleanupController {

    @Autowired
    Map<String, Map<String, List<String>>> repo;

    @PutMapping("/clear/{dataset}/{key}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "clears data for given key", notes = "deletes all data values in dataset under given key")
    public void clearData(@PathVariable @ApiParam(value = "dataset", example = "myService-prod", required = true)
                                  String dataset,
                          @PathVariable @ApiParam(value = "key", example = "active user", required = true) String key) {
        var actualDict = repo.getOrDefault(dataset, new HashMap<>());
        actualDict.put(key, new ArrayList<>());
        System.out.println(dataset + ": " + actualDict);
        repo.put(dataset, actualDict);
    }

    @DeleteMapping("/delete/{dataset}/{key}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "deletes given key", notes = "deletes given key from dataset")
    public void deleteKey(@PathVariable @ApiParam(value = "dataset", example = "myService-prod", required = true)
                                  String dataset,
                          @PathVariable @ApiParam(value = "key", example = "active user", required = true)
                                  String key) {
        var actualDict = repo.getOrDefault(dataset, new HashMap<>());
        actualDict.remove(key);
        System.out.println(dataset + ": " + actualDict);
        repo.put(dataset, actualDict);
    }

    @DeleteMapping("/delete/{dataset}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "deletes given dataset", notes = "deletes dataset including all its keys")
    public void deleteDataset(@PathVariable @ApiParam(value = "dataset", example = "myService-prod", required = true)
                                      String dataset) {
        repo.remove(dataset);
        System.out.println(dataset + ": DELETED.");
    }
}
