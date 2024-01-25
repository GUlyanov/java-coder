package ru.inno.tech.products.stubes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter @Getter @AllArgsConstructor @ToString
public class Client {
    public static String getClientIdByMdmCode(String mdmCode){ return mdmCode; }
    public static String getClientMdmCodeById(String id){
        return id;
    }
}
