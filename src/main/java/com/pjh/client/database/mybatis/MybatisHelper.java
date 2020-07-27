package com.pjh.client.database.mybatis;

import com.pjh.client.data.DatabaseConfigurationData;
import com.pjh.client.message.Result;
import com.pjh.client.message.Status;
import com.pjh.client.message.ValueEnumTypeHandler;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import javax.sql.DataSource;

public class MybatisHelper {
    public static SqlSessionFactory getSqlSessionFactory(String serviceName, DatabaseConfigurationData config) {
        DataSource dataSource = MessageDataSourceFactory.getDataSource(config);
        TransactionFactory transactionFactory = new JdbcTransactionFactory();
        Environment environment = new Environment(serviceName, transactionFactory, dataSource);
        Configuration configuration = new Configuration(environment);
        configuration.addMapper(null);
        configuration.getTypeHandlerRegistry().register(Status.class, new ValueEnumTypeHandler<>(Status.class));
        configuration.getTypeHandlerRegistry().register(Result.class, new ValueEnumTypeHandler<>(Result.class));
        return new SqlSessionFactoryBuilder().build(configuration);
    }
}
