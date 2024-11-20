package com.foo.realqquimz;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class AddFriendActivity extends AppCompatActivity {

    private List<User> allUsers = new ArrayList<>(); // 모든 사용자 데이터
    private List<User> searchResults = new ArrayList<>(); // 검색 결과 데이터
    private FriendAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        // ActionBar에서 뒤로가기 버튼 활성화
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Add Friend");
        }

        // 뒤로가기 버튼 클릭 처리
        toolbar.setNavigationOnClickListener(v -> finish());

        // UI 초기화
        EditText etSearch = findViewById(R.id.et_search);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);

        // JSON 데이터 로드
        loadJsonData();

        // RecyclerView 설정
        adapter = new FriendAdapter(searchResults, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // 엔터 키를 누르면 검색
        etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                String keyword = etSearch.getText().toString();
                searchFriends(keyword);
                return true;
            }
            return false;
        });
    }

    // JSON 데이터를 로드하여 allUsers 리스트에 추가
    private void loadJsonData() {
        try {
            // assets 폴더에서 JSON 파일 로드
            InputStream inputStream = getAssets().open("data.json");
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();
            String json = new String(buffer, "UTF-8");

            // JSON 데이터 파싱
            JSONArray jsonArray = new JSONArray(json);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject userObject = jsonArray.getJSONObject(i);
                String token = userObject.getString("token");
                int point = userObject.getInt("point");
                String achievement = userObject.getString("achievement");

                // User 객체 생성 후 리스트에 추가
                allUsers.add(new User(token, point, null, achievement));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 검색 기능 구현
    private void searchFriends(String keyword) {
        searchResults.clear();
        for (User user : allUsers) {
            if (user.getToken().toLowerCase().contains(keyword.toLowerCase())) {
                searchResults.add(user);
            }
        }
        adapter.notifyDataSetChanged(); // RecyclerView 갱신
    }
}
