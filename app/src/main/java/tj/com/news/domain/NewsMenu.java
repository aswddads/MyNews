package tj.com.news.domain;

import java.util.ArrayList;

/**
 * Created by Jun on 17/4/16.  分类信息封装
 *      使用Gson解析时，对象书写技巧
 *      1.逢｛ ｝创建对象，逢［ ］创建集合（ArrayList）
 *      2.所有字段名称要和json返回一致
 */

public class NewsMenu {
    public int retcode;
    public ArrayList<Integer> extend;
    public ArrayList<NewsMenuData> data;
//  侧边栏对象
    public class NewsMenuData {
        public int id;
        public String title;
        public int type;
        public ArrayList<NewsTabData> children;
    }
    //页签的对象
    public class NewsTabData{
        public int id;
        public String title;
        public int type;
        public String url;
    }
}
