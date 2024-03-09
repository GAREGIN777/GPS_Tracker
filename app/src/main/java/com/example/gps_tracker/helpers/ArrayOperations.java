package com.example.gps_tracker.helpers;

import java.util.HashMap;

public class ArrayOperations {
    public static HashMap arrayToMap(Integer[] keys,
                                     String[] values)
    {
        // two variables to store the length of the two
        // given arrays
        int keysSize = keys.length;
        int valuesSize = values.length;

        // if the size of both arrays is not equal, throw an
        // IllegalArgumentsException
        if (keysSize != valuesSize) {
            throw new IllegalArgumentException("The number of keys doesn't match the number of values.");
        }

        // if the length of the arrays is 0, then return an
        // empty HashMap
        if (keysSize == 0) {
            return new HashMap();
        }

        // create a new HashMap of the type of keys arrays
        // and values array
        HashMap<Integer, String> map = new HashMap<Integer, String>();

        // for every key, value
        for (int i = 0; i < keysSize; i++) {

            // add them into the HashMap by calling the
            // put() method on the key-value pair
            map.put(keys[i], values[i]);
        }

        // return the HashMap
        return map;
    }

}
