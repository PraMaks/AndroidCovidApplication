package com.pravdin.tpfinalmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {

    ImageView imageViewLogo;
    TextView textViewSalutation;
    TextView textViewAccount;

    TextInputEditText numAssurSocTextField;
    TextInputEditText passwordTextField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        imageViewLogo = findViewById(R.id.imageViewLogo);
        textViewSalutation = findViewById(R.id.textViewWelcome);
        textViewAccount = findViewById(R.id.textViewSignIn);

        numAssurSocTextField = findViewById(R.id.TextFieldNummAssurSocLogin);
        passwordTextField = findViewById(R.id.TextFieldPasswordLogin);
    }

    public void loginToAccount(View view){
        String numAssurSocText = numAssurSocTextField.getText().toString();
        String passwordText = passwordTextField.getText().toString();

        if(!(numAssurSocText.isEmpty() || passwordText.isEmpty())){
            ParseUser.logInInBackground(numAssurSocText, passwordText, new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if(e == null){
                        Log.i("login", "Success");
                        goToProfile();
                    }
                    else{
                        Toast.makeText(LoginActivity.this, "Erreur de connection, veuillez reesayez!", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        else{
            Toast.makeText(this, "S.V.P veuillez remplir tous les champs!", Toast.LENGTH_SHORT).show();
        }
    }

    public void goToSignUp(View view){
        Intent intent = new Intent(this, SignUpActivity.class);

        Pair[] pairs = new Pair[3];
        pairs[0] = new Pair<View, String>(imageViewLogo, "imageView_logo");
        pairs[1] = new Pair<View, String>(textViewSalutation, "textView_title");
        pairs[2] = new Pair<View, String>(textViewAccount, "textView_account");

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this, pairs);
        startActivity(intent, options.toBundle());
    }

    public void goToProfile(){
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Impossible de revenir à l'écran précédent!", Toast.LENGTH_SHORT).show();
    }
}