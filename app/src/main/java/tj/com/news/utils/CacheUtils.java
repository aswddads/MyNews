package tj.com.news.utils;

import android.content.Context;

/**
 * Created by Jun on 17/4/16.
 * 网络缓存工具类
 */

public class CacheUtils {
    /**
     * 以url为key，以json为value，保存在本地
     * @param url
     * @param json
     */
    public static void setCache(Context ctx, String url, String json){
//        也可以用文件缓存  以MD5(url)为文件名，以json为文件内容
        SpUtils.putString(ctx,url,json);
    }

    /**
     * 获取缓存
     * @param ctx
     * @param url
     * @return
     */
    public static String getCache(Context ctx, String url){
//        文件缓存：查找一个文件叫MD5(url)，然后获取缓存
        return SpUtils.getString(ctx,url,null);
    }
}
