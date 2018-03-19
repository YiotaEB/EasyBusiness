package com.easybusiness.eb_androidapp.Utilities;

import java.security.MessageDigest;

public class Hash {

    private static final String MD5 = "MD5";

    public static String MD5(String text) {

        if (text == null) return "";

        try {
            MessageDigest md = MessageDigest.getInstance(MD5);
            md.update(text.getBytes());
            byte byteData[] = md.digest();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++)
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            return sb.toString();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}
