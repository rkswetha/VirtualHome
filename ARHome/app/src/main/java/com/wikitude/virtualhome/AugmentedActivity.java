package com.wikitude.virtualhome;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;

import android.opengl.GLES20;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Toast;


import java.io.*;
import java.io.IOException;
import java.net.URLEncoder;
import com.wikitude.architect.ArchitectView;
import com.wikitude.architect.StartupConfiguration;



public class AugmentedActivity extends Activity {

    private static final String TAG = AugmentedActivity.class.getSimpleName();
    protected boolean isLoading = false;


    protected static final String WIKITUDE_SDK_KEY = "Z4eEVW8h32G9F2bo4eYaOrAI9Oq4ncTwh7GFpwoijkQyInzS2a2yqR1/8plTni+NYOl/MxdL+D5sBnjl8neSg37eyvx1eyz8Ho+09gprMbsUwpj8PNSCm0RljKFZ3h1MS/zUkp6lpGDl7RaCO5aN3OeEVwjoIXMTjHcCugD+q71TYWx0ZWRfX8rXhV9sRtlf7GEobnGagIUT2CCjqd3xAIniI8kLzfHHhucAwodvlsN7CW331kB9WzItqnBGxvFZuSmUQZTAglHfExq+CtcSenaxNgglgZGZhdO3QKEFjevm9plsuU2P3mYHqLxYMSiW9J+PvOyhbmWH4ldfq0c1FlNOuaQB9n8zzfPoBFD9O8+/RWaDqSW+eY2kd8yLFnZ8025T6JZk0Poty0JgoknYZI1p1lXRIVWzv4GMbN10Dv9phTMJjw5xWNYzpxRyFK3wAmeyw0d2YueGgGYAmcAjZNJwPIMiJWDb8drjj6lXJJUZDs+PRxuuP4sqse9WpL+6fAPjUVPNJvxX8oz0pJBM8BjVi7rf0OXSWCpUmufpMyb7MrGFko3PevB1X/mlcHKTXI9LvJLTssemFiXAets6IFwhCwP3EBzaFpz4CITHWgqy7gNG9ZycqhWLBWnV6nPbAedvHClyFMXMg9ek07E0gl06CUYzGpQKoO9R/Wn4TyE=";
    //As this is a Free Trial License it will put a TRIAL water mark across the screen
    protected ArchitectView architectView;

    /**
     * urlListener handling "document.location= 'architectsdk://...' " calls in JavaScript"
     */
    protected ArchitectView.ArchitectUrlListener urlListener;

    StartupConfiguration startupConfiguration;
    String markerPresent;
    String imagePath;

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
        final StartupConfiguration config = new StartupConfiguration( WIKITUDE_SDK_KEY, StartupConfiguration.Features.Tracking2D, StartupConfiguration.CameraPosition.BACK );
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
    protected void onPostCreate( final Bundle savedInstanceState ) {
        super.onPostCreate( savedInstanceState );

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
         */
        private void callJavaScript(final String methodName, final String imagePath) {

            if (this.architectView!=null) {

                final String js = ( methodName + "(\"" + imagePath +  "\");" );
                Log.e(this.getClass().getName(), " VIRTUALHOME: calling JS method:" + js);

                //this.architectView.callJavascript("World.readImagePath( \"http://www.ikea.com/ca/en/images/products/kivik-loveseat-and-chaise-lilac__0252355_PE391172_S4.JPG\");");
                this.architectView.callJavascript(js);

            }
        }

    /**
     * url listener fired once e.g. 'document.location = "architectsdk://foo?bar=123"' is called in JS
     * @return
     */
    public ArchitectView.ArchitectUrlListener getUrlListener() {

        Log.e(this.getClass().getName(), " VIRTUALHOME: Called getUrlListener");

        return new ArchitectView.ArchitectUrlListener() {

            @Override
            public boolean urlWasInvoked(String uriString) {

                Log.e(this.getClass().getName(), " VIRTUALHOME: Called urlWasInvoked- uriString" +uriString);
                Uri invokedUri = Uri.parse(uriString);

                // pressed snapshot button. check if host is button to fetch e.g. 'architectsdk://button?action=captureScreen', you may add more checks if more buttons are used inside AR scene
                if ("button1".equalsIgnoreCase(invokedUri.getHost())) {
                    Log.e(this.getClass().getName(), " VIRTUALHOME: Snapshot button pressed ");
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
                                final String chooserTitle = "Share Snaphot";
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
                    return true;
                }
                else if ("button2".equalsIgnoreCase(invokedUri.getHost())) {

                    Log.e(this.getClass().getName(), " VIRTUALHOME: Product Info button pressed ");
                    Intent intent = new Intent (getApplication(), ProductInfo.class);
                    intent.putExtra("ProductName", "MyProductName");
                    intent.putExtra("Dimension", "MyDimension");
                    intent.putExtra("Price", "MyPrice");
                    intent.putExtra("Description", "MyDescription");
                    startActivity(intent);

                }
                return false;
            }
        };
    }
}

