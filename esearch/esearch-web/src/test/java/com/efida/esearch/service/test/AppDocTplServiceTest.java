package com.efida.esearch.service.test;

import com.efida.esearch.model.AppDocTpl;
import com.efida.esearch.service.AppDocTplService;
import com.efida.esearch.test.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@EnableTransactionManagement
public class AppDocTplServiceTest extends BaseTest {

    @Autowired
    private AppDocTplService appDocTplService;

    @Test
    public void getAllFeild() {
    }

    @Test
    public void getAppDocTpl() {
    }

    @Test
    public void generateDocTpl() {
    }

    @Test
    @Transactional
    @Rollback(false)
    public void saveDocMappingTplAndDataTpl() {
        AppDocTpl docTpl =new AppDocTpl();
        docTpl.setAppId("znty_sof");
        docTpl.setTplCode("article_search");
        docTpl.setTplName("智能体育社交数据全文检索数据模板111");
        docTpl.setDocCode("zntyssof");
        docTpl.setTplDesc("描述");
        docTpl.setForecastDataSize(10000000L);
        docTpl.setApplyUserId("1234");
        docTpl.setApplyUserName("tp");
        docTpl.setAuditStatus("pass");
        docTpl.setAuditPassTime(new Date());
        docTpl.setIsLock(false);
        docTpl.setModifyUserId("1234");
        docTpl.setGmtCreate(new Date());
        docTpl.setGmtModified(new Date());


        appDocTplService.saveDocMappingTplAndDataTpl(docTpl);

    }
}
