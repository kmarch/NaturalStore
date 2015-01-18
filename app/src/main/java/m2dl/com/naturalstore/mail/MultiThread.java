package m2dl.com.naturalstore.mail;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Environment;
import android.preference.PreferenceManager;

import java.io.File;

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
            SharedPreferences mySharedPreferences = context.getSharedPreferences("myData", 0);
            String email = mySharedPreferences.getString("email", "");

            GMailSender sender = new GMailSender("naturalstorem2dl@gmail.com", "fds158efssdfsd");
            sender.addAttachment(context.getFilesDir()+File.separator +"cle.xml");
            sender.addAttachment(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES).toString()+File.separator + "temp.png");
            sender.sendMail("Votre base de données naturalstore",gps+"\n"+comment,"naturalstorem2dl@gmail.com",email);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}
