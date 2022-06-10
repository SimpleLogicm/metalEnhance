package com.msimplelogic.adapter;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.msimplelogic.activities.BuildConfig;
import com.msimplelogic.activities.Expenses;
import com.msimplelogic.activities.Global_Data;
import com.msimplelogic.activities.R;
import com.msimplelogic.model.Markert_servey3Model;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Marketservey3_adaptor extends RecyclerView.Adapter<Marketservey3_adaptor.MyViewHolder> {

    private Context context;
    List<Markert_servey3Model> list;
    Boolean B_flag;
    String image_check = "";
    private String pictureImagePath = "";
    private String mCurrentPhotoPath = "";

    public Marketservey3_adaptor(Context context, List<Markert_servey3Model> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_marketing_servey, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        if (list.get(position).title_card.equalsIgnoreCase("First")){
         holder.card1_img.setVisibility(View.VISIBLE);
         holder.card2_radio.setVisibility(View.VISIBLE);
        }

        holder.capture_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               // requestStoragePermission();

            }
        });
    }

//    private void requestStoragePermission() {
//
//        Dexter.withActivity(context)
//                .withPermissions(
//                                Manifest.permission.CAMERA,
//                        Manifest.permission.READ_EXTERNAL_STORAGE,
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                .withListener(new MultiplePermissionsListener() {
//                    @Override
//                    public void onPermissionsChecked(MultiplePermissionsReport report) {
//                        // check if all permissions are granted
//                        if (report.areAllPermissionsGranted()) {
//
//                            B_flag = isDeviceSupportCamera();
//
//                            if (B_flag == true) {
//
//
//                                final CharSequence[] options = {"Take Photo","Cancle"};
//
//
//                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
//
//                             //   builder.setTitle(getResources().getString(R.string.Add_Photo));
//
//                                builder.setItems(options, new DialogInterface.OnClickListener() {
//
//                                    @Override
//
//                                    public void onClick(DialogInterface dialog, int item) {
//
//                                        if (options[item].equals("Take Photo"))
//                                        {
//                                            image_check = "photo";
//
//                                            File photoFile = null;
//                                            try {
//                                                photoFile = createImageFile();
//                                            } catch (IOException ex) {
//                                                // Error occurred while creating the File
//                                                Log.i("Image TAG", "IOException");
//                                                pictureImagePath = "";
//                                            }
//                                            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                                            //cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
//                                            Uri photoURI = FileProvider.getUriForFile(context,
//                                                    BuildConfig.APPLICATION_ID + ".provider",
//                                                    photoFile);
//                                            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,photoURI);
//                                            cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//
//                                            startActivityForResult(cameraIntent, 1);
//
//
//                                        } else if (options[item].equals("Cancle")) {
//
//                                            dialog.dismiss();
//
//                                        }
//
//                                    }
//
//                                });
//
//                                builder.show();
//
//
//                            } else {
//                                Global_Data.Custom_Toast(context,"no camera on this device",);
//                                //Toast.makeText(context, .getString(R.string.no_camera), Toast.LENGTH_LONG).show();
//                            }
//
//
//                        }
//
//                        // check for permanent denial of any permission
//                        if (report.isAnyPermissionPermanentlyDenied()) {
//                            // show alert dialog navigating to Settings
//                            showSettingsDialog();
//                        }
//                    }
//
//                    @Override
//                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
//                        token.continuePermissionRequest();
//                    }
//                }).
//                withErrorListener(new PermissionRequestErrorListener() {
//                    @Override
//                    public void onError(DexterError error) {
//                        Global_Data.Custom_Toast(context,"Error occurred!","");
//                        //Toast.makeText(getApplicationContext(), getResources().getString(R.string.Error_occurredd) + error.toString(), Toast.LENGTH_SHORT).show();
//                    }
//                })
//                .onSameThread()
//                .check();
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        if (requestCode == 1 && resultCode == -1) {
//
//            //if (requestCode == 1 && resultCode == RESULT_OK) {
//
//
//            File imgFils = new File(mCurrentPhotoPath);
//            if (imgFils.exists()) {
//
////                img_show = findViewById(R.id.img_show);
////                crossimg.setVisibility(View.VISIBLE);
//                img_show.setVisibility(View.VISIBLE);
//                //img_show.setRotation((float) 90.0);
//                Glide.with(Expenses.this).load(mCurrentPhotoPath).into(img_show);
//                //img_show.setImageURI(Uri.fromFile(imgFils));
//            }
//        }
//    }

    private boolean isDeviceSupportCamera() {
        // this device has a camera
// no camera on this device
        return context.getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA);
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String imageFileName = "exp";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "M_Expenses");

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

        mCurrentPhotoPath = image.getAbsolutePath();

        // mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private CardView card1_img;
        private CardView card2_radio;
        private TextView tv_planogram;
        private TextView tv_picture;
        private ImageView imgshow;
        private LinearLayout capture_btn;
        private TextView radio_que_tv;
        private RadioGroup myRadioGroup;
        private RadioButton option_yes;
        private RadioButton option_no;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            card1_img=itemView.findViewById(R.id.card1_img);
            card2_radio=itemView.findViewById(R.id.card2_radio);
            tv_planogram=itemView.findViewById(R.id.tv_planogram);
            tv_picture=itemView.findViewById(R.id.tv_picture);
            imgshow=itemView.findViewById(R.id.imgshow);
            capture_btn=itemView.findViewById(R.id.capture_btn);
            radio_que_tv=itemView.findViewById(R.id.radio_que_tv);
            myRadioGroup=itemView.findViewById(R.id.myRadioGroup);
            option_yes=itemView.findViewById(R.id.option_yes);
            option_no=itemView.findViewById(R.id.option_no);
        }
    }
}
