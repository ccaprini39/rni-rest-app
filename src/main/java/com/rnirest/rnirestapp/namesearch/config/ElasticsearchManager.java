package com.rnirest.rnirestapp.namesearch.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.elasticsearch.common.xcontent.XContentFactory.*;

import com.rnirest.rnirestapp.namesearch.model.AdvancedNamesearchRequest;
import com.rnirest.rnirestapp.namesearch.model.NamesearchRequest;
import com.rnirest.rnirestapp.namesearch.model.Person;
import com.rnirest.rnirestapp.namesearch.model.SearchResult;

import org.apache.http.HttpHost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.*;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.index.query.AbstractQueryBuilder;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.rescore.QueryRescorerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.elasticsearch.common.xcontent.XContentBuilder;

/**
 * This class manages Elasticsearch configuration, indexing, and querying
 * 
 * @author Collin Caprini
 */
@Component
public class ElasticsearchManager {
    private static final Logger logger = LoggerFactory.getLogger(ElasticsearchManager.class);
    private static ElasticsearchManager instance;
    //the original testing url
    private static final String elastic_host = "ec2-18-222-127-24.us-east-2.compute.amazonaws.com"; 

    //the url for flat
    //private static final String elastic_host = "ec2-18-220-93-214.us-east-2.compute.amazonaws.com";

    //the url for nested
    //private static final String elastic_host = "ec2-18-217-23-131.us-east-2.compute.amazonaws.com";

    //private static final String elastic_host = "localhost";
    private static final Integer elastic_port = 9200;
    private static final String index = "namesearch";
    private RestHighLevelClient client;
    private static final int WINDOW  = 10;
    private int docID = 0;
    
    @Autowired
    private ElasticsearchManager() {
        initialize();
    }
    
    /**
     * Gets a single instance/connection to the Elasticsearch cluster
     * 
     * @return 
     */
    public static ElasticsearchManager getInstance() {
        if (instance == null) {
            instance = new ElasticsearchManager();
        }

        return instance;
    }
        
