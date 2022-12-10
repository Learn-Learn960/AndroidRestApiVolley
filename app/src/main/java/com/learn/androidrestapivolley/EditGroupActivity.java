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

public class EditGroupActivity extends AppCompatActivity {
    EditText name, description;
    Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_group);

        name = findViewById(R.id.groupName);
        description = findViewById(R.id.etDescription);
        save = findViewById(R.id.btnSave);

        name.setText(getIntent().getStringExtra("name"));
        description.setText(getIntent().getStringExtra("description"));

        save.setOnClickListener(view -> {
            String url = "https://demo.resthapi.com/group/" + getIntent().getStringExtra("id");
            StringRequest myRequest = new StringRequest(Request.Method.PUT, url,
                    response -> {
                        Toast.makeText(EditGroupActivity.this, "Successfully Edit Group", Toast.LENGTH_SHORT).show();
                        Intent a = new Intent(EditGroupActivity.this, MainActivity.class);
                        startActivity(a);
                        finish();
                    }, volleyError -> Toast.makeText(EditGroupActivity.this, volleyError.getMessage(), Toast.LENGTH_SHORT).show()) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("_id", getIntent().getStringExtra("id"));
                    params.put("name", name.getText().toString());
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