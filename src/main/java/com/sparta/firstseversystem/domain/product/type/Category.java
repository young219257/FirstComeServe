package com.sparta.firstseversystem.domain.product.type;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Category {
    TOP("상의"),
    BOTTOM("하의"),
    OUTER("아우터"),
    HAT("모자"),
    ACC("악세사리");

    private final String description;
}
