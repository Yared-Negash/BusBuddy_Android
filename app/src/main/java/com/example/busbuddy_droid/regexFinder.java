package com.example.busbuddy_droid;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class regexFinder {

   public static LinkedList<String> findString(String regex, String text){
       LinkedList<String> response = new LinkedList<String>();
       Pattern verifyRegEx = Pattern.compile(regex);
       Matcher compare = verifyRegEx.matcher(text);

       while(compare.find()){
           if(compare.group().length() != 0){
               System.out.println(compare.group().trim());
               response.add(compare.group());
           }
       }
       return response;
   }
}
