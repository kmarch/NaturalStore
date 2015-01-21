package m2dl.com.naturalstore.mail;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Environment;
import android.preference.PreferenceManager;

import java.io.File;

import m2dl.com.naturalstore.R;

/**
 * Creation de thread pour gérer l'envoi de mail, on recupere le context de l'application ceci pour
 * récuperer les données de l'activité DataEntryActivity ainsi que l'emplacement de la photo et du xml
 */
public class MultiThread extends AsyncTask {

    Context context;
    String gps;
    String comment;

    public MultiThread(Context context,String gps,String comment){

        this.context=context;
        this.gps=gps;
        this.comment=comment;
    }

    @Override
    protected Object doInBackground(Object...lol) {
        sendEmail();
        return null;

    }

    public void sendEmail() {
        try {
            SharedPreferences mySharedPreferences = context.getSharedPreferences(context.getResources().getString(R.string.persisterDonnee), 0);
            String email = mySharedPreferences.getString(context.getResources().getString(R.string.persisterDonneeEmail), "");

            GMailSender sender = new GMailSender(context,context.getResources().getString(R.string.adresse_mail),
                    context.getResources().getString(R.string.mot_de_passe));
                    sender.addAttachment(context.getFilesDir() + File.separator + context.getResources().getString(R.string.xmlFileName));
            sender.addAttachment(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES).toString()+File.separator + context.getResources().getString(R.string.photoFileName));
            sender.sendMail(context.getResources().getString(R.string.enTeteMail),gps+"\n"+comment,context.getResources().getString(R.string.adresse_mail),email);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}
