package pucp.telecom.moviles.lab3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.accessibilityservice.AccessibilityService;
import android.location.Location;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

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

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



    }

    public void guardarRemoto (View view){
        obtenerUbicacion();
    }
    public void obtenerUbicacion() {
        int permiso =
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permiso == PackageManager.PERMISSION_GRANTED) {
            FusedLocationProviderClient fusedLocationProviderClient =
                    LocationServices.getFusedLocationProviderClient(this);

            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    String  latitud= String.valueOf(location.getLatitude());
                    String  longitud= String.valueOf(location.getLongitude());
                }
            });


            fusedLocationProviderClient.getLastLocation().addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

    }

/*
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Toast.makeText(this,"Si se tiene permisos de posición",Toast.LENGTH_SHORT).show();
                obtenerUbicacion();
            } else {
                //Toast.makeText(this,"No se tiene permisos de posición",Toast.LENGTH_SHORT).show();
            }
        }
    }*/

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

}
