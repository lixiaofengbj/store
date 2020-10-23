package cn.store.store_web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.interceptor.TransactionInterceptor;
import tk.mybatis.spring.annotation.MapperScan;

import java.util.Properties;

@SpringBootApplication
@Controller
@ComponentScan(value = {"cn.store"})
@MapperScan(basePackages= {"cn.store.mapper"})
public class StoreWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(StoreWebApplication.class, args);
    }

    @Bean
    public TransactionInterceptor transactionInterceptor (PlatformTransactionManager platformTransactionManager) {
        TransactionInterceptor transactionInterceptor = new TransactionInterceptor();
        transactionInterceptor.setTransactionManager(platformTransactionManager);
        Properties transactionAttributes = new Properties();
        transactionAttributes.setProperty("insert*", "PROPAGATION_REQUIRED,-Throwable");
        transactionAttributes.setProperty("update*", "PROPAGATION_REQUIRED,-Throwable");
        transactionAttributes.setProperty("delete*", "PROPAGATION_REQUIRED,-Throwable");
        transactionAttributes.setProperty("save*", "PROPAGATION_REQUIRED,-Throwable");
        transactionAttributes.setProperty("get*", "PROPAGATION_SUPPORTS,-Throwable,readOnly");
        transactionAttributes.setProperty("query*", "PROPAGATION_SUPPORTS,-Throwable,readOnly");
        transactionAttributes.setProperty("find*", "PROPAGATION_SUPPORTS,-Throwable,readOnly");
        transactionAttributes.setProperty("select*", "PROPAGATION_SUPPORTS,-Throwable,readOnly");
        transactionInterceptor.setTransactionAttributes(transactionAttributes);
        return transactionInterceptor;
    }
}
