

package com.msimplelogic.activities;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaScannerConnection;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.os.TestLooperManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.gson.JsonArray;
import com.jsibbold.zoomage.ZoomageView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.msimplelogic.activities.AppLocationManager;
import com.msimplelogic.activities.Config;
import com.msimplelogic.activities.CropOption;
import com.msimplelogic.activities.DataBaseHelper;
import com.msimplelogic.activities.Global_Data;
import com.msimplelogic.activities.Local_Data;
import com.msimplelogic.activities.LoginDataBaseAdapter;
import com.msimplelogic.activities.PlayService_Location;
import com.msimplelogic.activities.R;
import com.msimplelogic.activities.SDcard_VideoMain;
import com.msimplelogic.activities.SDcard_imageMain;
import com.msimplelogic.activities.kotlinFiles.Neworderoptions;
import com.msimplelogic.adapter.AdapterCustServices;
import com.msimplelogic.helper.MultipartUtility;
import com.msimplelogic.imageadapters.Image;
import com.msimplelogic.model.CustServicesModel;
import com.msimplelogic.services.getServices;
import com.msimplelogic.webservice.ConnectionDetector;
//import com.soundcloud.android.crop.Crop;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.microedition.khronos.opengles.GL11ExtensionPack;

import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;

import static com.msimplelogic.activities.Customer_Stock.competitionStockCode;
import static com.msimplelogic.activities.ExpenseDetailsActivity.response_result_image;

public class Customer_Feed extends BaseActivity implements OnItemSelectedListener, MediaScannerConnection.MediaScannerConnectionClient {
    Toolbar toolbar;
    String user_email = "";
    static String claimPhotoCode = "";
    static String complaintCode = "";
    static String complaintPhotoPath = "";
    static String claimPhotoPath = "";
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private static final String TAG = Customer_Feed.class.getSimpleName();
    private static final String FILE_TYPE = "image/*";
    private static final int PICK_FROM_CAMERA = 1;
    private static final int CROP_FROM_CAMERA = 2;
    private static final int PICK_FROM_FILE = 3;
    private static final int SELECT_VIDEO = 3;
    private static final int SELECT_IMAGE = 4;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    LinearLayout imageVideoLout;
    private String mCurrentPhotoPath = "";
    private ImageView crossimg, crossimg1;
    ImageView img_show;
    RelativeLayout takePicLout;
    private String pictureImagePath_new = "";
    ZoomageView imgdialog;
    String imageType = "";
    String cropImage = "";
    String imageFolder = "";
    String image_check = "";
    private String pictureImagePath = "";
    protected String _path;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
    static String image_path = "";
    static String video_path = "";
    final int PIC_CROP = 1;
    public String[] allFiles;
    Drawable Glovel_Drawable;
    String video_code = "";
    String RE_ID = "";
    String filename = "";
    String image_url = "";
    String final_media_path = "";
    Boolean isInternetPresent = false;
    String response_result = "";
    ConnectionDetector cd;
    String media_coden = "";
    String media_text = "";
    Bitmap bitmap;
    TextView custservice_toolbar_title;
    String picturePath = "";
    String video_option = "";
    String image_option = "";
    List<String> C_Array = new ArrayList<String>();
    ArrayList<CustServicesModel> arrayOfCustServices = new ArrayList<CustServicesModel>();
    DataBaseHelper dbvoc = new DataBaseHelper(this);
    Spinner feed_spinner;
    Button button1, btnImage, btnVideo;
    ImageView video_play, video_stop, Media_Takefromga, customerServicesPicture;
    ImageView imageview, imageviewr, Take_pick, Crop_pick;
    Button media_image, media_video;
    LoginDataBaseAdapter loginDataBaseAdapter;
    Drawable db;
    ProgressDialog dialog;
    LinearLayout media_layout, m_viveo, emageview_option;
    Boolean B_flag;
    TextView txt_label, Textview2, textview3;
    CardView cardComplaint, cardFeedback, cardClaim, cardClaimAmount;
    EditText new_feedback, new_complaints, claim_amount, discription;
    PlayService_Location PlayServiceManager;
    MediaPlayer.OnCompletionListener myVideoViewCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            mp.reset();
            mp.release();
        }
    };
    private String SCAN_PATH;
    private MediaScannerConnection conn;
    private String selectedPath;
    private String[] feed_state = {"Feedback", "Complaints", "Claims", "Media"};
    private Uri mImageCaptureUri;
    private Uri fileUri; // file url to store image/video
    private VideoView videoPreview;
    private String Current_Date = "";
    private String name, CP_NAME, RE_TEXT;
    protected boolean _taken;

    SharedPreferences sp;
    ImageView hedder_theame;
    public static String getPathFromURI(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider


        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * returning image / video
     */
    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                Config.IMAGE_DIRECTORY_NAME + "/" + "CUSTOMER_SERVICES");

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "Oops! Failed create "
                        + Config.IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    private static File getOutputMediaFilenew(int type) {

        File mediaStorageDir;
        // External sdcard location
        if (type == MEDIA_TYPE_IMAGE) {
            mediaStorageDir = new File(
                    //Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),Config.IMAGE_DIRECTORY_NAME);
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "M_PICTURE");


            image_path = mediaStorageDir.getPath();
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    Log.d(TAG, "Oops! Failed create "
                            + Config.IMAGE_DIRECTORY_NAME + " directory");
                    return null;
                } else {
                    mediaStorageDir.mkdirs();
                }
            }

            // Create a media file name
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                    Locale.getDefault()).format(new Date());
            File mediaFile;
            if (type == MEDIA_TYPE_IMAGE) {
                mediaFile = new File(mediaStorageDir.getPath() + File.separator
                        + "IMG_" + timeStamp + ".jpg");

            } else if (type == MEDIA_TYPE_VIDEO) {
                mediaFile = new File(mediaStorageDir.getPath() + File.separator
                        + "VID_" + timeStamp + ".mp4");
            } else {
                return null;
            }

            return mediaFile;
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaStorageDir = new File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "M_VIDEO");

            video_path = mediaStorageDir.getPath();
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    Log.d(TAG, "Oops! Failed create "
                            + Config.IMAGE_DIRECTORY_NAME + " directory");
                    return null;
                }
            }

            // Create a media file name
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                    Locale.getDefault()).format(new Date());
            File mediaFile;
            if (type == MEDIA_TYPE_IMAGE) {
                mediaFile = new File(mediaStorageDir.getPath() + File.separator
                        + "IMG_" + timeStamp + ".jpg");


            } else if (type == MEDIA_TYPE_VIDEO) {
                mediaFile = new File(mediaStorageDir.getPath() + File.separator
                        + "VID_" + timeStamp + ".mp4");
            } else {
                return null;
            }

            return mediaFile;
        }


        // Create the storage directory if it does not exist
        return null;
    }

    private static byte[] loadFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);

        long length = file.length();
        if (length > Integer.MAX_VALUE) {
            // File is too large
        }
        byte[] bytes = new byte[(int) length];

        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
                && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }

        if (offset < bytes.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }

        is.close();
        return bytes;
    }

    public static void addImageToGallery(final String filePath, final Context context) {

        ContentValues values = new ContentValues();

        values.put(MediaStore.Images.Media.DATE_TAKEN, "IMG_" + System.currentTimeMillis());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.MediaColumns.DATA, filePath);

        context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }

    public static void addVideoToGallery(final String filePath, final Context context) {

        ContentValues values = new ContentValues();

        values.put(MediaStore.Images.Media.DATE_TAKEN, "VID_" + System.currentTimeMillis());
        values.put(MediaStore.Images.Media.MIME_TYPE, "video/.mp4");
        values.put(MediaStore.MediaColumns.DATA, filePath);

        context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }

    /**
     * ------------ Helper Methods ----------------------
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_customerservices);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        custservice_toolbar_title = findViewById(R.id.custservice_toolbar_title);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("");

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        Intent i = getIntent();
        name = i.getStringExtra("retialer");
        CP_NAME = i.getStringExtra("CP_NAME");
        RE_TEXT = i.getStringExtra("RE_TEXT");

        Global_Data.Default_Image_Path = "";
        Global_Data.Default_video_Path = "";

        //loginDataBaseAdapter = new LoginDataBaseAdapter(Customer_Feed.this);
        loginDataBaseAdapter = new LoginDataBaseAdapter(this);
        loginDataBaseAdapter = loginDataBaseAdapter.open();

        dialog = new ProgressDialog(Customer_Feed.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
        String strDate = sdf.format(c.getTime());
        Current_Date = sdf.format(c.getTime());
        cd = new ConnectionDetector(getApplicationContext());

        SharedPreferences spf = this.getSharedPreferences("SimpleLogic", 0);
        user_email = spf.getString("USER_EMAIL", null);

        if (CP_NAME.equals("Image")) {
            custservice_toolbar_title.setText(getResources().getString(R.string.Media));
            setTitle("");
        } else if (CP_NAME.equals("Claim")) {
            custservice_toolbar_title.setText(getResources().getString(R.string.Claims));
            setTitle("");
        } else if (CP_NAME.equals("Complaints")) {
            custservice_toolbar_title.setText(getResources().getString(R.string.Complaints));
            setTitle("");
        }
//		else{			custservice_toolbar_title.setText("Order");
//			setTitle("");
//		}


//		Toast.makeText(getApplicationContext(),
//				CP_NAME, Toast.LENGTH_LONG).show();

        //feed_spinner = (Spinner) findViewById(R.id.feed_spinner);
        new_feedback = (EditText) findViewById(R.id.new_feedback);
        new_complaints = (EditText) findViewById(R.id.new_complaints);
        claim_amount = (EditText) findViewById(R.id.claim_amount);
        discription = (EditText) findViewById(R.id.discription);
        img_show = (ImageView) findViewById(R.id.img_show);
        imageVideoLout = (LinearLayout) findViewById(R.id.imagevideo_lout);

        cardFeedback = findViewById(R.id.lout);
        cardComplaint = findViewById(R.id.lout1);
        cardClaim = findViewById(R.id.lout2);
        cardClaimAmount = findViewById(R.id.lout3);

        //Toast.makeText(getApplicationContext(), new_feedback.getText().toString(), Toast.LENGTH_LONG).show();

        /* claim list view code */
        RecyclerView listView = (RecyclerView) findViewById(R.id.C_list);

        /* end */

        videoPreview = (VideoView) findViewById(R.id.videoPreview);
        hedder_theame =  findViewById(R.id.hedder_theame);
        media_layout = (LinearLayout) findViewById(R.id.Media_Button);
        m_viveo = (LinearLayout) findViewById(R.id.Media_Video);
        emageview_option = (LinearLayout) findViewById(R.id.Emageview_option);
        Take_pick = (ImageView) findViewById(R.id.Take_pick);
        Crop_pick = (ImageView) findViewById(R.id.Crop_pick);
        video_play = (ImageView) findViewById(R.id.Media_Play);
        video_stop = (ImageView) findViewById(R.id.Media_STOP);
//		media_image = (ImageView) findViewById(R.id.Media_image_Button);
//		media_video = (ImageView) findViewById(R.id.Media_Video_Button);
        media_image = (Button) findViewById(R.id.Media_image_Button);
        media_video = (Button) findViewById(R.id.Media_Video_Button);
        //Media_Takefromga = (ImageView) findViewById(R.id.Media_Takefromga);
        imageview = (ImageView) findViewById(R.id.result_image);
        imageview.setTag(R.drawable.white_background);
        imageviewr = (ImageView) findViewById(R.id.result_imager);
        button1 = (Button) findViewById(R.id.button1);
        btnImage = (Button) findViewById(R.id.btn_image);
        btnVideo = (Button) findViewById(R.id.btn_video);
        txt_label = (TextView) findViewById(R.id.txt_label);
        Textview2 = (TextView) findViewById(R.id.textView2);
        textview3 = (TextView) findViewById(R.id.TextView01);
        customerServicesPicture = (ImageView) findViewById(R.id.customer_services_picture);
        takePicLout = (RelativeLayout) findViewById(R.id.takepic_lout);

        sp = getSharedPreferences("SimpleLogic", 0);
        int current_theme = sp.getInt("CurrentTheme",0);

        if (current_theme== 1) {
            hedder_theame.setImageResource(R.drawable.dark_hedder);

        }

        _path = "/sdcard/NewFolder/test1.jpg";
        txt_label.setText(getResources().getString(R.string.Previous_Feedback));

        customerServicesPicture.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                requestStoragePermission2();
            }
        });

        crossimg = findViewById(R.id.crossimg);
        crossimg1 = findViewById(R.id.crossimg1);
        crossimg.setVisibility(View.INVISIBLE);
        crossimg1.setVisibility(View.INVISIBLE);

        crossimg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                deletedialog();
            }
        });

        img_show.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pictureImagePath_new != "") {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Customer_Feed.this);
                    LayoutInflater inflater = getLayoutInflater();
                    View dialogLayout = inflater.inflate(R.layout.dialogeimage, null);
                    imgdialog = dialogLayout.findViewById(R.id.imageView);
                    Glide.with(Customer_Feed.this).load(pictureImagePath_new).into(imgdialog);
                    builder.setPositiveButton("OK", null);
                    builder.setView(dialogLayout);
                    builder.show();
                }
            }
        });

        crossimg1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                deletedialog1();
            }
        });

        if (CP_NAME.equals("video")) {
            media_image.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
            media_video.setVisibility(View.VISIBLE);
            takePicLout.setVisibility(View.GONE);

            // Media_Takefromga.setVisibility(View.VISIBLE);
            media_layout.setVisibility(View.VISIBLE);
            //imageview.setVisibility(View.VISIBLE);
            cardClaim.setVisibility(View.VISIBLE);
            cardClaimAmount.setVisibility(View.GONE);
            imageviewr.setVisibility(View.GONE);
            imageview.setVisibility(View.VISIBLE);
            cardFeedback.setVisibility(View.GONE);
            cardComplaint.setVisibility(View.GONE);
            Textview2.setVisibility(View.GONE);
            textview3.setVisibility(View.GONE);
            imageview.setVisibility(View.VISIBLE);
            imageVideoLout.setVisibility(View.VISIBLE);
            //m_viveo.setVisibility(View.VISIBLE);
            videoPreview.setVisibility(View.GONE);
            emageview_option.setVisibility(View.GONE);
            txt_label.setVisibility(View.GONE);
        } else if (CP_NAME.equals("Image")) {
            takePicLout.setVisibility(View.GONE);
            media_image.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
            media_video.setVisibility(View.VISIBLE);
            //Media_Takefromga.setVisibility(View.GONE);
            media_layout.setVisibility(View.VISIBLE);
            //imageview.setVisibility(View.VISIBLE);
            cardClaim.setVisibility(View.VISIBLE);
            cardClaimAmount.setVisibility(View.GONE);
            imageviewr.setVisibility(View.GONE);

            imageVideoLout.setVisibility(View.VISIBLE);
            cardFeedback.setVisibility(View.GONE);
            cardComplaint.setVisibility(View.GONE);
            Textview2.setVisibility(View.GONE);
            textview3.setVisibility(View.GONE);
            imageview.setVisibility(View.VISIBLE);
            imageVideoLout.setVisibility(View.VISIBLE);
            //m_viveo.setVisibility(View.VISIBLE);
            videoPreview.setVisibility(View.GONE);
            emageview_option.setVisibility(View.GONE);
            txt_label.setVisibility(View.GONE);
        } else if (CP_NAME.equals("Feedback")) {
            GetListData("Feedback");
            txt_label.setText(getResources().getString(R.string.Previous_Feedback));
            cardFeedback.setVisibility(View.VISIBLE);
            listView.setVisibility(View.VISIBLE);
            customerServicesPicture.setVisibility(View.VISIBLE);
            cardComplaint.setVisibility(View.GONE);
            Textview2.setVisibility(View.GONE);
            textview3.setVisibility(View.GONE);
            cardClaimAmount.setVisibility(View.GONE);
            cardClaim.setVisibility(View.GONE);
            media_image.setVisibility(View.GONE);
            media_video.setVisibility(View.GONE);
            //Media_Takefromga.setVisibility(View.GONE);
            media_layout.setVisibility(View.GONE);
            imageview.setVisibility(View.GONE);
            imageVideoLout.setVisibility(View.GONE);
            imageviewr.setVisibility(View.GONE);

            emageview_option.setVisibility(View.GONE);

            m_viveo.setVisibility(View.GONE);
            videoPreview.setVisibility(View.GONE);
        } else if (CP_NAME.equals("Complaints")) {
            GetListData("Complaints");
            takePicLout.setVisibility(View.VISIBLE);
            txt_label.setText(getResources().getString(R.string.Previous_Complaints));
            customerServicesPicture.setVisibility(View.VISIBLE);
            cardFeedback.setVisibility(View.GONE);
            cardComplaint.setVisibility(View.VISIBLE);
            listView.setVisibility(View.VISIBLE);
            Textview2.setVisibility(View.GONE);
            textview3.setVisibility(View.GONE);
            cardClaimAmount.setVisibility(View.GONE);
            cardClaim.setVisibility(View.GONE);
            media_image.setVisibility(View.GONE);
            media_video.setVisibility(View.GONE);
            //Media_Takefromga.setVisibility(View.GONE);
            media_layout.setVisibility(View.GONE);
            imageview.setVisibility(View.GONE);
            imageVideoLout.setVisibility(View.GONE);
            m_viveo.setVisibility(View.GONE);
            videoPreview.setVisibility(View.GONE);
            imageviewr.setVisibility(View.GONE);
            emageview_option.setVisibility(View.GONE);
        } else if (CP_NAME.equals("Claim")) {
            GetListData("Claim");
            txt_label.setText(getResources().getString(R.string.Previous_Claims));
            cardClaimAmount.setVisibility(View.VISIBLE);
            customerServicesPicture.setVisibility(View.VISIBLE);
            listView.setVisibility(View.VISIBLE);
            cardClaim.setVisibility(View.VISIBLE);
            Textview2.setVisibility(View.GONE);
            textview3.setVisibility(View.GONE);
            cardFeedback.setVisibility(View.GONE);
            cardComplaint.setVisibility(View.GONE);
            media_image.setVisibility(View.GONE);
            media_video.setVisibility(View.GONE);
            //Media_Takefromga.setVisibility(View.GONE);
            media_layout.setVisibility(View.GONE);
            imageview.setVisibility(View.GONE);
            imageVideoLout.setVisibility(View.GONE);
            m_viveo.setVisibility(View.GONE);
            videoPreview.setVisibility(View.GONE);
            imageviewr.setVisibility(View.GONE);
            emageview_option.setVisibility(View.GONE);
        }

//		ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.claim_list_view, C_Array);
//		listView.setAdapter(adapter);

        listView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        listView.setLayoutManager(llm);

        AdapterCustServices adapter1 = new AdapterCustServices(Customer_Feed.this, arrayOfCustServices);
