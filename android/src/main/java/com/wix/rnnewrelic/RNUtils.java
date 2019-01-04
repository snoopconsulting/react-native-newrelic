package com.wix.rnnewrelic;

import com.facebook.react.bridge.ReadableMapKeySetIterator;
import com.facebook.react.bridge.ReadableNativeArray;
import com.facebook.react.bridge.ReadableNativeMap;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by rotemm on 29/06/2016.
 */
public class RNUtils {

    /**
     * ReadableNativeMap to HashMap, this has been implemented already in RN 0.26's ReadableNativeMap, kill it when possible
     * @param readableNativeMap
     * @return
     */
    public static HashMap<String, Object> toHashMap(ReadableNativeMap readableNativeMap) {
        ReadableMapKeySetIterator iterator = readableNativeMap.keySetIterator();
        HashMap<String, Object> hashMap = new HashMap<>();

        while (iterator.hasNextKey()) {
            String key = iterator.nextKey();
            switch (readableNativeMap.getType(key)) {
                case Null:
                    hashMap.put(key, null);
                    break;
                case Boolean:
                    hashMap.put(key, readableNativeMap.getBoolean(key));
                    break;
                case Number:
                    hashMap.put(key, readableNativeMap.getDouble(key));
                    break;
                case String:
                    hashMap.put(key, readableNativeMap.getString(key));
                    break;
                case Map:
                    hashMap.put(key, toHashMap(readableNativeMap.getMap(key)));
                    break;
                case Array: 
                /* 
                Comento esto y lo cambio por la línea de abajo, dado que con la original
                obtengo el error
                    error: incompatible types: ReadableArray cannot be converted to ReadableNativeArray
                
                La solución la encontre en 
                - https://github.com/wix/react-native-newrelic/pull/28/commits/a1b64afe379b8da6d8a23948288a13c061d5d404
                - https://github.com/wix/react-native-newrelic/pull/28
                - https://github.com/wix/react-native-newrelic/issues/25
                */
                    // hashMap.put(key, toArrayList(readableNativeMap.getArray(key)));
                    hashMap.put(key, toArrayList((ReadableNativeArray)readableNativeMap.getArray(key)));
                    break;
                default:
                    throw new IllegalArgumentException("Could not convert object with key: " + key + ".");
            }
        }
        return hashMap;
    }

    /**
     * ReadableNativeArray to ArrayList, this has been implemented already in RN 0.26's ReadableNativeMap, kill it when possible
     * @param readableNativeArray
     * @return
     */
    public static ArrayList<Object> toArrayList(ReadableNativeArray readableNativeArray) {
        ArrayList<Object> arrayList = new ArrayList<>();

        for (int i = 0; i < readableNativeArray.size(); i++) {
            switch (readableNativeArray.getType(i)) {
                case Null:
                    arrayList.add(null);
                    break;
                case Boolean:
                    arrayList.add(readableNativeArray.getBoolean(i));
                    break;
                case Number:
                    arrayList.add(readableNativeArray.getDouble(i));
                    break;
                case String:
                    arrayList.add(readableNativeArray.getString(i));
                    break;
                case Map:
                    arrayList.add(toHashMap(readableNativeArray.getMap(i)));
                    break;
                case Array: 
                /* 
                Comento esto y lo cambio por la línea de abajo, dado que con la original
                obtengo el error
                    error: incompatible types: ReadableArray cannot be converted to ReadableNativeArray
                
                La solución la encontre en 
                - https://github.com/wix/react-native-newrelic/pull/28/commits/a1b64afe379b8da6d8a23948288a13c061d5d404
                - https://github.com/wix/react-native-newrelic/pull/28
                - https://github.com/wix/react-native-newrelic/issues/25
                */
                    // arrayList.add(toArrayList(readableNativeArray.getArray(i)));
                    arrayList.add(toArrayList((ReadableNativeArray)readableNativeArray.getArray(i)));
                    break;
                default:
                    throw new IllegalArgumentException("Could not convert object at index: " + i + ".");
            }
        }
        return arrayList;
    }
}
