package org.example.handlers;

import org.example.domain.Comment;
import org.example.domain.CommentsVO;
import org.example.enums.HTTPEnum;
import org.example.repository.CommentRepository;
import org.example.utils.json.Bson;
import org.example.web.RequestHandler;
import org.example.web.request.HttpRequest;

import java.util.List;

public class CommentListHandler implements RequestHandler<Void> {
    CommentRepository commentRepository = new CommentRepository();
    @Override
    public Object doHandle(HttpRequest<Void> httpRequest) {
        return Bson.serializeToJson(new CommentsVO(commentRepository.list()));
    }

    @Override
    public String getUrl() {
        return "/comments";
    }

    @Override
    public String getType() {
        return HTTPEnum.POST.type;
    }
}
