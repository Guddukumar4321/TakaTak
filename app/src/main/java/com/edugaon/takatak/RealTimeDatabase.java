package com.edugaon.takatak;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.edugaon.EmployeeInfo;
import com.edugaon.GetDateActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RealTimeDatabase extends AppCompatActivity {

    private EditText employeeNameEdt, employeePhoneEdt, employeeEmail, employeePassword;
    private Button sendDatabtn;
    private CardView cardView;
    private  Button uploadImageButtonRealtime;
    private ImageView imageView;
    ProgressDialog progressDialog;
    Uri imageUri;

    StorageReference storageReference;
    //private DatabaseReference root = FirebaseDatabase.getInstance().getReference("image");
    //private StorageReference reference = FirebaseDatabase.getInstance().getReference();

    private Uri imageUrl;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 101;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    EmployeeInfo employeeInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_time_database);
        employeeNameEdt = findViewById(R.id.name);
        employeePhoneEdt = findViewById(R.id.phone_number);
        employeeEmail = findViewById(R.id.email);
        imageView = findViewById(R.id.profile_image);
        employeePassword = findViewById(R.id.password);
        cardView = findViewById(R.id.selectImageViewRealtime);
        uploadImageButtonRealtime = findViewById(R.id.clear_button);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("EmployeeInfo");
        employeeInfo = new EmployeeInfo();
        sendDatabtn = findViewById(R.id.summit_button);
        cardView.setOnClickListener(view -> selectImageView());

      //  uploadImageButtonRealtime.setOnClickListener(view -> uploadImage());


        // Initialize the imageView (Link the imageView with front-end component ImageView)
//        imageView = findViewById(R.id.my_avatar_imageview);
//        if(checkAndRequestPermissions(RealTimeDatabase.this)){
//            chooseImage(RealTimeDatabase.this);
//        }


//        cardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                chooseImage(RealTimeDatabase.this);
//            }
//        });


        sendDatabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = employeeNameEdt.getText().toString();
                String phone = employeePhoneEdt.getText().toString();
                String email = employeeEmail.getText().toString();
                String password = employeePassword.getText().toString();
               // String image = imageUrl.toString();
//                if(imageUrl != null){
//
//                    uploadeImage(imageUrl);
//                }else {
//                    Toast.makeText(RealTimeDatabase.this, "please take a image", Toast.LENGTH_SHORT).show();
//                }

                if (TextUtils.isEmpty(name) && TextUtils.isEmpty(phone) && TextUtils.isEmpty(email) && TextUtils.isEmpty(password)) {
                    Toast.makeText(RealTimeDatabase.this, "Please add some data.", Toast.LENGTH_SHORT).show();
                } else {
                    addDatatoFirebase(name, phone, email, password);
                    selectImageView();
                    Intent intent = new Intent(RealTimeDatabase.this, GetDateActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void addDatatoFirebase(String name, String phone, String email, String password ) {

        employeeInfo.setEmployeeName(name);
        employeeInfo.setEmployeeContactNumber(phone);
        employeeInfo.setEmployeeEmail(email);
        employeeInfo.setEmployeePassword(password);
      //  employeeInfo.setEmployeeImage(image);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                databaseReference.setValue(employeeInfo);

                Toast.makeText(RealTimeDatabase.this, "data added", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(RealTimeDatabase.this, "Fail to add data " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private  void selectImageView(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 100);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && data != null && data.getData() != null){
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
    }
//    private void chooseImage(Context context){
//        final CharSequence[] optionsMenu = {"Take Photo", "Choose from Gallery", "Exit" }; // create a menuOption Array
//        // create a dialog for showing the optionsMenu
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        // set the items in builder
//        builder.setItems(optionsMenu, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                if(optionsMenu[i].equals("Take Photo")){
//                    // Open the camera and get the photo
//                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                    startActivityForResult(takePicture, 0);
//                }
//                else if(optionsMenu[i].equals("Choose from Gallery")){
//                    // choose from  external storage
//                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                    startActivityForResult(pickPhoto , 1);
//                }
//                else if (optionsMenu[i].equals("Exit")) {
//                    dialogInterface.dismiss();
//                }
//            }
//        });
//        builder.show();
//    }
//    public static boolean checkAndRequestPermissions(final Activity context) {
//        int WExtstorePermission = ContextCompat.checkSelfPermission(context,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE);
//        int cameraPermission = ContextCompat.checkSelfPermission(context,
//                Manifest.permission.CAMERA);
//        List<String> listPermissionsNeeded = new ArrayList<>();
//        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
//            listPermissionsNeeded.add(Manifest.permission.CAMERA);
//        }
//        if (WExtstorePermission != PackageManager.PERMISSION_GRANTED) {
//            listPermissionsNeeded
//                    .add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
//        }
//        if (!listPermissionsNeeded.isEmpty()) {
//            ActivityCompat.requestPermissions(context, listPermissionsNeeded
//                            .toArray(new String[listPermissionsNeeded.size()]),
//                    REQUEST_ID_MULTIPLE_PERMISSIONS);
//            return false;
//        }
//        return true;
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode,String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode) {
//            case REQUEST_ID_MULTIPLE_PERMISSIONS:
//                if (ContextCompat.checkSelfPermission(RealTimeDatabase.this,
//                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//                    Toast.makeText(getApplicationContext(),
//                            "FlagUp Requires Access to Camara.", Toast.LENGTH_SHORT)
//                            .show();
//                } else if (ContextCompat.checkSelfPermission(RealTimeDatabase.this,
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                    Toast.makeText(getApplicationContext(),
//                            "FlagUp Requires Access to Your Storage.",
//                            Toast.LENGTH_SHORT).show();
//                } else {
//                    chooseImage(RealTimeDatabase.this);
//                }
//                break;
//        }
//    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode != RESULT_CANCELED) {
//            switch (requestCode) {
//                case 0:
//                    if (resultCode == RESULT_OK && data != null) {
//                        imageUrl = data.getData();
//                        imageView.setImageURI(imageUrl);
////                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
////                        imageView.setImageBitmap(selectedImage);
//                    }
//                    break;
//                case 1:
//                    if (resultCode == RESULT_OK && data != null) {
//                        imageUrl = data.getData();
//                        imageView.setImageURI(imageUrl);
////                        Uri selectedImage = data.getData();
////                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
////                        if (selectedImage != null) {
////                            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
////                            if (cursor != null) {
////                                cursor.moveToFirst();
////                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
////                                String picturePath = cursor.getString(columnIndex);
////                                imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
////                                cursor.close();
////                            }
////                        }
//                    }
//                    break;
//            }
//        }
//    }

    private  void uploadImage(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("uploading file...");
        progressDialog.show();
        SimpleDateFormat format = new SimpleDateFormat("yyyy_MM_dd_HH_ss", Locale.CANADA);
        Date now = new Date();
        String fileName = format.format(now);
        storageReference = FirebaseStorage.getInstance().getReference("images/"+fileName);
        storageReference.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    imageView.setImageURI(null);
                    Toast.makeText(RealTimeDatabase.this, "Successfully upload", Toast.LENGTH_SHORT).show();
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                }).addOnFailureListener(e -> {
            if (progressDialog.isShowing())
                progressDialog.dismiss();
            Toast.makeText(RealTimeDatabase.this, "Fail upload ", Toast.LENGTH_SHORT).show();
        });
    }
}

