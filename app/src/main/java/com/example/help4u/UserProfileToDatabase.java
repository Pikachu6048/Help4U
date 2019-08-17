package com.example.help4u;

public class UserProfileToDatabase {

    private String id;
    private String email;
    private String name;
    private String phonenumber;
    private String photoUrl;
    private String quote;

    public UserProfileToDatabase() {
    }

    public UserProfileToDatabase(String id, String email, String name, String phonenumber, String photoUrl, String quote) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.phonenumber = phonenumber;
        this.photoUrl = photoUrl;
        this.quote = quote;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }
}
