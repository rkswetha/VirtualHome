 package com.wikitude.virtualhome;

 import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.opengl.GLES20;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.Toast;

import com.wikitude.architect.ArchitectView;
import com.wikitude.architect.StartupConfiguration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;



public class AugmentedActivity extends Activity {

    private static final String TAG = AugmentedActivity.class.getSimpleName();
    protected boolean isLoading = false;


    //protected static final String WIKITUDE_4_1_SDK_KEY = "Z4eEVW8h32G9F2bo4eYaOrAI9Oq4ncTwh7GFpwoijkQyInzS2a2yqR1/8plTni+NYOl/MxdL+D5sBnjl8neSg37eyvx1eyz8Ho+09gprMbsUwpj8PNSCm0RljKFZ3h1MS/zUkp6lpGDl7RaCO5aN3OeEVwjoIXMTjHcCugD+q71TYWx0ZWRfX8rXhV9sRtlf7GEobnGagIUT2CCjqd3xAIniI8kLzfHHhucAwodvlsN7CW331kB9WzItqnBGxvFZuSmUQZTAglHfExq+CtcSenaxNgglgZGZhdO3QKEFjevm9plsuU2P3mYHqLxYMSiW9J+PvOyhbmWH4ldfq0c1FlNOuaQB9n8zzfPoBFD9O8+/RWaDqSW+eY2kd8yLFnZ8025T6JZk0Poty0JgoknYZI1p1lXRIVWzv4GMbN10Dv9phTMJjw5xWNYzpxRyFK3wAmeyw0d2YueGgGYAmcAjZNJwPIMiJWDb8drjj6lXJJUZDs+PRxuuP4sqse9WpL+6fAPjUVPNJvxX8oz0pJBM8BjVi7rf0OXSWCpUmufpMyb7MrGFko3PevB1X/mlcHKTXI9LvJLTssemFiXAets6IFwhCwP3EBzaFpz4CITHWgqy7gNG9ZycqhWLBWnV6nPbAedvHClyFMXMg9ek07E0gl06CUYzGpQKoO9R/Wn4TyE=";
    protected static final String WIKITUDE_5_0_SDK_KEY = "Z4eEVW8h32G9F2bo4eYaOrAI9Oq4ncTwh7GFpwoijkQyInzS2a2yqR1/8plTni+NYOl/MxdL+D5sBnjl8neSg37eyvx1eyz8Ho+09gprMbsUwpj8PNSCm0RljKFZ3h1MS/zUkp6lpGDl7RaCO5aN3OeEVwjoIXMTjHcCugD+q71TYWx0ZWRfX8rXhV9sRtlf7GEobnGagIUT2CCjqd3xAIniI8kLzfHHhucAwodvlsN7CW331kB9WzItqnBGxvFZuSmUQZTAglHfExq+CtcSenaxNgglgZGZhdO3QKEFjevm9plsuU2P3mYHqLxYMSiW9J+PvOyhbmWH4ldfq0c1FlNOuaQB9n8zzfPoBFD9O8+/RWaDqSW+eY2kd8yLFnZ8025T6JZk0Poty0JgoknYZI1p1lXRIVWzv4GMbN10Dv9phTMJjw5xWNYzpxRyFK3wAmeyw0d2YueGgGYAmcAjZNJwPIMiJWDb8drjj6lXJJUZDs+PRxuuP4sqse9WpL+6fAPjUVPNJvxX8oz0pJBM8BjVi7rf0OXSWCpUmufpMyb7MrGFko3PevB1X/mlcHKTXI9LvJLTssemFiXAets6IFwhCwP3EBzaFpz4CITHWgqy7gNG9ZycqhWLBWnV6nPbAedvHClyFMXMg9ek07E0gl06CUYzGpQKoO9R/Wn4TyE=";

    //As this is a Free Trial License it will put a TRIAL water mark across the screen
    protected ArchitectView architectView;

    protected ArchitectView.ArchitectUrlListener urlListener;

    StartupConfiguration startupConfiguration;
    String markerPresent;
    String imagePath;
    private static final int SELECT_PICTURE = 1;
    private static final int MORE_PICTURE = 2;
    String selectedImagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_augmented);

        this.setTitle("AR Virtual Home");

        markerPresent = getIntent().getStringExtra("MarkerPresent");
        imagePath = getIntent().getStringExtra("ImagePath");

        Log.e(TAG, "VIRTUALHOME: User has marker?"+markerPresent);
        Log.e(TAG, "VIRTUALHOME: User selected image path:" +imagePath);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if ( 0 != ( getApplicationInfo().flags &= ApplicationInfo.FLAG_DEBUGGABLE ) ) {
                WebView.setWebContentsDebuggingEnabled(true);
            }
        }

        this.architectView = (ArchitectView)this.findViewById( R.id.architectView );

