package mad9132.maddapp.utils;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * JSONFileManager
 *
 * References
 * 1. https://developer.android.com/guide/topics/data/data-storage.html#filesInternal
 * 2. https://developer.android.com/training/basics/data-storage/files.html
 */
public class JSONFileManager {

    public static boolean exitsAsLocalFile(Context context, String filename) {
        File localFile = context.getFileStreamPath(filename);
        if ( localFile == null || !localFile.exists() ) {
            return false;
        }
        return true;
    }

    public static String readJSON(Context context, int rawResourceId) {
        String jsonString = null;
        try {
            InputStream is = context.getResources().openRawResource(rawResourceId);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            jsonString = new String(buffer, "UTF-8");
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return jsonString;
        }
    }

    public static String readJSON(Context context, String filename) {
        String jsonString = null;
        try {
            InputStream is = context.openFileInput(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            jsonString = new String(buffer, "UTF-8");
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return jsonString;
        }
    }

    public static boolean writeJSON(Context context, String filename, String json) {
        try {
            FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
            fos.write(json.getBytes());
            fos.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}