// Attach the adapter to a ListView
        //ListView listView = (ListView) findViewById(R.id.lvItems);
        listView.setAdapter(adapter1);

	/*	videoPreview.setOnTouchListener(new OnTouchListener() {
			@TargetApi(Build.VERSION_CODES.HONEYCOMB)
			@SuppressLint("InlinedApi")
			@Override
		    public boolean onTouch(View v, MotionEvent event) {

		    	videoPreview.stopPlayback();
		        //finish();

//		    	Toast.makeText(Customer_Feed.this,"Please Select City", Toast.LENGTH_SHORT).show();
//		    	Intent intent = new Intent(getApplicationContext(), VIDEO_DIALOG.class);
//				intent.putExtra("VIDEO_PATH", fileUri.getPath());
//				startActivity(intent);

		    	final Dialog dialog = new Dialog(Customer_Feed.this);
		    	dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		    	dialog.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		    	dialog.setContentView(R.layout.video_view);
		    	//final Button v_close = (Button) dialog.findViewById(R.id.v_close);
		    	final VideoView videoview = (VideoView) dialog.findViewById(R.id.video_pre);


		    	videoview.setVideoPath(fileUri.getPath());
		    	MediaController mediaController= new MediaController(Customer_Feed.this);
		    	//mediaController.setVisibility(View.GONE);
	            mediaController.setAnchorView(videoPreview);
	            videoview.setMediaController(mediaController);
		    	//videoview.setVideoURI(uri);
	          //  videoview.prepare();
		    	videoview.start();

		    	dialog.show();
		    	WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
		    	LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		    	lp.copyFrom(dialog.getWindow().getAttributes());
		    	dialog.getWindow().setAttributes(lp);

		    	dialog.setOnKeyListener(new Dialog.OnKeyListener() {

		            @Override
		            public boolean onKey(DialogInterface arg0, int keyCode,
		                    KeyEvent event) {
		                // TODO Auto-generated method stub
		                if (keyCode == KeyEvent.KEYCODE_BACK) {
		                   // finish();
		                	videoview.stopPlayback();
		                    dialog.dismiss();
		                }
		                return true;
		            }
		        });

//		    	v_close.setOnClickListener(new OnClickListener() {
//
//	                @Override
//	                public void onClick(View v) {
//	                	videoview.stopPlayback();
//	                	dialog.dismiss();
//	                }
//	            });



				return true;
		    }
		});*/

        videoPreview.setOnPreparedListener(new
                                                   MediaPlayer.OnPreparedListener() {
                                                       @Override
                                                       public void onPrepared(MediaPlayer mp) {
                                                           Log.i(TAG, "Duration = " +
                                                                   videoPreview.getDuration());
                                                           //	videoPreview.setBackgroundDrawable(null);
                                                       }
                                                   });

        videoPreview.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {

                if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                    // Here the video starts
                    videoPreview.setBackgroundDrawable(null);
                    return true;
                }
                return false;
            }
        });

        media_image.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                CP_NAME = "Image";
                get_dialogC_Image();

//				B_flag = isDeviceSupportCamera();
//
//				if(B_flag == true)
//				{
//					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//
//			        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
//
//			        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
//
//			       // performCrop(fileUri);
//			        // start the image capture Intent
//			        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
//
////					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
////					fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
////
////			        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
////	                startActivityForResult(Intent.createChooser(intent, "Complete action using"), PICK_FROM_CAMERA);
//				}
//				else
//				{
//					Toast.makeText(getApplicationContext(), "no camera on this device", Toast.LENGTH_LONG).show();
//				}

            }
        });

        Take_pick.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                CP_NAME = "Image";
                get_dialogC_Image();

            }
        });

        media_video.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                requestStoragePermissionvideo();
            }
        });

//		Media_Takefromga.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//
//				Intent intent = new Intent();
//				intent.setType("video/*");
//				intent.setAction(Intent.ACTION_GET_CONTENT);
//				startActivityForResult(Intent.createChooser(intent, "Select a Video "), SELECT_VIDEO);
//			}
//		});

        video_play.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Global_Data.Custom_Toast(Customer_Feed.this, "Vedio Start", "Yes");
                videoPreview.start();
                videoPreview.setBackgroundDrawable(null);
            }
        });

        video_stop.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Global_Data.Custom_Toast(Customer_Feed.this, "Vedio Pause", "Yes");
                videoPreview.pause();
            }
        });

        Crop_pick.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (image_option.equalsIgnoreCase("image")) {
                    try {
                        if (Uri.fromFile(new File(fileUri.getPath())) != null) {
                            db = imageview.getDrawable();
                            Glovel_Drawable = imageview.getDrawable();
                            imageview.setImageDrawable(null);
                            //Crop.pickImage(Customer_Feed.this);
                            Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
//							Crop.of(Uri.fromFile(new File(fileUri.getPath())), Uri.fromFile(new File(fileUri.getPath()))).asSquare().start(Customer_Feed.this);
                        } else {
                            //Toast.makeText(getApplicationContext(), "Please Capture Image First", Toast.LENGTH_LONG).show();
                            Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.Please_Capture_Image_First), "Yes");
//							Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.Please_Capture_Image_First), Toast.LENGTH_LONG);
//							toast.setGravity(Gravity.CENTER, 0, 0);
//							toast.show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        //Toast.makeText(getApplicationContext(), "Please Capture Image First", Toast.LENGTH_LONG).show();
                        Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.Please_Capture_Image_First), "Yes");
//						Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.Please_Capture_Image_First), Toast.LENGTH_LONG);
//						toast.setGravity(Gravity.CENTER, 0, 0);
//						toast.show();
                    }
                } else {
                    try {
                        if (Uri.fromFile(new File(picturePath)) != null) {
                            db = imageview.getDrawable();
                            Glovel_Drawable = imageview.getDrawable();
                            imageview.setImageDrawable(null);
                            //Crop.pickImage(Customer_Feed.this);
                            Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
//							Crop.of(Uri.fromFile(new File(picturePath)), Uri.fromFile(new File(picturePath))).asSquare().start(Customer_Feed.this);
                        } else {
                            //Toast.makeText(getApplicationContext(), "Please Capture Image First", Toast.LENGTH_LONG).show();

                            Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.Please_Capture_Image_First), "Yes");
//							Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.Please_Capture_Image_First), Toast.LENGTH_LONG);
//							toast.setGravity(Gravity.CENTER, 0, 0);
//							toast.show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        //Toast.makeText(getApplicationContext(), "Please Capture Image First", Toast.LENGTH_LONG).show();

                        Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.Please_Capture_Image_First), "Yes");
//						Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.Please_Capture_Image_First), Toast.LENGTH_LONG);
//						toast.setGravity(Gravity.CENTER, 0, 0);
//						toast.show();
                    }
                }
            }
        });

        button1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                final String formattedDate = df.format(c.getTime());

                SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd");
                final String formattedDatef = dff.format(c.getTime());

                List<Local_Data> contacts = dbvoc.getRetailer(RE_TEXT);
                for (Local_Data cn : contacts) {

                    RE_ID = cn.get_Retailer_id();
                }

                if (CP_NAME.equals("video") || CP_NAME.equals("Image")) {

                    try {
                        if (CP_NAME.equals("video")) {

                            if (video_option.equalsIgnoreCase("Gallery") && (selectedPath == null || selectedPath.equals(""))) {
                                //Toast.makeText(getApplicationContext(), "Please Capture Media First", Toast.LENGTH_LONG).show();
                                Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.Please_Capture_Video_First), "Yes");
//								Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.Please_Capture_Video_First), Toast.LENGTH_LONG);
//								toast.setGravity(Gravity.CENTER, 0, 0);
//								toast.show();
                            } else if (fileUri.getPath() == null || fileUri.getPath().equals("")) {
                                //Toast.makeText(getApplicationContext(), "Please Capture Media First", Toast.LENGTH_LONG).show();
                                Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.Please_Capture_Video_First), "Yes");
//								Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.Please_Capture_Video_First), Toast.LENGTH_LONG);
//								toast.setGravity(Gravity.CENTER, 0, 0);
//								toast.show();
                            } else if (discription.getText().toString() == null || discription.getText().toString().equals("")) {
                                //Toast.makeText(getApplicationContext(), "Please Enter Description", Toast.LENGTH_LONG).show();
                                Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.Enter_Description), "Yes");
//								Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.Enter_Description), Toast.LENGTH_LONG);
//								toast.setGravity(Gravity.CENTER, 0, 0);
//								toast.show();

                            } else {

                                //Toast.makeText(getApplicationContext(), discription.getText().toString(), Toast.LENGTH_LONG).show();

                                //image_url = getStringImage(bitmap);
                                //new UploadFileToServer().execute();

                                //executeMultipartPost();LoadDatabaseAsyncTask
                                media_text = discription.getText().toString();

                                if (video_option.equalsIgnoreCase("Gallery")) {
                                    final_media_path = selectedPath;
                                } else {
                                    final_media_path = fileUri.getPath();
                                }

                                filename = final_media_path.substring(final_media_path.lastIndexOf("/") + 1);



                                isInternetPresent = cd.isConnectingToInternet();
                                if (isInternetPresent) {

                                    AlertDialog alertDialog = new AlertDialog.Builder(Customer_Feed.this).create(); //Read Update

                                    alertDialog.setMessage(getResources().getString(R.string.Online_dialog_message));
//}

                                    alertDialog.setButton(Dialog.BUTTON_POSITIVE, getResources().getString(R.string.Online_Sync), new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialogn, int which) {
                                            // TODO Auto-generated method stub

                                            isInternetPresent = cd.isConnectingToInternet();
                                            if (isInternetPresent) {
                                                button1.setClickable(false);
                                                button1.setEnabled(false);

                                                response_result = "";

                                                runOnUiThread(new Runnable() {
                                                    public void run() {
                                                        dialog = new ProgressDialog(Customer_Feed.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                                                        dialog.setMessage(getResources().getString(R.string.Please_Wait));
                                                        dialog.setTitle(getResources().getString(R.string.app_name));
                                                        dialog.setCancelable(false);
                                                        dialog.show();
                                                    }
                                                });

                                                SharedPreferences spf = Customer_Feed.this.getSharedPreferences("SimpleLogic", 0);
                                                String user_email = spf.getString("USER_EMAIL", null);

                                                //media_coden = encodeVideoFile(final_media_path);

                                                SyncMediaData(CP_NAME, video_code, discription.getText().toString(), Global_Data.GLOvel_CUSTOMER_ID, user_email, filename);

//									new Mediaperation().execute(CP_NAME, video_code, discription.getText().toString(), Global_Data.GLOvel_CUSTOMER_ID, user_email, filename);
                                            } else {
                                                try {
                                                    AppLocationManager appLocationManager = new AppLocationManager(Customer_Feed.this);
                                                    Log.d("Class LAT LOG", "Class LAT LOG" + appLocationManager.getLatitude() + " " + appLocationManager.getLongitude());
                                                    Log.d("Service LAT LOG", "Service LAT LOG" + Global_Data.GLOvel_LATITUDE + " " + Global_Data.GLOvel_LONGITUDE);
                                                    PlayService_Location PlayServiceManager = new PlayService_Location(Customer_Feed.this);

                                                    if (PlayServiceManager.checkPlayServices(Customer_Feed.this)) {
                                                        Log.d("Play LAT LOG", "Play LAT LOG" + Global_Data.GLOvel_LATITUDE + " " + Global_Data.GLOvel_LONGITUDE);

                                                    } else if (!String.valueOf(appLocationManager.getLatitude()).equalsIgnoreCase("null") && !String.valueOf(appLocationManager.getLatitude()).equalsIgnoreCase(null) && !String.valueOf(appLocationManager.getLongitude()).equalsIgnoreCase(null) && !String.valueOf(appLocationManager.getLongitude()).equalsIgnoreCase(null)) {
                                                        Global_Data.GLOvel_LATITUDE = String.valueOf(appLocationManager.getLatitude());
                                                        Global_Data.GLOvel_LONGITUDE = String.valueOf(appLocationManager.getLongitude());
                                                    }
                                                } catch (Exception ex) {
                                                    ex.printStackTrace();
                                                }

                                                Long randomPIN = System.currentTimeMillis();
                                                String PINString = String.valueOf(randomPIN);

                                                SharedPreferences spf = Customer_Feed.this.getSharedPreferences("SimpleLogic", 0);
                                                String user_email = spf.getString("USER_EMAIL", null);

                                                loginDataBaseAdapter.insertCustomerServiceMedia("", Global_Data.GLOvel_CUSTOMER_ID, CP_NAME, RE_ID, user_email,
                                                        "", discription.getText().toString(), Current_Date, Current_Date, Global_Data.GLOvel_LATITUDE, Global_Data.GLOvel_LONGITUDE, Global_Data.Default_video_Path, PINString);

                                                loginDataBaseAdapter.insert_TRACK_MOVEMENT(user_email, Global_Data.order_retailer, "VIDEO", Global_Data.GLOvel_LATITUDE, Global_Data.GLOvel_LONGITUDE, formattedDate, user_email, Global_Data.GLOvel_CUSTOMER_ID, formattedDatef);

                                                Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.Offline_save_message), "Yes");
//											Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.Offline_save_message), Toast.LENGTH_LONG);
//											toast.setGravity(Gravity.CENTER, 0, 0);
//											toast.show();

                                                Intent intent = new Intent(Customer_Feed.this, Neworderoptions.class);
                                                startActivity(intent);
                                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                                finish();
                                            }
                                            dialogn.cancel();








                                        }
                                    });

                                    alertDialog.setButton(Dialog.BUTTON_NEGATIVE, getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialogn, int which) {

                                            dialogn.cancel();
                                        }
                                    });

                                    alertDialog.setCancelable(false);
                                    alertDialog.show();







                                }else {

                                    AlertDialog alertDialog = new AlertDialog.Builder(Customer_Feed.this).create(); //Read Update

                                    alertDialog.setMessage(getResources().getString(R.string.Offline_dialog_message));
//}

                                    alertDialog.setButton(Dialog.BUTTON_POSITIVE, getResources().getString(R.string.Save_Offline), new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialogn, int which) {
                                            // TODO Auto-generated method stub
                                            try {
                                                AppLocationManager appLocationManager = new AppLocationManager(Customer_Feed.this);
                                                Log.d("Class LAT LOG", "Class LAT LOG" + appLocationManager.getLatitude() + " " + appLocationManager.getLongitude());
                                                Log.d("Service LAT LOG", "Service LAT LOG" + Global_Data.GLOvel_LATITUDE + " " + Global_Data.GLOvel_LONGITUDE);
                                                PlayService_Location PlayServiceManager = new PlayService_Location(Customer_Feed.this);

                                                if (PlayServiceManager.checkPlayServices(Customer_Feed.this)) {
                                                    Log.d("Play LAT LOG", "Play LAT LOG" + Global_Data.GLOvel_LATITUDE + " " + Global_Data.GLOvel_LONGITUDE);

                                                } else if (!String.valueOf(appLocationManager.getLatitude()).equalsIgnoreCase("null") && !String.valueOf(appLocationManager.getLatitude()).equalsIgnoreCase(null) && !String.valueOf(appLocationManager.getLongitude()).equalsIgnoreCase(null) && !String.valueOf(appLocationManager.getLongitude()).equalsIgnoreCase(null)) {
                                                    Global_Data.GLOvel_LATITUDE = String.valueOf(appLocationManager.getLatitude());
                                                    Global_Data.GLOvel_LONGITUDE = String.valueOf(appLocationManager.getLongitude());
                                                }

                                            } catch (Exception ex) {
                                                ex.printStackTrace();
                                            }


                                            Long randomPIN = System.currentTimeMillis();
                                            String PINString = String.valueOf(randomPIN);

                                            SharedPreferences spf = Customer_Feed.this.getSharedPreferences("SimpleLogic", 0);
                                            String user_email = spf.getString("USER_EMAIL", null);

                                            loginDataBaseAdapter.insertCustomerServiceMedia("", Global_Data.GLOvel_CUSTOMER_ID, CP_NAME, RE_ID, user_email,
                                                    "", discription.getText().toString(), Current_Date, Current_Date, Global_Data.GLOvel_LATITUDE, Global_Data.GLOvel_LONGITUDE, Global_Data.Default_video_Path, PINString);

                                            loginDataBaseAdapter.insert_TRACK_MOVEMENT(user_email, Global_Data.order_retailer, "VIDEO", Global_Data.GLOvel_LATITUDE, Global_Data.GLOvel_LONGITUDE, formattedDate, user_email, Global_Data.GLOvel_CUSTOMER_ID, formattedDatef);

                                            Intent intent = new Intent(Customer_Feed.this, Neworderoptions.class);
                                            startActivity(intent);
                                            finish();

                                            Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.Save_Successfully), "Yes");
//										Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.Save_Successfully), Toast.LENGTH_LONG);
//										toast.setGravity(Gravity.CENTER, 0, 0);
//										toast.show();

                                            dialogn.cancel();

                                        }
                                    });

                                    alertDialog.setButton(Dialog.BUTTON_NEGATIVE, getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialogn, int which) {



                                            dialogn.cancel();
                                        }
                                    });

                                    alertDialog.setCancelable(false);
                                    alertDialog.show();
                                }















                                //new LoadDatabaseAsyncTask().execute();
                                //uploadFile();

                            }

                        } else {
                            if (image_option.equalsIgnoreCase("Gallery") && (picturePath == null || picturePath.equals(""))) {
                                //	Toast.makeText(getApplicationContext(), "Please Capture Media First", Toast.LENGTH_LONG).show();

                                Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.Please_Capture_Image_First), "Yes");
//								Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.Please_Capture_Image_First), Toast.LENGTH_LONG);
//								toast.setGravity(Gravity.CENTER, 0, 0);
//								toast.show();

                            } else if (fileUri.getPath() == null || fileUri.getPath().equals("") || fileUri.equals("")) {
                                //Toast.makeText(getApplicationContext(), "Please Capture Media First", Toast.LENGTH_LONG).show();
                                Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.Please_Capture_Image_First), "Yes");
//								Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.Please_Capture_Image_First), Toast.LENGTH_LONG);
//								toast.setGravity(Gravity.CENTER, 0, 0);
//								toast.show();

                            } else if (discription.getText().toString() == null || discription.getText().toString().equals("")) {
                                //Toast.makeText(getApplicationContext(), "Please Enter Description", Toast.LENGTH_LONG).show();

                                Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.Enter_Description), "Yes");
