package com.kakaobank.searchblog.dto;

import lombok.Data;

@Data
public class Documents {
    String blogname;
    String contents;
    String datetime;
    String thumbnail;
    String title;
    String url;
}
