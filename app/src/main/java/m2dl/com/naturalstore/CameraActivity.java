package m2dl.com.naturalstore;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.FloatMath;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

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
    private Bitmap bmpinterest = null;
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
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            float width = size.x;
            float height = size.y;
            //System.out.println("openGL = " + GL10.GL_MAX_TEXTURE_SIZE);

            ratio = Math.min((width / (float) bitmap.getWidth()), (height / (float) bitmap.getHeight()));

            bmporiginal = getCapturedPicture();
            bmporiginal = Bitmap.createScaledBitmap(bmporiginal, (int) (bmporiginal.getWidth()*ratio),(int) (bmporiginal.getHeight()*ratio) ,false);
            imageView.setImageBitmap(bmporiginal);

            //Listener pour la sélection du point d'intérêt
            imageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    float x = event.getX();
                    float y = event.getY();

                    if(xcenter == null) {
                        xcenter = x;
                        ycenter = y;
                    }

                    radius = FloatMath.sqrt((x - xcenter) * (x - xcenter) + (y - ycenter) * (y - ycenter));

                    bmpinterest = bmporiginal.copy(Bitmap.Config.ARGB_8888, true);

                    Paint p = new Paint();
                    p.setStyle(Paint.Style.STROKE);
                    p.setStrokeWidth(5f);
                    p.setColor(Color.RED);
                    Canvas c = new Canvas(bmpinterest);
                    c.drawCircle(xcenter, ycenter, radius, p);

                    ((ImageView) v).setImageBitmap(bmpinterest);

                    return true;
                }
            });
        } else {
            uncheckPicture();
        }
    }

    public Bitmap getCapturedPicture() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES).toString()+File.separator + "temp.png", options);
        return bitmap;
    }

    public Location getLocation() {
        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                //makeUseOfNewLocation(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}

            @Override
            public void onProviderEnabled(String provider) {}

            @Override
            public void onProviderDisabled(String provider) {}
        };

        // Register the listener with the Location Manager to receive location updates
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

        return locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
    }

    public void checkPicture() {
        //Location
        //LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        //Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        Location location = this.getLocation();
        System.out.println("My location please " + location);

        this.storePicture(this.bmpinterest);
        Intent intent = new Intent(this, DataEntryActivity.class);
        if(location != null) {
            intent.putExtra(getResources().getString(R.string.picture_location_latitude), location.getLatitude());
            intent.putExtra(getResources().getString(R.string.picture_location_longitude), location.getLongitude());
        } else {
            intent.putExtra(getResources().getString(R.string.picture_location_latitude), -1.0);
            intent.putExtra(getResources().getString(R.string.picture_location_longitude), -1.0);
        }
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

    private void storePicture(Bitmap image) {
        File pictureFile = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES).toString()+File.separator + "temp.png");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 0, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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
