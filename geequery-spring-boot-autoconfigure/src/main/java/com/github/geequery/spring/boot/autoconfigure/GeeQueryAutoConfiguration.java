/**
 *    Copyright 2015-2017 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.github.geequery.spring.boot.autoconfigure;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.easyframe.enterprise.spring.CommonDao;
import org.easyframe.enterprise.spring.CommonDaoImpl;
import org.easyframe.enterprise.spring.JefJpaDialect;
import org.easyframe.enterprise.spring.SessionFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import jef.database.SessionFactory;

/**
 * {@link EnableAutoConfiguration Auto-Configuration} for Geequery. Contributes
 * a {@link SqlSessionFactory} and a {@link SqlSessionTemplate}.
 *
 * If {@link org.Geequery.spring.annotation.MapperScan} is used, or a
 * configuration file is specified as a property, those will be considered,
 * otherwise this auto-configuration will attempt to register mappers based on
 * the interface definitions in or under the root auto-configuration package.
 *
 * @author Joey
 */
@org.springframework.context.annotation.Configuration
@ConditionalOnClass({ SessionFactory.class, SessionFactoryBean.class })
@ConditionalOnBean(DataSource.class)
@EnableConfigurationProperties(GeeQueryProperties.class)
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
@EnableTransactionManagement
public class GeeQueryAutoConfiguration {

	private static final Logger logger = LoggerFactory.getLogger(GeeQueryAutoConfiguration.class);

	private final GeeQueryProperties properties;

	private final ResourceLoader resourceLoader;

	public GeeQueryAutoConfiguration(GeeQueryProperties properties, ResourceLoader resourceLoader) {
		this.properties = properties;
		this.resourceLoader = resourceLoader;
	}

	@PostConstruct
	public void checkConfigFileExists() {
	}

	@Bean
	@ConditionalOnMissingBean
	public EntityManagerFactory entityManagerFactory(DataSource dataSource, Environment env) {
		SessionFactoryBean bean = new org.easyframe.enterprise.spring.SessionFactoryBean();
		bean.setDataSource(dataSource);
		bean.setAllowDropColumn(properties.isAllowDropColumn());
		bean.setAlterTable(properties.isAlterTable());
		bean.setAnnotatedClasses(properties.getAnnotatedClasses());
		bean.setCacheDebug(properties.isCacheDebug());
		bean.setCacheLevel1(properties.isCacheLevel1());
		bean.setCreateTable(properties.isCreateTable());
		bean.setDbInitHandler(properties.getDbInitHandler());
		bean.setDebug(properties.isShowSql());
		bean.setDynamicTables(properties.getDynamicTables());
		bean.setGlobalCacheLiveTime(properties.getGlobalCacheLiveTime());
		bean.setInitData(properties.isInitData());
		bean.setInitDataCharset(properties.getInitDataCharset());
//		bean.setMaxPoolSize(properties.get)
//		bean.setMinPoolSize(minPoolSize);
		bean.setNamedQueryFile(properties.getNamedQueryFile());
		bean.setNamedQueryTable(properties.getNamedQueryTable());
		bean.setPackagesToScan(properties.getPackagesToScan());
		bean.setRegisteNonMappingTableAsDynamic(properties.isRegisteNonMappingTableAsDynamic());
		bean.setTransactionMode(properties.getTransactionMode());
		bean.setUseDataInitTable(properties.isUseDataInitTable());
		bean.setPackagesToScan(new String[] { "com.github.geequery.springdata.test.entity" });
		bean.afterPropertiesSet();
		return bean.getObject();
	}

	@Bean
	@ConditionalOnMissingBean
	public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory,DataSource dataSource) {
		switch(properties.getTransactionMode()){
		case JDBC:
			//此种场景下是使用其他第三方框架的事务管理器。
			logger.info("There's no thirdparty transactionManager. creating new DataSourceTransactionManager.");
			return new DataSourceTransactionManager(dataSource);
		case JPA:
			JpaTransactionManager transactionManager = new JpaTransactionManager();
			transactionManager.setEntityManagerFactory(entityManagerFactory);
			transactionManager.setJpaDialect(new JefJpaDialect());
			return transactionManager;
		case JTA:
			return null;
		default:
			throw new IllegalArgumentException("Unknown transaction manager:"+properties.getTransactionMode());
		}
	}

	@Bean
	@ConditionalOnMissingBean
//	@ConditionalOnBean(EntityManagerFactory.class)
	CommonDao commonDao(@Qualifier("emf1") EntityManagerFactory entityManagerFactory) {
		return new CommonDaoImpl(entityManagerFactory);
	}

}