    /**
     * This method configures the ES client
     */
    @Bean
    private void initialize() {
        try {
            client = new RestHighLevelClient(RestClient.builder(new HttpHost(elastic_host, elastic_port, "http")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * This method queries Elasticsearch with a RNI query and rescorer
     * 
     * @param query
     * @param rescorer
     * @return 
     */
    public List<SearchResult> singleAdvancedRniQuery(AbstractQueryBuilder query, QueryRescorerBuilder rescorer, float minScore) throws Exception{
        SearchRequest searchRequest = new SearchRequest(index);
        List<SearchResult> hitList = new ArrayList<>();
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(query)
            .addRescorer(rescorer)
            .explain(Boolean.TRUE)
            .size(WINDOW);
        
        searchRequest.source(sourceBuilder);
        logger.info(sourceBuilder.toString());
        
        SearchResponse searchResponse = null;
        try {
            searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            SearchHit[] results = searchResponse.getHits().getHits();
            Long timeLong = searchResponse.getTook().getMillis();
            String time = String.valueOf(timeLong);

            for(SearchHit hit : results) {
                Float score = hit.getScore();
                String scoreString = String.valueOf(score);
                String sourceAsString = hit.getSourceAsString();
                if (sourceAsString != null){
                    Map<String, Object> fields = hit.getSourceAsMap();
                    //String indexedName = getNestedObject(fields);
                    SearchResult searchResult = new SearchResult( 
                        fields.get("name").toString(), fields.get("dob").toString(), time, scoreString
                    );
                    hitList.add(searchResult);
                }
            }
            return hitList;
        } catch (Exception e) {
            logger.info("error processing search");
            e.printStackTrace();
            throw new Exception("error processing search");
        }
    }

    /**
     * This method indexes a single ES document
     * 
     * @param esDocument
     * @throws IOException 
     */
    public void singleIndex(XContentBuilder esDocument) throws IOException {
        IndexRequest request = new IndexRequest(index).id(String.valueOf(docID)).source(esDocument);
        IndexResponse response = client.index(request, RequestOptions.DEFAULT);
        logger.info(String.format("IndexResponse -> %s", response.getResult()));
        docID++;
    }

    public void storeIndex(Person person) throws Exception {
        try {
            IndexRequest indexRequest = constructIndexRequest(person);
            logger.info(person.toString());
            logger.info(String.format("IndexRequest -> %s", indexRequest.toString()));
            //IndexResponse response = client.prepareIndex(indexRequest, RequestOptions.DEFAULT);
            IndexResponse response = client.index(indexRequest, RequestOptions.DEFAULT);
            logger.info(String.format("IndexResponse -> %s", response.getResult()));
        } catch (Exception e) {
            logger.info("IndexRequest failed. Unable to store NameSearchObject to name_search_index.");
            e.printStackTrace();
            throw new Exception("IndexRequest failed. Unable to store NameSearchObject to name_search_index");
        }
    }

    /**
     * Index used to store the needed data, and then let the plugin handle the rest.
     *
     * @param nso
     */
    public IndexRequest constructIndexRequest(Person person) throws IOException {
        logger.info("Creating IndexRequest");
        return new IndexRequest("namesearch")
            .source( "name", person.getName(),
                "rni_name", person.getName(),
                "dob", person.getDob(),
                "rni_dob", person.getDob());
    }
    
    /**
     * I guess this still needs to be implemented
     */
    public void bulkIndex() {
        
    }

    /**
     * 
     * @param indexName the name of the index being created
     * @throws Exception if it cannot complete the operation
     */
    public void createIndex(String indexName) throws Exception {
        CreateIndexRequest request = new CreateIndexRequest(indexName);
        try {
            CreateIndexResponse createIndexResponse = client.indices().create(request, RequestOptions.DEFAULT);
            boolean ack = createIndexResponse.isAcknowledged();
            logger.debug(String.format("Index created: %s", ack));
        } catch (IOException e) {
            throw new Exception();
        }  
    }

    /**
     * 
     * @param indexName the name of the index being created
     * @return true if the index exists, false if it does not
     * @throws Exception if it cannot complete the operation
     */
    public boolean checkIndex(String indexName) throws Exception {
        boolean result = false;
        GetIndexRequest request = new GetIndexRequest(indexName);
        result = client.indices().exists(request, RequestOptions.DEFAULT);
        return result;
    }

    public List<SearchResult> singleQuery(NamesearchRequest nsr) throws Exception{
        SearchRequest searchRequest = new SearchRequest("namesearch");
        List<SearchResult> hitList = new ArrayList<>();

        String name = nsr.getName();
        String dob = nsr.getDob();
        Integer window = nsr.getWindow();
        //MatchQueryBuilder query = QueryBuilders.matchQuery("name", name);
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder
            .should(QueryBuilders
                .matchQuery("name", name)
            )
            .should(QueryBuilders.matchQuery("dob", dob));
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(boolQueryBuilder)
            .explain(Boolean.TRUE)
            .size(window);

        searchRequest.source(sourceBuilder);
        SearchResponse searchResponse = null;
        try {
            searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            SearchHit[] results = searchResponse.getHits().getHits();
            Long timeLong = searchResponse.getTook().getMillis();
            String time = String.valueOf(timeLong);

            for(SearchHit hit : results) {
                Float score = hit.getScore();
                String scoreString = String.valueOf(score);
                String sourceAsString = hit.getSourceAsString();
                if (sourceAsString != null){
                    Map<String, Object> fields = hit.getSourceAsMap();
                    SearchResult searchResult = new SearchResult( 
                        fields.get("name").toString(), fields.get("dob").toString(), time, scoreString
                    );
                    hitList.add(searchResult);
                }
            }
            return hitList;
        } catch (Exception e) {
            logger.info("error processing search");
            throw new Exception("error processing search");
        }
    }

    public List<SearchResult> singleRniQuery(NamesearchRequest nsr, QueryRescorerBuilder rescoreBuilder) throws Exception{
        SearchRequest searchRequest = new SearchRequest("namesearch");
        List<SearchResult> hitList = new ArrayList<>();
        String name = nsr.getName();
        String dob = nsr.getDob();
        Integer window = nsr.getWindow();
        //MatchQueryBuilder query = QueryBuilders.matchQuery("rni_name", name);
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder
            .should(QueryBuilders.matchQuery("rni_name", name))
            .should(QueryBuilders.matchQuery("rni_dob", dob));
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(boolQueryBuilder)
            .addRescorer(rescoreBuilder)
            .explain(Boolean.TRUE)
            .size(window);

        searchRequest.source(sourceBuilder);
        SearchResponse searchResponse = null;
        try {
            searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            SearchHit[] results = searchResponse.getHits().getHits();
            Long timeLong = searchResponse.getTook().getMillis();
            String time = String.valueOf(timeLong);

            for(SearchHit hit : results) {
                Float score = hit.getScore();
                String scoreString = String.valueOf(score);
                String sourceAsString = hit.getSourceAsString();
                if (sourceAsString != null){
                    Map<String, Object> fields = hit.getSourceAsMap();
                    SearchResult searchResult = new SearchResult( 
                        fields.get("name").toString(), fields.get("dob").toString(), time, scoreString
                    );
                    hitList.add(searchResult);
                }
            }
            return hitList;
        } catch (Exception e) {
            logger.info("error processing search");
            throw new Exception("error processing search");
        }
    }
}
