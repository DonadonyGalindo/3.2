package com.example.donadonydonaji.a32;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    String id;


    JSONObject data = null;
    Button consultarid;
    TextView resultado;
    EditText ciudad,estado;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        consultarid = (Button) findViewById(R.id.consultarid);
        resultado = (TextView) findViewById(R.id.tresultado);
        ciudad = (EditText) findViewById(R.id.ciudad);
        estado = (EditText) findViewById(R.id.estado);



        consultarid.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),comprueba(ciudad.getText().toString()), Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(),comprueba(estado.getText().toString()), Toast.LENGTH_SHORT).show();
                new WSIdalumno().execute();
            }
        });

    }

    private class WSIdalumno extends AsyncTask<Void, Void, Void> {
        String cd=comprueba(ciudad.getText().toString());
        String es=comprueba(estado.getText().toString());


        String json_url;

        @Override
        protected Void doInBackground(Void... Voids) {



            json_url="http://api.wunderground.com/api/c5abe9887fb9069c/conditions/q/"+es+"/"+cd+".json";


            try {
                URL url = new URL(json_url);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                BufferedReader reader =
                        new BufferedReader(new InputStreamReader(connection.getInputStream()));

                StringBuffer json = new StringBuffer(1024);
                String tmp = "";

                while((tmp = reader.readLine()) != null)
                    json.append(tmp).append("\n");
                reader.close();

                data = new JSONObject(json.toString());



        } catch (Exception e) {

            System.out.println("Exception "+ e.getMessage());
            return null;
        }

                return null;


        }


        @Override
        protected void onPostExecute(Void Void) {

            if(data!=null){
                Log.d("my weather received",data.toString());
                try {
                    Log.d("-------->>>>",data.getString("current_observation")+"");
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                try {


                    JSONObject ab = new JSONObject(data.getString("current_observation"));
                    JSONObject na = new JSONObject(ab.getString("display_location"));

                    resultado.setText("Ciudad:" +na.getString("city")+", "+na.getString("state_name")+"\n"+
                            "Weather: "+ab.getString("weather")+"\nTemperature: "+ab.getString("temperature_string"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }

    public String comprueba(String cad){
        cad = cad.replace(" ","_");

        return cad;
    }
}
