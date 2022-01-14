package com.rnirest.rnirestapp.namesearch.api;

import io.swagger.annotations.Api;
import net.minidev.json.JSONObject;

import org.elasticsearch.action.search.SearchResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import com.rnirest.rnirestapp.namesearch.model.ElasticIndex;
import com.rnirest.rnirestapp.namesearch.model.NamesearchRequest;
import com.rnirest.rnirestapp.namesearch.model.Person;
import com.rnirest.rnirestapp.namesearch.model.SearchResult;
import com.rnirest.rnirestapp.namesearch.config.ElasticsearchManager;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@Api(value="Name Search",
        description="Operations pertaining to the querying and indexing of names",
        tags = {"namesearch"})
@ComponentScan("com")
public class NamesearchController {

    private static final Logger logger = LoggerFactory.getLogger(NamesearchController.class);
    private final ElasticsearchManager elasticManager;
    @Autowired 
    public NamesearchController(ElasticsearchManager elasticManager) {
        this.elasticManager = elasticManager;
    }

    //@PostMapping(value = "/internal", headers = "Content-Type=application/json")
    @GetMapping("hello")
    public ResponseEntity<Object> hello() {
        JSONObject object = new JSONObject();
        object.put("message", "hello world!!");
        return new ResponseEntity<Object>(object, org.springframework.http.HttpStatus.OK);
    }

    @PostMapping(value = "/createIndex", headers = "Content-Type=application/json")
    public ResponseEntity<Boolean> createNewIndex(@RequestBody ElasticIndex elasticIndexInput) {
        String indexName = elasticIndexInput.getName();
        try {
            elasticManager.createIndex(indexName);
            return new ResponseEntity<Boolean>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<Boolean>(HttpStatus.BAD_REQUEST);
            //TODO: handle exception
        }
    }

    @PostMapping(value = "/checkIndex", headers = "Content-Type=application/json")
    public ResponseEntity<Boolean> checkExistingIndex(@RequestBody ElasticIndex elasticIndexInput) {
        String indexName = elasticIndexInput.getName();
        try {
            final boolean result = elasticManager.checkIndex(indexName);
            return new ResponseEntity<Boolean>(result, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<Boolean>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/index-single", headers = "Content-Type=application/json")
    public ResponseEntity<String> indexNewPerson(@RequestBody Person personInput) {
        logger.info(personInput.toString());
        try {
            elasticManager.storeIndex(personInput);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            //e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/query-name-single", headers = "Content-Type=application/json")
    public ResponseEntity<List<SearchResult>> singleNameQuery(@RequestBody NamesearchRequest namesearchRequest) {
        logger.info(namesearchRequest.toString());
        try {
            List<SearchResult> results = elasticManager.singleQuery(namesearchRequest);
            return new ResponseEntity<List<SearchResult>>(results,HttpStatus.OK);
        } catch (Exception e) {
            //e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/query-rni-name-single", headers = "Content-Type=application/json")
    public ResponseEntity<List<SearchResult>> singleRniNameQuery(@RequestBody NamesearchRequest namesearchRequest) {
        logger.info(namesearchRequest.toString());
        try {
            List<SearchResult> results = elasticManager.singleRniQuery(namesearchRequest);
            return new ResponseEntity<List<SearchResult>>(results,HttpStatus.OK);
        } catch (Exception e) {
            //e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
