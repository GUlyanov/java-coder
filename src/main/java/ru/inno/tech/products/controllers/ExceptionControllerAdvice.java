package ru.inno.tech.products.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.inno.tech.products.exceptions.*;
import ru.inno.tech.products.requests.CallInfo;
import ru.inno.tech.products.requests.ErrorResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ExceptionControllerAdvice {
    // 1.Проверка наличия значения у обязательного поля
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> exceptionMethodArgumentNotValidHandler(
            MethodArgumentNotValidException ex){
        String sMess = "Обязательные параметры не заполнены: \n";
        sMess += ex.getBindingResult().getAllErrors()
                        .stream()
                        .map((item)->((FieldError)item).getField())
                        .sorted()
                        .collect(Collectors.joining(", "));
                        //String errorMessage = item.getDefaultMessage();
        return ResponseEntity
                .status(400) // BAD REQUEST
                .body(sMess);
    }

    // 2.Код класса продукта не найден
    @ExceptionHandler(ProductClassByValueNoException.class)
    public ResponseEntity<String> excProductClassCodeNotFoundHandler(ProductClassByValueNoException ex){
        String sMess = "КодПродукта <%s> не найдено в Каталоге продуктов prodsch.TPP_REF_PRODUCT_CLASS";
        sMess = sMess.formatted(ex.getValue());
        return ResponseEntity
                .status(404)  // NOT FOUND
                .body(sMess);
    }

    // 3.Договор с таким номером уже существует
    @ExceptionHandler(ProductByNumberExException.class)
    public ResponseEntity<String> excProductByNumberExHandler(ProductByNumberExException ex){
        String sMess = "Параметр ContractNumber № договора <%s> уже существует для ЭП с ИД <%s>";
        sMess = sMess.formatted(ex.getNumber(), ex.getProduct().getId());
        return ResponseEntity
                .status(400)  // BAD REQUEST
                .body(sMess);
    }

    // 4.Код типа счета не найден в справочнике типов счетов AccountType
    @ExceptionHandler(AccTypeByValueNoException.class)
    public ResponseEntity<String> excAccTypeByValueNoHandler(AccTypeByValueNoException ex){
        String sMess = "КодТипаСчета <%s> не найдено в Справочнике типов счетов prodsch.TPP_REF_ACCOUNT_TYPE";
        sMess = sMess.formatted(ex.getValue());
        return ResponseEntity
                .status(404)  // NOT FOUND
                .body(sMess);
    }

    // 5.Допсоглашение с таким номером уже существует у текущего договора
    @ExceptionHandler(AgreementByNumberExException.class)
    public ResponseEntity<String> excAgreementByNumberExHandler(AgreementByNumberExException ex){
        String sMess = "Параметр № Дополнительного соглашения (сделки) Number <%s> уже существует для ЭП с ИД  <%s>";
        sMess = sMess.formatted(ex.getNumber(), ex.getProduct().getId());
        return ResponseEntity
                .status(400)  // BAD REQUEST
                .body(sMess);
    }

   //---------------- Создание ПР --------------------------------------
   // 6.Продуктовый регистр с таким типом уже имеется у договора
   @ExceptionHandler(ProdRegByProductAndRegTypeExException.class)
   public ResponseEntity<String> excProdRegByProductAndRegTypeHandler(ProdRegByProductAndRegTypeExException ex){
       String sMess = "Параметр registryTypeCode тип регистра <%s> уже существует для ЭП с ИД  <%s>";
       sMess = sMess.formatted(ex.getRegType().getValue(), ex.getProduct().getId());
       return ResponseEntity
               .status(400)  // BAD REQUEST
               .body(sMess);
   }

    // 7.Требуемый в запросе тип регистра не позволяется для класса продукта текущего договора
    @ExceptionHandler(ProdRegNotAllowForProdClassException.class)
    public ResponseEntity<String> excProdRegNotAllowForProdClassHandler(ProdRegNotAllowForProdClassException ex){
        String sMess = "КодПродукта <%s> не найдено в Каталоге продуктов <prodsch.TPP_REF_PRODUCT_CLASS> для данного типа Регистра <%s>";
        sMess = sMess.formatted(ex.getProduct().getProductClass().getValue(), ex.getRegType().getValue());
        return ResponseEntity
                .status(404)  // NOT FOUND
                .body(sMess);
    }

    // 8.Пул счетов не смог вернуть номер счета с заданными параметрами
    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<String> excAccountNotFoundHandler(AccountNotFoundException ex){
        String sMess = "Пул счетов не смог вернуть номер счета с заданными параметрами: ";
        sMess = sMess + "branchCode=<%s>, currencyCode=<%s>, mdmCode=<%s>, proority=<%s>, regType=<%s>";
        sMess = sMess.formatted(ex.getBranchCode(), ex.getCurrencyCode(), ex.getMdmCode(), ex.getPriority(), ex.getRegistryTypeCode());
        return ResponseEntity
                .status(404)  // NOT FOUND
                .body(sMess);
    }

    // 8.Договор с заданным instanceId не найден в базе
    @ExceptionHandler(ProductByIdNoException.class)
    public ResponseEntity<String> excProductNotFoundHandler(ProductByIdNoException ex){
        String sMess = "Договор с instanceId = <%s> не найден в базе";
        sMess = sMess.formatted(ex.getId());
        return ResponseEntity
                .status(404)  // NOT FOUND
                .body(sMess);
    }

    // 9.Тип продуктового регистра с заданным кодом не существует
    @ExceptionHandler(ProdRegTypeByValueNoException.class)
    public ResponseEntity<String> excProdRegTypeByValueNoHandler(ProdRegTypeByValueNoException ex){
        String sMess = "Тип продуктового регистра с кодом <%s> не существует";
        sMess = sMess.formatted(ex.getValue());
        return ResponseEntity
                .status(404)  // NOT FOUND
                .body(sMess);
    }

    // 10.ПРОЧИЕ ИСКЛЮЧЕНИЯ - статус 500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseBody> exceptionHandler(
            RuntimeException ex){
        //LOGGER.error(ex.getMessage() + "\n Стек вызовов: \n" + getStack(), ex);
        return ResponseEntity
                .status(500) // SERVER ERROR
                .body(getErrRespBody(ex.getMessage()));
    }

    public ErrorResponseBody getErrRespBody(String mess){
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        List<CallInfo> lst = new ArrayList<>();
        for(StackTraceElement elem : stackTraceElements) {
            CallInfo callInfo = new CallInfo(elem.getClassName(), elem.getMethodName(), elem.getLineNumber());
            lst.add(callInfo);
        }
        return new ErrorResponseBody(mess, lst);
    }
}

