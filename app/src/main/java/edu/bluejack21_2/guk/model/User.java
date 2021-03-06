package edu.bluejack21_2.guk.model;

import android.content.Context;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.Scope;

import java.util.HashMap;

import edu.bluejack21_2.guk.R;
import edu.bluejack21_2.guk.util.Crypt;

public class User {
    public static User CURRENT_USER = null;

    public static final String COLLECTION_NAME = "users";

    private static GoogleSignInClient googleSignInClient;

    private String id, email, name, password, address, phone, profilePicture;
    private String role = "";
    private boolean isDeleted = false;
    private int point;

    public User() {

    }

    public static GoogleSignInClient getGoogleClient(Context ctx){
        if(googleSignInClient == null){
            String client_webId = "798408453919-lcjvkil6547t34l2fk0av3d777mf98bd.apps.googleusercontent.com";
            googleSignInClient = GoogleSignIn.getClient(ctx, new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().requestScopes(new Scope(Scopes.DRIVE_APPFOLDER)).requestServerAuthCode(client_webId).requestIdToken(client_webId).build());
        }
        return googleSignInClient;
    }

    public User(String email, String name, String password, String address, String phone, String profilePicture, int point) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.address = address;
        this.phone = phone;
        this.profilePicture = profilePicture;
        this.point = point;
    }

    public HashMap<String, Object> toMap() {
        HashMap<String, Object> user = new HashMap<>();
        user.put("name", name);
        user.put("address", address);
        user.put("email", email);
        user.put("password", Crypt.hash(password));
        user.put("phone", phone);
        user.put("point", point);
        user.put("profilePicture", profilePicture);
        user.put("isDeleted", isDeleted);

        return user;
    }

    public static int getBadgeColor(int point){
        if(point < 500){
            return R.color.bronze;
        } else if(point < 1000){
            return R.color.silver;
        } else if(point < 3000){
            return R.color.primary;
        } else {
            return R.color.diamond;
        }
    }

    public boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }
}
