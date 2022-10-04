package com.rnirest.rnirestapp.namesearch.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ExecutionException;

import com.rnirest.rnirestapp.namesearch.config.ElasticsearchManager;

@Repository("namesearch")
@ComponentScan
public class NamesearchRepository {

    private static final Logger logger = LoggerFactory.getLogger(NamesearchRepository.class);
    private final ElasticsearchManager esm;


    @Autowired
    public NamesearchRepository(ElasticsearchManager elasticsearchManager) {
        this.esm = elasticsearchManager;
    }

}