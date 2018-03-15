package com.janloong.jlogger.utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.Build;
import org.elasticsearch.Version;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.main.MainResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.cluster.ClusterName;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.profile.ProfileShardResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;


/**
 * des: elasticsearch工具集
 *
 * @author Janloong
 * @create 2017-11-10 15:15
 **/
public class ElasticsearchUtils {

    private static final Logger logger = LoggerFactory.getLogger(ElasticsearchUtils.class);
    private static RestHighLevelClient client = null;
    private static String token = null;

    static {
        try {
            HttpHost http = new HttpHost("elastic.yijiao2o.cn", 80,
                    "http");
            String username = "elastic";
            String password = "yjsh@kibana";
            byte[] bytes = Base64.encodeBase64((username + ":" + password).getBytes());
            String auth = new String(bytes, "UTF-8");
            BasicHeader header = new BasicHeader("Authorization", "Basic " + auth);
            client = new RestHighLevelClient(RestClient.builder(http).setDefaultHeaders(new Header[]{header}));
            //client.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * des: 获取集群节点基本信息
     *
     * @author Janloong
     * @create 2017-12-22 16:14
     **/
    public static void infoApi() throws IOException {
        MainResponse info = client.info();
        ClusterName clusterName = info.getClusterName();
        String clusterUuid = info.getClusterUuid();
        String nodeName = info.getNodeName();
        Version version = info.getVersion();
        Build build = info.getBuild();
        logger.info("\n-ElasticsearchUtils-infoApi：" + "\n"
                + "clusterName:" + clusterName.toString() + "\n"
                + "clusterUuid:" + clusterUuid + "\n"
                + "nodeName:" + nodeName + "\n"
                + "version:" + version.toString() + "\n"
                + "build:" + build.toString() + "\n"
        );
    }

    /**
     * des: 查看索引信息
     *
     * @author Janloong
     * @create 2017-12-22 16:25
     **/
    public static void indexInfo() {
        IndexRequest indexRequest = new IndexRequest("yjsh-database-2017.12", "items");
            Boolean a=true;
        boolean b = a.booleanValue();
        String s = a.toString();
        client.indexAsync(indexRequest, new ActionListener<IndexResponse>() {
            @Override
            public void onResponse(IndexResponse indexResponse) {
                logger.info("\n-ElasticsearchUtils-onResponse：" + "\n"
                        + "getIndex:" + indexResponse.getIndex() + "\n"
                        + "getId:" + indexResponse.getId() + "\n"
                        + "getType:" + indexResponse.getType() + "\n"
                        + "getResult:" + indexResponse.getResult() + "\n"
                );
            }

            @Override
            public void onFailure(Exception e) {
                logger.info("\n-ElasticsearchUtils-onFailure：" + "\n"
                        + "失败了啊:" + e.getMessage() + "\n"
                );
            }
        });
    }

    public static void getIndex() throws IOException {
        //GetRequest getRequest = new GetRequest("yjsh-database-2017.12", "items", "f15314b0f94d41b7b266b814c7f55c3c");
        GetRequest getRequest = new GetRequest("yjsh-database-2017.12", "items", "");
        GetResponse documentFields = client.get(getRequest);

        String index = documentFields.getIndex();
        String type = documentFields.getType();
        Map<String, Object> source = documentFields.getSource();
        logger.info("\n-ElasticsearchUtils-getIndex：" + "\n"
                + "index:" + index + "\n"
                + "type:" + type + "\n"
                + "source:" + source.toString() + "\n"
        );

    }

    /**
     * des: 获取index信息
     *
     * @author Janloong
     * @create 2017-12-22 16:31
     **/
    public static void getIndexAsync() {
        GetRequest getRequest = new GetRequest("yjsh-database-2017.12", "items", "f15314b0f94d41b7b266b814c7f55c3c");
        client.getAsync(getRequest, new ActionListener<GetResponse>() {
            @Override
            public void onResponse(GetResponse documentFields) {
                //Map<String, Object> source = documentFields.getSource();
                String source = documentFields.getType();
                logger.info("\n-ElasticsearchUtils-onResponse：" + "\n"
                        + "source:" + source + "\n"
                );
            }

            @Override
            public void onFailure(Exception e) {
                logger.info("\n-ElasticsearchUtils-onFailure：" + "\n"
                        + "失败了:" + e.getMessage() + "\n"
                );
            }
        });

    }


    /**
     * des: 异步搜索API
     *
     * @author Janloong
     * @create 2017-12-22 16:49
     **/
    public static void searchApi() {
        MatchQueryBuilder queryBuilder = new MatchQueryBuilder("", "");
        //模糊匹配
        queryBuilder.fuzziness(Fuzziness.AUTO);
        queryBuilder.prefixLength(3);
        queryBuilder.maxExpansions(10);


        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //searchSourceBuilder.query(queryBuilder);
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        searchSourceBuilder.from(0);
        searchSourceBuilder.size(10);

        SearchRequest searchRequest = new SearchRequest("yjsh-database-2017.12");
        //SearchRequest searchRequest = new SearchRequest(new String[]{"yjsh-database-2017.12"},searchSourceBuilder);
        searchRequest.source(searchSourceBuilder);
        searchRequest.types("items");
        client.searchAsync(searchRequest, new ActionListener<SearchResponse>() {
            @Override
            public void onResponse(SearchResponse searchResponse) {
                Map<String, ProfileShardResult> profileResults = searchResponse.getProfileResults();
                int totalShards = searchResponse.getTotalShards();
                SearchHits hits = searchResponse.getHits();
                SearchHit[] hits1 = hits.getHits();
                logger.info("\n-ElasticsearchUtils-onResponse：" + "\n"
                        + "profileResults:" + profileResults.toString() + "\n"
                        + "totalShards:" + totalShards + "\n"
                        + "hits1:" + hits1[1].getSourceAsString() + "\n"
                );
            }

            @Override
            public void onFailure(Exception e) {

            }
        });

    }

    public static void searchBuilder() {
        RangeQueryBuilder queryBuilder = QueryBuilders.rangeQuery("@timestamp").from(Instant.now().minus(1,
                ChronoUnit.HOURS)).to(Instant.now());
        logger.info("\n-ElasticsearchUtils-searchBuilder：" + "\n"
                + ":" + Instant.now() + "\n"
                + ":" + LocalDateTime.now() + "\n"
                + ":" + LocalDateTime.now().minusMinutes(10) + "\n"
        );
        MatchQueryBuilder queryBuilder1 = QueryBuilders.matchQuery("logsource", "tomcat");
        MatchQueryBuilder queryBuilder2 = QueryBuilders.matchQuery("web_mould", "bingdg-web");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(queryBuilder);
        searchSourceBuilder.postFilter(queryBuilder1);
        searchSourceBuilder.postFilter(queryBuilder2);
        //SearchRequest searchRequest = new SearchRequest("yjsh-database-2017.12");
        SearchRequest searchRequest = new SearchRequest("logstash-2017.12.27");
        //SearchRequest searchRequest = new SearchRequest(new String[]{"yjsh-database-2017.12"},searchSourceBuilder);
        searchRequest.source(searchSourceBuilder);
        searchRequest.types("doc");

        //searchRequest.indicesOptions()

        client.searchAsync(searchRequest, new ActionListener<SearchResponse>() {
            @Override
            public void onResponse(SearchResponse searchResponse) {
                long totalHits = searchResponse.getHits().getTotalHits();
                SearchHit[] hits = searchResponse.getHits().getHits();
                logger.info("\n数量-ElasticsearchUtils-onResponse：" + "\n"
                        + "totalHits:" + totalHits + "\n"
                        + "hits:" + hits.length + "\n"
                );
                for (SearchHit sh : hits) {
                    String sourceAsString = sh.getSourceAsString();
                    logger.info("\n-ElasticsearchUtils-onResponse：" + "\n"
                            + "source:" + sourceAsString + "\n"
                            + "-------------------------------------" + "\n"
                    );
                }
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
    }


    /**
     * des: 日志同步查询一 ，根据条件查询
     *
     * @param index  索引
     * @param filter 查询条件
     * @author Janloong
     * @create 2017-12-27 12:08
     **/
    public static String searchWithFilter(String index, Map<String, Object> filter) {
        RangeQueryBuilder queryBuilder = QueryBuilders.rangeQuery("@timestamp").from(Instant.now().minus(1,
                ChronoUnit.HOURS)).to(Instant.now());
        MatchQueryBuilder queryBuilder1 = QueryBuilders.matchQuery("logsource", "tomcat");
        return null;
    }
}
