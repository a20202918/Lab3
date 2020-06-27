package pucp.telecom.moviles.lab3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }



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

        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(fileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        int Amplitud = recorder.getMaxAmplitude();
        double dB = 10*Math.log10(Amplitud/3000);
        

        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        recorder.start();
    }

    public void stopRecording(View view) {
        recorder.stop();
        recorder.release();
        recorder = null;
    }
}
