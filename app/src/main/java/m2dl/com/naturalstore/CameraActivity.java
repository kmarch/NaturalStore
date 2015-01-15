package m2dl.com.naturalstore;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.FloatMath;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;


public class CameraActivity extends ActionBarActivity {
    private static final int CAMERA_REQUEST = 1888;
    private ImageView imageView;
    private Uri imageUri;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private File path;

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

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES).toString()+File.separator + "temp.png", options);
            imageView.setImageBitmap(bitmap);

            //set listener
            imageView.setOnTouchListener(new View.OnTouchListener() {
                private Float radius = null;
                private Float xcenter = null;
                private Float ycenter = null;
                private Bitmap bmporiginal;

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    float x = event.getX();
                    float y = event.getY();

                    System.out.println("x = "+x+" y = "+y);
                    if(xcenter == null) {
                        xcenter = x;
                        ycenter = y;

                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                        bmporiginal = BitmapFactory.decodeFile(Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_PICTURES).toString()+File.separator + "temp.png", options);
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
