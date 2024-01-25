package ru.inno.tech.products.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter @AllArgsConstructor
public class ErrorResponseBody {
    private String mess;
    private List<CallInfo> callStack;
    public void addCallInfo(CallInfo callInfo){
        callStack.add(callInfo);
    }
}
