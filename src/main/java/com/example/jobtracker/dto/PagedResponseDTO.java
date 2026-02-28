package com.example.jobtracker.dto;

import lombok.Data;

import java.util.List;

@Data
public class PagedResponseDTO<T> {
    private  List<T> content;
    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;
    private boolean hasNext;
    private boolean hasPrevious;

    public PagedResponseDTO(List<T> content, int pageNumber,
                            int pageSize,long totalElements,
                            int totalPages, boolean last, boolean hasNext, boolean hasPrevious){
        this.content = content;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.last = last;
        this.hasNext = hasNext;
        this.hasPrevious = hasPrevious;
    }

}
