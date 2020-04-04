package com.sprboot.ex.ormplugin.jpa.muldb.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;

/**
 * 编  号：
 * 名  称：SecondDbConfig
 * 描  述：
 * 完成日期：2020/4/4 18:22
 * @author：felix.shao
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.sprboot.ex.ormplugin.jpa.muldb.dao.db2",
	entityManagerFactoryRef = "secondEntityManagerFactory",
	transactionManagerRef = "secondTransactionManager")
public class SecondDbConfig {

    @Bean
    @ConfigurationProperties("db.second.datasource")
    public DataSourceProperties secondDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public DataSource secondDataSource() {
        return secondDataSourceProperties().initializeDataSourceBuilder().build();
    }

    /**
     * 实体管理对象
     * @param builder  由spring注入这个对象，首先根据type注入（多个就取声明@Primary的对象），否则根据name注入
     * @return
     */
    @Bean
    public LocalContainerEntityManagerFactoryBean secondEntityManagerFactory(
            EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(secondDataSource())
                .packages("com.sprboot.ex.ormplugin.jpa.muldb.model.db2")
                .persistenceUnit("secondDb")
                .build();
    }

    /**
     * 事物管理对象
     * @param secondEntityManagerFactory 实体管理工厂对象（按照名称注入）
     * @return 平台事物管理器
     */
    @Bean(name = "secondTransactionManager")
    public PlatformTransactionManager transactionManager(@Qualifier("secondEntityManagerFactory")LocalContainerEntityManagerFactoryBean secondEntityManagerFactory){
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(secondEntityManagerFactory.getObject());
        return transactionManager;
    }

    @Bean(name="jdbcTemplate2")
    public JdbcTemplate jdbcTemplate(){
        return new JdbcTemplate(secondDataSource());
    }

    @Bean(name = "transactionTemplate2")
    public TransactionTemplate transactionTemplate(@Qualifier("secondTransactionManager")PlatformTransactionManager transactionManager){
        return new TransactionTemplate(transactionManager);
    }

}
