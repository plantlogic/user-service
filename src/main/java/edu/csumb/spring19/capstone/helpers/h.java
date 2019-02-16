package edu.csumb.spring19.capstone.helpers;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class h {

    /**
     * Decode string from UTF-8 for transfer via URL
     * @param in String to decode
     * @return Decoded string
     */
    public static String decode(String in){
        try {
            return URLDecoder.decode(in, "UTF-8");
        } catch (UnsupportedEncodingException e) {return null;}
    }
}
