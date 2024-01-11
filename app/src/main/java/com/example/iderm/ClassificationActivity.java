package com.example.iderm;


import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.tensorflow.lite.Interpreter;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.util.Calendar;

public class ClassificationActivity extends AppCompatActivity {
    private TextView textViewDiseases;
    private Uri uri1;
    private Bitmap bitmap;
    FirebaseAuth mAuth;
    Button btnbrowse, btnupload, imageCamera;
    ImageView image_view,   image_view2;;
    Uri uri;
    public String agent,Id;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    DatabaseReference reference;
    int Image_Request_Code = 7;
    ProgressBar progressDialog;
    TextView textViewUploading;
    EditText editTextTextName, editTextDocType, editTextDocNo, editTextAddress;
    public Bitmap Image;
    public static Interpreter tflite;
    private static String MODEL_PATH;//Used to store model path
    float output[][]=new float[1][1];//For storing result
    int prob;

    Handler handler1 = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case 0:
                    //Model is loaded make choose image button visible
                    Button button = findViewById(R.id.button);
                    button.setVisibility(View.VISIBLE);
                    break;
                case 1:
                    //Computation is over display the output
                    TextView tv = findViewById(R.id.textView);
                    TextView tv2 = findViewById(R.id.Detectdiseasesuccess);



                    tv.setText("Mel "+output[0][0]);

                    double tester = output[0][0];
                    tv2.setText("Melanoma probability is " +String.format("%.2f", tester*100)+"%");




            }


        }
    };

    //Function to load model
    private void load_model()
    {

        //Thread for loading model
        Runnable load_model = new Runnable()
        {

            @Override
            public void run()
            {
                try
                {
                    tflite = new Interpreter(loadModelFile(getAssets(), MODEL_PATH));
                }
                catch (Exception e) {}
                handler1.sendEmptyMessage(0);
            }
        };

        Thread model_loading_thread=new Thread(load_model);
        model_loading_thread.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classification);
        textViewDiseases=findViewById(R.id.Detectdiseasesuccess);
        ImageView img2 = findViewById(R.id.img);
        // Assign FirebaseStorage instance to storageReference.
        storageReference = FirebaseStorage.getInstance().getReference();
        // Assign FirebaseDatabase instance with root database name.
        databaseReference = FirebaseDatabase.getInstance().getReference();
        reference= FirebaseDatabase.getInstance().getReference("Clients");
        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressBar(this);
        image_view = findViewById(R.id.img);
        image_view2 = findViewById(R.id.img2);
        editTextTextName = findViewById(R.id.editTextFullName);
        progressDialog = findViewById(R.id.progressBar);
        editTextDocType = findViewById(R.id.editTextDocType);
        editTextDocNo = findViewById(R.id.editTextDocNo);
        btnupload = findViewById(R.id.btnUpload);
        textViewUploading = findViewById(R.id.textViewUploading);
        Button button = findViewById(R.id.button);
        button.setVisibility(View.GONE);
        tflite=null;
        MODEL_PATH = "my_model.tflite";
        Image=null;


        //Load model as soon as activity is created.
        load_model();
        btnupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                UploadImage();

            }
        });

    }

    public void onClick(View view) {
        //Activity for choosing image
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setCropShape(CropImageView.CropShape.OVAL)
                .setAspectRatio(1, 1)
                .setRequestedSize(224,224)
                .setFixAspectRatio(true)
                .start(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // handle result of CropImageActivity
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                try {
                    Image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), result.getUri());
                } catch (IOException e) {
                }
                image_view.setImageBitmap(Image);

                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                Image.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                String path = MediaStore.Images.Media.insertImage(getApplication().getContentResolver(), Image, "Title", null);
                Uri uri= Uri.parse(path);
                image_view2.setImageURI(uri);
                String paths = MediaStore.Images.Media.insertImage(getApplication().getContentResolver(), Image, "Title", null);
                uri1 =Uri.parse(paths);


                //Running Thread for doing prediction as it is heavy operation
                Runnable predict_r = new Runnable() {
                    @Override
                    public void run() {
                        ByteBuffer byteBuffer = convertBitmapToByteBuffer(Image);
                        ClassificationActivity.tflite.run(byteBuffer, output);
                        handler1.sendEmptyMessage(1);
                    }
                };
                Thread predict = new Thread(predict_r);
                predict.start();

            }
        }
    }

    //Returns the required path for loading model.
    private MappedByteBuffer loadModelFile(AssetManager assetManager, String modelPath) throws IOException
    {
        AssetFileDescriptor fileDescriptor = assetManager.openFd(modelPath);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }


    //Convert bitmap to bytebuffer
    private ByteBuffer convertBitmapToByteBuffer(Bitmap bitmap)
    {

        //Initialize space for byte buffer FLOAT_SIZE*BATCH_SIZE*INPUT_SHAPE*INPUT_SHAPE*NUMBER_OF_CHANNELS
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4*1*224*224*3);

        byteBuffer.order(ByteOrder.nativeOrder());

        //Array for holding pixels values. Pixel values are stored in packed integer.
        int[] intValues = new int[224*224];

        bitmap.getPixels(intValues, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());

        int pixel = 0;
        float divi = 127.5f;

        for (int i = 0; i < 224; ++i)
        {
            for (int j = 0; j < 224; ++j)
            {
             
                final int val = intValues[pixel++];

                //Decoding pixel value,preprocessing and putting into bytebuffer.

                byteBuffer.putFloat((((val >> 16) & 0xFF))/divi-1);
                byteBuffer.putFloat((((val >> 8) & 0xFF))/divi-1);
                byteBuffer.putFloat((((val) & 0xFF))/divi-1);
            }
        }
        return byteBuffer;
    }
    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }
    public void UploadImage() {
        String ownerNames = textViewDiseases.getText().toString().trim();
        String Id= mAuth.getCurrentUser().getUid();
        String docType = editTextDocType.getText().toString().trim();
        String number = editTextDocNo.getText().toString().trim();
        String agentId= mAuth.getCurrentUser().getUid();
        Calendar calendar = Calendar.getInstance();
        String timePosted = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        reference.child(Id).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
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
                                    uploadinfo imageUploadInfo = new uploadinfo(uri.toString(), ownerNames, timePosted,agent);
                                    String  UploadId = databaseReference.push().getKey();
                                    databaseReference.child("posts").child(UploadId).setValue(imageUploadInfo);
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