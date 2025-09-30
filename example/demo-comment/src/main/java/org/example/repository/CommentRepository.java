package org.example.repository;

import org.example.domain.Comment;

import java.util.ArrayList;
import java.util.List;

public class CommentRepository {
    static List<Comment> comments = new ArrayList<>();
    public void addComment(Comment comment){
        comments.add(comment);
    }
    public List<Comment> list(){
        return new ArrayList<>(comments);
    }
}
