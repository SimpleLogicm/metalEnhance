package com.msimplelogic.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.vision.barcode.Barcode;
import com.msimplelogic.activities.kotlinFiles.kotGlobal.QuickOrder;

import java.util.List;

import info.androidhive.barcode.BarcodeReader;

public class ScanActivity extends BaseActivity implements BarcodeReader.BarcodeReaderListener {

    BarcodeReader barcodeReader;
    String customer_name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        Intent intent = getIntent();
        String shop_name = intent.getStringExtra("customer_name");
        if(shop_name != null){
            customer_name = shop_name;
        }

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // get the barcode reader instance
        barcodeReader = (BarcodeReader) getSupportFragmentManager().findFragmentById(R.id.barcode_scanner);
    }

    @Override
    public void onScanned(Barcode barcode) {

        // playing barcode reader beep sound
        barcodeReader.playBeep();

        // ticket details activity by passing barcode
        Log.d("Bar Code","Bar Code"+barcode.displayValue);

        //Toast.makeText(this, barcode.displayValue, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(ScanActivity.this, QuickOrder.class);

        intent.putExtra("barcode", barcode.displayValue);
        intent.putExtra("shopname", customer_name);
        startActivity(intent);
        finish();
    }

    @Override
    public void onScannedMultiple(List<Barcode> list) {

    }

    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {

    }

    @Override
    public void onScanError(String s) {
       // Toast.makeText(getApplicationContext(), "Error occurred while scanning " + s, Toast.LENGTH_SHORT).show();
        Global_Data.Custom_Toast(getApplicationContext(), "Error occurred while scanning " + s,"");
    }

    @Override
    public void onCameraPermissionDenied() {
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        //super.onBackPressed();
            super.onBackPressed();
            finish();

//        Intent i = new Intent(ScanActivity.this,QuickOrder.class);
//        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//        startActivity(i);
//

    }

}
