package com.pjh.client.database.mybatis;

import com.pjh.client.configuration.ColumnNameMapper;
import com.pjh.client.data.DatabaseConfigurationData;
import com.pjh.client.database.DataHandler;
import com.pjh.client.message.Message;
import com.pjh.client.message.Status;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
public class MybatisDBHandler implements DataHandler {
    private final DatabaseConfigurationData config;
    private final ColumnNameMapper columnNameMapper;
    private final SqlSessionFactory sqlSessionFactory;
    private final String serviceName;

    public MybatisDBHandler(String serviceName, DatabaseConfigurationData config) {
        this.config = config;
        this.serviceName = serviceName;
        this.sqlSessionFactory = MybatisHelper.getSqlSessionFactory(serviceName, config);
        this.columnNameMapper = config.getMapper();
    }

    @Override
    public List<Message> selectMessage() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            return null;
            //return getMapper...
        } catch (Exception e) {
            log.error("Message select fail!! : {}", e.toString());
            return null;
        }
    }


    @Override
    public void updateMessage(Message m) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            //mapper update...
            session.commit();
            log.info("DB UPDATE : " + m);
        } catch (Exception e) {
            log.error("Update Message error : " + e.toString());
        }
    }

    @Override
    public void deleteMessage(Message m) {

    }

    @Override
    public void addLogMessage(Message m) {

    }

    @Override
    public boolean connectTest() {
        if (sqlSessionFactory == null)
            return false;

        try (SqlSession session = sqlSessionFactory.openSession()) {
            log.info("DB Connection test success[{}]", serviceName);
            return true;
        } catch (Exception e) {
            log.error("DB Connection test fail[{}] : {}", serviceName, e.toString());
            return false;
        }
    }

    @Override
    public void cleanup() {
        log.info("Cleanup Mybatis DataHandler!");
    }
}
