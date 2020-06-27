package pucp.telecom.moviles.lab3.jsonmanage;

public class JsonLocal {

    int tiempo;
    float mediciones[];

    public JsonLocal(int tiempo, float[] mediciones) {
        this.tiempo = tiempo;
        this.mediciones = mediciones;
    }

    public int getTiempo() {
        return tiempo;
    }

    public float[] getMediciones() {
        return mediciones;
    }

    public void setTiempo(int tiempo) {
        this.tiempo = tiempo;
    }

    public void setMediciones(float[] mediciones) {
        this.mediciones = mediciones;
    }
}
