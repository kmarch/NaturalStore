package m2dl.com.naturalstore;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends ActionBarActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText name = (EditText) findViewById(R.id.user_name);
        final EditText email = (EditText) findViewById(R.id.user_email);
        final Button loginButton = (Button) findViewById(R.id.btnLogin);
        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final String nameTxt = name.getText().toString();
                final String emailTxt = email.getText().toString();

                if (nameTxt.equals("") && emailTxt.equals("")) {
                    Toast.makeText(LoginActivity.this,
                            "Merci de renseigner le nom et l'adresse email",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if (nameTxt.equals("")) {
                    Toast.makeText(LoginActivity.this,
                           "Merci de renseigner le nom",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if (emailTxt.equals("")) {
                    Toast.makeText(LoginActivity.this,
                            "Merci de renseigner l'adresse email",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!isEmailValid(emailTxt)){
                    Toast.makeText(LoginActivity.this,
                            "Email non valide", Toast.LENGTH_SHORT)
                            .show();
                    return;

                }

                SharedPreferences mPrefs = getSharedPreferences("myData", 0);

                SharedPreferences.Editor mEditor = mPrefs.edit();
                mEditor.putString("name", nameTxt);
                mEditor.putString("email", emailTxt);
                mEditor.commit();

                Intent intent = new Intent(LoginActivity.this,
                        HomeActivity.class);
                startActivity(intent);
            }
        });
    }

    boolean isEmailValid(CharSequence emailTxt) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(emailTxt).matches();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    
}
