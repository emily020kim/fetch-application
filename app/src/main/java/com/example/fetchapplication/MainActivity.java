package com.example.fetchapplication;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.fetchapplication.adapter.MobileDetailsAdapter;
import com.example.fetchapplication.model.GroupedMobileDetails;
import com.example.fetchapplication.model.MobileDetails;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Context context;
    private RequestQueue requestQueue;
    private StringRequest stringRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        init();
        requestJsonData();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void init() {
        recyclerView = findViewById(R.id.mobiles_rv);
        context = MainActivity.this;
    }

    public void requestJsonData() {
        Log.d("request", "called");
        requestQueue = Volley.newRequestQueue(context);
        stringRequest = new StringRequest(Request.Method.GET, "https://fetch-hiring.s3.amazonaws.com/hiring.json", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("response", response);
                    JSONArray jsonArray = new JSONArray(response);
                    fetchTheData(jsonArray);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showToast("API call error");
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return super.getHeaders();
            }
        };
        requestQueue.add(stringRequest);
    }

    private void fetchTheData(JSONArray jsonArray) {
        Map<Integer, Map<String, List<MobileDetails>>> groupedData = new HashMap<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject mobile = jsonArray.getJSONObject(i);
                String name = mobile.optString("name", "");
                if (name != null && !name.isEmpty() && !name.equals("null")) {
                    MobileDetails mobileDetails = new MobileDetails(
                            mobile.getInt("id"),
                            mobile.getInt("listId"),
                            name
                    );

                    int listId = mobileDetails.getListId();
                    String key = mobileDetails.getName();

                    if (!groupedData.containsKey(listId)) {
                        groupedData.put(listId, new HashMap<>());
                    }

                    if (!groupedData.get(listId).containsKey(key)) {
                        groupedData.get(listId).put(key, new ArrayList<>());
                    }

                    groupedData.get(listId).get(key).add(mobileDetails);
                }
            } catch (Exception e) {
                e.printStackTrace();
                showToast("Mobile Detail Error");
            }
        }

        List<MobileDetails> flatList = new ArrayList<>();
        for (Map<String, List<MobileDetails>> nameMap : groupedData.values()) {
            for (List<MobileDetails> list : nameMap.values()) {
                flatList.addAll(list);
            }
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        MobileDetailsAdapter adapter = new MobileDetailsAdapter(flatList, context);
        recyclerView.setAdapter(adapter);
    }

    public void showToast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
