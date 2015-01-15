package m2dl.com.naturalstore.mail;

import android.os.AsyncTask;

/**
 * Created by Jordan blanc on 15/01/2015.
 */
public class MultiThread extends AsyncTask {

    @Override
    protected Object doInBackground(Object...lol) {
        sendEmail();
        return null;
    }

    public void sendEmail() {
        try {

            GMailSender sender = new GMailSender(

                    "naturalstorem2dl@gmail.com",
                    "fds158efssdfsd");


            sender.sendMail("", "",

                    "jordan.blancpoujol@gmail.com",

                    "jordan.blancpoujol@gmail.com");




        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}
