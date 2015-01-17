package m2dl.com.naturalstore.mail;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Environment;
import android.preference.PreferenceManager;

public class MultiThread extends AsyncTask {

    Context context;

    public MultiThread(Context context){
        this.context=context;
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
            //sender.addAttachment(Environment.getExternalStorageDirectory().getPath()+"/image.jpg");
            sender.sendMail(email,email,email,"jordan.blancpoujol@gmail.com");
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}
