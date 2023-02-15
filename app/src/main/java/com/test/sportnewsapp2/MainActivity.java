package com.test.sportnewsapp2;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.test.sportnewsapp2.model.Articles;
import com.test.sportnewsapp2.model.Headlines;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private final String API_KEY = "8bad61c92479421e82b258a009106019";
    RecyclerView recyclerView;
    Adapter adapter;
    List<Articles> articles = new ArrayList<>();
    SwipeRefreshLayout swipeRefreshLayout;
    EditText editQuery;
    Button searchButton;

    public static final String CATEGORY = "sports";
    public static final String EMPTY = "";
    public final String COUNTRY = getCountry();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editQuery = findViewById(R.id.edit_query);
        searchButton = findViewById(R.id.search_button);

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        loadData(EMPTY);

        searchButton.setOnClickListener(v -> {
            if (editQuery.getText().toString().isEmpty()) {
                loadData(EMPTY);
            } else {
                loadData(editQuery.getText().toString());
            }
        });
    }

    private void loadData(String query) {
        swipeRefreshLayout.setOnRefreshListener(() -> MainActivity.this.retrieveJson(EMPTY));
        retrieveJson(query);
    }

    public void retrieveJson(String query) {
        swipeRefreshLayout.setRefreshing(true);
        Call<Headlines> call;
        if (editQuery.getText().toString().isEmpty()) {
            call = ApiClient
                    .getInstance()
                    .getApi()
                    .getHeadlines(COUNTRY, CATEGORY, API_KEY);
        } else {
            call = ApiClient
                   .getInstance()
                   .getApi()
                   .getSpecificData(query, API_KEY);
        }
        call.enqueue(new Callback<Headlines>() {
            @Override
            public void onResponse(Call<Headlines> call, Response<Headlines> response) {
                swipeRefreshLayout.setRefreshing(false);
                if (response.isSuccessful()) {
                    Headlines headlines = response.body();
                    if (headlines != null) {
                        articles.clear();
                        articles = headlines.getArticles();
                        adapter = new Adapter(MainActivity.this, articles);
                        recyclerView.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<Headlines> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(MainActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getCountry() {
        return Locale.getDefault().getCountry().toLowerCase(Locale.ROOT);
    }
}