//								Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.Enter_Description), Toast.LENGTH_LONG);
//								toast.setGravity(Gravity.CENTER, 0, 0);
//								toast.show();
                            } else {

                                //Toast.makeText(getApplicationContext(), discription.getText().toString(), Toast.LENGTH_LONG).show();
                                image_url = "";


//									loginDataBaseAdapter.insertCustomerServiceMedia("",Global_Data.GLOvel_CUSTOMER_ID,CP_NAME,Global_Data.GLOvel_CUSTOMER_ID,Global_Data.GLOvel_USER_EMAIL,
//											image_url,discription.getText().toString(), Current_Date, Current_Date);
//									//get_dialogC();


                                media_text = discription.getText().toString();
                                if (image_option.equalsIgnoreCase("Gallery")) {
                                    final_media_path = picturePath;
                                } else {
                                    final_media_path = fileUri.getPath();
                                }

                                filename = final_media_path.substring(final_media_path.lastIndexOf("/") + 1);


                                isInternetPresent = cd.isConnectingToInternet();
                                if (isInternetPresent) {



                                    AlertDialog alertDialog = new AlertDialog.Builder(Customer_Feed.this).create(); //Read Update

                                    alertDialog.setMessage(getResources().getString(R.string.Save_online_image_message));
                                    //}

                                    alertDialog.setButton(Dialog.BUTTON_POSITIVE, getResources().getString(R.string.Online_Sync), new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialogn, int which) {
                                            // TODO Auto-generated method stub

                                            isInternetPresent = cd.isConnectingToInternet();
                                            if (isInternetPresent) {
                                                button1.setClickable(false);
                                                button1.setEnabled(false);
                                                response_result = "";

                                                dialog = new ProgressDialog(Customer_Feed.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                                                dialog.setMessage(getResources().getString(R.string.Please_Wait));
                                                dialog.setTitle(getResources().getString(R.string.app_name));
                                                dialog.setCancelable(false);
                                                dialog.show();


                                                //media_coden = getStringImage(bitmap);

                                                SharedPreferences spf = Customer_Feed.this.getSharedPreferences("SimpleLogic", 0);
                                                String user_email = spf.getString("USER_EMAIL", null);

                                                loginDataBaseAdapter.insert_TRACK_MOVEMENT(user_email, Global_Data.order_retailer, "IMAGE", Global_Data.GLOvel_LATITUDE, Global_Data.GLOvel_LONGITUDE, formattedDate, user_email, Global_Data.GLOvel_CUSTOMER_ID, formattedDatef);

                                                new Mediaperation().execute(CP_NAME, image_url, discription.getText().toString(), Global_Data.GLOvel_CUSTOMER_ID, user_email, filename);

//									SyncMediaData(CP_NAME, image_url, discription.getText().toString(), Global_Data.GLOvel_CUSTOMER_ID, user_email, filename);
                                            } else {
                                                //Toast.makeText(getApplicationContext(),"No Internet. Data saved on your phone. Please Sync when network available.",Toast.LENGTH_LONG).show();

                                                try {
                                                    AppLocationManager appLocationManager = new AppLocationManager(Customer_Feed.this);
                                                    Log.d("Class LAT LOG", "Class LAT LOG" + appLocationManager.getLatitude() + " " + appLocationManager.getLongitude());
                                                    Log.d("Service LAT LOG", "Service LAT LOG" + Global_Data.GLOvel_LATITUDE + " " + Global_Data.GLOvel_LONGITUDE);
                                                    PlayService_Location PlayServiceManager = new PlayService_Location(Customer_Feed.this);

                                                    if (PlayServiceManager.checkPlayServices(Customer_Feed.this)) {
                                                        Log.d("Play LAT LOG", "Play LAT LOG" + Global_Data.GLOvel_LATITUDE + " " + Global_Data.GLOvel_LONGITUDE);

                                                    } else if (!String.valueOf(appLocationManager.getLatitude()).equalsIgnoreCase("null") && !String.valueOf(appLocationManager.getLatitude()).equalsIgnoreCase(null) && !String.valueOf(appLocationManager.getLongitude()).equalsIgnoreCase(null) && !String.valueOf(appLocationManager.getLongitude()).equalsIgnoreCase(null)) {
                                                        Global_Data.GLOvel_LATITUDE = String.valueOf(appLocationManager.getLatitude());
                                                        Global_Data.GLOvel_LONGITUDE = String.valueOf(appLocationManager.getLongitude());
                                                    }

                                                } catch (Exception ex) {
                                                    ex.printStackTrace();
                                                }

                                                Long randomPIN = System.currentTimeMillis();
                                                String PINString = String.valueOf(randomPIN);

                                                SharedPreferences spf = Customer_Feed.this.getSharedPreferences("SimpleLogic", 0);
                                                String user_email = spf.getString("USER_EMAIL", null);

                                                loginDataBaseAdapter.insertCustomerServiceMedia("", Global_Data.GLOvel_CUSTOMER_ID, CP_NAME, RE_ID, user_email,
                                                        "", discription.getText().toString(), Current_Date, Current_Date, Global_Data.GLOvel_LATITUDE, Global_Data.GLOvel_LONGITUDE, Global_Data.Default_Image_Path, PINString);

                                                loginDataBaseAdapter.insert_TRACK_MOVEMENT(user_email, Global_Data.order_retailer, "IMAGE", Global_Data.GLOvel_LATITUDE, Global_Data.GLOvel_LONGITUDE, formattedDate, user_email, Global_Data.GLOvel_CUSTOMER_ID, formattedDatef);

                                                Intent intent = new Intent(Customer_Feed.this, Neworderoptions.class);
                                                startActivity(intent);
                                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                                finish();
                                                Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.Offline_save_message), "Yes");
//												Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.Offline_save_message), Toast.LENGTH_LONG);
//												toast.setGravity(Gravity.CENTER, 0, 0);
//												toast.show();
                                            }

                                        }
                                    });

                                    alertDialog.setButton(Dialog.BUTTON_NEGATIVE, getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialogn, int which) {




                                            dialogn.cancel();
                                        }
                                    });

                                    alertDialog.setCancelable(false);
                                    alertDialog.show();





                                }else {

                                    AlertDialog alertDialog = new AlertDialog.Builder(Customer_Feed.this).create(); //Read Update

                                    alertDialog.setMessage(getResources().getString(R.string.Save_offline_image_message));
                                    //}

                                    alertDialog.setButton(Dialog.BUTTON_POSITIVE, getResources().getString(R.string.Save_Offline), new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialogn, int which) {
                                            // TODO Auto-generated method stub

                                            try {
                                                AppLocationManager appLocationManager = new AppLocationManager(Customer_Feed.this);
                                                Log.d("Class LAT LOG", "Class LAT LOG" + appLocationManager.getLatitude() + " " + appLocationManager.getLongitude());
                                                Log.d("Service LAT LOG", "Service LAT LOG" + Global_Data.GLOvel_LATITUDE + " " + Global_Data.GLOvel_LONGITUDE);
                                                PlayService_Location PlayServiceManager = new PlayService_Location(Customer_Feed.this);

                                                if (PlayServiceManager.checkPlayServices(Customer_Feed.this)) {
                                                    Log.d("Play LAT LOG", "Play LAT LOG" + Global_Data.GLOvel_LATITUDE + " " + Global_Data.GLOvel_LONGITUDE);

                                                } else if (!String.valueOf(appLocationManager.getLatitude()).equalsIgnoreCase("null") && !String.valueOf(appLocationManager.getLatitude()).equalsIgnoreCase(null) && !String.valueOf(appLocationManager.getLongitude()).equalsIgnoreCase(null) && !String.valueOf(appLocationManager.getLongitude()).equalsIgnoreCase(null)) {
                                                    Global_Data.GLOvel_LATITUDE = String.valueOf(appLocationManager.getLatitude());
                                                    Global_Data.GLOvel_LONGITUDE = String.valueOf(appLocationManager.getLongitude());
                                                }

                                            } catch (Exception ex) {
                                                ex.printStackTrace();
                                            }

                                            Long randomPIN = System.currentTimeMillis();
                                            String PINString = String.valueOf(randomPIN);

                                            SharedPreferences spf = Customer_Feed.this.getSharedPreferences("SimpleLogic", 0);
                                            String user_email = spf.getString("USER_EMAIL", null);

                                            loginDataBaseAdapter.insertCustomerServiceMedia("", Global_Data.GLOvel_CUSTOMER_ID, CP_NAME, RE_ID, user_email,
                                                    "", discription.getText().toString(), Current_Date, Current_Date, Global_Data.GLOvel_LATITUDE, Global_Data.GLOvel_LONGITUDE, Global_Data.Default_Image_Path, PINString);

                                            loginDataBaseAdapter.insert_TRACK_MOVEMENT(user_email, Global_Data.order_retailer, "IMAGE", Global_Data.GLOvel_LATITUDE, Global_Data.GLOvel_LONGITUDE, formattedDate, user_email, Global_Data.GLOvel_CUSTOMER_ID, formattedDatef);

                                            Intent intent = new Intent(Customer_Feed.this, Neworderoptions.class);
                                            startActivity(intent);
                                            finish();

                                            Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.Save_Successfully), "Yes");
//											Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.Save_Successfully), Toast.LENGTH_LONG);
//											toast.setGravity(Gravity.CENTER, 0, 0);
//											toast.show();
                                            dialogn.cancel();

                                        }
                                    });

                                    alertDialog.setButton(Dialog.BUTTON_NEGATIVE, getResources().getString(R.string.Cancel), new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialogn, int which) {



                                            dialogn.cancel();
                                        }
                                    });

                                    alertDialog.setCancelable(false);
                                    alertDialog.show();




                                }









                                //new LoadDatabaseAsyncTask().execute();


                            }
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                        //Toast.makeText(getApplicationContext(), "Please Capture Media First", Toast.LENGTH_LONG).show();

                        Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.Please_Capture_Media_First), "Yes");
//						Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.Please_Capture_Media_First), Toast.LENGTH_LONG);
//						toast.setGravity(Gravity.CENTER, 0, 0);
//						toast.show();

//						Intent intent = new Intent(Customer_Feed.this, Order.class);
//						intent.putExtra("filePath", "");
//
//						startActivity(intent);
//						overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }
                } else {
                    //discription.setVisibility(View.VISIBLE);
                    //Toast.makeText(getApplicationContext(), new_feedback.getText().toString(), Toast.LENGTH_LONG).show();
                    if (CP_NAME.equals("Feedback")) {
                        imageFolder = "feedback";
                        if (new_feedback.getText().toString() == null || new_feedback.getText().toString().equals("")) {
                            //Toast.makeText(getApplicationContext(), "Please Enter Feedback Description", Toast.LENGTH_LONG).show();

                            Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.Please_Enter_Feedback_Description), "Yes");
//							Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.Please_Enter_Feedback_Description), Toast.LENGTH_LONG);
//							toast.setGravity(Gravity.CENTER, 0, 0);
//							toast.show();

                        } else {
                            try {
                                try {
                                    Log.d("Play LAT LOG", "Play LAT LOG" + PlayServiceManager.getLatitude() + " " + PlayServiceManager.getLongitude());

                                    AppLocationManager appLocationManager = new AppLocationManager(Customer_Feed.this);
                                    Log.d("Class LAT LOG", "Class LAT LOG" + appLocationManager.getLatitude() + " " + appLocationManager.getLongitude());
                                    Log.d("Service LAT LOG", "Service LAT LOG" + Global_Data.GLOvel_LATITUDE + " " + Global_Data.GLOvel_LONGITUDE);
                                    PlayService_Location PlayServiceManager = new PlayService_Location(Customer_Feed.this);

                                    if (PlayServiceManager.checkPlayServices(Customer_Feed.this)) {
                                        Log.d("Play LAT LOG", "Play LAT LOG" + Global_Data.GLOvel_LATITUDE + " " + Global_Data.GLOvel_LONGITUDE);

                                    } else if (!String.valueOf(appLocationManager.getLatitude()).equalsIgnoreCase("null") && !String.valueOf(appLocationManager.getLatitude()).equalsIgnoreCase(null) && !String.valueOf(appLocationManager.getLongitude()).equalsIgnoreCase(null) && !String.valueOf(appLocationManager.getLongitude()).equalsIgnoreCase(null)) {
                                        Global_Data.GLOvel_LATITUDE = String.valueOf(appLocationManager.getLatitude());
                                        Global_Data.GLOvel_LONGITUDE = String.valueOf(appLocationManager.getLongitude());
                                    }

                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }

                                Long randomPIN = System.currentTimeMillis();
                                String PINString = String.valueOf(randomPIN);

                                SharedPreferences spf = Customer_Feed.this.getSharedPreferences("SimpleLogic", 0);
                                String user_email = spf.getString("USER_EMAIL", null);

                                loginDataBaseAdapter.insertCustomer_Service_Feedbacks("1", Global_Data.GLOvel_CUSTOMER_ID, RE_ID, user_email,
                                        Current_Date, new_feedback.getText().toString(), "Active", Current_Date, "User1", "User1",
                                        Current_Date, Current_Date, Global_Data.GLOvel_LATITUDE, Global_Data.GLOvel_LONGITUDE, PINString, pictureImagePath);

                                loginDataBaseAdapter.insert_TRACK_MOVEMENT(user_email, Global_Data.order_retailer, "FEEDBACK", Global_Data.GLOvel_LATITUDE, Global_Data.GLOvel_LONGITUDE, formattedDate, user_email, Global_Data.GLOvel_CUSTOMER_ID, formattedDatef);

                                String gaddress = "";
                                try {
                                    if (Global_Data.address.equalsIgnoreCase("null")) {
                                        gaddress = "";
                                    } else {
                                        gaddress = Global_Data.address;
                                    }
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }

                                String sms_body = "";
                                sms_body = "Dear " + Global_Data.USER_MANAGER_NAME + " ," + "\n" + " There is a feedback from  " + Global_Data.CUSTOMER_NAME_NEW + " about  " + new_feedback.getText().toString() + ". This is to keep you updated." + "\n\n" + " Thank you." + "\n" + " " + Global_Data.USER_FIRST_NAME + " " + Global_Data.USER_LAST_NAME + "\n" + " " + gaddress;

                                if (!Global_Data.cus_MAnager_mobile.equalsIgnoreCase(null) && !Global_Data.cus_MAnager_mobile.equalsIgnoreCase("null") && !Global_Data.cus_MAnager_mobile.equalsIgnoreCase("") && !Global_Data.cus_MAnager_mobile.equalsIgnoreCase(" ")) {
                                    //	Global_Data.sendSMS(Global_Data.cus_MAnager_mobile,sms_body, Customer_Feed.this);
                                    // mobile_numbers.add(Global_Data.cus_MAnager_mobile);
                                }

                                //call_service_FORCUSS("Feedback");

                                //Toast.makeText(getApplicationContext(), new_feedback.getText().toString(), Toast.LENGTH_LONG).show();
                                //Toast.makeText(getApplicationContext(), "Your Data Save Successfuly", Toast.LENGTH_LONG).show();
                                Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.Your_Data_Save_Successfuly), "Yes");
//								Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.Your_Data_Save_Successfuly), Toast.LENGTH_LONG);
//								toast.setGravity(Gravity.CENTER, 0, 0);
//								toast.show();

                                Intent intent = new Intent(Customer_Feed.this, Neworderoptions.class);
                                startActivity(intent);
                                finish();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            //String D_TEXT = new_feedback.getText().toString();


                        }
                    } else if (CP_NAME.equals("Claim")) {
                        imageFolder = "claim";
                        if (claim_amount.getText().toString() == null || claim_amount.getText().toString().equals("")) {
                            //Toast.makeText(getApplicationContext(), "Please Enter Claim Amount", Toast.LENGTH_LONG).show();
                            Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.Please_Enter_Claim_Amount), "Yes");
//							Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.Please_Enter_Claim_Amount), Toast.LENGTH_LONG);
//							toast.setGravity(Gravity.CENTER, 0, 0);
//							toast.show();
                        } else if (discription.getText().toString() == null || discription.getText().toString().equals("")) {
                            //Toast.makeText(getApplicationContext(), "Please Enter Description", Toast.LENGTH_LONG).show();

                            Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.Enter_Description), "Yes");
//							Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.Enter_Description), Toast.LENGTH_LONG);
//							toast.setGravity(Gravity.CENTER, 0, 0);
//							toast.show();

                        } else {
                            try {
                                try {
                                    AppLocationManager appLocationManager = new AppLocationManager(Customer_Feed.this);
                                    Log.d("Class LAT LOG", "Class LAT LOG" + appLocationManager.getLatitude() + " " + appLocationManager.getLongitude());
                                    Log.d("Service LAT LOG", "Service LAT LOG" + Global_Data.GLOvel_LATITUDE + " " + Global_Data.GLOvel_LONGITUDE);
                                    PlayService_Location PlayServiceManager = new PlayService_Location(Customer_Feed.this);

                                    if (PlayServiceManager.checkPlayServices(Customer_Feed.this)) {
                                        Log.d("Play LAT LOG", "Play LAT LOG" + Global_Data.GLOvel_LATITUDE + " " + Global_Data.GLOvel_LONGITUDE);
                                    } else if (!String.valueOf(appLocationManager.getLatitude()).equalsIgnoreCase("null") && !String.valueOf(appLocationManager.getLatitude()).equalsIgnoreCase(null) && !String.valueOf(appLocationManager.getLongitude()).equalsIgnoreCase(null) && !String.valueOf(appLocationManager.getLongitude()).equalsIgnoreCase(null)) {
                                        Global_Data.GLOvel_LATITUDE = String.valueOf(appLocationManager.getLatitude());
                                        Global_Data.GLOvel_LONGITUDE = String.valueOf(appLocationManager.getLongitude());
                                    }

                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }

                                Long randomPIN = System.currentTimeMillis();
                                String PINString = String.valueOf(randomPIN);

                                SharedPreferences spf = Customer_Feed.this.getSharedPreferences("SimpleLogic", 0);
                                String user_email = spf.getString("USER_EMAIL", null);

                                isInternetPresent = cd.isConnectingToInternet();
                                if (isInternetPresent) {
                                    dialog = new ProgressDialog(Customer_Feed.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                                    dialog.setMessage(getResources().getString(R.string.Please_Wait));
                                    dialog.setTitle(getResources().getString(R.string.app_name));
                                    dialog.setCancelable(false);
                                    dialog.show();
                                    loginDataBaseAdapter.insertCustomerServiceClaims("sync", Global_Data.GLOvel_CUSTOMER_ID, "Customer", RE_ID,
                                            user_email, Current_Date, discription.getText().toString(), claim_amount.getText().toString(), "Active", "10000", Current_Date, Current_Date, Global_Data.GLOvel_LATITUDE, Global_Data.GLOvel_LONGITUDE, PINString, pictureImagePath);

                                    loginDataBaseAdapter.insert_TRACK_MOVEMENT(user_email, Global_Data.order_retailer, "CLAIM", Global_Data.GLOvel_LATITUDE, Global_Data.GLOvel_LONGITUDE, formattedDate, user_email, Global_Data.GLOvel_CUSTOMER_ID, formattedDatef);

                                    claimsonnline(PINString, user_email, Current_Date, discription.getText().toString(), claim_amount.getText().toString());

                                    String gaddress = "";
                                    try {
                                        if (Global_Data.address.equalsIgnoreCase("null")) {
                                            gaddress = "";
                                        } else {
                                            gaddress = Global_Data.address;
                                        }
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }

                                    String sms_body = "";
                                    sms_body = "Dear " + Global_Data.USER_MANAGER_NAME + " ," + "\n" + " There is a claim from  " + Global_Data.CUSTOMER_NAME_NEW + " for Rs.  " + claim_amount.getText().toString() + " regarding " + discription.getText().toString() + ". This is to keep you updated." + "\n\n" + " Thank you." + "\n" + " " + Global_Data.USER_FIRST_NAME + " " + Global_Data.USER_LAST_NAME + "\n" + " " + gaddress;

                                } else {
                                    dialog = new ProgressDialog(Customer_Feed.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                                    dialog.setMessage(getResources().getString(R.string.Please_Wait));
                                    dialog.setTitle(getResources().getString(R.string.app_name));
                                    dialog.setCancelable(false);
                                    dialog.show();
                                    loginDataBaseAdapter.insertCustomerServiceClaims("1", Global_Data.GLOvel_CUSTOMER_ID, "Customer", RE_ID,
                                            user_email, Current_Date, discription.getText().toString(), claim_amount.getText().toString(), "Active", "10000", Current_Date, Current_Date, Global_Data.GLOvel_LATITUDE, Global_Data.GLOvel_LONGITUDE, PINString, pictureImagePath);

                                    loginDataBaseAdapter.insert_TRACK_MOVEMENT(user_email, Global_Data.order_retailer, "CLAIM", Global_Data.GLOvel_LATITUDE, Global_Data.GLOvel_LONGITUDE, formattedDate, user_email, Global_Data.GLOvel_CUSTOMER_ID, formattedDatef);

                                    String gaddress = "";
                                    try {
                                        if (Global_Data.address.equalsIgnoreCase("null")) {
                                            gaddress = "";
                                        } else {
                                            gaddress = Global_Data.address;
                                        }
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }

                                    String sms_body = "";
                                    sms_body = "Dear " + Global_Data.USER_MANAGER_NAME + " ," + "\n" + " There is a claim from  " + Global_Data.CUSTOMER_NAME_NEW + " for Rs.  " + claim_amount.getText().toString() + " regarding " + discription.getText().toString() + ". This is to keep you updated." + "\n\n" + " Thank you." + "\n" + " " + Global_Data.USER_FIRST_NAME + " " + Global_Data.USER_LAST_NAME + "\n" + " " + gaddress;

                                    if (!Global_Data.cus_MAnager_mobile.equalsIgnoreCase(null) && !Global_Data.cus_MAnager_mobile.equalsIgnoreCase("null") && !Global_Data.cus_MAnager_mobile.equalsIgnoreCase("") && !Global_Data.cus_MAnager_mobile.equalsIgnoreCase(" ")) {
                                        //				Global_Data.sendSMS(Global_Data.cus_MAnager_mobile,sms_body, Customer_Feed.this);
                                        // mobile_numbers.add(Global_Data.cus_MAnager_mobile);
                                    }

                                    //	Toast.makeText(getApplicationContext(), "Your Data Save Successfuly", Toast.LENGTH_LONG).show();

                                    Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.Your_Data_Save_Successfuly), "Yes");
//								Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.Your_Data_Save_Successfuly), Toast.LENGTH_LONG);
//								toast.setGravity(Gravity.CENTER, 0, 0);
//								toast.show();

                                    Intent intent = new Intent(Customer_Feed.this, Neworderoptions.class);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                    finish();
                                    dialog.dismiss();
                                }


//                                loginDataBaseAdapter.insertCustomerServiceClaims("1", Global_Data.GLOvel_CUSTOMER_ID, "Customer", RE_ID,
//                                        user_email, Current_Date, discription.getText().toString(), claim_amount.getText().toString(), "Active", "10000", Current_Date, Current_Date, Global_Data.GLOvel_LATITUDE, Global_Data.GLOvel_LONGITUDE, PINString, pictureImagePath);
//
//                                loginDataBaseAdapter.insert_TRACK_MOVEMENT(user_email, Global_Data.order_retailer, "CLAIM", Global_Data.GLOvel_LATITUDE, Global_Data.GLOvel_LONGITUDE, formattedDate, user_email, Global_Data.GLOvel_CUSTOMER_ID, formattedDatef);

                                //Toast.makeText(getApplicationContext(), new_feedback.getText().toString(), Toast.LENGTH_LONG).show();


                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            //String D_TEXT = new_feedback.getText().toString();
                        }
                    } else if (CP_NAME.equals("Complaints")) {
                        imageFolder = "complaints";
                        if (new_complaints.getText().toString() == null || new_complaints.getText().toString().equals("")) {
                            //Toast.makeText(getApplicationContext(), "Please Enter Complaints", Toast.LENGTH_LONG).show();

                            Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.Please_Enter_Complaints), "Yes");
