package com.learn.androidrestapivolley;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class AddRoleActivity extends AppCompatActivity {
    EditText roleNames, description;
    Button btnAddRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_role);

        roleNames = findViewById(R.id.roleNames);
        description = findViewById(R.id.descriptions);
        btnAddRole = findViewById(R.id.buttonRoleAdd);

        btnAddRole.setOnClickListener(view -> {
            String url = "https://demo.resthapi.com/group";
            StringRequest myRequest = new StringRequest(Request.Method.POST, url,
                    response -> {
                        Toast.makeText(AddRoleActivity.this, "Successfully Add Role", Toast.LENGTH_SHORT).show();
                        Intent a = new Intent(AddRoleActivity.this, MainActivity.class);
                        startActivity(a);
                        finish();
                    }, volleyError -> Toast.makeText(AddRoleActivity.this, volleyError.getMessage(), Toast.LENGTH_SHORT).show()) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("name", roleNames.getText().toString());
                    params.put("description", description.getText().toString());
                    return params;
                }

                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("content-type", "application/json");
                    headers.put("Content-Type", "application/x-www-form-urlencoded");
                    return headers;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(myRequest);
        });
    }
}