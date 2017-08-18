package com.misalud.saydex.misalud;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.zip.CheckedInputStream;

public class MainActivity extends AppCompatActivity {

    static boolean errored = false;
    Button b;
    TextView statusTV;
    EditText userNameET, passWordET;
    String editTextUsername;
    boolean loginStatus;
    ProgressBar webservicePG;
    String editTextPassword;
    String usuarioStr;
    public static  Usuario USUARIO_LOGIN;
    public static  Notificaciones NOTIFICACIONES_JSON;
    CheckBox chkRecordar;
    boolean preferenciasGuardadas;
    String notificacionesStr;

    String nombreUsuario;
    String claveUsuario;
    boolean esCheck;
    public static NotificationManager mNotificationManager;

    private VolleyS volley;
    protected RequestQueue fRequestQueue;

    @SuppressLint({"SetJavaScriptEnabled", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //voley
        volley = VolleyS.getInstance(this.getApplicationContext());
        fRequestQueue = volley.getRequestQueue();
        //pantalla completa
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        userNameET = (EditText) findViewById(R.id.login_usuario);
        passWordET = (EditText) findViewById(R.id.editTextPass);

        //acá debemos guardar en preferencias el usuario y la password
        //si queremos recordar la contraseña para setearla directamente
        //a los controles.
        chkRecordar = (CheckBox)findViewById(R.id.checkrecordar);

        chkRecordar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (((CheckBox) v).isChecked()) {
                    //Case 1
                    //GuardarPreferencias();
                }
                else{
                    //case 2
                }


            }
        });

        CargarPreferencias();
        if (this.preferenciasGuardadas) {
            //ahora setear los valores de las preferencias en los textbox
            if (this.nombreUsuario != "" && this.claveUsuario != "")
            {
                userNameET.setText(this.nombreUsuario);
                passWordET.setText(this.claveUsuario);
                chkRecordar.setChecked(this.esCheck);
            }
        }
        statusTV = (TextView) findViewById(R.id.tv_result);

        b = (Button) findViewById(R.id.buttonlogin);
        webservicePG = (ProgressBar) findViewById(R.id.progressBar1);

        //Button Click Listener
        b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Check if text controls are not empty
                if (userNameET.getText().length() != 0 && userNameET.getText().toString() != "") {
                    if (passWordET.getText().length() != 0 && passWordET.getText().toString() != "") {
                        editTextUsername = userNameET.getText().toString();
                        editTextPassword = passWordET.getText().toString();
                        statusTV.setText("");
                        //guardamos las preferencias
                        if (chkRecordar.isChecked())
                            GuardarPreferencias();

                        makeRequest(editTextUsername, editTextPassword);

                        //comentado por victor coronado QUITAR LLAMADA WS
/*
                        //Create instance for AsyncCallWS
                        AsyncCallWS task = new AsyncCallWS();
                        //Call execute
                        task.execute();
*/
                    }
                    //If Password text control is empty
                    else {
                        statusTV.setText("Ingrese Password");
                    }
                    //If Username text control is empty
                } else {
                    statusTV.setText("Ingrese nombre de usuario");
                }
            }
        });
        //comentado por victor coronado QUITAR LLAMADA WS
        /*
        Timer timer = new Timer();
        TimerTask task = new TimerTask()
        {
            @SuppressWarnings("deprecation")
            @Override
            public void run()
            {
                try {
                    //por mientras
                    //Notificar();
                    if (MainActivity.NOTIFICACIONES_JSON != null && MainActivity.NOTIFICACIONES_JSON.getId() > 0)
                    {
                        GenerarNotificaciones(MainActivity.NOTIFICACIONES_JSON);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }; timer.schedule(task, 20000, 600000);
        */

    }
    //cargar configuración aplicación Android usando SharedPreferences
    public void CargarPreferencias(){
        SharedPreferences prefs = getSharedPreferences("preferenciasMiApp", Context.MODE_PRIVATE);
        this.nombreUsuario = prefs.getString("nombreUsuario", "");
        this.claveUsuario = prefs.getString("claveUsuario", "");
        //this.esCheck=prefs.getBoolean("check",true);
        this.esCheck = prefs.getBoolean("check", true);
        preferenciasGuardadas = prefs.getBoolean("preferenciasGuardadas", false);

    }
    //guardar configuración aplicación Android usando SharedPreferences
    public void GuardarPreferencias(){
        SharedPreferences prefs = getSharedPreferences("preferenciasMiApp", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("preferenciasGuardadas", true);
        editor.putString("nombreUsuario", userNameET.getText().toString());
        editor.putString("claveUsuario", passWordET.getText().toString());
        editor.putBoolean("check", chkRecordar.isChecked());
        editor.commit();
        Toast.makeText(this, "Usuario y clave almacenadas", Toast.LENGTH_SHORT).show();
    }
    private class AsyncCallWS extends AsyncTask<String, Void, Void> {
        @Override
        public Void doInBackground(String... params) {
            //Call Web Method
            //loginStatus = WebService.invokeLoginWS(editTextUsername,editTextPassword,"Validar");
            //usuarioStr = WebService.invokeLoginWS(editTextUsername, editTextPassword, "Validar");
            makeRequest(editTextUsername,editTextPassword);
            if(usuarioStr != null) {
                if (usuarioStr.length() > 100) {
                    WebActivity.RESPUESTA_LOGIN = usuarioStr;
                    WebActivity.USUARIO_LOGIN = new Usuario();
                    WebActivity.CLAVE = editTextPassword;
                    WebActivity.NOMBRE_USUARIO = editTextUsername;
                    //WebActivity.ES_CHECK = chkRecordar.isSelected();
                    WebActivity.ES_CHECK = esCheck;


                    try {
                        MainActivity.USUARIO_LOGIN = ParserJson.leerJson(usuarioStr);
                        WebActivity.USUARIO_LOGIN = MainActivity.USUARIO_LOGIN;
                        //WebActivity.ES_CHECK = MainActivity.
                        if (MainActivity.USUARIO_LOGIN != null && MainActivity.USUARIO_LOGIN.getId() > 0) {
                            //TODAVIA NO APLICAMOS NOTIFICACIONES
                            /*
                            notificacionesStr = WebService.invokeNotificaciones(String.valueOf(MainActivity.USUARIO_LOGIN.getId()), "ObtenerNotificaciones");
                            if (notificacionesStr != null && notificacionesStr.length() > 0)
                            {
                                MainActivity.NOTIFICACIONES_JSON = ParserJson.leerJsonNot(notificacionesStr);
                                if (MainActivity.NOTIFICACIONES_JSON != null && MainActivity.NOTIFICACIONES_JSON.getId() > 0)
                                {
                                    GenerarNotificaciones(MainActivity.NOTIFICACIONES_JSON);
                                }
                            }
                            */
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }
            return null;
        }

        @Override
        //Once WebService returns response
        protected void onPostExecute(Void result) {
            //Make Progress Bar invisible
            webservicePG.setVisibility(View.INVISIBLE);
            //acá se debe abrir la pagina android incrustada CAMBIAR ESTO
            Intent intObj = new Intent(MainActivity.this, WebActivity.class);
            //startActivity(intObj);
            //Error status is false
            if (!errored) {
                //Based on Boolean value returned from WebService
                if (usuarioStr.length() > 100) {
                    //Navigate to Home Screen
                    //OJO DESCOMENTAR DESPUES
                    WebActivity.RESPUESTA_LOGIN = usuarioStr;
                    startActivity(intObj);

                } else {
                    //Set Error message
                    statusTV.setText("Error en el Login.");
                }
                //Error status is true
            } else {
                //el servidor no responde
                statusTV.setText("El Servidor no está disponible, intente más tarde.");
            }
            //Re-initialize Error Status to False
            errored = false;
        }

        @Override
        //Make Progress Bar visible
        protected void onPreExecute() {
            webservicePG.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }

    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void GenerarNotificaciones(Notificaciones notificaciones) throws JSONException {
        boolean mostrar = false;
        int notificationID = 0;
        if (notificaciones.getId() > 0){
            mostrar = true;
            notificationID = notificaciones.getId();
        }
        Context context = getApplicationContext();
        //Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(myBlog));
        Intent myIntent = new Intent(MainActivity.this, WebActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                MainActivity.this,
                0,
                myIntent,
                Intent.FLAG_ACTIVITY_NEW_TASK);
        //CONSTRUIR LA NOTIFICACIÓN
        Notification.Builder mBuilder = new Notification.Builder(MainActivity.this);
        mBuilder.setSmallIcon(R.drawable.logomisalud);
        mBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.logomisalud));

        mBuilder.setContentIntent(pendingIntent);

        mBuilder.setTicker(Html.fromHtml("<strong>Mi Familia</strong>"));
        mBuilder.setContentTitle(Html.fromHtml("<strong>Mi Familia</strong>"));
        //mBuilder.setContentTitle(notificaciones.getNombre());
        mBuilder.setContentText(notificaciones.getNombre());

        //mBuilder.setSubText("Existen movimientos nuevos en Mi Familia.");

        Notification.InboxStyle inboxStyle = new Notification.InboxStyle(mBuilder);
        inboxStyle.setBigContentTitle(Html.fromHtml("<strong>Eventos</strong>"));
        inboxStyle.setSummaryText("MÁS...");
        if (notificaciones != null && notificaciones.getLineas() != null && notificaciones.getLineas().length() > 0)
        {
            for (int i=0; i< notificaciones.getLineas().length(); i++)
            {
                String ele = notificaciones.getLineas().get(i).toString();
                inboxStyle.addLine(ele);
            }
        }
        //inboxStyle.addLine(Html.fromHtml(notificaciones.getDetalle()));
        mBuilder.setStyle(inboxStyle);
        mBuilder.setAutoCancel(true);
        //acá tomar el id de usuario recuperado en la llamada web y redirigirlo a la aplicación
        //tomar el USUARIO_LOGIN QUE DEBERIA TRAER EL ID DE USUARIO

            /*
            *
            *   Intent resultIntent = new Intent(this, ResultActivity.class);
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
                stackBuilder.addParentStack(ResultActivity.class);

                // Adds the Intent that starts the Activity to the top of the stack
                stackBuilder.addNextIntent(resultIntent);
                PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
                mBuilder.setContentIntent(resultPendingIntent);
            *
            * */
        //ENVIAR LA NOTIFICACION
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // notificationID allows you to update the notification later on.
        if (mostrar && MainActivity.USUARIO_LOGIN != null && MainActivity.USUARIO_LOGIN.getId() > 0)
            //mNotificationManager.notify(notificationID, mBuilder.build());
            mNotificationManager.notify(notificationID, mBuilder.build());
    }

    //voley
    public void addToQueue(Request request) {
        if (request != null) {
            request.setTag(this);
            if (fRequestQueue == null)
                fRequestQueue = volley.getRequestQueue();
            request.setRetryPolicy(new DefaultRetryPolicy(
                    60000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            ));
            onPreStartConnection();
            fRequestQueue.add(request);
        }
    }
    public void onPreStartConnection() {
        MainActivity.this.setProgressBarIndeterminateVisibility(true);
    }

    public void onConnectionFinished() {
        MainActivity.this.setProgressBarIndeterminateVisibility(false);
    }

    public void onConnectionFailed(String error) {
        MainActivity.this.setProgressBarIndeterminateVisibility(false);
        Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
    }
    private void makeRequest(String usuario, String clave){

        String url = "http://vcoronado-001-site7.dtempurl.com/consultausuario.ashx?usuario=" + usuario + "&clave=" + clave;
        JsonArrayRequest request = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {

                JSONArray array = jsonArray;

                for(int i=1;i < array.length();i++) {

                    Gson gson = new Gson();
                    Devnews contact = null;
                    try {
                        contact = gson.fromJson(String.valueOf(array.getJSONObject(i)), Devnews.class);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    //mTvResult.append(contact.getUrl() + " "+contact.getScore() + " " + contact.getSource()+ " "+ contact.getTitle());
                    statusTV.setText(String.valueOf(contact.getId()));

                }

/*
                usuarioStr = jsonArray.toString();

                Toast.makeText(MainActivity.this, usuarioStr, Toast.LENGTH_LONG).show();
                statusTV.setText(String.valueOf(MainActivity.USUARIO_LOGIN.getId()));
*/
                onConnectionFinished();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                statusTV.setText(volleyError.toString());
                onConnectionFailed(volleyError.toString());

            }
        });
        addToQueue(request);
    }

}
