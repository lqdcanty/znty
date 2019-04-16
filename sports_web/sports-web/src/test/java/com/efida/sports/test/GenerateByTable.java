package com.efida.sports.test;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.DbType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

public class GenerateByTable {

//    @Test
    public void generateCode() {
        String packageName = "com.efida.easy.ucenter";
        generateByTables(packageName, "user_auth");
    }

    private void generateByTables(String packageName, String... tableNames) {
        GlobalConfig config = new GlobalConfig();

        String dbUrl = "jdbc:mysql://122.115.40.84:23306/sports?useUnicode=true&characterEncoding=UTF-8&useSSL=false";
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setDbType(DbType.MYSQL).setUrl(dbUrl).setUsername("deploy_admin")
            .setPassword("WECBrsggz175sR1").setDriverName("com.mysql.jdbc.Driver");
        StrategyConfig strategyConfig = new StrategyConfig();
        strategyConfig.setCapitalMode(true).setEntityLombokModel(false).setDbColumnUnderline(true)
            .setNaming(NamingStrategy.underline_to_camel).setInclude(tableNames);
        config.setActiveRecord(false).setAuthor("zoutao").setOutputDir("D:\\aa")
            .setFileOverride(true).setBaseResultMap(true).setBaseColumnList(true);
        new AutoGenerator().setGlobalConfig(config).setDataSource(dataSourceConfig)
            .setStrategy(strategyConfig).setPackageInfo(new PackageConfig().setParent(packageName)
                .setController("controller").setEntity("entity"))
            .execute();
    }

}
