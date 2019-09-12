package com.mhaa98.pakarbangunan;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import pub.devrel.easypermissions.EasyPermissions;
import static android.os.Environment.getExternalStoragePublicDirectory;

public class FormActivity extends AppCompatActivity implements LocationListener {

    EditText nama_bg, lantai, tahun, alamat_bg, lati, longi, nama, alamat, no_hp;
    ImageView poto;
    Button next;

    final int CAMERA_REQUEST_CODE1 = 1;
    final int CAMERA_REQUEST_CODE2 = 0;
    String pathToFile;

    boolean isi_gambar;
    LocationManager locationManager;
    ImageButton gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        getSupportActionBar().setTitle("Formulir");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (Build.VERSION.SDK_INT >= 23) {
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
        }

        checkPermision();

        gps = findViewById(R.id.location_btn);
        gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();

            }
        });

        nama_bg = (EditText) findViewById(R.id.nama_bangunan);
        lantai = (EditText) findViewById(R.id.jml_lantai);
        tahun = (EditText) findViewById(R.id.thn_dibuat);
        alamat_bg = (EditText) findViewById(R.id.alamat_bangunan);
        lati = (EditText) findViewById(R.id.latitude);
        longi = (EditText) findViewById(R.id.longitude);
        poto = findViewById(R.id.add_photo_btn);
        nama = (EditText) findViewById(R.id.nama_person);
        alamat = (EditText) findViewById(R.id.alamat_person);
        no_hp = (EditText) findViewById(R.id.nomor_person);
        next = findViewById(R.id.next_btn);

        isi_gambar = false;
        poto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("masuk");
                SelectImage();
            }
        });


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nama_bg.getText().length() != 0 && lantai.getText().length() != 0 && tahun.getText().length() != 0
                        && alamat_bg.getText().length() != 0 && isi_gambar != false
                        && nama.getText().length() != 0 && alamat.getText().length() != 0
                        && no_hp.getText().length() != 0) {
                    next.setEnabled(false);
                    try {
                        System.out.println("nama b " + nama_bg.getText());
                        System.out.println("lantai:" + lantai.getText());
                        System.out.println("tahun " + tahun.getText());
                        System.out.println("alamat b " + alamat_bg.getText());
                        System.out.println("lati " + lati.getText());
                        System.out.println("longi " + longi.getText());
                        System.out.println("nama " + nama.getText());
                        System.out.println("alamat " + alamat.getText());
                        System.out.println("nomor " + no_hp.getText());
                        MainActivity.mSQLiteHelper.insertData(
                                nama_bg.getText().toString().trim(),
                                lantai.getText().toString().trim(),
                                tahun.getText().toString().trim(),
                                alamat_bg.getText().toString().trim(),
                                lati.getText().toString().trim(),
                                longi.getText().toString().trim(),
                                imageViewToByte(poto),
                                nama.getText().toString().trim(),
                                alamat.getText().toString().trim(),
                                no_hp.getText().toString().trim()
                        );
                        Toast.makeText(FormActivity.this, "Sukses", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(FormActivity.this, MainActivity.class);
                        startActivity(i);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(FormActivity.this, "Bertanda * harus diisi", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    private void SelectImage(){

        final CharSequence[] items={"Camera","Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(FormActivity.this);
        builder.setTitle("Add Image");

        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (items[i].equals("Camera")) {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if(intent.resolveActivity(getPackageManager()) != null){
                        File photoFile = null;
                        photoFile = createPhotoFile();
                        if(photoFile != null){
                            pathToFile = photoFile.getAbsolutePath();
                            Uri photoUri = FileProvider.getUriForFile(FormActivity.this, "com.thecodecity.cameraandroid.fileprovider", photoFile);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                            startActivityForResult(intent, CAMERA_REQUEST_CODE1);
                        }
                    }


                } else if (items[i].equals("Gallery")) {

                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    //startActivityForResult(intent.createChooser(intent, "Select File"), SELECT_FILE);
                    startActivityForResult(intent, CAMERA_REQUEST_CODE2);

                } else if (items[i].equals("Cancel")) {
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();

    }

    private  File createPhotoFile(){
        String name = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File storageDir = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = null;
        try{
            image = File.createTempFile(name,".jpg", storageDir);
        }
        catch (IOException e){
            Log.d("mylog","Excep : "+e.toString());
        }
        return image;
    }

    @Override
    public  void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode,data);

        if(requestCode == CAMERA_REQUEST_CODE1 && resultCode == RESULT_OK){
            CropImage.activity(Uri.fromFile(new File(pathToFile)))
                    .setAspectRatio(1,1)
                    .start(this);
        }
        else if(requestCode == CAMERA_REQUEST_CODE2 && resultCode == RESULT_OK){
            Uri imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setAspectRatio(1,1)
                    .start(this);
        }

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri imageUri = result.getUri(); //Mengubah data image kedalam Uri
                Bitmap  mBitmap = null;
                try {
                    mBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                    int a = mBitmap.getWidth();
                    int b = mBitmap.getHeight();
                    System.out.println("width = "+a+", height = "+b);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //Menampilkan Gambar pada ImageView
                Picasso.get().load(imageUri).into(poto);
                isi_gambar=true;

            }else if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                //Menangani Jika terjadi kesalahan
                String error = result.getError().toString();
                Log.d("Exception", error);
                Toast.makeText(getApplicationContext(), "Crop Image Error", Toast.LENGTH_SHORT).show();
            }
        }

    }


    private static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 500, 500, false);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        resizedBitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }
    @Override
    public void onBackPressed(){
        ViewDialog alert = new ViewDialog();
        alert.showDialog(FormActivity.this);

    }

    //latitude longitude
    @Override
    protected void onResume() {
        super.onResume();
        getLocation();
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    public void getLocation() {
        try{
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);
        } catch (SecurityException e){
            e.printStackTrace();
        }

    }

    public void checkPermision() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        double longi2 = location.getLongitude();
        double lati2 = location.getLatitude();
        lati.setText(""+lati2);
        longi.setText(""+longi2);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Enable new provider", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Please enable GPS and Internet", Toast.LENGTH_SHORT).show();
    }
    ///////////////////////////////////////

    public class ViewDialog {
        public void showDialog(Activity activity) {
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.popup);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            Button mDialogNo = dialog.findViewById(R.id.frmNo);
            mDialogNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(),"No" ,Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });

            Button mDialogOk = dialog.findViewById(R.id.frmOk);
            mDialogOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(),"Ok" ,Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    FormActivity.super.onBackPressed();
                }
            });

            dialog.show();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}
