package org.example.handlers;

import org.example.domain.Comment;
import org.example.domain.Response;
import org.example.enums.HTTPEnum;
import org.example.repository.CommentRepository;
import org.example.request.CommentDTO;
import org.example.utils.json.Bson;
import org.example.web.RequestHandler;
import org.example.web.biew.Biew;
import org.example.web.request.HttpRequest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommentSubmitHandler implements RequestHandler<Void> {
    private final CommentRepository commentRepository = new CommentRepository();
    @Override
    public Object doHandle(HttpRequest<Void> httpRequest) {
        String comment = httpRequest.getFormData().getString("comment");
        Comment newComment = new Comment(comment);
        commentRepository.addComment(newComment);
        return new Biew("frontPage.html");
    }

    @Override
    public String getUrl() {
        return "/comment";
    }

    @Override
    public String getType() {
        return HTTPEnum.POST.type;
    }
}
