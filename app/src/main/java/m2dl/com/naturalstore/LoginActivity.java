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

/**
 * Formulaire d'inscription lors de la première utilisation de l'application, on gére les cas ou
 * le nom et l'email est vide ainsi que la bonne syntaxe de l'email
 */
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
                            getResources().getString(R.string.nameAndMailLogin),
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if (nameTxt.equals("")) {
                    Toast.makeText(LoginActivity.this,
                            getResources().getString(R.string.nameLogin),
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if (emailTxt.equals("")) {
                    Toast.makeText(LoginActivity.this,
                            getResources().getString(R.string.emailLogin),
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!isEmailValid(emailTxt)){
                    Toast.makeText(LoginActivity.this,
                            getResources().getString(R.string.emailNonValide),
                            Toast.LENGTH_SHORT).show();
                    return;

                }


                 //Stocke les données dans le fichier de l'application pour qu'elles soient conservées
                SharedPreferences mPrefs = getSharedPreferences(getResources().getString(R.string.persisterDonnee), 0);

                SharedPreferences.Editor mEditor = mPrefs.edit();
                mEditor.putString(getResources().getString(R.string.persisterDonneeName), nameTxt);
                mEditor.putString(getResources().getString(R.string.persisterDonneeEmail), emailTxt);
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
