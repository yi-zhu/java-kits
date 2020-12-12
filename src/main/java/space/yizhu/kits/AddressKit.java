package space.yizhu.kits;/* Created by yi on 12/3/2020.*/

public class AddressKit {
    public static boolean isIpInRange(String start, String end, String real) {
        if (null==start||null==end||null==real){
            return false;
        }
        try {
            String[] stIpNums = start.split("\\.");
            String[] edIpNums = end.split("\\.");
            String[] ipNums = real.split("\\.");
            if (stIpNums.length != edIpNums.length||stIpNums.length != ipNums.length) {
                return false;
            }
            int forwordNums = 0,backNums=0;
            int s,e,r;
            for (int i = 0; i < stIpNums.length; i++) {
                r = Integer.parseInt(ipNums[i]);
                s = Integer.parseInt(stIpNums[i]);
                e = Integer.parseInt(edIpNums[i]);
                if (s>0) {

                    if (r<s||r>e) {
                        if (forwordNums==0){
                            return false;
                        }
                    }
                }else {
                    if (e>0){
                        if (r>e&&forwordNums==0) {
                            return false;
                        }
                    }
                }
                forwordNums += e > s ? 1 : 0;
            }
            return true;
        } catch (NumberFormatException e) {
            SysKit.print(e,"isIpInRange");
            return false;

        }
    }

    /**
     * 判断实际ip是否在预存的ip段中
     *
     * @param large 预存的IP段
     * @param small 要对比的实际ip
     * @return bool
     */
    public static boolean isSameIp(String large, String small) {
        if (large.equals(small)) {
            return true;
        } else {
            String[] larges = large.split("\\.");
            String[] smalls = small.split("\\.");
            if (larges.length != smalls.length) {
                return false;
            }
            for (int i = 0; i < larges.length; i++) {
                if (!larges[i].equals("0")) {
                    if (larges[i].equals(smalls[i])) {
                        continue;
                    }
                } else {
                    continue;
                }
                return false;
            }
            return true;

        }
    }


    public static void main(String[] args) {
        SysKit.print(isIpInRange("0.1.7.0", "0.161.0.1", "192.166.1.2")+"");
        SysKit.print(isSameIp("192.0.1.0",  "192.168.1.1")+"");
    }
}
