package com.example.iderm;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;

public class Res2 extends AppCompatActivity {
    private TextView textViewDiseases;
    String date,time,disease=null;
    private Uri uri1;
    private Bitmap bitmap;
    FirebaseAuth mAuth;
    Button btnbrowse, btnupload, imageCamera;
    ImageView image_view;
    Uri uri;
    public String agent,agentId;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    DatabaseReference reference;
    int Image_Request_Code = 7;
    ProgressBar progressDialog;
    TextView textViewUploading;
    EditText editTextTextName, editTextDocType, editTextDocNo, editTextAddress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_res2);
        textViewDiseases=findViewById(R.id.Detectdiseasesuccess);
        ImageView img2 = findViewById(R.id.img);
        // Assign FirebaseStorage instance to storageReference.
        storageReference = FirebaseStorage.getInstance().getReference();

        // Assign FirebaseDatabase instance with root database name.
        databaseReference = FirebaseDatabase.getInstance().getReference();
        reference= FirebaseDatabase.getInstance().getReference("users");
        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressBar(this);
        image_view = findViewById(R.id.img);
        editTextTextName = findViewById(R.id.editTextFullName);
        progressDialog = findViewById(R.id.progressBar);
        editTextDocType = findViewById(R.id.editTextDocType);
        editTextDocNo = findViewById(R.id.editTextDocNo);
        btnupload = findViewById(R.id.btnUpload);
        textViewUploading = findViewById(R.id.textViewUploading);
        textViewDiseases=findViewById(R.id.Detectdiseasesuccess);
        String imagePath = getIntent().getStringExtra("imageUri");
        uri1 =Uri.parse(imagePath);

        ImageView img = findViewById(R.id.img);
        Bundle extras = getIntent().getExtras();
        Uri uri = Uri.parse(extras.getString("imageUri"));
        image_view.setImageURI(uri);
        displayResult(uri);

        btnupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                UploadImage();

            }
        });
    }


    private void displayResult(Uri image) {
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), image);

        } catch (IOException e) {
            e.printStackTrace(); }
        bitmap=getResizedBitmap(bitmap,224);
        try {
            String sample = new ImageClassifier(Res2.this).classifyFrame(bitmap);
            textViewDiseases.setText(sample);
            //Toast.makeText(DetectActivity.this,sample,Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }



    }
    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }


    public void UploadImage() {
        String ownerName = textViewDiseases.getText().toString().trim();
        String docType = editTextDocType.getText().toString().trim();
        String number = editTextDocNo.getText().toString().trim();
        String agentId= mAuth.getCurrentUser().getUid();

        Calendar calendar = Calendar.getInstance();
        String timePosted = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        reference.child(agentId).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                agent=String.valueOf(dataSnapshot.child("Name").getValue());

            }
        });

        if (uri1 != null) {

            StorageReference fileReference = storageReference.child(System.currentTimeMillis() + "." + GetFileExtension(uri1));
            fileReference.putFile(uri1)

                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            progressDialog.setProgress(0);
                                        }
                                    }, 500);
                                    progressDialog.setVisibility(View.GONE);
                                    textViewUploading.setText("");
                                    Toast.makeText(getApplicationContext(), "Document Uploaded Successfully ", Toast.LENGTH_LONG).show();
                                    uploadinfo imageUploadInfo = new uploadinfo(uri.toString(), ownerName, timePosted,agent);
                                    String  UploadId = databaseReference.push().getKey();
                                    String postsId=databaseReference.push().getKey();
                                    databaseReference.child("Classifications").child(agentId).child("uploads").child(UploadId).setValue(imageUploadInfo);
                                    databaseReference.child("posts").child(postsId).setValue(imageUploadInfo);
                                }
                            });


                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    progressDialog.setVisibility(View.VISIBLE);
                    double progress = (100 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                    progressDialog.setProgress((int) progress);
                    textViewUploading.setText("Uploading Please wait...");


                }
            });
        } else {

            Toast.makeText(this, "Please Select Image or Add Image Name", Toast.LENGTH_LONG).show();

        }
    }
}