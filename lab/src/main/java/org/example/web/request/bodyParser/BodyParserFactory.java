package org.example.web.request.bodyParser;

public class BodyParserFactory {
    private final JsonDecorator jsonDecorator = new JsonDecorator();
    private final FormDataDecorator formDataDecorator = new FormDataDecorator();
    private final FormUrlencodedDecorator formUrlencodedDecorator = new FormUrlencodedDecorator();

}
