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
 *
 * @author Gerald.Hurdle@AlgonuinCollege.com
 */
public class JSONFileManager {

    /**
     * Return true if filename exists a a local file to context; otherwise, false.
     *
     * @param context
     * @param filename
     * @return boolean - true if filename exists; otherwise, false.
     *
     */
    public static boolean exitsAsLocalFile(Context context, String filename) {
        File localFile = context.getFileStreamPath(filename);
        if ( localFile == null || !localFile.exists() ) {
            return false;
        }
        return true;
    }

    /**
     * Return the JSON String stored at resourceId.
     *
     * Return null on error.
     *
     * @param context
     * @param rawResourceId
     * @return String - the JSON String
     */
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

    /**
     * Return the JSON String stored at (local file) filename.
     *
     * Return null on error.
     *
     * @param context
     * @param filename
     * @return String - the JSON String
     */
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

    /**
     * Store the JSON String to (local file) filename.
     *
     * @param context
     * @param filename
     * @param json
     * @return boolean - true on success; otherwise, false.
     */
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