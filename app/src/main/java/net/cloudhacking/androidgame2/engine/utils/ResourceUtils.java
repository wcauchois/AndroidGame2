package net.cloudhacking.androidgame2.engine.utils;

import android.util.Log;

import net.cloudhacking.androidgame2.engine.GameSkeleton;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Andrew on 1/19/2015.
 */
public class ResourceUtils extends Loggable {

    public static String readTextFileFromRawResource(final int resourceId) {

        InputStream is = GameSkeleton.getInstance()
                                     .getResources()
                                     .openRawResource(resourceId);
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader bufferedReader = new BufferedReader(isr);

        String nextLine;
        StringBuilder body = new StringBuilder();

        try {
            while ((nextLine = bufferedReader.readLine()) != null) {
                body.append(nextLine);
                body.append('\n');
            }
        } catch (IOException e) {
            Log.e(TAG, "error reading text file", e);
            return null;
        }

        return body.toString();
    }
}
