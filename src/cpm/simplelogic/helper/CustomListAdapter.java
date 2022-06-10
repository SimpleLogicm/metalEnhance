package cpm.simplelogic.helper;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.msimplelogic.App.AppController;
import com.msimplelogic.activities.Global_Data;
import com.msimplelogic.activities.R;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import static android.content.Context.DOWNLOAD_SERVICE;

public class CustomListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Movie> movieItems;
    private File Vediofile;
    URL u = null;
    InputStream is = null;
   DownloadManager downloadManager;

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public CustomListAdapter(Activity activity, List<Movie> movieItems) {
        this.activity = activity;
        this.movieItems = movieItems;
    }

    @Override
    public int getCount() {
        return movieItems.size();
    }

    @Override
    public Object getItem(int location) {
        return movieItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_row, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        NetworkImageView thumbNail = (NetworkImageView) convertView
                .findViewById(R.id.thumbnail);
        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView rating = (TextView) convertView.findViewById(R.id.rating);
        //TextView genre = (TextView) convertView.findViewById(R.id.genre);
        TextView year = (TextView) convertView.findViewById(R.id.releaseYear);
        TextView share = (TextView) convertView.findViewById(R.id.share);
        TextView download = (TextView) convertView.findViewById(R.id.download);

        // getting movie data for the row
        Movie m = movieItems.get(position);

        // thumbnail image
        thumbNail.setImageUrl(m.getThumbnailUrl(), imageLoader);

        // title
        title.setText(m.getTitle());

        // rating
        rating.setText(m.getRating());

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://www.youtube.com/watch?v=" + m.getRating();
//                try {
//                    Vediofile=createImageFile();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                //downloadFile(url,Vediofile );
//                DownloadManager downloadManager= (DownloadManager)activity.getSystemService(DOWNLOAD_SERVICE);
//                Uri uri = Uri.parse(url);
//               // DownloadManager
//                DownloadManager.Request request=new DownloadManager.Request(uri);
//                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//                Long reference =downloadManager.enqueue(request);
//                        .setTitle("Dummy File")// Title of the Download Notification
//                        .setDescription("Downloading")// Description of the Download Notification
//                        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)// Visibility of the download Notification
//                        .setDestinationUri(Uri.fromFile(Vediofile))// Uri of the destination file
//                        .setRequiresCharging(false)// Set if charging is required to begin the download
//                        .setAllowedOverMetered(true)// Set if download is allowed on Mobile network
//                        .setAllowedOverRoaming(true);// Set if download is allowed on roaming network

//                try {
//                    u = new URL(url);
//                    is = u.openStream();
//                    HttpURLConnection huc = (HttpURLConnection)u.openConnection(); //to know the size of video
//                    int size = huc.getContentLength();
//
//                    if(huc != null) {
//                        String fileName = m.getTitle()+"FILE.mp4";
//                        String storagePath = Environment.getExternalStorageDirectory().toString();
//                        File f = new File(storagePath,fileName);
//
//                        FileOutputStream fos = new FileOutputStream(f);
//                        byte[] buffer = new byte[1024];
//                        int len1 = 0;
//                        if(is != null) {
//                            while ((len1 = is.read(buffer)) > 0) {
//                                fos.write(buffer,0, len1);
//                            }
//                        }
//                        if(fos != null) {
//                            fos.close();
//                        }
//                    }
//                } catch (MalformedURLException mue) {
//                    mue.printStackTrace();
//                } catch (IOException ioe) {
//                    ioe.printStackTrace();
//                } finally {
//                    try {
//                        if(is != null) {
//                            is.close();
//                        }
//                    } catch (IOException ioe) {
//                        // just going to ignore this one
//                    }
//                }

            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //.Custom_Toast(activity,""+m.getRating().toString(),"");
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "https://www.youtube.com/watch?v=" + m.getRating();
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "" + m.getTitle());
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                activity.startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });

//		// genre
//		String genreStr = "";
//		for (String str : m.getGenre()) {
//			genreStr += str + ", ";
//		}
//		genreStr = genreStr.length() > 0 ? genreStr.substring(0,
//				genreStr.length() - 2) : genreStr;
//		genre.setText(genreStr);

        // release year
        year.setText(m.getYear());

        return convertView;
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String imageFileName = "Metal Vedio";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES), "Metal_Vedios");

        if (!storageDir.exists()) {
            storageDir.mkdir();
        }

        File image = File.createTempFile(
                imageFileName,  // prefix
                ".mp4",         // suffix
                storageDir      // directory
        );

        // Save a file: path for use with ACTION_VIEW intents
    //    mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        // mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }


    private static void downloadFile(String url, File outputFile) {
        try {
            URL u = new URL(url);
            URLConnection conn = u.openConnection();
            int contentLength = conn.getContentLength();

            DataInputStream stream = new DataInputStream(u.openStream());

            byte[] buffer = new byte[contentLength];
            stream.readFully(buffer);
            stream.close();

            DataOutputStream fos = new DataOutputStream(new FileOutputStream(outputFile));
            fos.write(buffer);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            return; // swallow a 404
        } catch (IOException e) {
            return; // swallow a 404
        }
    }
}

