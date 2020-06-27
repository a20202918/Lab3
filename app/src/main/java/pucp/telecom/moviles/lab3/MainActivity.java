package pucp.telecom.moviles.lab3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;

import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import pucp.telecom.moviles.lab3.Fragments.DialogFragmentEjemplo;
import pucp.telecom.moviles.lab3.Fragments.DialogFragmentEjemplo2;
import pucp.telecom.moviles.lab3.jsonmanage.JsonLocal;

public class MainActivity<StringRequest, Gson> extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void guardarLocalFragmento(View view){
        DialogFragmentEjemplo dialogFragmentEjemplo = new DialogFragmentEjemplo();
        dialogFragmentEjemplo.show(getSupportFragmentManager(), "guardar local");
    }

    public void guardarRemotoFragmento(View view){
        DialogFragmentEjemplo2 dialogFragmentEjemplo2 = new DialogFragmentEjemplo2();
        dialogFragmentEjemplo2.show(getSupportFragmentManager(), "guardar remoto");
    }

    public static void leerConfirmacion(boolean confirmacion){
        if (confirmacion){
            leerPermisos();
        }
    }

    private int codigoPermisosWriteReadSD = 1;

    public void leerPermisos() {

            int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permission == PackageManager.PERMISSION_GRANTED) {
                Log.d("infoApp", "Sí permisos");
                guardarComoTexto(//agregar JSON);
            } else {
                Log.d("infoApp", "No permisos");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        codigoPermisosWriteReadSD);
            }

    }


    public void guardarComoTexto(JsonLocal prueba){

        String fileName = "listaTrabajosJson";

        try (FileOutputStream fileOutputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
             FileWriter fileWriter = new FileWriter(fileOutputStream.getFD());){

            Gson gson = new Gson();
            String listaComoJson = gson.toJson(prueba);
            fileWriter.write(listaComoJson);

            Log.d("infoApp", "guardado exitoso");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void guardarRemoto(boolean confirmacion){

        if(confirmacion){

            String url2 = "http://ec2-34-234-229-191.compute-1.amazonaws.com:5000/saveData";

            StringRequest stringRequest2 = new StringRequest(StringRequest.Method.POST, url2,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response2) {

                            //Insertar JSON a subir

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("errorVol", error.getMessage());
                        }
                    }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> cabecera = new HashMap<>();
                    cabecera.put("X-Api-Token", "b7pKqop1qhOTOyixkj1ESmDbfsgi4Y");
                    cabecera.put("Content-Type", "application/json");
                    return cabecera;
                }
            };

            RequestQueue requestQueue2 = Volley.newRequestQueue(MainActivity.this);
            requestQueue2.add(stringRequest2);
        }

    }

    //Actividad que se utilizará para cuando s presioné el botón Iniciar Medición
    public void iniciarMedidicionBtn(View view){
        MedicionViewModel medicionViewModel =
                new ViewModelProvider(this).get(MedicionViewModel.class);
        medicionViewModel.iniciarMedicion();
    }


    //INICIO DE FUNCIONES PARA CREAR HILOS
    public class MedicionViewModel extends ViewModel{
        private MutableLiveData<Double> decibeles = new MutableLiveData<>();
        private Thread thread = null;
    }

    /* Inicio de la función para grabar*/


    String LOG_TAG = "AudioRecordTest";
    final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    String fileName = null;


    private MediaRecorder recorder = null;




    // Requesting permission to RECORD_AUDIO
    boolean permissionToRecordAccepted = false;
    String [] permissions = {Manifest.permission.RECORD_AUDIO};

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted ) finish();
    }

    public void startRecording(View view) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Toast IniciarGrabacion =
                        Toast.makeText(getApplicationContext(),
                                "SE HA INICIADO LA MEDICIÓN", Toast.LENGTH_SHORT);
                IniciarGrabacion.show();

                recorder = new MediaRecorder();
                recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                recorder.setOutputFile(fileName);
                recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                int Amplitud = recorder.getMaxAmplitude();
                double dB = 10 * Math.log10(Amplitud / 3000);

                Log.d("decibeles", "Nivel de ruido actual: " + dB + " dB");


                try {
                    recorder.prepare();
                } catch (IOException e) {
                    Log.e(LOG_TAG, "prepare() failed");
                }

                recorder.start();

            }
        });
       thread.start();
    }

    public void stopRecordingBtn(View view) {
        recorder.stop();
        recorder.release();
        recorder = null;
        MedicionViewModel medicionViewModel =
                new ViewModelProvider(this).get(MedicionViewModel.class);
        medicionViewModel.getThread().interrupt();
    }
}


