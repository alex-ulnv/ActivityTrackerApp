package com.activitytrackerapp.activitytracker;

import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class FileHandler<T> {

    private static final String TAG = "FileHandler";

    File file;

    public FileHandler(String fileName) {
        String basePath = App.fileDirectory;
        String filePath = basePath + File.separator + fileName;
        this.file = new File(filePath);
    }

    public void saveToCSV(ArrayList<T> values, String[] headers) {

        try {
            FileWriter fw;

            fw = new FileWriter(this.file);

            BufferedWriter bw = new BufferedWriter(fw);

            // Write headers
            for (int i = 0; i < headers.length; i++) {
                bw.write(headers[i]);
                if (i<headers.length-1)
                    bw.write(",");
            }
            bw.write("\n");

            // Write values
            for (int i = 0; i < values.size(); i++) {
                bw.write(values.get(i).toString());
                if (i<values.size()-1)
                    bw.write("\n");
            }

            bw.flush();
            bw.close();
        }
        catch (IOException e) {
            Log.e(TAG, "saveToCSV: Error locating the file " + this.file.getPath() + " " + e);
        }

    }
}