//							Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.Please_Enter_Complaints), Toast.LENGTH_LONG);
//							toast.setGravity(Gravity.CENTER, 0, 0);
//							toast.show();

                        } else {
                            try {

                                try {
                                    AppLocationManager appLocationManager = new AppLocationManager(Customer_Feed.this);
                                    Log.d("Class LAT LOG", "Class LAT LOG" + appLocationManager.getLatitude() + " " + appLocationManager.getLongitude());
                                    Log.d("Service LAT LOG", "Service LAT LOG" + Global_Data.GLOvel_LATITUDE + " " + Global_Data.GLOvel_LONGITUDE);
                                    PlayService_Location PlayServiceManager = new PlayService_Location(Customer_Feed.this);

                                    if (PlayServiceManager.checkPlayServices(Customer_Feed.this)) {
                                        Log.d("Play LAT LOG", "Play LAT LOG" + Global_Data.GLOvel_LATITUDE + " " + Global_Data.GLOvel_LONGITUDE);

                                    } else if (!String.valueOf(appLocationManager.getLatitude()).equalsIgnoreCase("null") && !String.valueOf(appLocationManager.getLatitude()).equalsIgnoreCase(null) && !String.valueOf(appLocationManager.getLongitude()).equalsIgnoreCase(null) && !String.valueOf(appLocationManager.getLongitude()).equalsIgnoreCase(null)) {
                                        Global_Data.GLOvel_LATITUDE = String.valueOf(appLocationManager.getLatitude());
                                        Global_Data.GLOvel_LONGITUDE = String.valueOf(appLocationManager.getLongitude());
                                    }

                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }

                                Long randomPIN = System.currentTimeMillis();
                                String PINString = String.valueOf(randomPIN);

                                SharedPreferences spf = Customer_Feed.this.getSharedPreferences("SimpleLogic", 0);
                                String user_email = spf.getString("USER_EMAIL", null);

                                isInternetPresent = cd.isConnectingToInternet();
                                if (isInternetPresent) {
                                    dialog = new ProgressDialog(Customer_Feed.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                                    dialog.setMessage(getResources().getString(R.string.Please_Wait));
                                    dialog.setTitle(getResources().getString(R.string.app_name));
                                    dialog.setCancelable(false);
                                    dialog.show();

                                    loginDataBaseAdapter.insertCustomer_Service_Complaints("sync", Global_Data.GLOvel_CUSTOMER_ID, RE_ID, user_email, Current_Date, new_complaints.getText().toString(), "Active", Current_Date, Current_Date, Global_Data.GLOvel_LATITUDE, Global_Data.GLOvel_LONGITUDE, PINString, pictureImagePath);

                                    loginDataBaseAdapter.insert_TRACK_MOVEMENT(user_email, Global_Data.order_retailer, "COMPLAIN", Global_Data.GLOvel_LATITUDE, Global_Data.GLOvel_LONGITUDE, formattedDate, user_email, Global_Data.GLOvel_CUSTOMER_ID, formattedDatef);
                                    claimsonnline(PINString, user_email, Current_Date, new_complaints.getText().toString(), claim_amount.getText().toString());
                                    String gaddress = "";
                                    try {
                                        if (Global_Data.address.equalsIgnoreCase("null")) {
                                            gaddress = "";
                                        } else {
                                            gaddress = Global_Data.address;
                                        }
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }

                                    String sms_body = "";
                                    sms_body = "Dear " + Global_Data.USER_MANAGER_NAME + " ," + "\n" + " There is a complaint  from  " + Global_Data.CUSTOMER_NAME_NEW + " about  " + new_complaints.getText().toString() + ". This is to keep you updated." + "\n\n" + " Thank you." + "\n" + " " + Global_Data.USER_FIRST_NAME + " " + Global_Data.USER_LAST_NAME + "\n" + " " + gaddress;

                                    if (!Global_Data.cus_MAnager_mobile.equalsIgnoreCase(null) && !Global_Data.cus_MAnager_mobile.equalsIgnoreCase("null") && !Global_Data.cus_MAnager_mobile.equalsIgnoreCase("") && !Global_Data.cus_MAnager_mobile.equalsIgnoreCase(" ")) {
                                        //				Global_Data.sendSMS(Global_Data.cus_MAnager_mobile,sms_body, Customer_Feed.this);
                                        // mobile_numbers.add(Global_Data.cus_MAnager_mobile);
                                    }


                                } else {
                                    loginDataBaseAdapter.insertCustomer_Service_Complaints("1", Global_Data.GLOvel_CUSTOMER_ID, RE_ID, user_email, Current_Date, new_complaints.getText().toString(), "Active", Current_Date, Current_Date, Global_Data.GLOvel_LATITUDE, Global_Data.GLOvel_LONGITUDE, PINString, pictureImagePath);

                                    loginDataBaseAdapter.insert_TRACK_MOVEMENT(user_email, Global_Data.order_retailer, "COMPLAIN", Global_Data.GLOvel_LATITUDE, Global_Data.GLOvel_LONGITUDE, formattedDate, user_email, Global_Data.GLOvel_CUSTOMER_ID, formattedDatef);

                                    //Toast.makeText(getApplicationContext(), new_complaints.getText().toString(), Toast.LENGTH_LONG).show();

                                    String gaddress = "";
                                    try {
                                        if (Global_Data.address.equalsIgnoreCase("null")) {
                                            gaddress = "";
                                        } else {
                                            gaddress = Global_Data.address;
                                        }
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }

                                    String sms_body = "";
                                    sms_body = "Dear " + Global_Data.USER_MANAGER_NAME + " ," + "\n" + " There is a complaint  from  " + Global_Data.CUSTOMER_NAME_NEW + " about  " + new_complaints.getText().toString() + ". This is to keep you updated." + "\n\n" + " Thank you." + "\n" + " " + Global_Data.USER_FIRST_NAME + " " + Global_Data.USER_LAST_NAME + "\n" + " " + gaddress;

                                    if (!Global_Data.cus_MAnager_mobile.equalsIgnoreCase(null) && !Global_Data.cus_MAnager_mobile.equalsIgnoreCase("null") && !Global_Data.cus_MAnager_mobile.equalsIgnoreCase("") && !Global_Data.cus_MAnager_mobile.equalsIgnoreCase(" ")) {
                                        //				Global_Data.sendSMS(Global_Data.cus_MAnager_mobile,sms_body, Customer_Feed.this);
                                        // mobile_numbers.add(Global_Data.cus_MAnager_mobile);
                                    }

                                    //Toast.makeText(getApplicationContext(), "Your Data Save Successfuly", Toast.LENGTH_LONG).show();
                                    Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.Your_Data_Save_Successfuly), "Yes");
//								Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.Your_Data_Save_Successfuly), Toast.LENGTH_LONG);
//								toast.setGravity(Gravity.CENTER, 0, 0);
//								toast.show();

                                    Intent intent = new Intent(Customer_Feed.this, Neworderoptions.class);
                                    startActivity(intent);
                                    finish();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            //String D_TEXT = new_feedback.getText().toString();
                        }
                    }
                }
                //i.putExtra("isImage", isImage);
                //launchUploadActivity(true);
            }
        });



          /*ArrayAdapter<String> adapter_state = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, feed_state);
		  adapter_state.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		  feed_spinner.setAdapter(adapter_state);
		  feed_spinner.setOnItemSelectedListener(this);*/
        try {
            ActionBar mActionBar = getActionBar();
            mActionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#910505")));
            // mActionBar.setDisplayShowHomeEnabled(false);
            // mActionBar.setDisplayShowTitleEnabled(false);
            LayoutInflater mInflater = LayoutInflater.from(this);

            View mCustomView = mInflater.inflate(R.layout.action_bar, null);
            mCustomView.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#910505")));
            TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.screenname);
            mTitleTextView.setText(CP_NAME);

            TextView todaysTarget = (TextView) mCustomView.findViewById(R.id.todaysTarget);
            ImageView H_LOGO = (ImageView) mCustomView.findViewById(R.id.Header_logo);
            SharedPreferences sp = Customer_Feed.this.getSharedPreferences("SimpleLogic", 0);

            H_LOGO.setImageResource(R.drawable.list);
            H_LOGO.setVisibility(View.VISIBLE);

