package com.leonardosilva.cotaodemoedas;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {
    private TextView textCode;
    private TextView textName;
    private TextView textHigh;
    private TextView textLow;
    private TextView textAsk;
    private TextView textPctChange;

    private EditText editMoeda;

    private Button btnMoeda;

    private static String TAG = "TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textCode = findViewById(R.id.textCode);
        textName = findViewById(R.id.textName);
        textHigh = findViewById(R.id.textHigh);
        textLow = findViewById(R.id.textLow);
        textAsk = findViewById(R.id.textAsk);
        textPctChange = findViewById(R.id.textPctChange);
        editMoeda = findViewById(R.id.editMoeda);
        btnMoeda = findViewById(R.id.btnMoeda);



        btnMoeda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editMoeda.equals("")) {
                    Toast.makeText(MainActivity.this, "Preencha o campo", Toast.LENGTH_SHORT).show();
                }else{

                    MyTask task = new MyTask();
                    String urlApi = "https://economia.awesomeapi.com.br/json/all/" + editMoeda.getText().toString();
                    task.execute(urlApi);
                }

            }
        });

    }


    class MyTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            String moeda = null;
            String codigoMoeda = null;
            String nomeMoeda = null;
            String altaMoeda = null;
            String minimaMoeda = null;
            String vendaMoeda = null;
            String pctVariacaoMoeda = null;

            try {
                JSONObject jsonObject = new JSONObject(s); // É passado como parametro o retorno do valor do doInBackground, que no caso é o resultado em Json
                moeda = jsonObject.getString(editMoeda.getText().toString()); // Aqui colocamos o que ele vai buscar dentro do json

                JSONObject jsonObjectReal = new JSONObject(moeda); // Foi passado como paramentro o resultado do que foi achado, dentro desse resultado dizemos o que queremos recuperar ou seja qual linha do json desejamos
                codigoMoeda = jsonObjectReal.getString("code"); // Dentro do getString colocamos igual o que está no json para ele recuperar o valor desejado
                nomeMoeda = jsonObjectReal.getString("name");
                altaMoeda = jsonObjectReal.getString("high");
                minimaMoeda = jsonObjectReal.getString("low");
                vendaMoeda = jsonObjectReal.getString("ask");
                pctVariacaoMoeda = jsonObjectReal.getString("pctChange");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            textCode.setText(codigoMoeda);
            textName.setText(nomeMoeda);
            textHigh.setText(altaMoeda);
            textLow.setText(minimaMoeda);
            textAsk.setText(vendaMoeda);
            textPctChange.setText(pctVariacaoMoeda);

        }

        @Override
        protected String doInBackground(String... strings) {
            BufferedReader reader = null;
            StringBuffer buffer = null;
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                InputStream inputStream = connection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                reader = new BufferedReader(inputStreamReader);

                buffer = new StringBuffer();

                String linha = "";
                while ((linha = reader.readLine()) != null) {
                    buffer.append(linha);
                }


            } catch (MalformedURLException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return buffer.toString();
        }

    }

}
