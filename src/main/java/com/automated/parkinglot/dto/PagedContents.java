package com.automated.parkinglot.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class PagedContents<T> {
    private int currentPageNumber;
    private int totalPages;
    private int totalElements;
    private List<T> contents;
}
