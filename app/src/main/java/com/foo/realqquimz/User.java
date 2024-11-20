package com.foo.realqquimz;

import java.util.List;

public class User {
    private String token;
    private int point;
    private List<String> friends;
    private String achievement;

    // 기존 생성자
    public User(String token, int point, List<String> friends, String achievement) {
        this.token = token;
        this.point = point;
        this.friends = friends;
        this.achievement = achievement;
    }

    // 오버로딩된 생성자
    public User(String token, int point) {
        this.token = token;
        this.point = point;
        this.friends = null; // 기본값
        this.achievement = null; // 기본값
    }

    public String getToken() {
        return token;
    }

    public int getPoint() {
        return point;
    }

    public List<String> getFriends() {
        return friends;
    }

    public String getAchievement() {
        return achievement;
    }
}
