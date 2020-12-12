package space.yizhu.kits;/* Created by yi on 12/1/2020.*/

import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

public class RandKit {
    //基础概率
    private static int baseChange = 1000;
    //基数时间
//    private static long baseTime = 12 * 60 * 60 * 1000;
    private static long baseTime = 3*1000;
    //基数时间
    private static Integer basePerson = 1000;
    //难度
    private static int def = 1;

    //奖品数
    private static  int spoils = 100;

    private static List<Long> times = new ArrayList<>();
    private static List<Integer> persons = new ArrayList<>();
    private static List<Integer> changes = new ArrayList<>();

    private static boolean gotIt = false;
    private static int nowI = 0;
    public static void main(String[] args) throws InterruptedException {
        changes.add(baseChange);
        times.add(baseTime);
        persons.add(basePerson);
      int change=0;
        int changeSum=0;
        for (int i = 0; i < spoils; i++) {
            for (int ch:changes){
                changeSum += ch;
            }
            nowI = i;

            gotIt = false;
            SysKit.print("开始第"+i+"次抽奖","第"+nowI+"次");
int mc;
            Integer p = 0;long t=System.currentTimeMillis();
            long sumTime = 0;
            long sumChange = 0;
            for (long t1:times){
                sumTime+=t1;
            }
            for (int c1:changes){
                sumChange+=c1;
            }
            double avgTime = sumTime / times.size();
            int avgChange = Math.toIntExact(sumChange / changes.size());

            while (!gotIt){
                persons.set(i, persons.get(i) -2);
                try {
                    change = avgChange-p;
                } catch (Exception e) {
                    SysKit.print(change);
                    return;
                }
                mc=ToolKit.getRandomInt(change+1,0);
                if (change==mc){
                    jiangPin();
                }
                sleep(1);
                p++;

            }

            times.add(System.currentTimeMillis() - t);
            sumTime = 0;
            for (int y=0;y<times.size();y++){
                if (y!=0)
                sumTime+=times.get(y);
            }
             avgTime = sumTime /( times.size()-1);

            avgChange=(avgChange+p)/2;
            avgChange = (int) (((avgChange * (baseTime / (avgTime * 1.0)))));
            persons.add(avgChange);
            changes.add(avgChange);
            SysKit.print(p+":"+changes.get(nowI+1));

        }

    }

    private static void  jiangPin(){
        SysKit.print("you got it","第"+nowI+"次");
        gotIt = true;

    }

}
