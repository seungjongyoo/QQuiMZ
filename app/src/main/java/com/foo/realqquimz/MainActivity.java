package com.foo.realqquimz;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<User> allUsers = new ArrayList<>(); // 전체 사용자 데이터
    private List<User> friendsList = new ArrayList<>(); // 친구 데이터
    private User loggedInUser; // 로그인된 사용자
    private FriendAdapter adapter; // RecyclerView 어댑터

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // UI 초기화
        Button btnAddFriend = findViewById(R.id.btn_add_friend);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        Switch toggleSwitch = findViewById(R.id.toggle_switch);

        // JSON 데이터 로드
        loadJsonData();

        // RecyclerView 설정
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FriendAdapter(new ArrayList<>(allUsers), this); // 초기 데이터는 전체 사용자
        recyclerView.setAdapter(adapter);

        // 토글 스위치 활성화/비활성화 처리
        toggleSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // 친구 랭킹만 표시
                adapter.setData(new ArrayList<>(friendsList));
            } else {
                // 전체 랭킹 표시
                adapter.setData(new ArrayList<>(allUsers));
            }
        });

        // 친구 추가 버튼 클릭 이벤트
        btnAddFriend.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddFriendActivity.class);
            startActivity(intent);
        });
    }

    private void loadJsonData() {
        try {
            // assets에서 JSON 파일 로드
            InputStream inputStream = getAssets().open("data.json");
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();
            String json = new String(buffer, "UTF-8");

            // JSON 데이터 파싱
            JSONArray jsonArray = new JSONArray(json);

            // 로그인된 사용자 토큰 (예: "user_001")
            String loggedInUserToken = "user_001";

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject userObject = jsonArray.getJSONObject(i);
                String token = userObject.getString("token");
                int point = userObject.getInt("point");
                JSONArray isFriendArray = userObject.getJSONArray("isFriend");

                List<String> isFriend = new ArrayList<>();
                for (int j = 0; j < isFriendArray.length(); j++) {
                    isFriend.add(isFriendArray.getString(j));
                }

                String achievement = userObject.getString("achievement");
                User user = new User(token, point, isFriend, achievement);

                // 전체 사용자 목록에 추가
                allUsers.add(user);

                // 로그인된 사용자 확인
                if (token.equals(loggedInUserToken)) {
                    loggedInUser = user;

                    // 친구 목록 가져오기
                    for (String friendToken : isFriend) {
                        User friend = findUserByToken(jsonArray, friendToken);
                        if (friend != null) {
                            friendsList.add(friend);
                        }
                    }
                }
            }

            // 전체 사용자 및 친구 목록을 점수 기준으로 정렬
            Collections.sort(allUsers, Comparator.comparingInt(User::getPoint).reversed());
            Collections.sort(friendsList, Comparator.comparingInt(User::getPoint).reversed());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private User findUserByToken(JSONArray jsonArray, String token) {
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject userObject = jsonArray.getJSONObject(i);
                if (userObject.getString("token").equals(token)) {
                    String userToken = userObject.getString("token");
                    int point = userObject.getInt("point");
                    String achievement = userObject.getString("achievement");
                    return new User(userToken, point, null, achievement);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