//	        if (sp.getFloat("Target", 0.00f)-sp.getFloat("Current_Target", 0.00f)>=0) {
//	        	//todaysTarget.setText("Today's Target : Rs "+String.format("%.2f", (sp.getFloat("Target", 0.00f)-sp.getFloat("Current_Target", 0.00f)))+"");
//				todaysTarget.setText("Target/Acheived : Rs "+String.format(sp.getFloat("Target",0)+"/"+sp.getFloat("Achived", 0)));
//			}
            try {
                int target = (int) Math.round(sp.getFloat("Target", 0));
                int achieved = (int) Math.round(sp.getFloat("Achived", 0));
                Float age_float = (sp.getFloat("Achived", 0) / sp.getFloat("Target", 0)) * 100;
                if (String.valueOf(age_float).equalsIgnoreCase("infinity")) {
                    int age = (int) Math.round(age_float);
                    if (Global_Data.rsstr.length() > 0) {
                        todaysTarget.setText("T/A : " + Global_Data.rsstr + String.format(target + "/" + achieved + " [" + "infinity") + "%" + "]");
                    } else {
                        todaysTarget.setText("T/A : Rs " + String.format(target + "/" + achieved + " [" + "infinity") + "%" + "]");
                    }
                    //todaysTarget.setText("T/A : Rs "+String.format(target+"/"+achieved+" ["+"infinity")+"%"+"]");
                } else {
                    int age = (int) Math.round(age_float);
                    if (Global_Data.rsstr.length() > 0) {
                        todaysTarget.setText("T/A : " + Global_Data.rsstr + String.format(target + "/" + achieved + " [" + age) + "%" + "]");
                    } else {
                        todaysTarget.setText("T/A : Rs " + String.format(target + "/" + achieved + " [" + age) + "%" + "]");
                    }
                    //todaysTarget.setText("T/A : Rs "+String.format(target+"/"+achieved+" ["+age)+"%"+"]");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }

            if (sp.getFloat("Target", 0.00f) - sp.getFloat("Current_Target", 0.00f) < 0) {
//	        	todaysTarget.setText("Today's Target Acheived: Rs "+(sp.getFloat("Current_Target", 0.00f)-sp.getFloat("Target", 0.00f))+"");
                todaysTarget.setText("Today's Target Acheived");
            }

            mActionBar.setCustomView(mCustomView);
            mActionBar.setDisplayShowCustomEnabled(true);
            mActionBar.setHomeButtonEnabled(true);
            mActionBar.setDisplayHomeAsUpEnabled(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private void claimsonnline(String PINString, String user_email, String current_Date, String s, String toString) {

        JSONArray CLAIM = new JSONArray();
        JSONObject product_value_n = new JSONObject();
        JSONArray COM = new JSONArray();

        if (CP_NAME.equals("Claim")) {


            JSONObject cl = new JSONObject();
            try {
                cl.put("code", PINString);
                cl.put("customer_code", Global_Data.GLOvel_CUSTOMER_ID);
                cl.put("user_email", user_email);
                cl.put("date", current_Date);
                cl.put("text", s);
                cl.put("amount", toString);
                cl.put("latitude", Global_Data.GLOvel_LATITUDE);
                cl.put("longitude", Global_Data.GLOvel_LONGITUDE);
                claimPhotoCode = PINString;
                claimPhotoPath = pictureImagePath;

            } catch (JSONException e) {
                e.printStackTrace();
            }
            CLAIM.put(cl);
            try {
                product_value_n.put("claims", CLAIM);
                product_value_n.put("email", user_email);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (CP_NAME.equals("Complaints")) {
            JSONObject cl = new JSONObject();
            try {
                cl.put("code", PINString);
                cl.put("customer_code", Global_Data.GLOvel_CUSTOMER_ID);
                cl.put("user_email", user_email);
                cl.put("date", current_Date);
                cl.put("text", s);
                //  cl.put("amount", toString);
                cl.put("latitude", Global_Data.GLOvel_LATITUDE);
                cl.put("longitude", Global_Data.GLOvel_LONGITUDE);
                complaintCode = PINString;
                complaintPhotoPath = pictureImagePath;

            } catch (JSONException e) {
                e.printStackTrace();
            }
            COM.put(cl);
            try {
                product_value_n.put("complaints", COM);
                product_value_n.put("email", user_email);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        try {

            String domain = Customer_Feed.this.getResources().getString(R.string.service_domain);
            String service_url = "";
            service_url = domain + "uploads/upload_masters_data";
//            product_valuenew.put("no_orders", no_order);
//            product_valuenew.put("email", user_email);
//            Log.d("No Order", no_order.toString());

            Log.i("volley", "domain: " + service_url + product_value_n);
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, service_url, product_value_n, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.i("volley", "response: " + response);


                    response_result = "";
                    //if (response.has("result")) {
                    try {
                        response_result = response.getString("result");
                    } catch (JSONException e) {
                        e.printStackTrace();

                        runOnUiThread(new Runnable() {
                            public void run() {

                                dialog.dismiss();
//                                buttonnoOrderSave.setEnabled(true);
//                                buttonnoOrderSave.setText("Submit");
//                                buttonnoOrdercancel.setEnabled(true);
                            }
                        });
                    }

                    if (response_result.equalsIgnoreCase("Device not found.")) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Global_Data.Custom_Toast(getApplicationContext(), response_result, "yes");
//
                                dialog.dismiss();
                            }
                        });

                    } else {

                        runOnUiThread(new Runnable() {
                            public void run() {
//                                buttonnoOrderSave.setEnabled(true);
//                                buttonnoOrderSave.setText("Submit");
//                                buttonnoOrdercancel.setEnabled(true);
                                if (CP_NAME.equals("Claim")) {
                                    if (claimPhotoPath.length() > 0) {

                                        new doFileUpload3().execute(claimPhotoCode, claimPhotoPath);
                                        //  Global_Data.Custom_Toast(getApplicationContext(), response_result, "yes");

                                    } else {
                                        Global_Data.Custom_Toast(getApplicationContext(), response_result, "yes");

                                        Intent intent = new Intent(Customer_Feed.this, Neworderoptions.class);
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                        finish();
                                        dialog.dismiss();
//                                    progressBar_cyclic.setVisibility(View.GONE);
//                                    priceLout.setVisibility(View.VISIBLE);
//                                    cust_lout1.setVisibility(View.VISIBLE);
//                                    cust_lout2.setVisibility(View.VISIBLE);
//                                    cust_lout3.setVisibility(View.VISIBLE);
//                                    claims_submit.setVisibility(View.VISIBLE);


                                    }
                                } else if (CP_NAME.equals("Complaints")) {
                                    if (complaintPhotoPath.length() > 0) {

                                        new doFileUpload4().execute(complaintCode, complaintPhotoPath);
                                        //  Global_Data.Custom_Toast(getApplicationContext(), response_result, "yes");

                                    } else {
                                        Global_Data.Custom_Toast(getApplicationContext(), response_result, "yes");

                                        Intent intent = new Intent(Customer_Feed.this, Neworderoptions.class);
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                        finish();
                                        dialog.dismiss();
//                                    progressBar_cyclic.setVisibility(View.GONE);
//                                    priceLout.setVisibility(View.VISIBLE);
//                                    cust_lout1.setVisibility(View.VISIBLE);
//                                    cust_lout2.setVisibility(View.VISIBLE);
//                                    cust_lout3.setVisibility(View.VISIBLE);
//                                    claims_submit.setVisibility(View.VISIBLE);


                                    }

                                }


                                // dialog.dismiss();


                            }
                        });


                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(final VolleyError error) {
                    //Toast.makeText(GetData.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    // dialog.dismiss();
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                dialog.dismiss();
//                                progressBar_cyclic.setVisibility(View.GONE);
//                                priceLout.setVisibility(View.VISIBLE);
//                                cust_lout1.setVisibility(View.VISIBLE);
//                                cust_lout2.setVisibility(View.VISIBLE);
//                                cust_lout3.setVisibility(View.VISIBLE);
//                                claims_submit.setVisibility(View.VISIBLE);
                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                                Global_Data.Custom_Toast(getApplicationContext(), "your internet connection is not working, saving locally. Please sync when Internet is available", "");
//
//                                        Toast.makeText(NoOrderActivity.this,
//                                                "your internet connection is not working, saving locally. Please sync when Internet is available",
//                                                Toast.LENGTH_LONG).show();
                            }
                        });

                    } else if (error instanceof AuthFailureError) {
                        runOnUiThread(new Runnable() {
                            public void run() {
//                                progressBar_cyclic.setVisibility(View.GONE);
//                                priceLout.setVisibility(View.VISIBLE);
//                                cust_lout1.setVisibility(View.VISIBLE);
//                                cust_lout2.setVisibility(View.VISIBLE);
//                                cust_lout3.setVisibility(View.VISIBLE);
//                                claims_submit.setVisibility(View.VISIBLE);
                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                dialog.dismiss();
                                Global_Data.Custom_Toast(getApplicationContext(), "Server AuthFailureError  Error", "");
//
//                                        Toast.makeText(NoOrderActivity.this,
//                                                "Server AuthFailureError  Error",
//                                                Toast.LENGTH_LONG).show();
                            }
                        });

                    } else if (error instanceof ServerError) {
                        runOnUiThread(new Runnable() {
                            public void run() {
//                                progressBar_cyclic.setVisibility(View.GONE);
//                                priceLout.setVisibility(View.VISIBLE);
//                                cust_lout1.setVisibility(View.VISIBLE);
//                                cust_lout2.setVisibility(View.VISIBLE);
//                                cust_lout3.setVisibility(View.VISIBLE);
//                                claims_submit.setVisibility(View.VISIBLE);
                                dialog.dismiss();
                                Global_Data.Custom_Toast(getApplicationContext(), "Server   Error", "");

//                                        Toast.makeText(NoOrderActivity.this,
//                                                "Server   Error",
//                                                Toast.LENGTH_LONG).show();
                            }
                        });

                    } else if (error instanceof NetworkError) {
                        runOnUiThread(new Runnable() {
                            public void run() {
//                                progressBar_cyclic.setVisibility(View.GONE);
//                                priceLout.setVisibility(View.VISIBLE);
//                                cust_lout1.setVisibility(View.VISIBLE);
//                                cust_lout2.setVisibility(View.VISIBLE);
//                                cust_lout3.setVisibility(View.VISIBLE);
//                                claims_submit.setVisibility(View.VISIBLE);
                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                dialog.dismiss();
                                Global_Data.Custom_Toast(getApplicationContext(), "your internet connection is not working, saving locally. Please sync when Internet is available", "");

//                                        Toast.makeText(NoOrderActivity.this,
//                                                "your internet connection is not working, saving locally. Please sync when Internet is available",
//                                                Toast.LENGTH_LONG).show();
                            }
                        });

                    } else if (error instanceof ParseError) {
                        runOnUiThread(new Runnable() {
                            public void run() {
//                                progressBar_cyclic.setVisibility(View.GONE);
//                                priceLout.setVisibility(View.VISIBLE);
//                                cust_lout1.setVisibility(View.VISIBLE);
//                                cust_lout2.setVisibility(View.VISIBLE);
//                                cust_lout3.setVisibility(View.VISIBLE);
//                                claims_submit.setVisibility(View.VISIBLE);
                                Global_Data.Custom_Toast(getApplicationContext(), "ParseError   Error", "");
                                dialog.dismiss();
//                                        Toast.makeText(NoOrderActivity.this,
//                                                "ParseError   Error",
//                                                Toast.LENGTH_LONG).show();
                            }
                        });

                    } else {
                        runOnUiThread(new Runnable() {
                            public void run() {
//                                progressBar_cyclic.setVisibility(View.GONE);
//                                priceLout.setVisibility(View.VISIBLE);
//                                cust_lout1.setVisibility(View.VISIBLE);
//                                cust_lout2.setVisibility(View.VISIBLE);
//                                cust_lout3.setVisibility(View.VISIBLE);
//                                claims_submit.setVisibility(View.VISIBLE);
                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                dialog.dismiss();
                                Global_Data.Custom_Toast(getApplicationContext(), error.getMessage(), "");


//                                        Toast.makeText(NoOrderActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });

                    }
                    runOnUiThread(new Runnable() {
                        public void run() {
//                            buttonnoOrderSave.setEnabled(true);
//                            buttonnoOrderSave.setText("Submit");
//                            buttonnoOrdercancel.setEnabled(true);
//
//                            dialog.dismiss();
                        }
                    });

                    // finish();
                }
            });

            RequestQueue requestQueue = Volley.newRequestQueue(Customer_Feed.this);
            // queue.add(jsObjRequest);
            int socketTimeout = 30000;//30 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            jsObjRequest.setRetryPolicy(policy);
            requestQueue.add(jsObjRequest);

        } catch (Exception ex) {
            ex.printStackTrace();

            runOnUiThread(new Runnable() {
                public void run() {
//                    buttonnoOrderSave.setEnabled(true);
//                    buttonnoOrderSave.setText("Submit");
//                    buttonnoOrdercancel.setEnabled(true);
//                    dialog.dismiss();
                    dialog.dismiss();
                }
            });
        }


    }


    /**
     * Here we store the file url as it will be null after returning from camera
     * app
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on screen orientation
        // changes
        outState.putParcelable("file_uri", fileUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        fileUri = savedInstanceState.getParcelable("file_uri");
    }

    private void recordVideo() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        //fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);

        fileUri = getOutputMediaFileUrinew(MEDIA_TYPE_VIDEO);

        // set video quality
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        //video limit 3 sec
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 3);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file
        Global_Data.Default_video_Path = fileUri.getPath();

        // start the video capture Intent
        videoPreview.start();
        videoPreview.setBackgroundDrawable(null);
        startActivityForResult(intent, CAMERA_CAPTURE_VIDEO_REQUEST_CODE);
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                               long arg3) {
        // TODO Auto-generated method stub
        txt_label.setText(getResources().getString(R.string.Previous) + " " + feed_spinner.getSelectedItem().toString());

        if (feed_spinner.getSelectedItem().toString().equalsIgnoreCase("Feedback")) {
            cardFeedback.setVisibility(View.VISIBLE);
            cardComplaint.setVisibility(View.GONE);
            Textview2.setVisibility(View.VISIBLE);
            textview3.setVisibility(View.VISIBLE);
            cardClaimAmount.setVisibility(View.GONE);
            cardClaim.setVisibility(View.GONE);
            media_image.setVisibility(View.GONE);
            media_video.setVisibility(View.GONE);
            media_layout.setVisibility(View.GONE);
            imageview.setVisibility(View.GONE);
            imageVideoLout.setVisibility(View.GONE);
            imageviewr.setVisibility(View.GONE);

            emageview_option.setVisibility(View.GONE);

            m_viveo.setVisibility(View.GONE);
            videoPreview.setVisibility(View.GONE);
        } else if (feed_spinner.getSelectedItem().toString().equalsIgnoreCase("Complaints")) {
            cardFeedback.setVisibility(View.GONE);
            cardComplaint.setVisibility(View.VISIBLE);
            takePicLout.setVisibility(View.VISIBLE);
            Textview2.setVisibility(View.VISIBLE);
            textview3.setVisibility(View.VISIBLE);
            cardClaimAmount.setVisibility(View.GONE);
            cardClaim.setVisibility(View.GONE);
            media_image.setVisibility(View.GONE);
            media_video.setVisibility(View.GONE);
            media_layout.setVisibility(View.GONE);
            imageview.setVisibility(View.GONE);
            imageVideoLout.setVisibility(View.GONE);
            m_viveo.setVisibility(View.GONE);
            videoPreview.setVisibility(View.GONE);
            imageviewr.setVisibility(View.GONE);
            emageview_option.setVisibility(View.GONE);
        } else if (feed_spinner.getSelectedItem().toString().equalsIgnoreCase("Claims")) {
            cardClaimAmount.setVisibility(View.VISIBLE);
            cardClaim.setVisibility(View.VISIBLE);
            Textview2.setVisibility(View.VISIBLE);
            textview3.setVisibility(View.VISIBLE);
            cardFeedback.setVisibility(View.GONE);
            cardComplaint.setVisibility(View.GONE);
            media_image.setVisibility(View.GONE);
            media_video.setVisibility(View.GONE);
            media_layout.setVisibility(View.GONE);
            imageview.setVisibility(View.GONE);
            imageVideoLout.setVisibility(View.GONE);
            m_viveo.setVisibility(View.GONE);
            videoPreview.setVisibility(View.GONE);
            imageviewr.setVisibility(View.GONE);
            emageview_option.setVisibility(View.GONE);
        } else if (feed_spinner.getSelectedItem().toString().equalsIgnoreCase("Media")) {

            media_image.setVisibility(View.VISIBLE);
            media_video.setVisibility(View.VISIBLE);
            media_layout.setVisibility(View.VISIBLE);
            //imageview.setVisibility(View.VISIBLE);
            cardClaim.setVisibility(View.VISIBLE);
            cardClaimAmount.setVisibility(View.GONE);
            imageviewr.setVisibility(View.GONE);
            cardFeedback.setVisibility(View.GONE);
            cardComplaint.setVisibility(View.GONE);
            Textview2.setVisibility(View.GONE);
            textview3.setVisibility(View.GONE);
            imageview.setVisibility(View.VISIBLE);
            imageVideoLout.setVisibility(View.GONE);
            m_viveo.setVisibility(View.GONE);
            videoPreview.setVisibility(View.GONE);
            emageview_option.setVisibility(View.GONE);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                Global_Data.CatlogueStatus = "";
                return true;
        }
//        return super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.add:
                String targetNew = "";
                SharedPreferences sp = Customer_Feed.this.getSharedPreferences("SimpleLogic", 0);
                try {
                    int target = (int) Math.round(sp.getFloat("Target", 0));
                    int achieved = (int) Math.round(sp.getFloat("Achived", 0));
                    Float age_float = (sp.getFloat("Achived", 0) / sp.getFloat("Target", 0)) * 100;
                    if (String.valueOf(age_float).equalsIgnoreCase("infinity")) {
                        int age = (int) Math.round(age_float);
                        if (Global_Data.rsstr.length() > 0) {
                            targetNew = "T/A : " + Global_Data.rsstr + String.format(target + "/" + achieved + " [" + "infinity") + "%" + "]";
                            //todaysTarget.setText("T/A : " + Global_Data.rsstr + String.format(target + "/" + achieved + " [" + "infinity") + "%" + "]");
                        } else {
                            targetNew = "T/A : Rs " + String.format(target + "/" + achieved + " [" + "infinity") + "%" + "]";
                            //todaysTarget.setText("T/A : Rs " + String.format(target + "/" + achieved + " [" + "infinity") + "%" + "]");
                        }
                        //todaysTarget.setText("T/A : Rs "+String.format(target+"/"+achieved+" ["+"infinity")+"%"+"]");
                    } else {
                        int age = (int) Math.round(age_float);
                        if (Global_Data.rsstr.length() > 0) {
                            targetNew = "T/A : " + Global_Data.rsstr + String.format(target + "/" + achieved + " [" + age) + "%" + "]";
                            // todaysTarget.setText();
                        } else {
                            targetNew = "T/A : Rs " + String.format(target + "/" + achieved + " [" + age) + "%" + "]";
                            //todaysTarget.setText("T/A : Rs " + String.format(target + "/" + achieved + " [" + age) + "%" + "]");
                        }
                        //todaysTarget.setText("T/A : Rs "+String.format(target+"/"+achieved+" ["+age)+"%"+"]");
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                View yourView = findViewById(R.id.add);
                new SimpleTooltip.Builder(this)
                        .anchorView(yourView)
                        .text(targetNew)
                        .gravity(Gravity.START)
                        .animated(true)
                        .transparentOverlay(false)
                        .build()
                        .show();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        //super.onBackPressed();
        C_Array.clear();

        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        fileUri = getOutputMediaFileUrinew(MEDIA_TYPE_IMAGE);


        if (CP_NAME.equals("Image")) {

            Object tag = imageview.getTag();

            if (!tag.equals(R.drawable.white_background)) {
                if (fileUri.getPath().equalsIgnoreCase("null") || fileUri.getPath() != null) {
                    get_dialogC_Back();
                } else {
                    this.finish();
                }
            } else {

                this.finish();
            }

        } else if (CP_NAME.equals("video")) {

            if (imageview.getVisibility() == View.GONE) {
                get_dialogC_Back();
            } else {
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                this.finish();
            }

        } else {
            File mediaStorageDir = new File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    Config.IMAGE_DIRECTORY_NAME + "/" + "CUSTOMER_SERVICES");

            try {
                //delete(mediaStorageDir);
                if (mediaStorageDir.isDirectory()) {
                    String[] children = mediaStorageDir.list();
                    for (int i = 0; i < children.length; i++) {
                        new File(mediaStorageDir, children[i]).delete();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            this.finish();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        C_Array.clear();

        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        C_Array.clear();

        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    /**
     * Creating file uri to store image/video
     */
    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    public Uri getOutputMediaFileUrinew(int type) {
        try {
            return Uri.fromFile(getOutputMediaFilenew(type));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Checking device has camera hardware or not
     */
    private boolean isDeviceSupportCamera() {
        if (getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    /**
     * Receiving activity result method will be called after closing the camera
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
//		try {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            File imgFils = new File(pictureImagePath_new);
            if (imgFils.exists()) {

                //img_show = findViewById(R.id.img_show);
                crossimg.setVisibility(View.VISIBLE);
                img_show.setVisibility(View.VISIBLE);

                //img_show.setRotation((float) 90.0);

                Glide.with(Customer_Feed.this).load(pictureImagePath_new).into(img_show);

                //img_show.setImageURI(Uri.fromFile(imgFils));
            }


        }
//		else if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
//			beginCrop(data.getData());
//		} else if (requestCode == Crop.REQUEST_CROP) {
//
//			try {
//				if (data == null) {
//					imageview.setVisibility(View.VISIBLE);
//					imageview.setImageDrawable(Glovel_Drawable);
//					// imageview.setTag(Glovel_bitmap);
//					//imageviewr.setImageBitmap(Glovel_bitmap);
//				} else {
//					handleCrop(resultCode, data, data.getData());
//				}
//			} catch (Exception ex) {
//				ex.printStackTrace();
//				imageview.setVisibility(View.VISIBLE);
//				imageview.setImageDrawable(Glovel_Drawable);
//				// imageview.setTag(Glovel_bitmap);
//				// imageviewr.setImageBitmap(Glovel_bitmap);
//			}
//		}

        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // successfully captured the image
                // display it in image view


//					if (data == null) {
//						Toast.makeText(Customer_Feed.this, "Please Select Image File.", Toast.LENGTH_LONG).show();
//					} else {
                if (image_option.equalsIgnoreCase("image")) {
                    previewCapturedImage();
                } else {
                    if (data == null) {
                        Log.d(TAG, "Data is null");
                        //Toast.makeText(Customer_Feed.this, "Image Not Selected", Toast.LENGTH_LONG).show();
                        Global_Data.Custom_Toast(Customer_Feed.this, getResources().getString(R.string.Image_Not_Selected), "Yes");

//						Toast toast = Toast.makeText(Customer_Feed.this, getResources().getString(R.string.Image_Not_Selected), Toast.LENGTH_LONG);
//						toast.setGravity(Gravity.CENTER, 0, 0);
//						toast.show();

                    } else {
                        previewCapturedImageGellary(data.getData());
                    }
                }

                //}


            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                Global_Data.Custom_Toast(getApplicationContext(),
                        getResources().getString(R.string.User_cancelled_image_capture), "");
//				Toast.makeText(getApplicationContext(),
//						getResources().getString(R.string.User_cancelled_image_capture), Toast.LENGTH_SHORT)
//						.show();
            } else {
                // failed to capture image
                Global_Data.Custom_Toast(getApplicationContext(),
                        getResources().getString(R.string.Failed_to_capture_image), "");
//				Toast.makeText(getApplicationContext(),
//						getResources().getString(R.string.Failed_to_capture_image), Toast.LENGTH_SHORT)
//						.show();
            }
        } else if (requestCode == CAMERA_CAPTURE_VIDEO_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // video successfully recorded
                // preview the recorded video
                //Uri selectedImageUri = data.getData();
                //selectedPath = getPath(selectedImageUri);
                //previewVideonew();
                previewVideo();
            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled recording
                Global_Data.Custom_Toast(getApplicationContext(),
                        getResources().getString(R.string.User_cancelled_video_recording), "Yes");
//				Toast.makeText(getApplicationContext(),
//						getResources().getString(R.string.User_cancelled_video_recording), Toast.LENGTH_SHORT)
//						.show();
            } else {
                // failed to record video
                Global_Data.Custom_Toast(getApplicationContext(),
                        getResources().getString(R.string.Failed_to_record_video), "Yes");
//				Toast.makeText(getApplicationContext(),
//						getResources().getString(R.string.Failed_to_record_video), Toast.LENGTH_SHORT)
//						.show();
            }
        } else if (requestCode == SELECT_VIDEO) {
            System.out.println("SELECT_VIDEO");

            if (data == null) {
                Log.d(TAG, "Data is null");
                //Toast.makeText(Customer_Feed.this, "Video Not Selected", Toast.LENGTH_LONG).show();

                Global_Data.Custom_Toast(Customer_Feed.this, getResources().getString(R.string.Video_Not_Selected), "Yes");
//				Toast toast = Toast.makeText(Customer_Feed.this, getResources().getString(R.string.Video_Not_Selected), Toast.LENGTH_LONG);
//				toast.setGravity(Gravity.CENTER, 0, 0);
//				toast.show();
            } else {
                Log.d(TAG, "Data: " + data);
                // the intent has data, so set the media uri
                Uri selectedImageUri = data.getData();


                if (data == null) {
                    //Toast.makeText(Customer_Feed.this, "Please Select Video File.", Toast.LENGTH_LONG).show();

                    Global_Data.Custom_Toast(Customer_Feed.this, getResources().getString(R.string.Please_Select_Video_File), "Yes");
//					Toast toast = Toast.makeText(Customer_Feed.this, getResources().getString(R.string.Please_Select_Video_File), Toast.LENGTH_LONG);
//					toast.setGravity(Gravity.CENTER, 0, 0);
//					toast.show();

                } else {
                    //selectedPath = getPath(selectedImageUri);
                    selectedPath = getPathFromURI(Customer_Feed.this, selectedImageUri);
                    previewVideonew();
                }

            }

            //fileUri = getOutputMediaFileUri(selectedPath);
            //textView.setText(selectedPath);
        } else if (requestCode == 2 && resultCode == RESULT_OK) {

            try {
                Uri selectedImage = data.getData();

                String[] filePath = {MediaStore.Images.Media.DATA};

                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);

                c.moveToFirst();

                int columnIndex = c.getColumnIndex(filePath[0]);

                pictureImagePath = "file:" + c.getString(columnIndex);


                c.close();

                //new Expenses.LongOperation().execute();
            } catch (Exception ex) {
                ex.printStackTrace();
            }


        }


//		}catch (Exception ex){ex.printStackTrace();
//			Toast.makeText(Customer_Feed.this, "Please Select Media File.", Toast.LENGTH_LONG).show();
//		}
    }

    public String getPath(Uri uri) {
        String path = "";
        try {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            cursor.moveToFirst();
            String document_id = cursor.getString(0);
            document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
            cursor.close();

            cursor = getContentResolver().query(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
            cursor.moveToFirst();
            path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
            cursor.close();
        } catch (Exception ex) {
            ex.printStackTrace();

            String id = uri.getLastPathSegment().split(":")[1];
            final String[] imageColumns = {MediaStore.Video.Media.DATA};
            final String imageOrderBy = null;

            Uri urib = getUri();
            String selectedImagePath = "path";

            Cursor imageCursor = managedQuery(urib, imageColumns,
                    MediaStore.Video.Media._ID + "=" + id, null, imageOrderBy);

            if (imageCursor.moveToFirst()) {
                selectedImagePath = imageCursor.getString(imageCursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            imageCursor.close();
        }

        return path;
    }

    private void doCrop() {
        final ArrayList<CropOption> cropOptions = new ArrayList<CropOption>();

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setType("image/*");

        List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, 0);

        int size = list.size();

        if (size == 0) {
            //Toast.makeText(this, "Can not find image crop app", Toast.LENGTH_SHORT).show();
            Global_Data.Custom_Toast(Customer_Feed.this, getResources().getString(R.string.Can_not_find_image_crop_app), "Yes");
//			Toast toast = Toast.makeText(Customer_Feed.this, getResources().getString(R.string.Can_not_find_image_crop_app), Toast.LENGTH_LONG);
//			toast.setGravity(Gravity.CENTER, 0, 0);
//			toast.show();

            return;
        } else {

            BitmapFactory.Options options = new BitmapFactory.Options();

            // downsizing image as it throws OutOfMemory Exception for larger
            // images


            options.inSampleSize = 8;
            final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
                    options);
            intent.setData(Uri.fromFile(new File(fileUri.getPath())));
            intent.putExtra("outputX", 200);
            intent.putExtra("outputY", 200);
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("scale", true);
            intent.putExtra("return-data", true);

            if (size == 1) {
                Intent i = new Intent(intent);
                ResolveInfo res = list.get(0);

                i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));

                startActivityForResult(i, CROP_FROM_CAMERA);
            } else {
                for (ResolveInfo res : list) {
                    final CropOption co = new CropOption();

                    co.title = getPackageManager().getApplicationLabel(res.activityInfo.applicationInfo);
                    co.icon = getPackageManager().getApplicationIcon(res.activityInfo.applicationInfo);
                    co.appIntent = new Intent(intent);
                    co.appIntent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
                    cropOptions.add(co);
                }

                startActivityForResult(intent, CROP_FROM_CAMERA);


            }
        }
    }

    private void previewVideo() {
        try {
            // hide image preview
            imageview.setVisibility(View.GONE);
            imageVideoLout.setVisibility(View.GONE);
            media_layout.setVisibility(View.GONE);
            emageview_option.setVisibility(View.GONE);
            m_viveo.setVisibility(View.VISIBLE);
            videoPreview.setVisibility(View.VISIBLE);
            Bitmap thumb = ThumbnailUtils.createVideoThumbnail(fileUri.getPath(), MediaStore.Video.Thumbnails.MINI_KIND);
            BitmapDrawable bitmapDrawable = new BitmapDrawable(thumb);
            videoPreview.setBackgroundDrawable(bitmapDrawable);
            videoPreview.setVideoPath(fileUri.getPath());
            // start playing
            videoPreview.requestFocus();
            videoPreview.suspend();

            MediaController mediaController = new MediaController(this);
            mediaController.setAnchorView(videoPreview);

            addVideoToGallery(fileUri.getPath(), Customer_Feed.this);

            //specify the location of media file
            // Uri uri=Uri.parse(Environment.getExternalStorageDirectory().getPath()+"/media/1.mp4");

            //Setting MediaController and URI, then starting the videoView
            videoPreview.setMediaController(mediaController);
            //  videoPreview.setVideoURI(uri);
            videoPreview.requestFocus();
            // videoPreview.setZOrderOnTop(true);
            videoPreview.pause();

            new MediaperationBackGroundVideo().execute(fileUri.getPath());

//			int id = videoPreview.getId();
//
//			ContentResolver crThumb = getContentResolver();
//			BitmapFactory.Options options=new BitmapFactory.Options();
//			options.inSampleSize = 1;
//			Bitmap curThumb = MediaStore.Video.Thumbnails.getThumbnail(crThumb, id, MediaStore.Video.Thumbnails.MICRO_KIND, options);
//			imageview.setImageBitmap(curThumb);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void previewVideonew() {
        try {
            // hide image preview
            imageview.setVisibility(View.GONE);
            imageVideoLout.setVisibility(View.GONE);
            media_layout.setVisibility(View.GONE);
            emageview_option.setVisibility(View.GONE);
            m_viveo.setVisibility(View.VISIBLE);
            videoPreview.setVisibility(View.VISIBLE);
            Bitmap thumb = ThumbnailUtils.createVideoThumbnail(selectedPath, MediaStore.Video.Thumbnails.MINI_KIND);
            BitmapDrawable bitmapDrawable = new BitmapDrawable(thumb);
            videoPreview.setBackgroundDrawable(bitmapDrawable);
            videoPreview.setVideoPath(selectedPath);
            // start playing
            videoPreview.requestFocus();
            videoPreview.suspend();

            MediaController mediaController = new MediaController(this);
            mediaController.setAnchorView(videoPreview);

            //specify the location of media file
            // Uri uri=Uri.parse(Environment.getExternalStorageDirectory().getPath()+"/media/1.mp4");

            //Setting MediaController and URI, then starting the videoView
            videoPreview.setMediaController(mediaController);
            //  videoPreview.setVideoURI(uri);
            videoPreview.requestFocus();
            // videoPreview.setZOrderOnTop(true);
            videoPreview.pause();

            new MediaperationBackGroundVideo().execute(selectedPath);

//			int id = videoPreview.getId();
//
//			ContentResolver crThumb = getContentResolver();
//			BitmapFactory.Options options=new BitmapFactory.Options();
//			options.inSampleSize = 1;
//			Bitmap curThumb = MediaStore.Video.Thumbnails.getThumbnail(crThumb, id, MediaStore.Video.Thumbnails.MICRO_KIND, options);
//			imageview.setImageBitmap(curThumb);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * Display image from a path to ImageView
     */
    private void previewCapturedImage() {
        try {
            // hide video preview
            videoPreview.setVisibility(View.GONE);
            media_layout.setVisibility(View.GONE);
            imageview.setVisibility(View.VISIBLE);
            imageVideoLout.setVisibility(View.VISIBLE);
            crossimg1.setVisibility(View.VISIBLE);
            //
            emageview_option.setVisibility(View.GONE);

            // bimatp factory
            BitmapFactory.Options options = new BitmapFactory.Options();

            // downsizing image as it throws OutOfMemory Exception for larger
            // images
            options.inSampleSize = 8;


            bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
                    options);

            addImageToGallery(fileUri.getPath(), Customer_Feed.this);

            imageview.setImageBitmap(bitmap);
            imageview.setTag(bitmap);
            imageviewr.setImageBitmap(bitmap);

            new MediaperationBackGround().execute(bitmap);

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void previewCapturedImageGellary(Uri data) {
        try {
            // hide video preview
            videoPreview.setVisibility(View.GONE);
            media_layout.setVisibility(View.GONE);
            imageview.setVisibility(View.VISIBLE);
            imageVideoLout.setVisibility(View.VISIBLE);
            //
            emageview_option.setVisibility(View.GONE);

            // bimatp factory
            BitmapFactory.Options options = new BitmapFactory.Options();

            // downsizing image as it throws OutOfMemory Exception for larger
            // images
            options.inSampleSize = 8;


            Uri selectedImage = data;
            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePath[0]);
            //picturePath = c.getString(columnIndex);
            picturePath = getPathfgdg(data);
            c.close();
            //Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));

            //viewImage.setImageBitmap(thumbnail);

            bitmap = (BitmapFactory.decodeFile(picturePath));

            imageview.setImageBitmap(bitmap);
            imageview.setTag(bitmap);
            imageviewr.setImageBitmap(bitmap);

            new MediaperationBackGround().execute(bitmap);

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public String getPathfgdg(Uri uri) {
        if (uri == null) {
            return null;
        }
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        return uri.getPath();
    }


    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        // Crop.of(source, destination).asSquare().start(this);


    }

    private void handleCrop(int resultCode, Intent result, Uri data) {
        if (resultCode == 0) {
            //db
            imageview.setImageDrawable(imageviewr.getDrawable());
            videoPreview.setVisibility(View.GONE);
            media_layout.setVisibility(View.GONE);
            imageview.setVisibility(View.VISIBLE);
            imageVideoLout.setVisibility(View.VISIBLE);
            //
            emageview_option.setVisibility(View.GONE);
        }
        if (resultCode == RESULT_OK) {
            //imageview.setImageURI(Crop.getOutput(result));
            //cropImage= String.valueOf(Crop.getOutput(result));
            //Uri imgUri = (Crop.getOutput(result));
            //Uri imgUri=Uri.parse("content:/"+Crop.getOutput(result));
            //if (imgUri != null) {
            Bitmap bitmap = null;
            try {
                //bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imgUri);
                //cropImage=BitMapToString(bitmap);
                //filename = imgUri.getPath();
                new MediaperationBackGround().execute(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }

            //cropImage = imageview.getTag().toString();

//			String path = imgUri.getPath();
//			cropImage = path.substring(path.lastIndexOf('/') + 1);
            //cropImage = imgUri.getPath();
            //}
            videoPreview.setVisibility(View.GONE);
            media_layout.setVisibility(View.GONE);
            imageview.setVisibility(View.VISIBLE);
            imageVideoLout.setVisibility(View.VISIBLE);
            //
            emageview_option.setVisibility(View.GONE);

            //String picturePath = getPathfgdg(data);
            //bitmap = (BitmapFactory.decodeFile(picturePath));
            //new MediaperationBackGround().execute(bitmap);

        }
//        else if (resultCode == Crop.RESULT_ERROR) {
//            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
//            videoPreview.setVisibility(View.GONE);
//        	media_layout.setVisibility(View.GONE);
//        	imageview.setVisibility(View.VISIBLE);
//        	emageview_option.setVisibility(View.VISIBLE);
//        }
    }

    public void GetListData(String Product_type) {
        if (Product_type.equals("Feedback")) {

            // List<Local_Data> contacts = dbvoc.getAllFeedback();
            List<Local_Data> contacts = dbvoc.getAllFeedback_BYCUSTOMERID(Global_Data.GLOvel_CUSTOMER_ID);
            for (Local_Data cn : contacts) {
                //C_Array.add(cn.getC_Date()+" : "+cn.get_Description());
                //C_Array.add(cn.get_Description()+"             "+cn.getC_Date());
                arrayOfCustServices.add(new CustServicesModel(cn.get_Description(), cn.getC_Date()));
                //Global_Data.local_user = ""+cn.getUser();
                //Global_Data.local_pwd = ""+cn.getPwd();
                //System.out.println("Local Values:-"+Global_Data.local_user+","+Global_Data.local_pwd);

            }
        } else if (Product_type.equals("Claim")) {
            // List<Local_Data> contacts = dbvoc.getAllClaims();
            List<Local_Data> contacts = dbvoc.getAllClaims_BYCUSTOMERID(Global_Data.GLOvel_CUSTOMER_ID);
            for (Local_Data cn : contacts) {
                //C_Array.add(cn.getC_Date()+" :  Rs. "+cn.get_Claims_amount()+"  "+cn.get_Claims());
                arrayOfCustServices.add(new CustServicesModel(cn.get_Claims_amount() + " : " + cn.get_Claims(), cn.getC_Date()));
                //Global_Data.local_user = ""+cn.getUser();
                //Global_Data.local_pwd = ""+cn.getPwd();
                //System.out.println("Local Values:-"+Global_Data.local_user+","+Global_Data.local_pwd);

            }
        } else if (Product_type.equals("Complaints")) {
            //List<Local_Data> contacts = dbvoc.getAllComplaints();
            List<Local_Data> contacts = dbvoc.getAllComplaints_BYCUSTOMERIDN(Global_Data.GLOvel_CUSTOMER_ID);
            for (Local_Data cn : contacts) {
                // C_Array.add(cn.getC_Date()+":"+cn.get_complaints());
                //C_Array.add(cn.getC_Date()+" : "+cn.get_complaints());
                arrayOfCustServices.add(new CustServicesModel(cn.get_complaints(), cn.getC_Date()));
                //Global_Data.local_user = ""+cn.getUser();
                //Global_Data.local_pwd = ""+cn.getPwd();
                //System.out.println("Local Values:-"+Global_Data.local_user+","+Global_Data.local_pwd);

            }
        }
    }


    void delete(File f) throws IOException {
        if (f.isDirectory()) {
            for (File c : f.listFiles()) {
                delete(c);
            }
        } else if (f.getAbsolutePath().endsWith("CUSTOMER_SERVICES")) {
            if (!f.delete()) {
                new FileNotFoundException("Failed to delete file: " + f);
            }
        }
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        String encodedImage = "";
        try {
            bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] imageBytes = baos.toByteArray();
            encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            // String encodedImage =imageBytes.toString();

            return encodedImage;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return encodedImage;
    }

    public void get_dialogC_Back() {
        AlertDialog alertDialog = new AlertDialog.Builder(Customer_Feed.this).create(); //Read Update
        alertDialog.setTitle(getResources().getString(R.string.Confirmation));
        if (CP_NAME.equals("video")) {
            alertDialog.setMessage(getResources().getString(R.string.video_back_button_message));
        } else {
            alertDialog.setMessage(getResources().getString(R.string.image_back_button_message));
        }

        alertDialog.setButton(Dialog.BUTTON_POSITIVE, getResources().getString(R.string.Yes), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub


                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                } else
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//								   InsertOrderAsyncTask insertOrderAsyncTask =new InsertOrderAsyncTask(CaptureSignature.this);
//								   insertOrderAsyncTask.execute();

                Intent intent = new Intent(Customer_Feed.this, Neworderoptions.class);
//				if(fileUri.getPath() != null)
//				{
                intent.putExtra("filePath", fileUri.getPath());


                fileUri = null;
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                dialog.cancel();

//				}
//				else
//				{
//					intent.putExtra("filePath", "");
//				}

            }
        });

        alertDialog.setButton(Dialog.BUTTON_NEGATIVE, getResources().getString(R.string.No_Button_label), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {


                //Toast.makeText(getApplicationContext(),"Your Data Submit Successfuly", Toast.LENGTH_LONG).show();


                dialog.cancel();
            }
        });

        alertDialog.setCancelable(false);
        alertDialog.show();
    }


    public void get_dialogC_Video() {
        AlertDialog alertDialog = new AlertDialog.Builder(Customer_Feed.this).create(); //Read Update
        alertDialog.setTitle(getResources().getString(R.string.Videos));

        alertDialog.setMessage(getResources().getString(R.string.Select_option));

        alertDialog.setButton(Dialog.BUTTON_POSITIVE, getResources().getString(R.string.View_Save_Video_Gallery), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                video_option = "Gallery";

                File folder = new File(video_path);

                //   uriAllFiles= new Uri[allFiles.length];

//				if (!Global_Data.Default_video_Path.equalsIgnoreCase("")) {
//
//					if (discription.getText().toString() == null || discription.getText().toString().equals("")) {
//						//Toast.makeText(getApplicationContext(), "Please Enter Description", Toast.LENGTH_LONG).show();
//
//						Toast toast = Toast.makeText(getApplicationContext(), "Please Enter Description", Toast.LENGTH_LONG);
//						toast.setGravity(Gravity.CENTER, 0, 0);
//						toast.show();
//					}
//					else
//					{
//						try
//						{
//							loginDataBaseAdapter.insertCustomerServiceMedia("",Global_Data.GLOvel_CUSTOMER_ID,CP_NAME, RE_ID, Global_Data.GLOvel_USER_EMAIL,
//									"", discription.getText().toString(),Current_Date, Current_Date,Global_Data.GLOvel_LATITUDE,Global_Data.GLOvel_LONGITUDE,Global_Data.Default_video_Path);
//
//							if (folder.isDirectory()) {
//								allFiles = folder.list();
//
//								if(allFiles.length > 0)
//								{
////							for (int i = 0; i < allFiles.length; i++) {
////								Log.d("all file path" + i, allFiles[i] + allFiles.length);
////							}
//
//									Intent map = new Intent(Customer_Feed.this,SDcard_VideoMain.class);
//									startActivity(map);
//									finish();
//								}
//								else
//								{
//									Toast.makeText(Customer_Feed.this, "Video Not Found.", Toast.LENGTH_SHORT).show();
//								}
//
//							}
//							else
//							{
//								Toast.makeText(Customer_Feed.this, "Video Not Found.", Toast.LENGTH_SHORT).show();
//							}
//
//							//Global_Data.Default_Image_Path = fileUri.getPath();
//
//
//						}catch (Exception ex){
//							ex.printStackTrace();
//						}
//
//
//						SCAN_PATH=image_path;
//						System.out.println(" SCAN_PATH  " +SCAN_PATH);
//
//						Log.d("SCAN PATH", "Scan Path " + SCAN_PATH);
//					}
//
//				}
//				else
//				{
                try {
                    File mediaStorageDir = new File(
                            //Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),Config.IMAGE_DIRECTORY_NAME);
                            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "M_VIDEO");

                    File foldern = new File(mediaStorageDir.getPath());
                    if (foldern.isDirectory()) {
                        allFiles = foldern.list();

                        if (allFiles.length > 0) {
//							for (int i = 0; i < allFiles.length; i++) {
//								Log.d("all file path" + i, allFiles[i] + allFiles.length);
//							}

                            Intent map = new Intent(Customer_Feed.this, SDcard_VideoMain.class);
                            startActivity(map);
                            finish();
                        } else {
                            Global_Data.Custom_Toast(Customer_Feed.this, getResources().getString(R.string.Video_Not_Found), "");
                            //Toast.makeText(Customer_Feed.this, getResources().getString(R.string.Video_Not_Found), Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Global_Data.Custom_Toast(Customer_Feed.this, getResources().getString(R.string.Video_Not_Found), "");
                        //	Toast.makeText(Customer_Feed.this, getResources().getString(R.string.Video_Not_Found), Toast.LENGTH_SHORT).show();
                    }

                    //Global_Data.Default_Image_Path = fileUri.getPath();


                } catch (Exception ex) {
                    ex.printStackTrace();
                }


                SCAN_PATH = image_path;
                System.out.println(" SCAN_PATH  " + SCAN_PATH);

                Log.d("SCAN PATH", "Scan Path " + SCAN_PATH);
                //}
                //Intent intent = new Intent();
                //Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
//				Intent intent;
//				if(android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
//				{
//					intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
//				}
//				else
//				{
//					intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.INTERNAL_CONTENT_URI);
//				}
//				intent.setType("video/*");
//				intent.setAction(Intent.ACTION_GET_CONTENT);
//				startActivityForResult(Intent.createChooser(intent, "Select a Video "), SELECT_VIDEO);

                dialog.cancel();

//

            }
        });

        alertDialog.setButton(Dialog.BUTTON_NEGATIVE, getResources().getString(R.string.Take_Video), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                video_option = "Record";
                CP_NAME = "video";
                recordVideo();
                dialog.cancel();
            }
        });

        //alertDialog.setCancelable(false);
        alertDialog.show();
    }

    public void get_dialogC_Image() {
        AlertDialog alertDialog = new AlertDialog.Builder(Customer_Feed.this).create(); //Read Update
        alertDialog.setTitle(getResources().getString(R.string.Photo));
        alertDialog.setMessage(getResources().getString(R.string.Select_option));
        alertDialog.setButton(Dialog.BUTTON_POSITIVE, getResources().getString(R.string.View_Save_Image_Gallery), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                image_option = "Gallery";
//				Intent intent = new Intent();
                //	fileUri = getOutputMediaFileUrinew(MEDIA_TYPE_IMAGE);
//
                File folder = new File(image_path);
                //   uriAllFiles= new Uri[allFiles.length];
                if (!Global_Data.Default_Image_Path.equalsIgnoreCase("")) {
                    if (discription.getText().toString() == null || discription.getText().toString().equals("")) {
                        //Toast.makeText(getApplicationContext(), "Please Enter Description", Toast.LENGTH_LONG).show();
                        Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.Enter_Description), "Yes");
//						 Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.Enter_Description), Toast.LENGTH_LONG);
//						 toast.setGravity(Gravity.CENTER, 0, 0);
//						 toast.show();
                    } else {
                        try {
                            try {
                                AppLocationManager appLocationManager = new AppLocationManager(Customer_Feed.this);
                                Log.d("Class LAT LOG", "Class LAT LOG" + appLocationManager.getLatitude() + " " + appLocationManager.getLongitude());
                                Log.d("Service LAT LOG", "Service LAT LOG" + Global_Data.GLOvel_LATITUDE + " " + Global_Data.GLOvel_LONGITUDE);
                                PlayService_Location PlayServiceManager = new PlayService_Location(Customer_Feed.this);

                                if (PlayServiceManager.checkPlayServices(Customer_Feed.this)) {
                                    Log.d("Play LAT LOG", "Play LAT LOG" + Global_Data.GLOvel_LATITUDE + " " + Global_Data.GLOvel_LONGITUDE);
                                } else if (!String.valueOf(appLocationManager.getLatitude()).equalsIgnoreCase("null") && !String.valueOf(appLocationManager.getLatitude()).equalsIgnoreCase(null) && !String.valueOf(appLocationManager.getLongitude()).equalsIgnoreCase(null) && !String.valueOf(appLocationManager.getLongitude()).equalsIgnoreCase(null)) {
                                    Global_Data.GLOvel_LATITUDE = String.valueOf(appLocationManager.getLatitude());
                                    Global_Data.GLOvel_LONGITUDE = String.valueOf(appLocationManager.getLongitude());
                                }

                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }

                            Long randomPIN = System.currentTimeMillis();
                            String PINString = String.valueOf(randomPIN);

                            Calendar c = Calendar.getInstance();
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String formattedDate = df.format(c.getTime());

                            SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd");
                            String formattedDatef = dff.format(c.getTime());

                            SharedPreferences spf = Customer_Feed.this.getSharedPreferences("SimpleLogic", 0);
                            String user_email = spf.getString("USER_EMAIL", null);

                            loginDataBaseAdapter.insertCustomerServiceMedia("", Global_Data.GLOvel_CUSTOMER_ID, CP_NAME, RE_ID, user_email,
                                    "", discription.getText().toString(), Current_Date, Current_Date, Global_Data.GLOvel_LATITUDE, Global_Data.GLOvel_LONGITUDE, Global_Data.Default_Image_Path, PINString);

                            loginDataBaseAdapter.insert_TRACK_MOVEMENT(user_email, Global_Data.order_retailer, "IMAGE", Global_Data.GLOvel_LATITUDE, Global_Data.GLOvel_LONGITUDE, formattedDate, user_email, Global_Data.GLOvel_CUSTOMER_ID, formattedDatef);

                            if (folder.isDirectory()) {
                                allFiles = folder.list();

                                if (allFiles.length > 0) {
                                    Intent map = new Intent(Customer_Feed.this, SDcard_imageMain.class);
                                    startActivity(map);
                                    finish();
                                } else {
                                    Global_Data.Custom_Toast(Customer_Feed.this, getResources().getString(R.string.Image_Not_Found), "");
                                    // Toast.makeText(Customer_Feed.this, getResources().getString(R.string.Image_Not_Found), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Global_Data.Custom_Toast(Customer_Feed.this, getResources().getString(R.string.Image_Not_Found), "");
                                // Toast.makeText(Customer_Feed.this, getResources().getString(R.string.Image_Not_Found), Toast.LENGTH_SHORT).show();
                            }
                            //Global_Data.Default_Image_Path = fileUri.getPath();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                        SCAN_PATH = image_path;
                        System.out.println(" SCAN_PATH  " + SCAN_PATH);
                        Log.d("SCAN PATH", "Scan Path " + SCAN_PATH);
                    }
                } else {
                    try {
                        File mediaStorageDir = new File(
                                //Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),Config.IMAGE_DIRECTORY_NAME);
                                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "M_PICTURE");

                        File foldern = new File(mediaStorageDir.getPath());
                        if (foldern.isDirectory()) {
                            allFiles = foldern.list();

                            if (allFiles.length > 0) {
//							for (int i = 0; i < allFiles.length; i++) {
//								Log.d("all file path" + i, allFiles[i] + allFiles.length);
//							}

                                Intent map = new Intent(Customer_Feed.this, SDcard_imageMain.class);
                                startActivity(map);
                                finish();
                            } else {
                                Global_Data.Custom_Toast(Customer_Feed.this, getResources().getString(R.string.Image_Not_Found), "");
                                //Toast.makeText(Customer_Feed.this, getResources().getString(R.string.Image_Not_Found), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Global_Data.Custom_Toast(Customer_Feed.this, getResources().getString(R.string.Image_Not_Found), "");
                            // Toast.makeText(Customer_Feed.this, getResources().getString(R.string.Image_Not_Found), Toast.LENGTH_SHORT).show();
                        }

                        //Global_Data.Default_Image_Path = fileUri.getPath();

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    SCAN_PATH = image_path;
                    System.out.println(" SCAN_PATH  " + SCAN_PATH);

                    Log.d("SCAN PATH", "Scan Path " + SCAN_PATH);
                }

                //File file = new File(SCAN_PATH);

//				Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//				i.setDataAndType(Uri.fromFile(new File(fileUri.getPath())), "image/*");
//				i.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
//				startActivityForResult(i, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
                dialog.cancel();
//
            }
        });

        alertDialog.setButton(Dialog.BUTTON_NEGATIVE, getResources().getString(R.string.Take_Photo), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                requestStoragePermission();

                dialog.cancel();
            }
        });

        //alertDialog.setCancelable(false);
        alertDialog.show();
    }

    public String encodeVideoFile(String filepath) {
        File tempFile = new File(filepath);
        String encodedString = null;

        StringBuilder sb = new StringBuilder();


        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(tempFile);

            byte[] bytes;
            int bSize = 3 * 512;
            byte[] buffer = new byte[bSize];
            int bytesRead;
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            try {
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    output.write(buffer, 0, bytesRead);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            bytes = output.toByteArray();
            encodedString = Base64.encodeToString(bytes, Base64.DEFAULT);

        } catch (Exception e) {
            // TODO: handle exception
        }

        return encodedString;
    }

    private String encodeVideoFiletest(String fileName)
            throws IOException {

        File file = new File(fileName);
        byte[] bytes = loadFile(file);
        byte[] encoded = Base64.encode(bytes, Base64.DEFAULT);
        String encodedString = new String(encoded);

        return encodedString;
    }

    public void SyncMediaData(String media_type, String media_code, String discription, String CUSTOMER_ID, String GLOvel_USER_EMAIL, String file_name) {
        System.gc();
        String reason_code = "";
        try {

            button1.setClickable(false);
            button1.setEnabled(false);

//			dialog = new ProgressDialog(Customer_Feed.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
//			dialog.setMessage("Please wait....");
//			dialog.setTitle("Metal");
//			dialog.setCancelable(false);
//			dialog.show();

            String domain = "";
            String device_id = "";


//			if(media_type.equalsIgnoreCase("Image"))
//			{
//
//				Customer_Feed.this.runOnUiThread(new Runnable() {
//					public void run() {
//						media_coden = getStringImage(bitmap);
//					}
//				});
//			}
//			else
//			{
//				Customer_Feed.this.runOnUiThread(new Runnable() {
//					public void run() {
//						media_coden = encodeVideoFile(final_media_path);
//					}
//				});
//
//			}

            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formattedDate = df.format(c.getTime());

            SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDatef = dff.format(c.getTime());

            SharedPreferences spf = Customer_Feed.this.getSharedPreferences("SimpleLogic", 0);
            String user_email = spf.getString("USER_EMAIL", null);

            loginDataBaseAdapter.insert_TRACK_MOVEMENT(user_email, Global_Data.order_retailer, "VIDEO", Global_Data.GLOvel_LATITUDE, Global_Data.GLOvel_LONGITUDE, formattedDate, user_email, Global_Data.GLOvel_CUSTOMER_ID, formattedDatef);

            SharedPreferences sp = getApplicationContext().getSharedPreferences("SimpleLogic", 0);
            String Cust_domain = sp.getString("Cust_Service_Url", "");
            String service_url = Cust_domain + "metal/api/v1/";
            device_id = sp.getString("devid", "");
            domain = service_url;


            JsonObjectRequest jsObjRequest = null;
            try {


                Log.d("Server url", "Server url" + domain + "customer_service_media");


                JSONArray CUSTOMERSN = new JSONArray();
                JSONArray PICTURE = new JSONArray();
                //JSONObject product_value = new JSONObject();
                JSONObject product_value_n = new JSONObject();
                JSONArray product_imei = new JSONArray();

                final DataBaseHelper dbvoc = new DataBaseHelper(Customer_Feed.this);

                List<Local_Data> contacts = dbvoc.getAllRetailer_cre();

                for (Local_Data cn : contacts) {
                    JSONObject product_value = new JSONObject();
                    product_value.put("user_email", cn.getemail());
                    product_value.put("code", cn.getLEGACY_CUSTOMER_CODE());
                    product_value.put("name", cn.getCUSTOMER_NAME());
                    product_value.put("shop_name", cn.getCUSTOMER_SHOPNAME());
                    product_value.put("address", cn.getADDRESS());
                    product_value.put("street", cn.getSTREET());
                    product_value.put("landmark", cn.getLANDMARK());
                    product_value.put("pincode", cn.getPIN_CODE());
                    product_value.put("mobile_no", cn.getMOBILE_NO());
                    product_value.put("email", cn.getEMAIL_ADDRESS());
                    product_value.put("status", cn.getSTATUS());
                    product_value.put("state_code", cn.getSTATE_ID());
                    product_value.put("city_code", cn.getCITY_ID());
                    product_value.put("beat_code", cn.getBEAT_ID());
                    product_value.put("vatin", cn.getvatin());
                    product_value.put("latitude", cn.getlatitude());
                    product_value.put("longitude", cn.getlongitude());
                    CUSTOMERSN.put(product_value);
                }

                Long randomPIN = System.currentTimeMillis();
                String PINString = String.valueOf(randomPIN);

                JSONObject picture = new JSONObject();
                picture.put("code", PINString);
                picture.put("customer_code", CUSTOMER_ID);
                picture.put("user_email", GLOvel_USER_EMAIL);
                picture.put("media_type", media_type);
                picture.put("media_text", discription);
//                picture.put("media_data", media_coden);
//                picture.put("filename", file_name);
                picture.put("filename", file_name);
                picture.put("media_data", media_coden);
                PICTURE.put(picture);

                product_value_n.put("customers", CUSTOMERSN);
                product_value_n.put("media", PICTURE);
                product_value_n.put("email", user_email);
                Log.d("customers Service", product_value_n.toString());

                jsObjRequest = new JsonObjectRequest(Request.Method.POST, domain + "customer_service_media", product_value_n, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("volley", "response: " + response);

                        Log.d("jV", "JV length" + response.length());
                        //JSONObject json = new JSONObject(new JSONTokener(response));
                        try {

                            String response_result = "";
                            if (response.has("result")) {
                                response_result = response.getString("result");
                            } else {
                                response_result = "data";
                            }


                            if (response_result.equalsIgnoreCase("success")) {

                                dialog.dismiss();
//                                button1.setClickable(true);
//                                button1.setEnabled(true);
                                //Toast.makeText(Customer_Feed.this, "Media Upload Successfully.", Toast.LENGTH_LONG).show();

                                String val = "";
                                //   dbvoc.updateCustomerby_CreateAt(val);

                                try {
                                    File file = new File(Global_Data.Default_video_Path);
                                    if (file.exists()) {

                                        file.delete();
                                        //  dbvoc.getDeleteMediaBYID(Global_Data.Default_video_Path);
                                    }
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }


                                Global_Data.Custom_Toast(Customer_Feed.this, getResources().getString(R.string.Media_Upload_Successfully), "Yes");

//								Toast toast = Toast.makeText(Customer_Feed.this, getResources().getString(R.string.Media_Upload_Successfully), Toast.LENGTH_LONG);
//								toast.setGravity(Gravity.CENTER, 0, 0);
//								toast.show();

                                Intent a = new Intent(Customer_Feed.this, Neworderoptions.class);
                                startActivity(a);
                                finish();


                            } else {

                                dialog.dismiss();
                                button1.setClickable(true);
                                button1.setEnabled(true);
                                Global_Data.Custom_Toast(Customer_Feed.this, response_result, "Yes");
//								Toast toast = Toast.makeText(Customer_Feed.this, response_result, Toast.LENGTH_SHORT);
//								toast.setGravity(Gravity.CENTER, 0, 0);
//								toast.show();
//                                    Intent a = new Intent(context,Order.class);
//                                    context.startActivity(a);
//                                    ((Activity)context).finish();
                            }

                            //  finish();
                            // }

                            // output.setText(data);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            dialog.dismiss();
                        }


                        dialog.dismiss();
                        dialog.dismiss();


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("volley", "error: " + error);
                        //Toast.makeText(Customer_Feed.this, "Some server error occur Please Contact it team.", Toast.LENGTH_LONG).show();

                        Global_Data.Custom_Toast(Customer_Feed.this, getResources().getString(R.string.Server_Error), "Yes");
//                        Toast toast = Toast.makeText(Customer_Feed.this, getResources().getString(R.string.Server_Error), Toast.LENGTH_LONG);
//						toast.setGravity(Gravity.CENTER, 0, 0);
//						toast.show();


                        try {
                            String responseBody = new String(error.networkResponse.data, "utf-8");
                            JSONObject jsonObject = new JSONObject(responseBody);
                        } catch (JSONException e) {
                            //Handle a malformed json response
                        } catch (UnsupportedEncodingException errorn) {

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        dialog.dismiss();
                        button1.setClickable(true);
                        button1.setEnabled(true);
                    }
                });


                RequestQueue requestQueue = Volley.newRequestQueue(Customer_Feed.this);

                int socketTimeout = 2000000;//90 seconds - change to what you want
                RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                jsObjRequest.setRetryPolicy(policy);
                // requestQueue.se
                //requestQueue.add(jsObjRequest);
                jsObjRequest.setShouldCache(false);
                requestQueue.getCache().clear();
                requestQueue.add(jsObjRequest);

            } catch (Exception e) {
                e.printStackTrace();
                dialog.dismiss();
            }


            //createdID=myDbHelper.generateNoOrder(userID,cityID,beatID,retailerID,retailer_code,reasonID,reasonOther,formattedDate);
            //createdID=1;
			/*if (!mobile.equalsIgnoreCase("NA")) {
				SmsManager smsManager=SmsManager.getDefault();
				smsManager.sendTextMessage("mobile", null, "Order ID : "+createdID+" is generated", null, null);
			}



			  if (cd.isConnectingToInternet()) {
                    // Internet Connection is Present
                    // make HTTP requests

                }
			 */

        } catch (Exception e) {
            // TODO: handle exception
            Log.e("DATA", e.getMessage());
        }
    }

    private Uri getUri() {
        String state = Environment.getExternalStorageState();
        if (!state.equalsIgnoreCase(Environment.MEDIA_MOUNTED))
            return MediaStore.Video.Media.INTERNAL_CONTENT_URI;

        return MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    }

    private void startScan() {
        Log.d("Connected", "success" + conn);
        if (conn != null) {
            conn.disconnect();
        }
        conn = new MediaScannerConnection(this, this);
        conn.connect();
    }

    @Override
    public void onMediaScannerConnected() {
        Log.d("onMediaScannerConnected", "success" + conn);
        conn.scanFile(SCAN_PATH, FILE_TYPE);
    }

    @Override
    public void onScanCompleted(String path, Uri uri) {
        try {
            //Log.d("onScanCompleted",uri + "success"+conn);
            System.out.println("URI " + uri);
            if (uri != null) {
//				Intent intent = new Intent(Intent.ACTION_VIEW);
//				intent.setData(uri);
//				startActivity(intent);

                Intent i = new Intent();
                i.setAction(Intent.ACTION_VIEW);
                i.setDataAndType(uri, "image/*");
                startActivity(i);


            }
        } finally {
            //conn.disconnect();
            //conn = null;
        }
    }


    private class MediaperationBackGround extends AsyncTask<Bitmap, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(Bitmap... response) {
            System.gc();

            media_coden = getStringImage(response[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {

            // might want to change "executed" for the returned string passed
            // into onPostExecute() but that is upto you
            Customer_Feed.this.runOnUiThread(new Runnable() {
                public void run() {
                    //dialog.dismiss();
                }
            });
            //dialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    private class MediaperationBackGroundVideo extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... response) {
            System.gc();

            try {
                media_coden = encodeVideoFile(response[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            // might want to change "executed" for the returned string passed
            // into onPostExecute() but that is upto you
            Customer_Feed.this.runOnUiThread(new Runnable() {
                public void run() {
                    //dialog.dismiss();
                }
            });
            //dialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    public class Mediaperation extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... response) {
            System.gc();
            String reason_code = "";
            try {

                Customer_Feed.this.runOnUiThread(new Runnable() {
                    public void run() {
                        button1.setClickable(false);
                        button1.setEnabled(false);
                    }
                });

                String domain = "";
                String device_id = "";

//				if(response[0].equalsIgnoreCase("Image"))
//				{
//
//					Customer_Feed.this.runOnUiThread(new Runnable() {
//						public void run() {
//							media_coden = getStringImage(bitmap);
//						}
//					});
//				}
//				else
//				{
//					Customer_Feed.this.runOnUiThread(new Runnable() {
//						public void run() {
//							media_coden = encodeVideoFile(final_media_path);
//						}
//					});
//
//				}

                SharedPreferences sp = getApplicationContext().getSharedPreferences("SimpleLogic", 0);
                String Cust_domain = sp.getString("Cust_Service_Url", "");
                String service_url = Cust_domain + "metal/api/v1/";
                device_id = sp.getString("devid", "");
                domain = service_url;

                JsonObjectRequest jsObjRequest = null;
                try {
                    Log.d("Server url", "Server url" + domain + "customer_service_media");

                    JSONArray CUSTOMERSN = new JSONArray();
                    JSONArray PICTURE = new JSONArray();
                    //JSONObject product_value = new JSONObject();
                    JSONObject product_value_n = new JSONObject();
                    JSONArray product_imei = new JSONArray();

                    final DataBaseHelper dbvoc = new DataBaseHelper(Customer_Feed.this);

                    List<Local_Data> contacts = dbvoc.getAllRetailer_cre();

                    for (Local_Data cn : contacts) {
                        JSONObject product_value = new JSONObject();
                        product_value.put("user_email", cn.getemail());
                        product_value.put("code", cn.getLEGACY_CUSTOMER_CODE());
                        product_value.put("name", cn.getCUSTOMER_NAME());
                        product_value.put("shop_name", cn.getCUSTOMER_SHOPNAME());
                        product_value.put("address", cn.getADDRESS());
                        product_value.put("street", cn.getSTREET());
                        product_value.put("landmark", cn.getLANDMARK());
                        product_value.put("pincode", cn.getPIN_CODE());
                        product_value.put("mobile_no", cn.getMOBILE_NO());
                        product_value.put("email", cn.getEMAIL_ADDRESS());
                        product_value.put("status", cn.getSTATUS());
                        product_value.put("state_code", cn.getSTATE_ID());
                        product_value.put("city_code", cn.getCITY_ID());
                        product_value.put("beat_code", cn.getBEAT_ID());
                        product_value.put("vatin", cn.getvatin());
                        product_value.put("latitude", cn.getlatitude());
                        product_value.put("longitude", cn.getlongitude());
                        CUSTOMERSN.put(product_value);

                    }

                    Long randomPIN = System.currentTimeMillis();
                    String PINString = String.valueOf(randomPIN);

                    JSONObject picture = new JSONObject();
                    picture.put("code", PINString);
                    picture.put("customer_code", response[3]);
                    picture.put("user_email", response[4]);
                    picture.put("media_type", response[0]);
                    picture.put("media_text", response[2]);
                    picture.put("media_data", media_coden);
                    picture.put("filename", response[5]);
                    PICTURE.put(picture);

                    product_value_n.put("customers", CUSTOMERSN);
                    product_value_n.put("media", PICTURE);
                    product_value_n.put("email", user_email);
                    Log.d("customers Service", product_value_n.toString());

                    jsObjRequest = new JsonObjectRequest(Request.Method.POST, domain + "customer_service_media", product_value_n, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.i("volley", "response: " + response);

                            Log.d("jV", "JV length" + response.length());
                            //JSONObject json = new JSONObject(new JSONTokener(response));
                            try {
                                if (response.has("result")) {
                                    response_result = response.getString("result");
                                } else {
                                    response_result = "data";
                                }

                                //return Response.success(response, HttpHeaderParser.parseCacheHeaders(response));


                                if (response_result.equalsIgnoreCase("success")) {

                                    Customer_Feed.this.runOnUiThread(new Runnable() {
                                        public void run() {
                                            dialog.dismiss();
                                            button1.setClickable(true);
                                            button1.setEnabled(true);
                                        }
                                    });

                                    //Toast.makeText(Customer_Feed.this, "Media Upload Successfully.", Toast.LENGTH_LONG).show();

                                    String val = "";
                                    dbvoc.updateCustomerby_CreateAt(val);

                                    try {

                                        File file = new File(Global_Data.Default_Image_Path);
                                        if (file.exists()) {
//
                                            file.delete();
                                            dbvoc.getDeleteMediaBYID(Global_Data.Default_Image_Path);


                                        }
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }

                                    Customer_Feed.this.runOnUiThread(new Runnable() {
                                        public void run() {
                                            Global_Data.Custom_Toast(Customer_Feed.this, getResources().getString(R.string.Media_Upload_Successfully), "Yes");
//											Toast toast = Toast.makeText(Customer_Feed.this, getResources().getString(R.string.Media_Upload_Successfully), Toast.LENGTH_LONG);
//											toast.setGravity(Gravity.CENTER, 0, 0);
//											toast.show();
                                        }
                                    });


                                    Intent a = new Intent(Customer_Feed.this, Neworderoptions.class);
                                    startActivity(a);
                                    finish();


                                } else {

                                    Customer_Feed.this.runOnUiThread(new Runnable() {
                                        public void run() {
                                            dialog.dismiss();
                                            button1.setClickable(true);
                                            button1.setEnabled(true);
                                            Global_Data.Custom_Toast(Customer_Feed.this, response_result, "Yes");
//											Toast toast = Toast.makeText(Customer_Feed.this,response_result, Toast.LENGTH_SHORT);
//											toast.setGravity(Gravity.CENTER, 0, 0);
//											toast.show();
                                        }
                                    });


//                                    Intent a = new Intent(context,Order.class);
//                                    context.startActivity(a);
//                                    ((Activity)context).finish();
                                }

                                //  finish();
                                // }

                                // output.setText(data);
                            } catch (JSONException e) {
                                e.printStackTrace();

                                Customer_Feed.this.runOnUiThread(new Runnable() {
                                    public void run() {
                                        dialog.dismiss();
                                    }
                                });
                            }


                            Customer_Feed.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    dialog.dismiss();
                                }
                            });
                            //dialog.dismiss();
                            //dialog.dismiss();


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i("volley", "error: " + error);
                            //Toast.makeText(Customer_Feed.this, "Some server error occur Please Contact it team.", Toast.LENGTH_LONG).show();

                            Customer_Feed.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    Global_Data.Custom_Toast(Customer_Feed.this, getResources().getString(R.string.Server_Error), "Yes");
//									Toast toast = Toast.makeText(Customer_Feed.this, getResources().getString(R.string.Server_Error), Toast.LENGTH_LONG);
//									toast.setGravity(Gravity.CENTER, 0, 0);
//									toast.show();
                                }
                            });


                            try {
                                String responseBody = new String(error.networkResponse.data, "utf-8");
                                JSONObject jsonObject = new JSONObject(responseBody);
                            } catch (JSONException e) {
                                //Handle a malformed json response
                            } catch (UnsupportedEncodingException errorn) {

                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }


                            Customer_Feed.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    dialog.dismiss();
                                    button1.setClickable(true);
                                    button1.setEnabled(true);
                                }
                            });

                        }
                    });


                    RequestQueue requestQueue = Volley.newRequestQueue(Customer_Feed.this);

                    int socketTimeout = 2000000;//90 seconds - change to what you want
                    RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                    jsObjRequest.setRetryPolicy(policy);
                    // requestQueue.se
                    //requestQueue.add(jsObjRequest);
                    jsObjRequest.setShouldCache(false);
                    requestQueue.getCache().clear();
                    requestQueue.add(jsObjRequest);

                } catch (Exception e) {
                    e.printStackTrace();

                    Customer_Feed.this.runOnUiThread(new Runnable() {
                        public void run() {
                            dialog.dismiss();
                        }
                    });

                }


                //createdID=myDbHelper.generateNoOrder(userID,cityID,beatID,retailerID,retailer_code,reasonID,reasonOther,formattedDate);
                //createdID=1;
			/*if (!mobile.equalsIgnoreCase("NA")) {
				SmsManager smsManager=SmsManager.getDefault();
				smsManager.sendTextMessage("mobile", null, "Order ID : "+createdID+" is generated", null, null);
			}



			  if (cd.isConnectingToInternet()) {
                    // Internet Connection is Present
                    // make HTTP requests

                }
			 */

            } catch (Exception e) {
                // TODO: handle exception
                Log.e("DATA", e.getMessage());
            }


            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {

            // might want to change "executed" for the returned string passed
            // into onPostExecute() but that is upto you
            Customer_Feed.this.runOnUiThread(new Runnable() {
                public void run() {
                    //dialog.dismiss();
                }
            });
            //dialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    private void requestStoragePermissionvideo() {

        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            CP_NAME = "video";
                            get_dialogC_Video();

                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.Error_occurredd) + error.toString(), "");
                        //Toast.makeText(getApplicationContext(), getResources().getString(R.string.Error_occurredd) + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    private void requestStoragePermission() {

        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            image_option = "image";
                            B_flag = isDeviceSupportCamera();

                            if (B_flag == true) {
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                                fileUri = getOutputMediaFileUrinew(MEDIA_TYPE_IMAGE);

                                Global_Data.Default_Image_Path = fileUri.getPath();

                                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

                                // performCrop(fileUri);
                                // start the image capture Intent
                                startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);

