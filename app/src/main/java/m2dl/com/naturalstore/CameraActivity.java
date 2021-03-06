package m2dl.com.naturalstore;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


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

        //Chemin d'enregistrement de la photo
        File newfolder = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        path = new File(newfolder, File.separator + "temp.png");

        //Appel de l'application photo
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(path));
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Si une photo a été prise
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {


            Bitmap bitmap = this.getCapturedPicture();
            Display display = getWindowManager().getDefaultDisplay();

            /*
                L'image doit être redimentionnée pour diverses raisons :
                    - L’image doit être contenue en entier dans la vue.
                    - OpenGL impose une limite pour la taille d’image affichable dans un canvas.
             */
            Point size = new Point();
            display.getSize(size);
            float width = size.x;
            float height = size.y;

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

                    //Si le centre du cercle n'est pas défini (ex : premier touché sur l'image
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
            //Si l'appareil photo est quitté on retourne à la page d'acceuil
            uncheckPicture();
        }
    }

    /**
     *
     * @return L'image prise par l'application
     */
    public Bitmap getCapturedPicture() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES).toString()+File.separator + "temp.png", options);
        return bitmap;
    }

    /**
     *
     * @return La position (coordonnées) de l'utilisateur
     */
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

    /**
     * Cette activité est complétée et doit appeler la suivante
     */
    public void checkPicture() {

        Location location = this.getLocation();
        if(this.bmpinterest==null) {
            this.bmpinterest = this.bmporiginal;
        }

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

    /**
     * Ré-initialiser le centre du cercle (point d'intérêt
     */
    public void refreshInterestPoint() {
        xcenter = null;
        ycenter = null;
        imageView.setImageBitmap(Bitmap.createScaledBitmap(bmporiginal, (int) (bmporiginal.getWidth()*ratio),(int) (bmporiginal.getHeight()*ratio) ,false));
    }

    /**
     * Retour à l'acceuil
     */
    public void uncheckPicture() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    /**
     *
     * @param image Image à enregistrers sur le smartphone
     */
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
