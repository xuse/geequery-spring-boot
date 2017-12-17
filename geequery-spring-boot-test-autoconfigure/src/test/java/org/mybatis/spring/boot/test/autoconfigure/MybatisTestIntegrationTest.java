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
package org.mybatis.spring.boot.test.autoconfigure;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.easyframe.enterprise.spring.CommonDao;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.CacheInterceptor;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.interceptor.TransactionInterceptor;

/**
 * Integration tests for {@link MybatisTest}.
 *
 * @author wonwoo
 * @since 1.2.1
 */
@RunWith(SpringRunner.class)
@MybatisTest
@TestPropertySource(properties = {
  "mybatis.type-aliases-package=org.mybatis.spring.boot.test.autoconfigure",
  "logging.level.org.springframework.jdbc=debug",
  "spring.datasource.schema=classpath:org/mybatis/spring/boot/test/autoconfigure/schema.sql"
})
public class MybatisTestIntegrationTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Autowired
  private CommonDao sqlSession;

  @Autowired
  private ApplicationContext applicationContext;

  @Test
  public void testSqlSession() {
    
  }

  @Test
  public void testMapper() {
   
  }

  @Test
  public void testAutoConfigureComponents() {
    // @AutoConfigureMybatis
    this.applicationContext.getBean(JdbcTemplate.class);
    this.applicationContext.getBean(NamedParameterJdbcTemplate.class);
    this.applicationContext.getBean(DataSourceTransactionManager.class);
    this.applicationContext.getBean(TransactionInterceptor.class);
    // @AutoConfigureCache
    this.applicationContext.getBean(CacheManager.class);
    this.applicationContext.getBean(CacheInterceptor.class);
  }

  @Test
  public void didNotInjectExampleComponent() {
    this.thrown.expect(NoSuchBeanDefinitionException.class);
    this.applicationContext.getBean(ExampleComponent.class);
  }

}
