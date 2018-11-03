package cm.siplus2018.bocom;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cm.siplus2018.bocom.utils.Util;

public class Login extends AppCompatActivity  implements View.OnClickListener {

    private EditText edit_username, edit_password;
    private Button loginButton;
    private CheckBox show_hide_password;
    private TextView forgotPassword;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edit_username = findViewById(R.id.edit_username);
        edit_password =  findViewById(R.id.edit_password);
        loginButton = findViewById(R.id.loginBtn);
        forgotPassword = findViewById(R.id.forgot_password);
        show_hide_password = (CheckBox) findViewById(R.id.show_hide_password);

        loginButton.setOnClickListener(this);
        forgotPassword.setOnClickListener(this);

        show_hide_password.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton button, boolean isChecked) {
                // If it is checkec then show password else hide
                // password
                if (isChecked) {

                    show_hide_password.setText(R.string.hide_pwd);// change
                    // checkbox
                    // text
                    edit_password.setInputType(InputType.TYPE_CLASS_TEXT);
                    edit_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());// show password
                } else {
                    show_hide_password.setText(R.string.show_pwd);// change

                    edit_password.setInputType(InputType.TYPE_CLASS_TEXT
                            | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    edit_password.setTransformationMethod(PasswordTransformationMethod
                            .getInstance());// hide password
                }

            }
        });

        edit_username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    String getEmailId = ((EditText)v).getText().toString().trim();
                    // Check patter for email id
                    if (getEmailId.length() > 0){
                        Pattern p = Pattern.compile(Util.REGEX_EMAIL);
                        Matcher m = p.matcher(getEmailId);
                        if (!m.matches()) {
                            Toast.makeText(getApplicationContext(), "E-Mail Invalide", Toast.LENGTH_LONG).show();
                        }else{

                        }
                    }

                }

            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginBtn:
                checkValidation();
                break;
            case R.id.forgot_password:
                // Replace forgot password fragment with animation
                break;
        }
    }

    private void checkValidation() {
        // Get email id and password
        String username = edit_username.getText().toString().trim();
        String password = edit_password.getText().toString();

        // Check patter for email id
        Pattern p = Pattern.compile(Util.REGEX_EMAIL);

        Matcher m = p.matcher(username);

        // Check for both field is empty or not
        if (username.equals("") || username.length() == 0 || password.equals("") || password.length() == 0) {
            //loginLayout.startAnimation(shakeAnimation);
            Toast.makeText(this, "Champ Obligatoire", Toast.LENGTH_LONG).show();
        }
        // Check if email id is valid or not
        else if (!m.matches()) {
            Toast.makeText(getApplicationContext(), "E-Mail Invalide", Toast.LENGTH_LONG).show();
        }

        // Else do login and do your stuff
        else {
            Toast.makeText(getApplicationContext(), "Do Login.", Toast.LENGTH_LONG).show();

            ProgressBar progressBar = findViewById(R.id.login_progrees_bar);
            progressBar.setVisibility(View.GONE);

            DoLogin doLogin = new DoLogin(this, progressBar, username, password);
            doLogin.execute(Util.BASE_URL + "makelogin", "email=" + username + "&password=" + password + "&token=something");

        }


    }

    private class DoLogin extends AsyncTask<String,Integer, String> {
        private ProgressBar dialog;
        private Context context;
        private int code;
        private String username, password;
        public DoLogin(Context context, ProgressBar dialog, String username, String password) {
            this.username = username;
            this.password = password;
            this.context = context;
            this.dialog = dialog;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.dialog.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            String resultat = "";
            String string_url = strings[0];
            String query = strings[1];
            URL url = null;
            try {
                url = new URL(string_url);
                HttpURLConnection urlConnection;
                try {
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setDoInput(true);
                    urlConnection.setDoOutput(true);
                    OutputStream os = urlConnection.getOutputStream();
                    OutputStreamWriter out = new OutputStreamWriter(os);
                    out.write(query);
                    out.close();
                    code = urlConnection.getResponseCode();
                    if(code == 200){
                        InputStream in = urlConnection.getInputStream();
                        BufferedReader br = null;
                        StringBuilder sb = new StringBuilder();
                        String line;
                        try {
                            br = new BufferedReader(new InputStreamReader(in));
                            while ((line = br.readLine()) != null) {
                                sb.append(line);
                            }

                        } catch (IOException e) {
                            return  "Failed";
                        } finally {
                            if (br != null) {
                                try {
                                    br.close();
                                } catch (IOException e) {
                                    return  "Failed";
                                }
                            }
                        }
                        in.close();
                        //os.close();
                        resultat = sb.toString();
                    }else{
                        return  "Failed";
                    }
                } catch (IOException e) {
                    return  "Failed";
                }
            } catch (MalformedURLException e) {
                return  "Failed";
            }

            return resultat;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute( result);
            if (dialog.getVisibility() == View.VISIBLE) {
                dialog.setVisibility(View.GONE);
            }

            Log.e("RESULTATO", result);

            if (!result.equals("Failed")){

                try {
                    JSONArray jsonArray = new JSONArray(result);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    int failed = jsonObject.getInt("failed");
                    int succeded = jsonObject.getInt("succeded");
                    int errorcode = jsonObject.getInt("errorcode");
                    String cause = jsonObject.getString("cause");

                    if (failed == 0 && succeded == 1 && errorcode == 0){
                        SharedPreferences.Editor editor = getSharedPreferences(Util.APP_AUTHENTICATION, MODE_PRIVATE).edit();
                        editor.putString(Util.USERNAME, this.username);
                        editor.putString(Util.PASSWORD, this.password);
                        editor.putLong(Util.CONNECTED_SINCE, System.currentTimeMillis());
                        editor.apply();
                        Intent intent = new Intent(getApplicationContext(), SaveStation.class);
                        startActivity(intent);
                        finish();
                    }else{
                        TextView resultLogin = (TextView) findViewById(R.id.resultLogin);
                        resultLogin.setText(cause);
                    }

                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                }

            }else {
                TextView resultLogin = (TextView) findViewById(R.id.resultLogin);
                resultLogin.setText("ERRO: code : " + code);
            }
        }
    }
}
