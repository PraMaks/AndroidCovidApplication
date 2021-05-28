package com.pravdin.tpfinalmobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.parse.ParseUser;
import com.pravdin.tpfinalmobile.models.Citizen;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class ProfileActivity extends AppCompatActivity {
    final int WIDTH = 250;

    TextView textViewAccountName;
    TextView textViewNumAssurSoc;
    TextView textViewDateCreated;
    TextInputEditText textInputEditTextEmailAccount;
    ImageView imageViewQrCode;

    Citizen account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        textViewAccountName = findViewById(R.id.textViewAccountName);
        textViewNumAssurSoc = findViewById(R.id.textViewNumAssurSoc);
        textViewDateCreated = findViewById(R.id.textViewDateCreated);
        imageViewQrCode = findViewById(R.id.imageViewQrCode);
        textInputEditTextEmailAccount = findViewById(R.id.TextFieldEmailAccount);

        String numAssurSoc = ParseUser.getCurrentUser().getUsername();
        String firstName = ParseUser.getCurrentUser().get("firstName").toString();
        String lastName = ParseUser.getCurrentUser().get("lastName").toString();
        String email = ParseUser.getCurrentUser().getEmail();
        int age = ParseUser.getCurrentUser().getInt("age");
        Date creationDate = ParseUser.getCurrentUser().getDate("dateCreated");

        account = new Citizen(numAssurSoc, firstName, lastName, age, email, creationDate);

        textViewNumAssurSoc.setText("Votre NAS: " + numAssurSoc);
        textViewAccountName.setText("Bonjour, bienvenue " + firstName + " " + lastName);
        textViewDateCreated.setText("Date de création du permis: " + account.getDayFromDate());
        textInputEditTextEmailAccount.setText(email);

        imageViewQrCode.setImageBitmap(createQrCode(account.toString()));
        Log.i("AccountInfo", account.toString());

        if(getIntent().getStringExtra("Signup").equals("creation")){
            Intent emailIntent = SendEmail(email);
            emailIntent.setType("message/rfc822");
            startActivity(Intent.createChooser(emailIntent, "Choose an Email client :"));
        }

    }

    @NotNull
    private Intent SendEmail(String email) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{ email});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "QrCode Permit Covid-19 Création");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Le compte et le permis ont été créé avec succès (l'image du code qr est enregistré dans la gallerie)");
        return emailIntent;
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Impossible de revenir à l'écran précédent", Toast.LENGTH_SHORT).show();
    }

    private Bitmap createQrCode(String accountInfo){
        BitMatrix matrix;

        try {
            matrix = new MultiFormatWriter().encode(accountInfo, BarcodeFormat.QR_CODE, WIDTH, WIDTH, null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        int width = matrix.getWidth();
        int height = matrix.getHeight();
        int[] pixels = new int[width * height];

        for (int y = 0; y < height; y++) {
            int offset = y * width;

            for (int x = 0; x < width; x++) {
                pixels[offset + x] = matrix.get(x, y) ? Color.BLACK : Color.WHITE;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, WIDTH, 0, 0, width, height);
        return bitmap;
    }

    public void logOut(View view){
        ParseUser.logOut();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void changeEmail(View view){
        String newEmail = textInputEditTextEmailAccount.getText().toString();
        if(newEmail.contains("@")){
            if(newEmail.equals(ParseUser.getCurrentUser().getEmail())){
                Toast.makeText(this, "Impossible de changer d'email, car c'est le même", Toast.LENGTH_SHORT).show();
            }
            else{
                ParseUser.getCurrentUser().setEmail(newEmail);
                Toast.makeText(this, "Email modifié!!!", Toast.LENGTH_SHORT).show();
                Log.i("Email",  ParseUser.getCurrentUser().getEmail());
                try{
                    ParseUser.getCurrentUser().save();
                    Intent intent = new Intent(this, ProfileActivity.class);
                    finish();
                    startActivity(intent);
                } catch(Exception e){
                    e.printStackTrace();
                }

            }
        }
        else{
            Toast.makeText(this, "Email is not valid", Toast.LENGTH_SHORT).show();
        }
    }

    public void saveToGallery(View view){
        imageViewQrCode.setDrawingCacheEnabled(true);
        Bitmap bitmap = imageViewQrCode.getDrawingCache();
        MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "qr_code_permit_covid" , "QR Code is valid");
    }

}