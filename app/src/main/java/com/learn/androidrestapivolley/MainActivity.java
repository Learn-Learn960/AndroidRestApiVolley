package com.learn.androidrestapivolley;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.learn.androidrestapivolley.model.Role;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton btnAdd;
    RecyclerView rvRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = findViewById(R.id.btnAddRole);
        rvRole = findViewById(R.id.rvRole);

        getAllRole();

        btnAdd.setOnClickListener(view -> {
            Intent a = new Intent(MainActivity.this, AddRoleActivity.class);
            startActivity(a);
        });
    }

    public void getAllRole() {
        List<Role> roleList = new ArrayList<>();
        String url = "https://demo.resthapi.com/group";
        StringRequest myRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    JSONObject myJsonObject = null;
                    try {
                        myJsonObject = new JSONObject(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        for (int i = 0; i < Objects.requireNonNull(myJsonObject).getJSONArray("docs").length(); i++) {
                            Role role = new Role();
                            role.set_id(myJsonObject.getJSONArray("docs").getJSONObject(i).getString("_id"));
                            role.setName(myJsonObject.getJSONArray("docs").getJSONObject(i).getString("name"));
                            role.setDescription(myJsonObject.getJSONArray("docs").getJSONObject(i).getString("description"));
                            role.setCreatedAt(myJsonObject.getJSONArray("docs").getJSONObject(i).getString("createdAt"));
                            roleList.add(role);
                        }

                        RoleAdapter roleAdapter = new RoleAdapter(MainActivity.this, roleList);
                        RecyclerView.LayoutManager mLayout = new LinearLayoutManager(MainActivity.this);
                        rvRole.setLayoutManager(mLayout);
                        rvRole.setItemAnimator(new DefaultItemAnimator());
                        rvRole.setAdapter(roleAdapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, volleyError -> Toast.makeText(MainActivity.this, volleyError.getMessage(), Toast.LENGTH_SHORT).show());
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(myRequest);
    }
}

class RoleAdapter extends RecyclerView.Adapter<RoleAdapter.ViewHolder> {
    private final List<Role> roleList;
    Context context;

    public RoleAdapter(Context context, List<Role> roleList) {
        this.context = context;
        this.roleList = roleList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.role_adapter, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public int getItemCount() {
        return roleList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView description;
        CardView cardView;
        ImageButton btnEdit;
        ImageButton btnDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvName);
            description = itemView.findViewById(R.id.tvDescriotion);
            cardView = itemView.findViewById(R.id.cardView);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Role role = roleList.get(position);
        holder.name.setText(role.getName());
        holder.description.setText(role.getDescription());
        holder.btnEdit.setOnClickListener(view -> {
            Intent a = new Intent(context, EditGroupActivity.class);
            a.putExtra("id", role.get_id());
            a.putExtra("name", role.getName());
            a.putExtra("description", role.getDescription());
            context.startActivity(a);
        });

        holder.btnDelete.setOnClickListener(view -> {
            String url = "https://demo.resthapi.com/group/" + role.get_id();
            StringRequest myRequest = new StringRequest(Request.Method.DELETE, url,
                    response -> {
                        Toast.makeText(context, "Successfully Delete Group", Toast.LENGTH_SHORT).show();
                        ((MainActivity)context).getAllRole();
                    }, volleyError -> Toast.makeText(context, volleyError.getMessage(), Toast.LENGTH_SHORT).show()) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
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
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(myRequest);
        });
    }
}