//		To use the Wikitude Android SDK you need to provide a valid license key to the onCreate lifecycle-method of the ArchitectView.
//		This can either be done directly by providing the key as a string and the call the onCreate(final String key) method or creating an
//		StartupConfiguration object, passing it the license as a string and then call the onCreate(final StartupConfiguration config) method.
//		Please refer to the AbstractArchitectCamActivity of the SDK Examples project for a practical example of how to set the license key.
        final StartupConfiguration config = new StartupConfiguration( WIKITUDE_5_0_SDK_KEY, StartupConfiguration.Features.Tracking2D, StartupConfiguration.CameraPosition.BACK );
        try{

            this.architectView.onCreate(config);

        } catch (RuntimeException rex) {
            this.architectView = null;
            Toast.makeText(getApplicationContext(), "can't create Architect View", Toast.LENGTH_SHORT).show();
            Log.e(this.getClass().getName(), "VIRTUALHOME: Exception in ArchitectView.onCreate()", rex);
        }

        Log.e(this.getClass().getName(), " VIRTUALHOME: Assigning UrlListener");

        // set urlListener, any calls made in JS like "document.location = 'architectsdk://foo?bar=123'" is forwarded to this listener, use this to interact between JS and native Android activity/fragment
        this.urlListener = this.getUrlListener();

        // register valid urlListener in architectView, ensure this is set before content is loaded to not miss any event
        if (this.urlListener != null && this.architectView != null) {
            Log.e(this.getClass().getName(), " VIRTUALHOME: Call registerUrlListener");
            this.architectView.registerUrlListener( this.urlListener );
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Maintain different menu items based on marker/markerless
        if(markerPresent.equals("YES")) {

            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_augmented, menu);
        }
        else if(markerPresent.equals("NO")) {

            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.augmented_actions, menu);
        }

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (item.getItemId()) {

            case R.id.action_gallery:
                // OPen gallery to choose wall paper
                Toast.makeText(getApplicationContext(), "Opening Android Gallery", Toast.LENGTH_SHORT).show();
                setBkgImage();
                return true;

            case R.id.action_chooseAnother:
                // Open app gallery to choose additional product images
                Toast.makeText(getApplicationContext(), "Choose another product", Toast.LENGTH_SHORT).show();
                chooseMoreImage();
                return true;

            case R.id.action_snapShot:
                Toast.makeText(getApplicationContext(), "Take snapshot", Toast.LENGTH_SHORT).show();
                shareSnapShot();
                return true;

            case R.id.action_snapToScreen:
                Toast.makeText(getApplicationContext(), "Snap to Screen", Toast.LENGTH_SHORT).show();
                this.architectView.callJavascript("World.displaySnapToScreen()");
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {

            if (requestCode == SELECT_PICTURE) {

                Uri selectedImageUri = data.getData();
                selectedImagePath = getPath(selectedImageUri);
                //Changing the location
                Log.i("AA", "selected Path " + selectedImagePath);

                try {
                    if (selectedImagePath != null) {
                            callJavaScript("setBackgroundImageUsingImagePath", URLEncoder.encode(selectedImagePath, "UTF-8"));
                            //callJavaScript("addImage", URLEncoder.encode("http://anushar.com/cmpe295Images/coffeetable.png", "UTF-8"));
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        else if (resultCode == 2) {

                if (requestCode == MORE_PICTURE) {
                    Log.i("AugmentedA", "inside MORE_PICTURE");
                    String photoPath = data.getStringExtra("location");

                    Log.i("Augement-ExtraProd", "Photo Path " + photoPath);
                    try {
                            if(markerPresent.equals("YES"))
                            {
                                callJavaScript("World.chooseAnotherImage", URLEncoder.encode(photoPath, "UTF-8"));
                            }
                            else if (markerPresent.equals("NO"))
                            {
                                callJavaScript("addImage", URLEncoder.encode(photoPath, "UTF-8"));
                            }
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                }
        }

    }


    public String getPath(Uri uri) {
        // try to retrieve the image from the media store first
        // this will only work for images selected from gallery
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if( cursor != null ){
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        // this is our fallback here
        return uri.getPath();
    }

/*
This function is used to add more product images to the AR screen
 */
    public void chooseMoreImage()
    {
        Intent intent = new Intent(getApplication(),SofaGallery.class);
        intent.putExtra("additionalProduct", "yes");
        startActivityForResult(intent, MORE_PICTURE);
    }


    public void setBkgImage()
    {
        Intent intent = new Intent();
        intent.setType("image");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
                "Select Picture"), SELECT_PICTURE);
    }

    @Override
    protected void onPostCreate( final Bundle savedInstanceState ) {
        super.onPostCreate(savedInstanceState);

        if ( this.architectView != null ) {

            // call mandatory live-cycle method of architectView
            this.architectView.onPostCreate();

            try  {

                    if(markerPresent.equals("YES")){

                        Log.e(this.getClass().getName(), " VIRTUALHOME: Invoking Marker based AR View");

                        this.architectView.load("arviews/ImageOnTarget/index.html");
                        callJavaScript("World.readImagePath", URLEncoder.encode(imagePath, "UTF-8"));
                    }
                    else if (markerPresent.equals("NO")){

                        Log.e(this.getClass().getName(), " VIRTUALHOME: Invoking Markerless based AR View");
                        this.architectView.load("arviews/MarkerlessImageOnTarget/index.html");
                        callJavaScript("addImage", URLEncoder.encode(imagePath, "UTF-8"));
                    }
                    Log.e(TAG, "VIRTUALHOME: Loaded the asset folder/web app correctly");

            } catch (IOException e1) {
                e1.printStackTrace();
            }

        }
    }

    public static final boolean isVideoDrawablesSupported() {
        String extensions = GLES20.glGetString( GLES20.GL_EXTENSIONS );
        return extensions != null && extensions.contains( "GL_OES_EGL_image_external" ) && android.os.Build.VERSION.SDK_INT >= 14 ;
    }


    @Override
    public void onResume() {
        super.onResume();

        this.architectView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        // call mandatory live-cycle method of architectView
        if ( this.architectView != null ) {
            this.architectView.onPause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // call mandatory live-cycle method of architectView
        if ( this.architectView != null ) {
            this.architectView.onDestroy();
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (this.architectView != null) {
            this.architectView.onLowMemory();
        }
    }

    /**
      * call JacaScript in architectView
      * @param methodName
      * @param imagePath
     * */
    private void callJavaScript(final String methodName, final String imagePath) {

         if (this.architectView!=null) {

                final String js = ( methodName + "(\"" + imagePath +  "\");" );
                Log.e(this.getClass().getName(), " VIRTUALHOME: calling JS method:" + js);

                //this.architectView.callJavascript("World.readImagePath( \"http://www.ikea.com/ca/en/images/products/kivik-loveseat-and-chaise-lilac__0252355_PE391172_S4.JPG\");");
                this.architectView.callJavascript(js);

         }
    }


    public void shareSnapShot(){

        Log.e(this.getClass().getName(), " VIRTUALHOME: calling captureSnapShot method");

        architectView.captureScreen(ArchitectView.CaptureScreenCallback.CAPTURE_MODE_CAM_AND_WEBVIEW, new ArchitectView.CaptureScreenCallback() {

            @Override
            public void onScreenCaptured(final Bitmap screenCapture) {
                // store screenCapture into external cache directory
                final File screenCaptureFile = new File(Environment.getExternalStorageDirectory().toString(), "screenCapture_" + System.currentTimeMillis() + ".jpg");

                // 1. Save bitmap to file & compress to jpeg. You may use PNG too
                try {
                    final FileOutputStream out = new FileOutputStream(screenCaptureFile);
                    screenCapture.compress(Bitmap.CompressFormat.JPEG, 90, out);
                    out.flush();
                    out.close();

                    // 2. create send intent
                    final Intent share = new Intent(Intent.ACTION_SEND);
                    share.setType("image/jpg");
                    share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(screenCaptureFile));

                    // 3. launch intent-chooser
                    final String chooserTitle = "Share Snapshot";
                    startActivity(Intent.createChooser(share, chooserTitle));

                } catch (final Exception e) {
                    // should not occur when all permissions are set
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Log.e(this.getClass().getName(), " VIRTUALHOME: Share Snapshot failed ");
                            // show toast message in case something went wrong
                            //Toast.makeText(this, " Unexpected error", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }


    //url listener fired once e.g. 'document.location = "architectsdk://foo?bar=123"' is called in JS
    public ArchitectView.ArchitectUrlListener getUrlListener() {

        Log.e(this.getClass().getName(), " VIRTUALHOME: Called getUrlListener");

        return new ArchitectView.ArchitectUrlListener() {

            @Override
            public boolean urlWasInvoked(String uriString) {

                Log.e(this.getClass().getName(), " VIRTUALHOME: Called urlWasInvoked- uriString" +uriString);
                Uri invokedUri = Uri.parse(uriString);
                return true;
            }
        };
    }
}

