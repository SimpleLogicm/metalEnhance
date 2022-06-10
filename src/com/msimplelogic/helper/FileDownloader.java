package com.msimplelogic.helper;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.msimplelogic.activities.Global_Data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class FileDownloader {
    private static final int  MEGABYTE = 2024 * 2024;
    static Context context;
    public static void downloadFile(String fileUrl, File directory, String file_name, Context contexts, String file_format){
        try {
            context = contexts;




//            ((Activity)context).runOnUiThread(new Runnable() {
//                public void run() {
//
//                }
//            });

            URL url = new URL(fileUrl);
            HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
            //urlConnection.setRequestMethod("GET");
            //urlConnection.setDoOutput(true);
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            FileOutputStream fileOutputStream = new FileOutputStream(directory);
            int totalSize = urlConnection.getContentLength();

            byte[] buffer = new byte[MEGABYTE];
            int bufferLength = 0;
            while((bufferLength = inputStream.read(buffer))>0 ){
                fileOutputStream.write(buffer, 0, bufferLength);
            }
            fileOutputStream.close();


            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {

                }
            });

            if(file_format.equalsIgnoreCase(".png"))
            {
                File pdfFiles = new File(Environment.getExternalStorageDirectory() + "/PORTELPDF/" + file_name);  // -> filename = maven.pdf
                Uri path = Uri.fromFile(pdfFiles);
//                Intent pdfIntent = new Intent(Intent.ACTION_SEND);
//                pdfIntent.setDataAndType(path, "image/*");
//                pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                sharingIntent.setType("image/*");
                sharingIntent.putExtra(Intent.EXTRA_STREAM, path);
              context.startActivity(Intent.createChooser(sharingIntent, "Share Image Using"));

//                try{
//                    pdfIntent.addFlags(FLAG_ACTIVITY_NEW_TASK);
//                   // context.startActivity(Intent.createChooser(sharingIntent, "Share image using"));
//                    context.startActivity(pdfIntent);
//                }catch(ActivityNotFoundException e){
//                    new Handler(Looper.getMainLooper()).post(new Runnable() {
//                        @Override
//                        public void run() {
//
//                            Toast.makeText(context, "No Application available to view PDF", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//
//                }

            }else {
                File pdfFiles = new File(Environment.getExternalStorageDirectory() + "/PORTELPDF/" + file_name);  // -> filename = maven.pdf
                Uri path = Uri.fromFile(pdfFiles);
                Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
                pdfIntent.setDataAndType(path, "text/html");
                pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                try{
                    pdfIntent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                    pdfIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    context.startActivity(pdfIntent);
                }catch(ActivityNotFoundException e){
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {


                            Global_Data.Custom_Toast(context, "No Application available to view PDF", "");
                        }
                    });

                }
            }





        } catch (FileNotFoundException e) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {


                }
            });
            e.printStackTrace();
        } catch (MalformedURLException e) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {


                }
            });
            e.printStackTrace();
        } catch (IOException e) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {


                }
            });
            e.printStackTrace();
        }
    }



}
