package com.study.rabbitmq.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class Order implements Serializable {

    private static final long serialVersionUID = 1472256579123627184L;

    private int id;

    private String name;

    private String des;

    public Order() {}

    public Order(int id, String name, String des) {
        this.id = id;
        this.name = name;
        this.des = des;
    }
}
