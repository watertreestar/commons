package com.young.commons.collection;

import java.util.ArrayList;
import java.util.List;

public class Lists {
    public static List<Integer> fromTo(int from, int to) {
        ArrayList<Integer> result = new ArrayList<>();
        for (int i = from; i <= to; ++i) {
            result.add(i);
        }
        return result;
    }
}
