package com.misalud.saydex.misalud;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class WebActivity extends AppCompatActivity {

    private WebView mWebView;
    private long id;
    private DownloadManager downloadManager;
    private ProgressBar progressBar;
    //variable del usuario
    public static  Usuario USUARIO_LOGIN;
    public static  String CLAVE;
    private ImageButton btnVolver;
    public static String RESPUESTA_LOGIN;
    public static  String NOMBRE_USUARIO;
    public static boolean ES_CHECK;
    public static String URL_ENVIAR = "http://catest.saludsidra.cl/misalud/Inicio_1.aspx?ID_USUARIO=";

    private static final String TAG = MainActivity.class.getSimpleName();
    private String mCM;
    private ValueCallback<Uri> mUM;
    private ValueCallback<Uri[]> mUMA;
    private final static int FCR=1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
        super.onActivityResult(requestCode, resultCode, intent);
        if(Build.VERSION.SDK_INT >= 21){
            Uri[] results = null;
            //Check if response is positive
            if(resultCode== Activity.RESULT_OK){
                if(requestCode == FCR){
                    if(null == mUMA){
                        return;
                    }
                    if(intent == null){
                        //Capture Photo if no image available
                        if(mCM != null){
                            results = new Uri[]{Uri.parse(mCM)};
                        }
                    }else{
                        String dataString = intent.getDataString();
                        if(dataString != null){
                            results = new Uri[]{Uri.parse(dataString)};
                        }
                    }
                }
            }
            mUMA.onReceiveValue(results);
            mUMA = null;
        }else{
            if(requestCode == FCR){
                if(null == mUM) return;
                Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
                mUM.onReceiveValue(result);
                mUM = null;
            }
        }
    }

    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };

    @SuppressLint({"SetJavaScriptEnabled", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //pantalla completa
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_web);


        btnVolver = (ImageButton)findViewById(R.id.imgBtnVolver);
        btnVolver.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                //abrir activity url
                /*Intent intObj = new Intent(InicioActivity.this, WebActivity.class);
                startActivity(intObj);*/
                finish();
            }

        });

        mWebView = (WebView) findViewById(R.id.webView1);
        /*
        mWebView.setWebViewClient(new WebViewClient() {
            // evita que los enlaces se abran fuera nuestra app en el navegador de android
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

        });
        */
        progressBar = (ProgressBar) findViewById(R.id.progressBarN);
        /*
        mWebView.setDownloadListener(new DownloadListener()
        {
            public void onDownloadStart(final String url, String userAgent, final String contentDisposition, final String mimetype, long contentLength)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(WebActivity.this);
                builder.setTitle("Descarga");
                builder.setMessage("¿Desea guardar el fichero en su tarjeta SD?");
                builder.setCancelable(false).setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        String fileName;
                        try {
                            fileName = URLUtil.guessFileName(url, contentDisposition, mimetype);
                            downloadFileAsync(url, fileName);
                        }catch (Exception e){

                        }
                        //(new DownloadAsyncTask()).execute(url);
                    }

                }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        dialog.cancel();
                    }
                });
                builder.create().show();


            }


        });
        */
        // Activamos Javascript
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        //agregados para vista de escritorio
        //webSettings.setLoadWithOverviewMode(true);
        //webSettings.setUseWideViewPort(true);
        webSettings.setAllowFileAccess(true);

        webSettings.setAllowContentAccess(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setSupportMultipleWindows(true);
        webSettings.setSaveFormData(true);
        mWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        mWebView.setScrollbarFadingEnabled(false);
        mWebView.requestFocusFromTouch();

        if(Build.VERSION.SDK_INT >= 21){
            webSettings.setMixedContentMode(0);
            mWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }else if(Build.VERSION.SDK_INT >= 19){
            mWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }else if(Build.VERSION.SDK_INT >=11 && Build.VERSION.SDK_INT < 19){
            mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        mWebView.setWebViewClient(new Callback());

        if (USUARIO_LOGIN != null)
            //http://localhost:32894/Inicio_1.aspx
            mWebView.loadUrl(URL_ENVIAR + USUARIO_LOGIN.getId());
            //mWebView.loadUrl("http://localhost:32894/Inicio_1.aspx?ID_USUARIO=" + USUARIO_LOGIN.getId());
        else
        {
            //mWebView.loadUrl("http://catest.saludsidra.cl/misalud/Login.aspx");
            //si viene nulo lo volvemos al main
            Toast.makeText(this, "Usted ha perdido la sesión debe volver a ingresar", Toast.LENGTH_LONG);
            finish();
        }


        mWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int progress) {
                progressBar.setProgress(0);
                progressBar.setVisibility(View.VISIBLE);
                WebActivity.this.setProgress(progress * 1000);

                progressBar.incrementProgressBy(progress);

                if (progress == 100) {
                    progressBar.setVisibility(View.GONE);
                }
            }
            //For Android 3.0+
            public void openFileChooser(ValueCallback<Uri> uploadMsg){
                mUM = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                WebActivity.this.startActivityForResult(Intent.createChooser(i,"File Chooser"), FCR);
            }
            // For Android 3.0+, above method not supported in some android 3+ versions, in such case we use this
            public void openFileChooser(ValueCallback uploadMsg, String acceptType){
                mUM = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                WebActivity.this.startActivityForResult(
                        Intent.createChooser(i, "File Browser"),
                        FCR);
            }
            //For Android 4.1+
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture){
                mUM = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                WebActivity.this.startActivityForResult(Intent.createChooser(i, "File Chooser"), WebActivity.FCR);
            }
            //For Android 5.0+
            public boolean onShowFileChooser(
                    WebView webView, ValueCallback<Uri[]> filePathCallback,
                    WebChromeClient.FileChooserParams fileChooserParams){
                if(mUMA != null){
                    mUMA.onReceiveValue(null);
                }
                mUMA = filePathCallback;
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(takePictureIntent.resolveActivity(WebActivity.this.getPackageManager()) != null){
                    File photoFile = null;
                    try{
                        photoFile = createImageFile();
                        takePictureIntent.putExtra("PhotoPath", mCM);
                    }catch(IOException ex){
                        Log.e(TAG, "Image file creation failed", ex);
                    }
                    if(photoFile != null){
                        mCM = "file:" + photoFile.getAbsolutePath();
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                    }else{
                        takePictureIntent = null;
                    }
                }
                Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
                contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
                contentSelectionIntent.setType("image/*");
                Intent[] intentArray;
                if(takePictureIntent != null){
                    intentArray = new Intent[]{takePictureIntent};
                }else{
                    intentArray = new Intent[0];
                }

                Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
                chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
                chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
                startActivityForResult(chooserIntent, FCR);
                return true;
            }
        });
        /*
        webSettings.setAllowContentAccess(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setSupportMultipleWindows(true);
        webSettings.setSaveFormData(true);

        mWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        mWebView.setScrollbarFadingEnabled(false);
        mWebView.requestFocusFromTouch();

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int progress) {
                progressBar.setProgress(0);
                progressBar.setVisibility(View.VISIBLE);
                WebActivity.this.setProgress(progress * 1000);

                progressBar.incrementProgressBy(progress);

                if (progress == 100) {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });


        mWebView.setWebViewClient(new WebViewClient());
        */
    }

    public class Callback extends WebViewClient{
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl){
            Toast.makeText(getApplicationContext(), "Failed loading app!", Toast.LENGTH_SHORT).show();
        }
    }
    // Create an image file
    private File createImageFile() throws IOException{
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "img_"+timeStamp+"_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName,".jpg",storageDir);
    }
    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event){
        if(event.getAction() == KeyEvent.ACTION_DOWN){
            switch(keyCode){
                case KeyEvent.KEYCODE_BACK:
                    if(mWebView.canGoBack()){
                        String url = mWebView.getUrl();
                        String urlOriginal = mWebView.getOriginalUrl();
                        if (url.contains("SubirImagen"))
                        {
                            mWebView.loadUrl(urlOriginal);
                        }
                        else {
                            finish();
                            //mWebView.loadUrl(urlOriginal);
                            //mWebView.goBack();
                        }
                        //mWebView.goBack();
                    }else{
                        finish();
                    }
                    return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
    }

    private void downloadFileAsync(final String url, String filename){

        new AsyncTask<String, Void, String>() {
            String SDCard;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(String... params) {

                String retorno="";
                String servicestring = Context.DOWNLOAD_SERVICE;
                DownloadManager downloadmanager;
                try {
                    downloadmanager = (DownloadManager) getSystemService(servicestring);
                /*Uri uri = Uri
                        .parse("https://sites.google.com/site/compiletimeerrorcom/android-programming/CameraApp.rar");*/
                    Uri uri = Uri
                            .parse(url);
                    //http://www.cpas.cl/Repositorio/fsdrt32u.mhp%20Acta%20Segunda%20Reunion%20cgpa%2018%206%202015.pdf
                    DownloadManager.Request request = new DownloadManager.Request(uri);
                    Long reference = downloadmanager.enqueue(request);
                    retorno = "Guardado con éxito.";
                }
                catch (Exception ex)
                {
                    retorno = ex.getMessage();
                }
                return retorno;
            }
            @Override
            protected void onPostExecute(final String result) {
                AlertDialog.Builder builder = new AlertDialog.Builder(WebActivity.this);
                builder.setMessage(result).setPositiveButton("Aceptar", null).setTitle("Descarga");
                builder.show();
            }

        }.execute(url, filename);
    }

    public String getFilename(String contentDisposition){
        String filename[] = contentDisposition.split("filename=");
        return filename[1].replace("filename=", "").replace("\"", "").trim();
    };

    public class DownloadAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... arg0)
        {
            String result = null;
            String url = arg0[0];

            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
            {
                URL urlObject = null;
                InputStream inputStream = null;
                HttpURLConnection urlConnection = null;
                try
                {
                    urlObject = new URL(url);
                    urlConnection = (HttpURLConnection) urlObject.openConnection();
                    inputStream = urlConnection.getInputStream();

                    String fileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/webviewdemo";
                    File directory = new File(fileName);
                    File file = new File(directory, url.substring(url.lastIndexOf("/")));
                    directory.mkdirs();


                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int len = 0;

                    while (inputStream.available() > 0   && (len = inputStream.read(buffer)) != -1)
                    {
                        byteArrayOutputStream.write(buffer, 0, len);
                    }

                    fileOutputStream.write(byteArrayOutputStream.toByteArray());
                    fileOutputStream.flush();
                    result = "guardado en : " + file.getAbsolutePath();
                }
                catch (Exception ex)
                {
                    result = ex.getClass().getSimpleName() + " " + ex.getMessage();
                }
                finally
                {
                    if (inputStream != null)
                    {
                        try
                        {
                            inputStream.close();
                        }
                        catch (IOException ex)
                        {
                            result = ex.getClass().getSimpleName() + " " + ex.getMessage();
                        }
                    }
                    if (urlConnection != null)
                    {
                        urlConnection.disconnect();
                    }
                }
            }
            else
            {
                result = "Almacenamiento no disponible";
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(WebActivity.this);
            builder.setMessage(result).setPositiveButton("Aceptar", null).setTitle("Descarga");
            builder.show();
        }
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        //delayedHide(100);
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        //mControlsView.setVisibility(View.GONE);
        //mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        //mHideHandler.removeCallbacks(mShowPart2Runnable);
        //mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        //mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        //mHideHandler.removeCallbacks(mHidePart2Runnable);
        //mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        //mHideHandler.removeCallbacks(mHideRunnable);
        //mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
    @Override
    public void onBackPressed()
    {
        if(mWebView.canGoBack()){
            mWebView.goBack();
        }else{
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Salir!")
                    .setMessage("¿Esta seguro de salir?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }

                    })
                    .setNegativeButton("No", null)
                    .show();
        }
    }

}
