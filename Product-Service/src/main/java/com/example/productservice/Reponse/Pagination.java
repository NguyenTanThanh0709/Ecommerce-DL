package com.example.productservice.Reponse;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Pagination {
    private int page;
    private int limit;
    private int page_size;
}