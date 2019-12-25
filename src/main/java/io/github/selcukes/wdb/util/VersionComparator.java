package io.github.selcukes.wdb.util;

import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class VersionComparator
    implements Comparator<String> {
    final Pattern p = Pattern.compile("^\\d+");

    @Override
    public int compare(String object1, String object2) {
        Matcher m = p.matcher(object1);
        Integer number1 = null;
        if (!m.find()) {
            Matcher m1 = p.matcher(object2);
            if (m1.find()) {
                return object2.compareTo(object1);
            } else {
                return object1.compareTo(object2);
            }
        } else {
            Integer number2 = null;
            number1 = Integer.parseInt(m.group());
            m = p.matcher(object2);
            if (!m.find()) {
                Matcher m1 = p.matcher(object1);
                if (m1.find()) {
                    return object2.compareTo(object1);
                } else {
                    return object1.compareTo(object2);
                }
            } else {
                number2 = Integer.parseInt(m.group());
                int comparison = number1.compareTo(number2);
                if (comparison != 0) {
                    return comparison;
                } else {
                    return object1.compareTo(object2);
                }
            }
        }
    }

}