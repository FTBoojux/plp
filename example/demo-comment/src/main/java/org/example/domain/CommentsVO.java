package org.example.domain;

import java.util.ArrayList;
import java.util.List;

public class CommentsVO {
    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public CommentsVO(List<Comment> comments) {
        this.comments = new ArrayList<>(comments);
    }

    private List<Comment> comments;
}
