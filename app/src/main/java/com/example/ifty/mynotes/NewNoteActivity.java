package com.example.ifty.mynotes;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.method.KeyListener;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ifty.mynotes.datadases.NotesDatabaseManager;
import com.example.ifty.mynotes.models.Note;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class NewNoteActivity extends AppCompatActivity{
    private static final int REQUEST_CAMERA = 1;
    private static final int IMAGE_CHOOSE_CODE = 2;
    Toolbar toolbar;
    NotesDatabaseManager databaseManager;
    EditText titleEt, noteEt;
    ImageView imageVw;
    LinearLayout touchEnabled;
    int id;
    String photoPath=null;
    BottomSheetBehavior bottomSheetBehavior;
    LinearLayout bottomSheetLinearLayout, deleteNoteLL, makeACopyLL, sendNoteLL, takePhotoLL, chooseImageLL;


    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);

        databaseManager = new NotesDatabaseManager(this);
        titleEt=findViewById(R.id.titleEt);
        touchEnabled=findViewById(R.id.touchEnable);
        noteEt=findViewById(R.id.noteEt);



        String newOrView = getIntent().getStringExtra("newOrViewNote");


        imageVw=findViewById(R.id.imageVw);

        deleteNoteLL = findViewById(R.id.deleteNote);
        makeACopyLL = findViewById(R.id.makeACopyLL);
        sendNoteLL = findViewById(R.id.sendNoteLL);
        takePhotoLL = findViewById(R.id.takePhotoLL);
        chooseImageLL = findViewById(R.id.chooseImageLL);
        toolbar=findViewById(R.id.app_bar);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        if (newOrView.equals("new note")){
            titleEt.requestFocus();
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            SpannableString DateTime = currentDateTime();
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_black_24dp);
            getSupportActionBar().setTitle(DateTime);
        }
        else if (newOrView.equals("note view")){
            touchEnabled.setFocusable(true);
            touchEnabled.setFocusableInTouchMode(true);
            Note note = databaseManager.singleNote(getIntent().getIntExtra("id",0));
            titleEt.setText(note.getTitle());
            noteEt.setText(note.getNote());
            deleteNoteLL.setVisibility(View.VISIBLE);
            id=note.getId();
            photoPath=note.getPhotoPath();
            if (photoPath!=null){
                imageVw.setVisibility(View.VISIBLE);
                Bitmap bmp = BitmapFactory.decodeFile(photoPath );
                imageVw.setImageBitmap(bmp);
            }

            SpannableString DateTime = lastEditedDateTime("last edited  "+note.getDateNtime());
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_black_24dp);
            getSupportActionBar().setTitle(DateTime);
        }



        titleEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    closeMoreBottomSheetAndKeyboard();
                    closePlusBottomSheetAndKeyboard();
                }
            }
        });

        noteEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    closeMoreBottomSheetAndKeyboard();
                    closePlusBottomSheetAndKeyboard();
                }
            }
        });
        imageVw.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    closeMoreBottomSheetAndKeyboard();
                    closePlusBottomSheetAndKeyboard();
                }
            }
        });
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private SpannableString currentDateTime() {
        DateFormat dateFormat2 = new SimpleDateFormat("dd/MM/yyyy hh.mm aa");
        SpannableString ss1=  new SpannableString(dateFormat2.format(new Date()).toString());
        ss1.setSpan(new RelativeSizeSpan(.8f), 0,10, 0); // set size
        ss1.setSpan(new ForegroundColorSpan(Color.GRAY), 0, 10, 0);
        ss1.setSpan(new RelativeSizeSpan(.7f), 11,19, 0); // set size
        ss1.setSpan(new ForegroundColorSpan(Color.BLACK), 11, 19, 0);
        return ss1;
    }

    private SpannableString lastEditedDateTime(String dateNtime) {
        SpannableString ss1=  new SpannableString(dateNtime);
        ss1.setSpan(new RelativeSizeSpan(.6f), 0,13, 0); // set size
        ss1.setSpan(new ForegroundColorSpan(Color.LTGRAY), 0, 13, 0);
        ss1.setSpan(new RelativeSizeSpan(.8f), 13,24, 0); // set size
        ss1.setSpan(new ForegroundColorSpan(Color.GRAY), 13, 24, 0);
        ss1.setSpan(new RelativeSizeSpan(.7f), 24,32, 0); // set size
        ss1.setSpan(new ForegroundColorSpan(Color.DKGRAY), 24, 32, 0);
        return ss1;
    }

    public void saveNupdateNote(View view){
        if (id>0){
            Note note =new Note(id,titleEt.getText().toString(),noteEt.getText().toString(),currentDateTime().toString(),photoPath);
            long isUpdated = databaseManager.updateNote(note);
            if (isUpdated>0){
                Intent intent = new Intent(this,MainActivity.class);
                finish();
                startActivity(intent);
            }
            else {
                Toast.makeText(this, "Faild to update Note.", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Note note =new Note(titleEt.getText().toString(),noteEt.getText().toString(),currentDateTime().toString(),photoPath);
            long isInserted = databaseManager.addNote(note);
            if (isInserted>0){
                Intent intent = new Intent(this,MainActivity.class);
                finish();
                startActivity(intent);
            }
            else {
                Toast.makeText(this, "Faild to insert Note.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void plusHorizontalView(View view) {
        closeMoreBottomSheetAndKeyboard();
        bottomSheetLinearLayout = findViewById(R.id.bottom_sheet_plus_layout);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLinearLayout);

        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED || bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

    public void moreHorizontalView(View view) {
        closePlusBottomSheetAndKeyboard();
        bottomSheetLinearLayout = findViewById(R.id.bottom_sheet_more_layout);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLinearLayout);

        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED || bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

    private void closeMoreBottomSheetAndKeyboard() {
        bottomSheetLinearLayout = findViewById(R.id.bottom_sheet_more_layout);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLinearLayout);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

    }

    private void closePlusBottomSheetAndKeyboard() {
        bottomSheetLinearLayout = findViewById(R.id.bottom_sheet_plus_layout);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLinearLayout);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    public void takePhoto(View view){
        ActivityCompat.requestPermissions(NewNoteActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
    }

    public void chooseImage(View view) {
        ActivityCompat.requestPermissions(NewNoteActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, IMAGE_CHOOSE_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == IMAGE_CHOOSE_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, IMAGE_CHOOSE_CODE);
            } else {
                Toast.makeText(this, "need permission to access.", Toast.LENGTH_SHORT).show();
            }
        }
        else if (requestCode==REQUEST_CAMERA){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent,REQUEST_CAMERA);
            }
            else {
                Toast.makeText(this, "need permission to access.", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==Activity.RESULT_OK){
            if (requestCode==REQUEST_CAMERA){
                Bundle bundle = data.getExtras();
                final Bitmap bitmap = (Bitmap) bundle.get("data");
               if (bitmap != null){
                photoPath = saveToInternalStorage(bitmap);
                imageVw.setVisibility(View.VISIBLE);
                imageVw.setImageBitmap(bitmap);
                closeMoreBottomSheetAndKeyboard();
                closePlusBottomSheetAndKeyboard();
               }
            }
            else if (requestCode==IMAGE_CHOOSE_CODE){
                Uri selectedImageUri= data.getData();
                try {
                    if (selectedImageUri != null) {
                        InputStream inputStream = getContentResolver().openInputStream(selectedImageUri); //read resource from uri
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        Glide.with(this)
                                .asBitmap()
                                .load(bitmap)
                                .into(imageVw);

                        photoPath = saveToInternalStorage(bitmap);
                        imageVw.setVisibility(View.VISIBLE);
                        imageVw.setImageBitmap(bitmap);
                        closeMoreBottomSheetAndKeyboard();
                        closePlusBottomSheetAndKeyboard();
                    }
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private String saveToInternalStorage(Bitmap bitmap) {
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());

        if (photoPath!=null){
            File fdelete = new File(photoPath);
            if (fdelete.exists()) {
                if (fdelete.delete()) {
                    Toast.makeText(contextWrapper, "file deleted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(contextWrapper, "file not deleted", Toast.LENGTH_SHORT).show();
                }
            }
        }
        File directory = contextWrapper.getDir("sqlite_image", Context.MODE_PRIVATE); //create or get root directory with the desire name

        //imageName = getImageName();
        File filePath = new File(directory, getImageName()); //file object create a file with directory and file name and return file path instance
        String imgUrl = filePath.toString(); //get the file path with string value



        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(imgUrl); //write the file using the file path with our root directory location
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream); //compress and write the the bitmap with the output stream location
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.flush(); // ensure that all the bytes are written
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return imgUrl;
    }
    private String getImageName() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH);
        String imageTimeStrap = simpleDateFormat.format(new Date());
        return "sqlite_image_"+imageTimeStrap+".jpg";
    }

    public void deleteNote(View view) {
        final AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Confirm");
        builder.setMessage("This will permanently delete this Note from record.");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                long isDeleted=databaseManager.deleteNote(id);
                if(isDeleted>0){
                    Toast.makeText(NewNoteActivity.this, "Note deleted successfully", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(NewNoteActivity.this,MainActivity.class);
                    finish();
                    startActivity(intent);

                }
                dialogInterface.cancel();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();

    }

    public void makeCopy(View view) {
        if (!titleEt.getText().toString().equals("") || !noteEt.getText().toString().equals("")){
            Note note =new Note(titleEt.getText().toString(),noteEt.getText().toString(),currentDateTime().toString(),photoPath);
            long isInserted = databaseManager.addNote(note);
            if (isInserted>0){
                Toast.makeText(this, "maked a copy successfully!", Toast.LENGTH_LONG).show();
                closeMoreBottomSheetAndKeyboard();
                closePlusBottomSheetAndKeyboard();
            }
            else {
                Toast.makeText(this, "Faild to make a copy of this Note.", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(this, "Empty note can't be copied.", Toast.LENGTH_SHORT).show();
        }
    }

    public void shareNote(View view) {
        if (!titleEt.getText().toString().equals("") || !noteEt.getText().toString().equals("")){
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT, titleEt.getText().toString());
            intent.putExtra(Intent.EXTRA_TEXT, noteEt.getText().toString());
            startActivity(Intent.createChooser(intent, "Send note"));
        }
        else {
            Toast.makeText(this, "Empty note can't be shared.", Toast.LENGTH_SHORT).show();
        }
    }
}
