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

import static org.assertj.core.api.Assertions.assertThat;

import javax.persistence.EntityManagerFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.EmbeddedDataSourceConfiguration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Tests for {@link MybatisAutoConfiguration}
 *
 * @author Eddú Meléndez
 * @author Josh Long
 * @author Kazuki Shimizu
 */
public class GeeQueryAutoConfigurationTest {

	private AnnotationConfigApplicationContext context;

	@Before
	public void init() {
		this.context = new AnnotationConfigApplicationContext();
	}

	@After
	public void closeContext() {
		if (this.context != null) {
			this.context.close();
		}
	}

	@Test
	public void testNoDataSource() throws Exception {
		this.context.register(GeeQueryAutoConfiguration.class,
				PropertyPlaceholderAutoConfiguration.class);
		this.context.refresh();
		assertThat(this.context.getBeanNamesForType(EntityManagerFactory.class)).isEmpty();
	}

	@Test
	public void testDefaultConfiguration() {
		this.context.register(EmbeddedDataSourceConfiguration.class,
				GeeQueryAutoConfiguration.class,
				PropertyPlaceholderAutoConfiguration.class);
		this.context.refresh();
		assertThat(this.context.getBeanNamesForType(EntityManagerFactory.class)).hasSize(1);
//		assertThat(this.context.getBean(SqlSessionTemplate.class).getExecutorType()).isEqualTo(ExecutorType.SIMPLE);
//		assertThat(this.context.getBean(SqlSessionFactory.class).getConfiguration().isMapUnderscoreToCamelCase()).isFalse();
	}

//	@Test
//	public void testWithConfigLocation() {
//		TestPropertyValues.of("mybatis.config-location:mybatis-config.xml").applyTo(this.context);
//		this.context.register(EmbeddedDataSourceConfiguration.class,
//				MybatisAutoConfiguration.class, MybatisMapperConfiguration.class,
//				PropertyPlaceholderAutoConfiguration.class);
//		this.context.refresh();
//		assertThat(this.context.getBeanNamesForType(SqlSessionFactory.class)).hasSize(1);
//		assertThat(this.context.getBeanNamesForType(CityMapperImpl.class)).hasSize(1);
//		assertThat(this.context.getBean(SqlSessionTemplate.class).getExecutorType()).isEqualTo(ExecutorType.BATCH);
//		assertThat(this.context.getBean(SqlSessionFactory.class).getConfiguration().isMapUnderscoreToCamelCase()).isTrue();
//	}
//
//	@Test
//	public void testWithCheckConfigLocationFileExists() {
//		TestPropertyValues.of("mybatis.config-location:mybatis-config.xml",
//				"mybatis.check-config-location=true").applyTo(this.context);
//		this.context.register(EmbeddedDataSourceConfiguration.class,
//				MybatisAutoConfiguration.class);
//		this.context.refresh();
//		assertThat(this.context.getBeanNamesForType(SqlSessionFactory.class)).hasSize(1);
//	}
}
