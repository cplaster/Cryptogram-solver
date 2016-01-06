package com.xyphoid.cryptogram_solver;

/**
 * Created by Chad on 1/5/2016.
 */
import java.util.AbstractCollection;
import java.util.Iterator;
import java.util.ArrayList;

public class CollectionUtils {

    private CollectionUtils() {
    }

    public static String join(AbstractCollection<String> s) {
        return CollectionUtils.join(s, "");
    }

    public static String join
            (AbstractCollection<String> s, String delimiter) {
        if (s == null || s.isEmpty()) return "";
        Iterator<String> iter = s.iterator();
        StringBuilder builder = new StringBuilder(iter.next());
        while (iter.hasNext()) {
            builder.append(delimiter).append(iter.next());
        }
        return builder.toString();
    }
}
