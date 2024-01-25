package ru.inno.tech.products.requests;

import lombok.*;

@Data @AllArgsConstructor
public class AdditionalProperty {
    private String key;
    private String value;
    private String name;
}
