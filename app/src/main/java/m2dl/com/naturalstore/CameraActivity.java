package m2dl.com.naturalstore;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.FloatMath;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;

import javax.microedition.khronos.opengles.GL10;


public class CameraActivity extends ActionBarActivity {
    private static final int CAMERA_REQUEST = 1888;
    private ImageView imageView;
    private File path;


    //Variable sur le point d'intérêt
    private Float xcenter = null;
    private Float ycenter = null;
    private Float radius = null;
    private Bitmap bmporiginal = null;
    float ratio;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        this.imageView = (ImageView)this.findViewById(R.id.imageView1);

        File newfolder = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        path = new File(newfolder, File.separator + "temp.png");

        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(path));
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {

            Bitmap bitmap = this.getCapturedPicture();
            imageView.setImageBitmap(bitmap);
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            float width = size.x;
            float height = size.y;
            System.out.println("openGL = " + GL10.GL_MAX_TEXTURE_SIZE);

            ratio = Math.min((width / (float) bitmap.getWidth()), (height / (float) bitmap.getHeight()));
            imageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth()*ratio),(int) (bitmap.getHeight()*ratio) ,false));

            //Listener pour la sélection du point d'intérêt
            imageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    float x = event.getX();
                    float y = event.getY();

                    System.out.println("x = "+x+" y = "+y);
                    if(xcenter == null) {
                        xcenter = x;
                        ycenter = y;

                        bmporiginal = getCapturedPicture();
                        bmporiginal = Bitmap.createScaledBitmap(bmporiginal, (int) (bmporiginal.getWidth()*ratio),(int) (bmporiginal.getHeight()*ratio) ,false);
                        imageView.setImageBitmap(bmporiginal);
                    }

                    radius = FloatMath.sqrt((x - xcenter) * (x - xcenter) + (y - ycenter) * (y - ycenter));
                    System.out.println("radius = "+radius);

                    Bitmap bmp = bmporiginal.copy(Bitmap.Config.ARGB_8888, true);

                    Paint p = new Paint();
                    p.setStyle(Paint.Style.STROKE);
                    p.setStrokeWidth(5f);
                    p.setColor(Color.RED);
                    Canvas c = new Canvas(bmp);
                    c.drawCircle(xcenter, ycenter, radius, p);

                    ((ImageView) v).setImageBitmap(bmp);

                    return true;
                }
            });
        }
    }

    public Bitmap getCapturedPicture() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES).toString()+File.separator + "temp.png", options);
        return bitmap;
    }

    public void checkPicture() {
        Intent intent = new Intent(this, DataEntryActivity.class);
        startActivity(intent);
    }

    public void refreshInterestPoint() {
        xcenter = null;
        ycenter = null;
        imageView.setImageBitmap(Bitmap.createScaledBitmap(bmporiginal, (int) (bmporiginal.getWidth()*ratio),(int) (bmporiginal.getHeight()*ratio) ,false));
    }

    public void uncheckPicture() {
        System.exit(RESULT_OK);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_camera, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_check) {
            this.checkPicture();
            return true;
        } else if (id == R.id.action_uncheck) {
            this.uncheckPicture();
            return true;
        } else if (id == R.id.action_refresh) {
            this.refreshInterestPoint();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
