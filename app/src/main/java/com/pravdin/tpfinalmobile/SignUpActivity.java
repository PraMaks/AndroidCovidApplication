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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.text.SimpleDateFormat;
import java.util.Calendar;
//import okhttp3.Request;
//import okhttp3.Response;

public class SignUpActivity extends AppCompatActivity {

    final String urlStart = "http://10.0.2.2:8080/ministere/";
    TextInputEditText numAssurSocTextField;
    TextInputEditText firstNameTextField;
    TextInputEditText lastNameTextField;
    TextInputEditText ageTextField;
    TextInputEditText emailTextField;
    TextInputEditText passwordTextField;

    boolean result;

    ImageView imageViewLogo;
    TextView textViewSalutation;
    TextView textViewAccount;

    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        imageViewLogo = findViewById(R.id.imageViewLogoSignUp);
        textViewSalutation = findViewById(R.id.textViewWelcomeFromSignUp);
        textViewAccount = findViewById(R.id.textViewSignUp);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        numAssurSocTextField = findViewById(R.id.TextFieldNumAssurSoc);
        firstNameTextField = findViewById(R.id.TextFieldFirstName);
        lastNameTextField = findViewById(R.id.TextFieldLastName);
        ageTextField = findViewById(R.id.TextFieldAge);
        emailTextField = findViewById(R.id.TextFieldEmail);
        passwordTextField = findViewById(R.id.TextFieldPassword);

        queue = Volley.newRequestQueue(this);
    }

    public void goToLogin(View view){
        Intent intent = new Intent(this, LoginActivity.class);

        Pair[] pairs = new Pair[3];
        pairs[0] = new Pair<View, String>(imageViewLogo, "imageView_logo");
        pairs[1] = new Pair<View, String>(textViewSalutation, "textView_title");
        pairs[2] = new Pair<View, String>(textViewAccount, "textView_account");

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignUpActivity.this, pairs);
        startActivity(intent, options.toBundle());
    }

    public void goToProfile(){
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("Signup", "creation");
        startActivity(intent);
    }

    public void createAccount(View view){
        //10.0.2.2:8080/ministere/12340987
        String numAssurSocText = numAssurSocTextField.getText().toString();
        String firstNameText = firstNameTextField.getText().toString();
        String lastNameText = lastNameTextField.getText().toString();
        String passwordText = passwordTextField.getText().toString();
        String emailText = emailTextField.getText().toString();

        String resultResponse;
        //Log.i("URL:", url);
        //boolean result = getCitizenInfoVerification(url);

        if(!(numAssurSocText.isEmpty() || firstNameText.isEmpty() || lastNameText.isEmpty() || emailText.isEmpty() || passwordText.isEmpty() || ageTextField.getText().toString().isEmpty())){
            int ageText = Integer.parseInt(ageTextField.getText().toString());
            String url = urlStart + ageText + "/" + numAssurSocText + "/" + lastNameText + "+" + firstNameText;
            StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    result = Boolean.parseBoolean(response);
                    //Log.i("Resultat connection In", String.valueOf(result));

                    ParseUser parseUser = new ParseUser();
                    parseUser.setUsername(numAssurSocText);
                    parseUser.setPassword(passwordText);
                    parseUser.put("firstName", firstNameText);
                    parseUser.put("lastName", lastNameText);
                    parseUser.put("age", ageText);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
                    Calendar c = Calendar.getInstance();
                    parseUser.put("dateCreated", c.getTime());
                    parseUser.setEmail(emailText);

                    parseUser.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e == null){
                                Log.i("Signup", "Success");
                                goToProfile();
                            } else{
                                Toast.makeText(SignUpActivity.this, e.getMessage().substring(e.getMessage().indexOf(" ")), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("error",error.toString());
                }
            });
            queue.add(request);
        }
        else{
            Toast.makeText(this, "S.V.P veuillez remplir tous les champs!", Toast.LENGTH_SHORT).show();
        }


    }

}