package org.example.domain;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Comment {
    private String comment;
    private String time;

    public Comment(String comment) {
        this.comment = comment;
        String dateString = DateTimeFormatter.ISO_INSTANT.format(Instant.now());
        this.time = dateString;
    }
}
