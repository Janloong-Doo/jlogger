import com.janloong.jlogger.utils.ElasticsearchUtils;

/**
 * @author Janloong
 * @create 2017-12-22 14:44
 **/
public class ElkTest {

    public static void main(String[] args) {

        try {
            //ElasticsearchUtils.infoApi();
            //ElasticsearchUtils.indexInfo();
            //ElasticsearchUtils.getIndex();
            //ElasticsearchUtils.getIndexAsync();
            //ElasticsearchUtils.searchApi();
            ElasticsearchUtils.searchBuilder();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
