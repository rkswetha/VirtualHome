package com.wikitude.virtualhome;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.opengl.GLES20;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Toast;
import java.io.IOException;
import com.wikitude.architect.ArchitectView;
import com.wikitude.architect.StartupConfiguration;
import com.wikitude.virtualhome.R;



public class AugmentedActivity extends Activity {

    private static final String TAG = AugmentedActivity.class.getSimpleName();
    protected boolean isLoading = false;


    protected static final String WIKITUDE_SDK_KEY = "Z4eEVW8h32G9F2bo4eYaOrAI9Oq4ncTwh7GFpwoijkQyInzS2a2yqR1/8plTni+NYOl/MxdL+D5sBnjl8neSg37eyvx1eyz8Ho+09gprMbsUwpj8PNSCm0RljKFZ3h1MS/zUkp6lpGDl7RaCO5aN3OeEVwjoIXMTjHcCugD+q71TYWx0ZWRfX8rXhV9sRtlf7GEobnGagIUT2CCjqd3xAIniI8kLzfHHhucAwodvlsN7CW331kB9WzItqnBGxvFZuSmUQZTAglHfExq+CtcSenaxNgglgZGZhdO3QKEFjevm9plsuU2P3mYHqLxYMSiW9J+PvOyhbmWH4ldfq0c1FlNOuaQB9n8zzfPoBFD9O8+/RWaDqSW+eY2kd8yLFnZ8025T6JZk0Poty0JgoknYZI1p1lXRIVWzv4GMbN10Dv9phTMJjw5xWNYzpxRyFK3wAmeyw0d2YueGgGYAmcAjZNJwPIMiJWDb8drjj6lXJJUZDs+PRxuuP4sqse9WpL+6fAPjUVPNJvxX8oz0pJBM8BjVi7rf0OXSWCpUmufpMyb7MrGFko3PevB1X/mlcHKTXI9LvJLTssemFiXAets6IFwhCwP3EBzaFpz4CITHWgqy7gNG9ZycqhWLBWnV6nPbAedvHClyFMXMg9ek07E0gl06CUYzGpQKoO9R/Wn4TyE=";
    //As this is a Free Trial License it will put a TRIAL water mark across the screen
    protected ArchitectView architectView;
    StartupConfiguration startupConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_augmented);

        this.setTitle("AR Virtual Home");

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
        final StartupConfiguration config = new StartupConfiguration( WIKITUDE_SDK_KEY, StartupConfiguration.Features.Geo, StartupConfiguration.CameraPosition.BACK );
        try{

            this.architectView.onCreate(config);

        } catch (RuntimeException rex) {
            this.architectView = null;
            Toast.makeText(getApplicationContext(), "can't create Architect View", Toast.LENGTH_SHORT).show();
            Log.e(this.getClass().getName(), "Exception in ArchitectView.onCreate()", rex);
        }

    }

    @Override
    protected void onPostCreate( final Bundle savedInstanceState ) {
        super.onPostCreate( savedInstanceState );

        if ( this.architectView != null ) {

            // call mandatory live-cycle method of architectView
            this.architectView.onPostCreate();

            try {
                // load content via url in architectView, ensure '<script src="architect://architect.js"></script>' is part of this HTML file,
                // have a look at wikitude.com's developer section for API references
                //
//	            String s= getAssets().open("samples/Test1/index.html").toString();
//Log.e(TAG, "LOCATION="+s);
//	            this.architectView.load(getAssets().open("samples/Test1/index.html").toString());

                //for some odd reason it doesn't always load the files from the assets
                //also, do we REALLY want to embed the stuff inside the app?
                //it makes more sense to host the files on a web-server outside of the app
                //allows for making changes outside of the app
                //this.architectView.load(getAssets().open("arviews/ImageOnTarget/index.html").toString());
                //this.architectView.load(getAssets().open("arviews/ImageOnTarget/index.html").toString());

                this.architectView.load("arviews/ImageOnTarget/index.html");

                Log.e(TAG, "Loaded the asset folder/web app correctly");

                Toast.makeText(this, " asset  folder loaded",
                        Toast.LENGTH_SHORT).show();

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
        if ( this.architectView != null ) {
            this.architectView.onLowMemory();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_augmented, menu);
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

