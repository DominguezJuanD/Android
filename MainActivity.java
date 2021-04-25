package com.example.djd_d.baloopizza3;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    EditText etnombre, etpass;
    Button bt_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Toast.makeText(this, "Bienvenido", Toast.LENGTH_SHORT).show();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etnombre = findViewById(R.id.et_usuario);
        etpass = findViewById(R.id.et_pass);

        bt_login = findViewById(R.id.bt_login);

        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getApplicationContext(), "Cargando...", Toast.LENGTH_LONG).show();
                final String username = etnombre.getText().toString();
                final String password = etpass.getText().toString();


                Response.Listener<String> responselistener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) {
                                String usuario = jsonResponse.getString("usuario");
                                Bundle bundle = new Bundle();

                                Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                                bundle.putString("usuario", usuario);
                                intent.putExtras(bundle);
                                MainActivity.this.startActivity(intent);
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                builder.setMessage("Error Login")
                                        .setNegativeButton("Reintentar", null)
                                        .create().show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                LoginRequest loginRequest = new LoginRequest(username, password, responselistener);
                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                queue.add(loginRequest);
            }
        });
    }
}
