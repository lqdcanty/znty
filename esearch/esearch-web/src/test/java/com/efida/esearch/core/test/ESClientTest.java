package com.efida.esearch.core.test;

import com.efida.esearch.core.ESClient;
import com.efida.esearch.service.AppService;
import com.efida.esearch.test.BaseTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class ESClientTest extends BaseTest {

    @Autowired
    private ESClient esClient;
    @Autowired
    private AppService appService;
    @Test
    public void createIndex() {
        esClient.createIndex("test111");
    }

    @Test
    public void createMapping() {
        //appService.createDocMapping();
//        esClient.createMapping("test111","doc/_mapping",);
    }

    @Test
    public void updateDocument() {
    }

    @Test
    public void updateDocument1() {
    }

    @Test
    public void addDocument() {
    }

    @Test
    public void bulkDocument() {
    }

    @Test
    public void asyncBulkDocument() {
    }

    @Test
    public void bulkDocument1() {
    }

    @Test
    public void bulkDocument2() {
    }

    @Test
    public void deleteDocument() {
    }

    @Test
    public void query() {
    }

    @Test
    public void query1() {
    }
}
