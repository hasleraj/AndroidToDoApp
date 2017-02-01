package ca.ashleyhasler.todolist;

import android.content.Context;
import android.util.Log;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class DatabaseBundler {

    // public static property for easy access to name
    public static String name;

    public static void bundle(Context context, String databaseName) {
        // bundle database from assets with app if not already
        try {

            // set static property to database name
            name = databaseName;

            // setup full path to where the database will be stored in Android OS
            String destPath = "/data/data/" + context.getPackageName() + "/databases/" + databaseName;
            // construct File object
            File f = new File(destPath);

            // check if it already exists or not
            if (!f.exists()) {

                Log.d("ashley", "copying database");
                // manually make the databases directory
                File directory = new File("/data/data/" + context.getPackageName() + "/databases");
                directory.mkdir();

                // bundling with app : if not then we need to copy the database from assets folder to the destination
                copyDB(context.getAssets().open(databaseName), new FileOutputStream(destPath));
            }
        } catch (FileNotFoundException e) {
            Log.d("ashley", "FileNotFoundException: " + e.getMessage());
        } catch (IOException e) {
            Log.d("ashley", "IOException: " + e.getMessage());
        }
    }

    // ------------------------------------------------------------------------ private methods
    private static void copyDB(InputStream inputStream, OutputStream outputStream) throws IOException {
        // array of 1024 bytes (1K of data)
        byte[] buffer = new byte[1024];
        int length;
        // read method of input stream reads in data and puts into buffer array
        // pulls in the entire database 1Kilobyte at a time until nothing left
        while ((length = inputStream.read(buffer)) > 0) {
            // write each 1K block back to the outputStream
            outputStream.write(buffer, 0, length);
        }
        // close streams
        inputStream.close();
        outputStream.close();

        Log.d("ashley","database has been bundled successfully");
    }

}
