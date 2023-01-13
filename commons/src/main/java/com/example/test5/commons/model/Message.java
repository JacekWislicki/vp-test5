package com.example.test5.commons.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    private String field01;
    private String field02;
    private String field03;

    public Message(String id) {
        this.field01 = id;
        this.field02 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").format(new Date());
    }
}