package com.efida.esearch.service.test;


import com.efida.esearch.service.AppService;
import com.efida.esearch.test.BaseTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@EnableTransactionManagement
public class AppServiceTest extends BaseTest {

    @Autowired
    private AppService appService;

    @Test
    @Transactional
    @Rollback(false)
    public void createDocMapping() {
        appService.createDocMapping("sof","article_search");
    }


}
