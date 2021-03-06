package edu.bluejack21_2.guk.model;

import com.google.firebase.firestore.DocumentReference;

public class Comment {
    private String content, id;
    private DocumentReference user;


    public Comment(String content, DocumentReference user) {
        this.content = content;
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public DocumentReference getUser() {
        return user;
    }

    public void setUser(DocumentReference user) {
        this.user = user;
    }
}
