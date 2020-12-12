package space.yizhu.kits;/* Created by yi on 12/2/2020.*/

import org.apache.commons.lang3.StringUtils;

public class SafeKit {
    private static String desensitizName(String fullName) {
        String name=fullName;
        if (CharKit.isNotNull(fullName)) {
            name = StringUtils.left(fullName, 1);
            return StringUtils.rightPad(name, StringUtils.length(fullName), "*");
        }
        return name;
    }
    private static String desensitizPhoneNumber(String phoneNumber){
        if(CharKit.isNotNull(phoneNumber)){
            phoneNumber = phoneNumber.replaceAll("(\\w{3})\\w*(\\w{4})", "$1****$2");
        }
        return phoneNumber;
    }

    private static String desensitizIdNumber(String idNumber){
        if (CharKit.isNotNull(idNumber)) {
            if (idNumber.length() == 15){
                idNumber = idNumber.replaceAll("(\\w{6})\\w*(\\w{3})", "$1******$2");
            }else
            if (idNumber.length() == 18){
                idNumber = idNumber.replaceAll("(\\w{6})\\w*(\\w{3})", "$1*********$2");
            }else {
                idNumber= StringUtils.left(idNumber, 6).concat(StringUtils.removeStart(StringUtils.leftPad(StringUtils.right(idNumber, 3), StringUtils.length(idNumber), "*"), "******"));
            }
        }
        return idNumber;
    }


    private static String desensitizAddress(String address){
        if (CharKit.isNotNull(address)) {
            return StringUtils.left(address, 3).concat(StringUtils.removeStart(StringUtils.leftPad(StringUtils.right(address, address.length()-11), StringUtils.length(address), "*"), "***"));
        }
        return address;
}}
