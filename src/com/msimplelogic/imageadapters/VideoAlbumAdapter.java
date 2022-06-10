package com.msimplelogic.imageadapters;

/**
 * Created by vinod on 16-11-2016.
 */

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;

import androidx.recyclerview.widget.RecyclerView;

import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.msimplelogic.activities.Customer_Feed;
import com.msimplelogic.activities.DataBaseHelper;
import com.msimplelogic.activities.Global_Data;
import com.msimplelogic.activities.Local_Data;
import com.msimplelogic.activities.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by Ravi Tamada on 18/05/16.
 */
public class VideoAlbumAdapter extends RecyclerView.Adapter<VideoAlbumAdapter.MyViewHolder> {

    private Context mContext;
    public String[] allFiles;
    private File mediaStorageDir;
    private List<Album> albumList;
    DataBaseHelper dbvoc;
    ProgressDialog dialog;
    String response_result = "";
    String r = "";


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, count;
        public ImageView thumbnail, overflow;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            count = (TextView) view.findViewById(R.id.count);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            overflow = (ImageView) view.findViewById(R.id.overflow);

        }
    }


    public VideoAlbumAdapter(Context mContext, List<Album> albumList) {
        this.mContext = mContext;
        this.albumList = albumList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sdcard_medialist, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Album album = albumList.get(position);
        holder.title.setText(album.getName());
        holder.count.setText("");


        // loading album cover using Glide library
        Bitmap thumb = ThumbnailUtils.createVideoThumbnail(album.getThumbnail(), MediaStore.Video.Thumbnails.MINI_KIND);
        BitmapDrawable bitmapDrawable = new BitmapDrawable(thumb);
        holder.thumbnail.setBackgroundDrawable(bitmapDrawable);

        // Glide.with(mContext).load(BitmapDrawable).into(holder.thumbnail);

        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showPopupMenu(holder.overflow, position);
            }
        });
    }

    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view, final int posi) {
        // inflate menu

        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_album, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener(posi));
        popup.show();
    }

    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        int pos;

        public MyMenuItemClickListener(int posi) {
            pos = posi;
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {


            //  int listPosition = getAdapterPosition();
            dbvoc = new DataBaseHelper(mContext);
            String image_url = albumList.get(pos).getThumbnail();

            switch (menuItem.getItemId()) {
                case R.id.action_add_favourite:
                    //Toast.makeText(mContext, "Add to favourite", Toast.LENGTH_SHORT).show();

                    String r = requestStoragegpsPermissionsigna();
                    if (r.equalsIgnoreCase("granted")) {
                        CallService(mContext, image_url, pos);
                    }

                    // uploadVideo(mContext,image_url);

                    return true;
                case R.id.action_play_next:

                    //String animal = albumList.get(position);

                    String rr = requestStoragegpsPermissionsigna();
                    if (rr.equalsIgnoreCase("granted")) {
                        try {


                            File file = new File(image_url);
                            if (file.exists()) {
//                            if (Global_Data.Default_Image_Path.equalsIgnoreCase(image_url)) {
//                                Toast.makeText(mContext, "You can not delete current capture image.", Toast.LENGTH_SHORT).show();
//                            } else {
                                file.delete();
                                albumList.remove(pos);
                                notifyDataSetChanged();
                                dbvoc.getDeleteMediaBYID(image_url);
                                // }

                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                        mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "M_VIDEO");
                        //Toast.makeText(mContext, image_url, Toast.LENGTH_SHORT).show();
                        File folder = new File(mediaStorageDir.getPath());
                        try {
                            if (folder.isDirectory()) {
                                allFiles = folder.list();

                                if (allFiles.length <= 0) {

                                    Global_Data.Custom_Toast(mContext, mContext.getResources().getString(R.string.All_files_deleted),"");
                                    Intent i = new Intent(mContext, Customer_Feed.class);
                                    i.putExtra("CP_NAME", "video");
                                    i.putExtra("RE_TEXT", "");
                                    mContext.startActivity(i);
                                    ((Activity) mContext).finish();
                                }


                            }


                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                        List<Local_Data> details = dbvoc.getAllPICTURESDBY("video");

                        if (details.size() <= 0) {


                            Global_Data.Custom_Toast(mContext, mContext.getResources().getString(R.string.All_files_deleted),"");
                            Intent i = new Intent(mContext, Customer_Feed.class);
                            i.putExtra("CP_NAME", "video");
                            i.putExtra("RE_TEXT", "");
                            mContext.startActivity(i);
                            ((Activity) mContext).finish();
                        }
                    }


                    return true;
                default:
            }
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return albumList.size();
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

    public void CallService(final Context context, final String imageurl, final int posi) {
        String filename = imageurl.substring(imageurl.lastIndexOf("/") + 1);
        System.gc();
        dialog = new ProgressDialog(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        dialog.setMessage(context.getResources().getString(R.string.Please_Wait));
        dialog.setTitle(context.getResources().getString(R.string.app_name));
        dialog.setCancelable(false);
        dialog.show();
        String reason_code = "";
        try {

            String hashcode = encodeVideoFile(imageurl);
            String domain = "";
            String device_id = "";


            SharedPreferences sp = context.getSharedPreferences("SimpleLogic", 0);
            String Cust_domain = sp.getString("Cust_Service_Url", "");
            String service_url = Cust_domain + "metal/api/v1/";
            device_id = sp.getString("devid", "");
            domain = service_url;


            String email = "";

            SharedPreferences spf = context.getSharedPreferences("SimpleLogic", 0);
            email = spf.getString("USER_EMAIL", null);

            JsonObjectRequest jsObjRequest = null;
            try {


                Log.d("Server url", "Server url" + domain + "customer_service_media");


                JSONArray CUSTOMERSN = new JSONArray();
                JSONArray PICTURE = new JSONArray();
                //JSONObject product_value = new JSONObject();
                JSONObject product_value_n = new JSONObject();
                JSONArray product_imei = new JSONArray();

                final DataBaseHelper dbvoc = new DataBaseHelper(context);

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

                List<Local_Data> imagevalue = dbvoc.getAllPICTURESDBYID(imageurl);

                for (Local_Data cn : imagevalue) {
                    JSONObject picture = new JSONObject();
                    picture.put("code", cn.getCode());
                    picture.put("customer_code", cn.getCust_Code());
                    picture.put("user_email", cn.getEMAIL_ADDRESS());
                    picture.put("media_type", cn.get_mediaType());
                    picture.put("media_text", cn.getcalender_details());
                    picture.put("filename", filename);
                    picture.put("media_data", hashcode);

                    PICTURE.put(picture);
                }


                product_value_n.put("customers", CUSTOMERSN);
                product_value_n.put("media", PICTURE);
                product_value_n.put("email", email);
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


                                String val = "";
                                dbvoc.updateCustomerby_CreateAt(val);

                                String r = requestStoragegpsPermissionsigna();
                                if (r.equalsIgnoreCase("granted")) {
                                    try {


                                        File file = new File(imageurl);
                                        if (file.exists()) {
//                            if (Global_Data.Default_Image_Path.equalsIgnoreCase(image_url)) {
//                                Toast.makeText(mContext, "You can not delete current capture image.", Toast.LENGTH_SHORT).show();
//                            } else {
                                            file.delete();
                                            albumList.remove(posi);
                                            notifyDataSetChanged();
                                            dbvoc.getDeleteMediaBYID(imageurl);
                                            // }

                                        }
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }

                                    mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "M_VIDEO");
                                    // Toast.makeText(mContext, imageurl, Toast.LENGTH_SHORT).show();
                                    File folder = new File(mediaStorageDir.getPath());
                                    try {
                                        if (folder.isDirectory()) {
                                            allFiles = folder.list();

                                            if (allFiles.length <= 0) {
                                                // Toast.makeText(mContext, "All files deleted", Toast.LENGTH_SHORT).show();
                                                Intent i = new Intent(mContext, Customer_Feed.class);
                                                i.putExtra("CP_NAME", "video");
                                                i.putExtra("RE_TEXT", "");
                                                mContext.startActivity(i);
                                                ((Activity) mContext).finish();
                                            }


                                        }


                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                }



                                Global_Data.Custom_Toast(context, context.getResources().getString(R.string.Media_Upload_Successfully),"yes");

                                List<Local_Data> details = dbvoc.getAllPICTURESDBY("video");

                                if (details.size() <= 0) {

                                    // Toast.makeText(mContext, "All files deleted", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(mContext, Customer_Feed.class);
                                    i.putExtra("CP_NAME", "video");
                                    i.putExtra("RE_TEXT", "");
                                    mContext.startActivity(i);
                                    ((Activity) mContext).finish();
                                }

                                dialog.dismiss();

                            } else {


                                dialog.dismiss();

                                Global_Data.Custom_Toast(context, response_result,"yes");

                            }

                            //  finish();
                            // }

                            // output.setText(data);
                        } catch (JSONException e) {
                            e.printStackTrace();


                            dialog.dismiss();

                        }


                        dialog.dismiss();

                        //dialog.dismiss();
                        //dialog.dismiss();


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("volley", "error: " + error);
                        //Toast.makeText(Customer_Feed.this, "Some server error occur Please Contact it team.", Toast.LENGTH_LONG).show();




                        Global_Data.Custom_Toast(context, context.getResources().getString(R.string.Server_Error), "yes");

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


                    }
                });


                RequestQueue requestQueue = Volley.newRequestQueue(context);

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

        } catch (Exception e) {
            // TODO: handle exception
            Log.e("DATA", e.getMessage());
        }
    }


    private String requestStoragegpsPermissionsigna() {


        Dexter.withActivity((Activity) mContext)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {

                            r = "granted";
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

                        Global_Data.Custom_Toast(mContext, mContext.getResources().getString(R.string.Error_occurredd) + error.toString(),"");
                        r = "d";
                    }
                })
                .onSameThread()
                .check();

        return r;
    }

    /**
     * Showing Alert Dialog with Settings option
     * Navigates user to app settings
     * NOTE: Keep proper title and message depending on your app
     */
    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(mContext.getResources().getString(R.string.need_permission_message));
        builder.setCancelable(false);
        builder.setMessage(mContext.getResources().getString(R.string.need_permission_setting_message));
        builder.setPositiveButton(mContext.getResources().getString(R.string.GOTO_SETTINGS), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton(mContext.getResources().getString(R.string.Cancel), new DialogInterface.OnClickListener() {
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
        Uri uri = Uri.fromParts("package", mContext.getPackageName(), null);
        intent.setData(uri);
        ((Activity) mContext).startActivityForResult(intent, 101);
    }

}