//					File mediaStorageDir = new File(
//							Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
//							Config.IMAGE_DIRECTORY_NAME+"/"+"CUSTOMER_SERVICES");
//
//					try {
//						//delete(mediaStorageDir);
//						if (mediaStorageDir.isDirectory())
//						{
//							String[] children = mediaStorageDir.list();
//							for (int i = 0; i < children.length; i++)
//							{
//								new File(mediaStorageDir, children[i]).delete();
//							}
//						}
//					} catch (Exception e) {
//						e.printStackTrace();
//					}

//					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//					fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
//
//			        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
//	                startActivityForResult(Intent.createChooser(intent, "Complete action using"), PICK_FROM_CAMERA);
                            } else {
                                Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.no_camera), "");
                                //	Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_camera), Toast.LENGTH_LONG).show();
                            }
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.Error_occurredd) + error.toString(), "");
                        //Toast.makeText(getApplicationContext(), getResources().getString(R.string.Error_occurredd) + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    /**
     * Showing Alert Dialog with Settings option
     * Navigates user to app settings
     * NOTE: Keep proper title and message depending on your app
     */
    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Customer_Feed.this);
        builder.setTitle(getResources().getString(R.string.need_permission_message));
        builder.setCancelable(false);
        builder.setMessage(getResources().getString(R.string.need_permission_setting_message));
        builder.setPositiveButton(getResources().getString(R.string.GOTO_SETTINGS), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.Cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    private void requestStoragePermission1() {

        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {

                            B_flag = isDeviceSupportCamera();

                            if (B_flag == true) {
//                    //Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//
//                   // fileUri = getOutputMediaFileUrinew(MEDIA_TYPE_IMAGE);
//                    Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    //startActivityForResult(intent1, MEDIA_TYPE_IMAGE);
//                    startActivityForResult(intent1, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
//                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
//                    }

//                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
//                    }

                                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                                    // Create the File where the photo should go
                                    File photoFile = null;
                                    try {
                                        photoFile = createImageFile();
                                    } catch (Exception ex) {
                                        // Error occurred while creating the File
                                        Log.i("Image TAG", "IOException");
                                        mCurrentPhotoPath = "";
                                    }
                                    // Continue only if the File was successfully created
                                    if (photoFile != null) {
                                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                                        startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
                                    }
                                }

                            } else {
                                Global_Data.Custom_Toast(getApplicationContext(), "no camera on this device", "");
                                // Toast.makeText(getApplicationContext(), "no camera on this device", Toast.LENGTH_LONG).show();
                            }


                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.Error_occurredd) + error.toString(), "");
                        //  Toast.makeText(getApplicationContext(), getResources().getString(R.string.Error_occurredd) + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

