package com.msimplelogic.adapter;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.msimplelogic.activities.Global_Data;
import com.msimplelogic.activities.R;
import com.msimplelogic.helper.FileDownloader;
import com.msimplelogic.model.Productdemo_model;

import java.io.File;
import java.io.IOException;
import java.util.List;



public class Product_demo_adaptor extends RecyclerView.Adapter<Product_demo_adaptor.MyViewHolder> {
    Context context;
    private List<Productdemo_model> array;
    private String down;
    Uri uri;
    ProgressDialog dialog;

    public Product_demo_adaptor(Context context, List<Productdemo_model> array) {
        this.context = context;
        this.array = array;
    }

    @NonNull
    @Override
    public Product_demo_adaptor.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View  itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_product_demo, parent, false);

//        View itemView = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.mob_grid_txt, parent, false);

        return new Product_demo_adaptor.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull Product_demo_adaptor.MyViewHolder holder, int position) {

        holder.discription.setText(array.get(position).getProddiscrip());
        holder.title.setText(array.get(position).getProductname());
        holder.lastupdate.setText(array.get(position).getLastsync());
       // holder.thumbnail.setImageResource(array.get(position).getImage());

        Glide.with(context).load(array.get(position).getImage())
                .thumbnail(Glide.with(context).load("file:///android_asset/loading.gif"))
                .fitCenter()
                // .crossFade()
                .into(holder.thumbnail);


        holder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                File photoFile = null;
                File dir = new File(Environment.DIRECTORY_DOWNLOADS, array.get(position).getProductname());
                if (dir.isDirectory())
                {
                    String[] children = dir.list();
                    for (int i = 0; i < children.length; i++)
                    {
                        new File(dir, children[i]).delete();
                    }
                }

            //    new Product_demo_adaptor.DownloadFile().execute(array.get(position).getImage(), array.get(position).getProductname() + ".html");
                Global_Data.Custom_Toast(context,array.get(position).getProductname()+"Start Downloading","yes");
             Uri Download_Uri;
              DownloadManager  downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                Download_Uri = Uri.parse(array.get(position).getImage());

                DownloadManager.Request request = new DownloadManager.Request(Download_Uri);
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
                request.setAllowedOverRoaming(false);
                request.setTitle(array.get(position).getProductname());
                request.setDescription(array.get(position).getProddiscrip());
                request.setNotificationVisibility(request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setVisibleInDownloadsUi(true);
//

                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, array.get(position).getProductname());

//
//
//
//             downloadManager.enqueue(request);
            }
        });


        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//
                if (array.get(position).getImage().toLowerCase().toLowerCase().contains(".doc")
                        || array.get(position).getImage().toLowerCase().contains(".docx")) {

                    down = ".doc";
                    new Product_demo_adaptor.DownloadFile().execute(array.get(position).getImage(), array.get(position).getProductname() + down);



                } else if (array.get(position).getImage().toLowerCase().contains(".pdf")) {

                    down = ".pdf";
                    new Product_demo_adaptor.DownloadFile().execute(array.get(position).getImage(), array.get(position).getProductname() + down);

                    //request.setDestinationUri(uri);
                } else if (array.get(position).getImage().toLowerCase().contains(".ppt")
                        || array.get(position).getImage().toLowerCase().contains(".pptx")) {

                    down = ".ppt";
                    new Product_demo_adaptor.DownloadFile().execute(array.get(position).getImage(), array.get(position).getProductname() + down);


                } else if (array.get(position).getImage().toLowerCase().contains(".xls")
                        || array.get(position).getImage().toLowerCase().contains(".xlsx")) {

                    down = ".xls";
                    new Product_demo_adaptor.DownloadFile().execute(array.get(position).getImage(), array.get(position).getProductname() + down);

                } else if (array.get(position).getImage().toLowerCase().contains(".zip")
                        || array.get(position).getImage().toLowerCase().contains(".rar")) {

                    down = ".zip";
                    new Product_demo_adaptor.DownloadFile().execute(array.get(position).getImage(), array.get(position).getProductname() + down);

//                    request.setDestinationUri(uri);
                } else if (array.get(position).getImage().toLowerCase().contains(".rtf")) {

                    down = ".rtf";
                    new Product_demo_adaptor.DownloadFile().execute(array.get(position).getImage(), array.get(position).getProductname() + down);

                } else if (array.get(position).getImage().toLowerCase().contains(".wav")
                        || array.get(position).getImage().toLowerCase().contains(".mp3")) {

                    down = ".mp3";
                    new Product_demo_adaptor.DownloadFile().execute(array.get(position).getImage(), array.get(position).getProductname() + down);


                } else if (array.get(position).getImage().toLowerCase().contains(".gif")) {

                    down = ".gif";
                    new Product_demo_adaptor.DownloadFile().execute(array.get(position).getImage(), array.get(position).getProductname() + down);

                } else if (array.get(position).getImage().toLowerCase().contains(".jpg")
                        || array.get(position).getImage().toLowerCase().contains(".jpeg")
                        || array.get(position).getImage().toLowerCase().contains(".png")) {

                    down = ".png";
                    new Product_demo_adaptor.DownloadFile().execute(array.get(position).getImage(), array.get(position).getProductname() + down);

                } else if (array.get(position).getImage().toLowerCase().contains(".txt")) {

                    down = ".txt";
                    new Product_demo_adaptor.DownloadFile().execute(array.get(position).getImage(), array.get(position).getProductname() + down);

                } else if (array.get(position).getImage().toLowerCase().contains(".3gp")
                        || array.get(position).getImage().toLowerCase().contains(".mpg")
                        || array.get(position).getImage().toLowerCase().contains(".mpeg")
                        || array.get(position).getImage().toLowerCase().contains(".mpe")
                        || array.get(position).getImage().toLowerCase().contains(".mp4")
                        || array.get(position).getImage().toLowerCase().contains(".avi")) {

                    down = ".mp4";
                    new Product_demo_adaptor.DownloadFile().execute(array.get(position).getImage(), array.get(position).getProductname() + down);

                } else {

                    //   request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, array.get(position).getProductname());

                }
                //openfile(array.get(position).getImage().toString() ,array.get(position));
            }
        });
    }

    private File createImageFile(String images) throws IOException {
        // Create an image file name
        String imageFileName = "Metal";
        File image= null;
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "Metal");

        if (!storageDir.exists()) {
            storageDir.mkdir();
        }

        if (images.contains(".doc")){
             image = File.createTempFile(
                    imageFileName,  // prefix
                    ".doc",         // suffix
                    storageDir      // directory
            );
        }else if (images.contains(".ppt")||images.contains(".pptx")){
            image = File.createTempFile(
                    imageFileName,  // prefix
                    ".ppt",         // suffix
                    storageDir      // directory
            );
        }else if (images.contains(".xls")|| images.contains(".xlsx")){
            image = File.createTempFile(
                    imageFileName,  // prefix
                    ".xls",         // suffix
                    storageDir      // directory
            );

        }else if (images.contains(".zip")|| images.contains(".rar")){
            image = File.createTempFile(
                    imageFileName,  // prefix
                    ".zip",         // suffix
                    storageDir      // directory
            );


        }else if (images.contains(".rtf")){
            image = File.createTempFile(
                    imageFileName,  // prefix
                    ".rft",         // suffix
                    storageDir      // directory
            );
        }else if (images.contains(".wav")
                || images.contains(".mp3")){
            image = File.createTempFile(
                    imageFileName,  // prefix
                    ".mp3",         // suffix
                    storageDir      // directory
            );

        }else if (images.contains(".gif")){
            image = File.createTempFile(
                    imageFileName,  // prefix
                    ".gif",         // suffix
                    storageDir      // directory
            );
        }else if (images.contains(".jpg")
                || images.contains(".jpeg")
                || images.contains(".png")){
            image = File.createTempFile(
                    imageFileName,  // prefix
                    ".png",         // suffix
                    storageDir      // directory
            );

        }else if (images.contains(".txt")){
            image = File.createTempFile(
                    imageFileName,  // prefix
                    ".txt",         // suffix
                    storageDir      // directory
            );

        }else if (images.contains(".3gp")
                || images.contains(".mpg")
                || images.contains(".mpeg")
                || images.contains(".mpe")
                || images.contains(".mp4")
                || images.contains(".avi")){

            image = File.createTempFile(
                    imageFileName,  // prefix
                    ".txt",         // suffix
                    storageDir      // directory
            );
        }


        // Save a file: path for use with ACTION_VIEW intents
        down = "file:" + image.getAbsolutePath();
        // mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }


    private void openfile(String urlString, Productdemo_model s) {
//        File f = new File(down);
//        Uri uri = Uri.parse(f.getPath());


        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
//        sharingIntent.setType("text/plain");
//        String shareBody =array.get(position).getImage();
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "" +s.getProductname());
       // sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);

        if (urlString.toLowerCase().toLowerCase().contains(".doc")
                || urlString.toLowerCase().contains(".docx")) {
            // Word document
            sharingIntent.setDataAndType(uri, "application/msword");
        } else if (urlString.toLowerCase().contains(".pdf")) {
            // PDF file
            sharingIntent.setDataAndType(uri, "application/pdf");
        } else if (urlString.toLowerCase().contains(".ppt")
                || urlString.toLowerCase().contains(".pptx")) {
            // Powerpoint file
            sharingIntent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        } else if (urlString.toLowerCase().contains(".xls")
                || urlString.toLowerCase().contains(".xlsx")) {
            // Excel file
            sharingIntent.setDataAndType(uri, "application/vnd.ms-excel");
        } else if (urlString.toLowerCase().contains(".zip")
                || urlString.toLowerCase().contains(".rar")) {
            // ZIP file
            sharingIntent.setDataAndType(uri, "application/trap");
        } else if (urlString.toLowerCase().contains(".rtf")) {
            // RTF file
            sharingIntent.setDataAndType(uri, "application/rtf");
        } else if (urlString.toLowerCase().contains(".wav")
                || urlString.toLowerCase().contains(".mp3")) {
            // WAV/MP3 audio file
            sharingIntent.setDataAndType(uri, "audio/*");
        } else if (urlString.toLowerCase().contains(".gif")) {
            // GIF file
            sharingIntent.setDataAndType(uri, "image/gif");
        } else if (urlString.toLowerCase().contains(".jpg")
                || urlString.toLowerCase().contains(".jpeg")
                || urlString.toLowerCase().contains(".png")) {
            // JPG file
          //  sharingIntent.setDataAndType(uri, "image/jpeg");
//            IntentShare.with(context)
//                    .chooserTitle("Select a sharing target : ")
//                    .text("Default text you would like to share.")
//                    .image(Uri.parse(down))
//                    .deliver();
        } else if (urlString.toLowerCase().contains(".txt")) {
            // Text file
            sharingIntent.setDataAndType(uri, "text/plain");
        } else if (urlString.toLowerCase().contains(".3gp")
                || urlString.toLowerCase().contains(".mpg")
                || urlString.toLowerCase().contains(".mpeg")
                || urlString.toLowerCase().contains(".mpe")
                || urlString.toLowerCase().contains(".mp4")
                || urlString.toLowerCase().contains(".avi")) {
            // Video files
            sharingIntent.setDataAndType(uri, "video/*");
        } else {
            // if you want you can also define the intent type for any other file

            // additionally use else clause below, to manage other unknown extensions
            // in this case, Android will show all applications installed on the device
            // so you can choose which application to use
            sharingIntent.setDataAndType(uri, "*/*");
        }

        sharingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(sharingIntent, "Share via"));

    }

    @Override
    public int getItemCount() {
        return array.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnail;
        TextView title,discription,share,download,lastupdate;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            thumbnail=itemView.findViewById(R.id.thumbnail);
            title=itemView.findViewById(R.id.title);
            discription=itemView.findViewById(R.id.discription);
            share=itemView.findViewById(R.id.share);
            download=itemView.findViewById(R.id.download);
            lastupdate=itemView.findViewById(R.id.lastupdate);
        }
    }

    private class DownloadFile extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... strings) {

            File dir = new File( Environment.getExternalStorageDirectory(), "PORTELPDF");
            if (dir.isDirectory())
            {
                String[] children = dir.list();
                for (int i = 0; i < children.length; i++)
                {
                    new File(dir, children[i]).delete();
                }
            }
            String fileUrl = strings[0];   // -> http://maven.apache.org/maven-1.x/maven.pdf
            String fileName = strings[1];  // -> maven.pdf
            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            File folder = new File(extStorageDirectory, "PORTELPDF");
            folder.mkdir();

            File pdfFile = new File(folder, fileName);

            try {
                pdfFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            FileDownloader.downloadFile(fileUrl, pdfFile, fileName, context, down);
            return null;
        }
    }
}
