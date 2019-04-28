import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 获取澳元汇率
 */
public class UtilExchange {
    public static final String DEF_CHATSET = "UTF-8";
    public static final int DEF_CONN_TIMEOUT = 30000;
    public static final int DEF_READ_TIMEOUT = 30000;
    public static String userAgent =  "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.66 Safari/537.36";

    //配置您申请的KEY
    public static final String APPKEY ="b34d6d12dc17cbaa581900bc42c8d085";

    public static double getAUSDollarExRate(){
        double ausDollarExRate = 0.0;
        String res =null;
        String url ="http://web.juhe.cn:8080/finance/exchange/rmbquot";
        Map params = new HashMap();
        params.put("key",APPKEY);
        params.put("type","1");

        try {
            res =net(url, params, "GET");
            JSONObject jsonObjectRes = JSONObject.fromObject(res);
            JSONArray arrJsonObjResult;

            if(jsonObjectRes.getInt("error_code")==0){
                //System.out.println(jsonObjectRes.get("result"));

                arrJsonObjResult = jsonObjectRes.getJSONArray("result");

                JSONObject tempJsonObj;
                for (int i = 0; i < arrJsonObjResult.size(); i++) {
                    tempJsonObj = arrJsonObjResult.getJSONObject(i);
                    if (tempJsonObj.containsKey("澳大利亚元")){
                        ausDollarExRate = tempJsonObj.getJSONObject("澳大利亚元").getDouble("fSellPri");
                        break;
                    }
                }
            }else{
                System.out.println(jsonObjectRes.get("error_code")+":"+jsonObjectRes.get("reason"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ausDollarExRate;
    }

    /**
     *
     * @param strUrl 请求地址
     * @param params 请求参数
     * @param method 请求方法
     * @return  网络请求字符串
     * @throws Exception
     */
    public static String net(String strUrl, Map params,String method) throws Exception {
        HttpURLConnection conn = null;
        BufferedReader reader = null;
        String rs = null;
        try {
            StringBuffer sb = new StringBuffer();
            if(method==null || method.equals("GET")){
                strUrl = strUrl+"?"+urlencode(params);
            }
            URL url = new URL(strUrl);
            conn = (HttpURLConnection) url.openConnection();
            if(method==null || method.equals("GET")){
                conn.setRequestMethod("GET");
            }else{
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
            }
            conn.setRequestProperty("User-agent", userAgent);
            conn.setUseCaches(false);
            conn.setConnectTimeout(DEF_CONN_TIMEOUT);
            conn.setReadTimeout(DEF_READ_TIMEOUT);
            conn.setInstanceFollowRedirects(false);
            conn.connect();
            if (params!= null && method.equals("POST")) {
                try (DataOutputStream out = new DataOutputStream(conn.getOutputStream())) {
                    out.writeBytes(urlencode(params));
                }
            }
            InputStream is = conn.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, DEF_CHATSET));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sb.append(strRead);
            }
            rs = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
        return rs;
    }

    //将map型转为请求参数型
    public static String urlencode(Map<String,Object>data) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry i : data.entrySet()) {
            try {
                sb.append(i.getKey()).append("=").append(URLEncoder.encode(i.getValue()+"","UTF-8")).append("&");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}