package pucp.telecom.moviles.lab3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import pucp.telecom.moviles.lab3.Fragments.DialogFragmentEjemplo;
import pucp.telecom.moviles.lab3.Fragments.DialogFragmentEjemplo2;
import pucp.telecom.moviles.lab3.jsonmanage.JsonLocal;

public class MainActivity extends AppCompatActivity {

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

    private int codigoPermisosWriteReadSD = 1;

    public void leerPermisos(boolean confirmacion) {

        if (confirmacion) {
            int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permission == PackageManager.PERMISSION_GRANTED) {
                Log.d("infoApp", "Sí permisos");
                guardarComoTexto();
            } else {
                Log.d("infoApp", "No permisos");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        codigoPermisosWriteReadSD);
            }
        } else {

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
}