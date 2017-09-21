package com.lai.LogToMongo;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;

/**
 * Created by lailai on 2017/9/21.
 * log4j自定义的输出器
 * 自定义appender输出到MongoDB，只需要继承AppenderSkeleton类，并实现几个方法即可完成
 */
public class MongoAppender extends AppenderSkeleton {

    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;
    private MongoCollection<BasicDBObject> logsCollection;
    private String connectionUrl;
    private String databaseName;
    private String collectionName;

    /**
     * 根据log4j.properties中的配置创建mongodb连接
     * LoggingEvent提供getMessage()函数来获取日志消息
     * 往配置的记录日志的collection中插入日志消息
     * @param event
     */
    @Override
    protected void append(LoggingEvent event) {

        if(mongoClient==null){
            MongoClientURI connectionString=new MongoClientURI(connectionUrl);
            mongoClient=new MongoClient(connectionString);
            mongoDatabase=mongoClient.getDatabase(databaseName);
            logsCollection=mongoDatabase.getCollection(collectionName,BasicDBObject.class);
        }
        logsCollection.insertOne((BasicDBObject)event.getMessage());
    }

    /**
     * 重写close函数：关闭mongodb的
     */
    @Override
    public void close() {

        if(mongoClient!=null){
            mongoClient.close();
        }
    }

    @Override
    public boolean requiresLayout() {
        return false;
    }

    public String getConnectionUrl() {
        return connectionUrl;
    }

    public void setConnectionUrl(String connectionUrl) {
        this.connectionUrl = connectionUrl;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }
}
