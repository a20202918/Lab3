package pucp.telecom.moviles.lab3.Fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;

import pucp.telecom.moviles.lab3.MainActivity;

public class DialogFragmentEjemplo extends androidx.fragment.app.DialogFragment {

    boolean confirmacion;
    public void devolverConfirmacion(boolean confirmacion){
        MainActivity mainActivity = (MainActivity) getActivity();
        MainActivity.leerConfirmacion(confirmacion);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity());
        builder.setMessage("¿Guardar información en el sistema de archivos local?")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        confirmacion = true;
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        confirmacion = false;
                    }
                });
        return builder.create();
    }
}