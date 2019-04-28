/**
 *货币汇率调用示例代码 － 聚合数据
 *在线接口文档：http://www.juhe.cn/docs/23
 **/

public class MainClass{

    public static void main(String[] args) throws Exception{

        while (true){
            double ausCurrentExrate = UtilExchange.getAUSDollarExRate();
            System.out.println("Current AUS dollar's foreign exchange rate: " + ausCurrentExrate);

            if ((ausCurrentExrate != 0) && (ausCurrentExrate <= 450)){
                UtilShortMessage.mobileQuery();
                System.out.println("send megg");
            }

            Thread.sleep(1000 * 100);
        }
    }
}


