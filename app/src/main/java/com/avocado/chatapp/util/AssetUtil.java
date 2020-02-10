package com.avocado.chatapp.util;

import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public final class AssetUtil {

    private AssetUtil() {
    }

    public static String readTextFileFromAsset(Context context, String fileName) throws Exception {
        StringBuilder output = new StringBuilder();
        try (InputStream inputStream = context.getResources().getAssets()
                .open(fileName); InputStreamReader inputStreamReader = new InputStreamReader(inputStream); BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                output.append(line);
            }
        } catch (Exception e) {
            throw new Exception(e);
        }
        return output.toString();
    }
}
