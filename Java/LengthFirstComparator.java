package edu.sdsc.wellderly.rules;

import java.util.Comparator;


public class LengthFirstComparator implements Comparator<String> {
    @Override
    public int compare(String o1, String o2) {             
        if (o2.length()!=o1.length()) {
        	return o1.length()-o2.length(); //overflow impossible since lengths are non-negative
        }
       return o1.compareTo(o2);
    }
}