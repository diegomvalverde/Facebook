package com.example.proyectoii;

public class Session {

    private static Profile profile;

    private static final Session ourInstance = new Session();

    public static Session getInstance() {
        return ourInstance;
    }

    private Session() {
    }

    public static Profile getProfile() {
        return profile;
    }

    public static void setProfile(Profile profile) {
        Session.profile = profile;
    }
}