//    private File createImageFile() throws IOException {
//        // Create an image file name
//        String imageFileName = Global_Data.custServiceType;
//        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), Global_Data.custServiceType);
//
//        if (!storageDir.exists()) {
//            storageDir.mkdir();
//        }
//
//        File image = File.createTempFile(
//                imageFileName,  // prefix
//                ".jpg",         // suffix
//                storageDir      // directory
//        );
//
//        // Save a file: path for use with ACTION_VIEW intents
//        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
//        // mCurrentPhotoPath = image.getAbsolutePath();
//        return image;
//    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String imageFileName = "custserv";
        File storageDir = null;
        if (CP_NAME.equalsIgnoreCase("Feedback")) {
            storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "feedback");
        } else if (CP_NAME.equalsIgnoreCase("Claim")) {
            storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "claim");
        } else if (CP_NAME.equalsIgnoreCase("Complaints")) {
            storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "complaints");
        } else {
            storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Cust_Services");
        }

        if (!storageDir.exists()) {
            storageDir.mkdir();
        }

        File image = File.createTempFile(
                imageFileName,  // prefix
                ".jpg",         // suffix
                storageDir      // directory
        );

        // Save a file: path for use with ACTION_VIEW intents
        pictureImagePath = "file:" + image.getAbsolutePath();
        pictureImagePath_new = image.getAbsolutePath();
        // mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void requestStoragePermission2() {

        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {

                            B_flag = isDeviceSupportCamera();

                            if (B_flag == true) {

                                //final CharSequence[] options = {getResources().getString(R.string.Take_Photo), getResources().getString(R.string.Choose_from_Gallery), getResources().getString(R.string.Cancel)};
                                final CharSequence[] options = {getResources().getString(R.string.Take_Photo), getResources().getString(R.string.Cancel)};

                                AlertDialog.Builder builder = new AlertDialog.Builder(Customer_Feed.this);

                                builder.setTitle(getResources().getString(R.string.Add_Photo));

                                builder.setItems(options, new DialogInterface.OnClickListener() {

                                    @Override

                                    public void onClick(DialogInterface dialog, int item) {

                                        if (options[item].equals(getResources().getString(R.string.Take_Photo))) {
                                            image_check = "photo";
                                            File photoFile = null;
                                            try {
                                                photoFile = createImageFile();
                                            } catch (IOException ex) {
                                                // Error occurred while creating the File
                                                Log.i("Image TAG", "IOException");
                                                pictureImagePath = "";
                                            }
                                            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                                            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                                            startActivityForResult(cameraIntent, 1);

                                        }
//										else if (options[item].equals(getResources().getString(R.string.Choose_from_Gallery)))
//
//										{
//
//											image_check = "gallery";
//											Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//
//											startActivityForResult(intent, 2);
//
//
//										}
                                        else if (options[item].equals(getResources().getString(R.string.Cancel))) {

                                            dialog.dismiss();

                                        }

                                    }

                                });

                                builder.show();


                            } else {
                                Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.no_camera), "");
                                //Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_camera), Toast.LENGTH_LONG).show();
                            }


                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Global_Data.Custom_Toast(getApplicationContext(), getResources().getString(R.string.Error_occurredd) + error.toString(), "");
                        //Toast.makeText(getApplicationContext(), getResources().getString(R.string.Error_occurredd) + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    private void deletedialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(Customer_Feed.this).create(); //Read Update
        alertDialog.setTitle(getResources().getString(R.string.Warning));
        alertDialog.setMessage(getResources().getString(R.string.image_dialog_warning_message));
        alertDialog.setButton(Dialog.BUTTON_POSITIVE, getResources().getString(R.string.Yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                File file = new File(pictureImagePath_new);
                boolean delete = file.delete();

                File file1 = new File(pictureImagePath);
                boolean delete1 = file1.delete();

                img_show.setVisibility(View.INVISIBLE);
                crossimg.setVisibility(View.INVISIBLE);

                Global_Data.Custom_Toast(Customer_Feed.this, getResources().getString(R.string.image_dialog_delete_success), "Yes");
            }
        });
        alertDialog.setButton(Dialog.BUTTON_NEGATIVE, getResources().getString(R.string.No_Button_label), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    private void deletedialog1() {
        AlertDialog alertDialog = new AlertDialog.Builder(Customer_Feed.this).create(); //Read Update
        alertDialog.setTitle(getResources().getString(R.string.Warning));
        alertDialog.setMessage(getResources().getString(R.string.image_dialog_warning_message));
        alertDialog.setButton(Dialog.BUTTON_POSITIVE, getResources().getString(R.string.Yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                File file1 = new File(Global_Data.Default_Image_Path);
                boolean delete1 = file1.delete();

                imageview.setImageResource(R.drawable.white_background);
                crossimg1.setVisibility(View.INVISIBLE);

                Global_Data.Custom_Toast(Customer_Feed.this, getResources().getString(R.string.image_dialog_delete_success), "Yes");
            }
        });
        alertDialog.setButton(Dialog.BUTTON_NEGATIVE, getResources().getString(R.string.No_Button_label), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }


    public class doFileUpload3 extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... response) {

            String domain = getResources().getString(R.string.service_domain) + "customer_service_feedbacks/customer_service_upload_images";
            Log.i("volley", "domain: " + domain);


            Uri uri4 = Uri.parse(response[1]);
            final File file4 = new File(uri4.getPath());

            try {

                String charset = "UTF-8";
                MultipartUtility multipart = new MultipartUtility(domain, charset);

                //for comp stock
                if (!response[0].equalsIgnoreCase("")) {
                    multipart.addFormField("claim_code", response[0]);
                    multipart.addFormField("claim_type", "claim");
                }

                if (!response[1].equalsIgnoreCase("")) {
                    multipart.addFilePart("claim_image", file4);
                }


                List<String> response1 = multipart.finish();

                Log.v("rht", "SERVER REPLIED:");

                Customer_Feed.this.runOnUiThread(new Runnable() {
                    public void run() {

                        // dialog.dismiss();
                    }
                });

                for (String line : response1) {
                    Log.v("rht", "Line : " + line);
                    response_result_image = line;
                    Customer_Feed.this.runOnUiThread(new Runnable() {
                        public void run() {

                            try {

                                JSONObject obj = new JSONObject(response_result_image);

                                if (obj.getString("message").equalsIgnoreCase("Customer Services uploaded images successfully.")) {
                                    Global_Data.Custom_Toast(getApplicationContext(), "claim created successfully", "yes");
                                    if (file4.exists()) {
                                        if (file4.delete()) {
                                            System.out.println("file Deleted :" + claimPhotoPath);
                                        } else {
                                            System.out.println("file not Deleted :" + claimPhotoPath);
                                        }
                                    }

                                    claimPhotoPath = "";
                                    dialog.dismiss();
                                    finish();

                                }

                                Log.d("My App", obj.toString());

                            } catch (Throwable t) {
                                Log.e("My App", "Could not parse malformed JSON: \"" + response_result_image + "\"");

                                Customer_Feed.this.runOnUiThread(new Runnable() {
                                    public void run() {
                                        dialog.dismiss();
                                        //  dialog.dismiss();

                                        // Globel_Data.Custom_Toast(context, "Something went wrong,Please try again.", "");
                                        // Sub_Dealer_Approval_Stage(context,sub_dealer_approval_stag_val);
                                    }
                                });
                            }
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();

                Customer_Feed.this.runOnUiThread(new Runnable() {
                    public void run() {
                        //  dialog.dismiss();
                        dialog.dismiss();
                        //Globel_Data.Custom_Toast(context, "Something went wrong,Please try again.", "");
                        // Sub_Dealer_Approval_Stage(context,sub_dealer_approval_stag_val);
                    }
                });
            }
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {


            Customer_Feed.this.runOnUiThread(new Runnable() {
                public void run() {

                    dialog.dismiss();
                    // Sub_Dealer_Approval_Stage(context,sub_dealer_approval_stag_val);
                }
            });

        }

        @Override
        protected void onPreExecute() {

//            dialog = new ProgressDialog(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
//            dialog.setMessage("Image Sync in Progress, Please Wait");
//            dialog.setTitle("Metal");
//            dialog.setCancelable(false);
//            dialog.show();

            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }
    public class doFileUpload4 extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... response) {

            String domain = getResources().getString(R.string.service_domain) + "customer_service_feedbacks/customer_service_upload_images";
            Log.i("volley", "domain: " + domain);


            Uri uri4 = Uri.parse(response[1]);
            final File file4 = new File(uri4.getPath());

            try {

                String charset = "UTF-8";
                MultipartUtility multipart = new MultipartUtility(domain, charset);

                //for comp stock
                if (!response[0].equalsIgnoreCase("")) {
                    multipart.addFormField("complaint_code", response[0]);
                    multipart.addFormField("complaint_type", "complaint");
                }

                if (!response[1].equalsIgnoreCase("")) {
                    multipart.addFilePart("complaint_image", file4);
                }


                List<String> response1 = multipart.finish();

                Log.v("rht", "SERVER REPLIED:");

                Customer_Feed.this.runOnUiThread(new Runnable() {
                    public void run() {

                        // dialog.dismiss();
                    }
                });

                for (String line : response1) {
                    Log.v("rht", "Line : " + line);
                    response_result_image = line;
                    Customer_Feed.this.runOnUiThread(new Runnable() {
                        public void run() {

                            try {

                                JSONObject obj = new JSONObject(response_result_image);

                                if (obj.getString("message").equalsIgnoreCase("Customer Services uploaded images successfully.")) {
                                    Global_Data.Custom_Toast(getApplicationContext(), "complaint created successfully", "yes");
                                    if (file4.exists()) {
                                        if (file4.delete()) {
                                            System.out.println("file Deleted :" + complaintPhotoPath);
                                        } else {
                                            System.out.println("file not Deleted :" + complaintPhotoPath);
                                        }
                                    }

                                    complaintPhotoPath = "";
                                    dialog.dismiss();
                                    finish();

                                }

                                Log.d("My App", obj.toString());

                            } catch (Throwable t) {
                                Log.e("My App", "Could not parse malformed JSON: \"" + response_result_image + "\"");

                                Customer_Feed.this.runOnUiThread(new Runnable() {
                                    public void run() {
                                        dialog.dismiss();
                                        //  dialog.dismiss();

                                        // Globel_Data.Custom_Toast(context, "Something went wrong,Please try again.", "");
                                        // Sub_Dealer_Approval_Stage(context,sub_dealer_approval_stag_val);
                                    }
                                });
                            }
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();

                Customer_Feed.this.runOnUiThread(new Runnable() {
                    public void run() {
                        //  dialog.dismiss();
                        dialog.dismiss();
                        //Globel_Data.Custom_Toast(context, "Something went wrong,Please try again.", "");
                        // Sub_Dealer_Approval_Stage(context,sub_dealer_approval_stag_val);
                    }
                });
            }
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {


            Customer_Feed.this.runOnUiThread(new Runnable() {
                public void run() {

                    dialog.dismiss();
                    // Sub_Dealer_Approval_Stage(context,sub_dealer_approval_stag_val);
                }
            });

        }

        @Override
        protected void onPreExecute() {

//            dialog = new ProgressDialog(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
//            dialog.setMessage("Image Sync in Progress, Please Wait");
//            dialog.setTitle("Metal");
//            dialog.setCancelable(false);
//            dialog.show();

            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

}
