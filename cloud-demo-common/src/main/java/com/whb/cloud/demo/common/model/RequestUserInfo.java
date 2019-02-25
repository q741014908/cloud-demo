package com.whb.cloud.demo.common.model;

import lombok.Data;
import lombok.ToString;


@Data
@ToString
public class RequestUserInfo {
    private String name;
    private String rols;
}