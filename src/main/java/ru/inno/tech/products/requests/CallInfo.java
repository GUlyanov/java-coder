package ru.inno.tech.products.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor
public class CallInfo {
    String clas;
    String method;
    int nStr;
}
