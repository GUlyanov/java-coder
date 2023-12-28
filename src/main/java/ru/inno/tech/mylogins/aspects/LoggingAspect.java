package ru.inno.tech.mylogins.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import java.lang.annotation.Annotation;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Aspect
@Component
public class LoggingAspect {
    @Around("execution(* ru.inno.tech.mylogins.validators.*.*(..))")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        Class cls = joinPoint.getSignature().getDeclaringType();
        String fileName = logFile(cls);
        String className = cls.getName();
        String methodName = joinPoint.getSignature().getName();
        className = className.substring(className.lastIndexOf(".")+1);
        LocalDateTime dt = LocalDateTime.now();
        String strArgs = "Аргументы: нет";
        String strTarget = "Возврат: нет";
        if (fileName != null) {
            Object[] arguments = joinPoint.getArgs();
            if (arguments != null && arguments.length != 0){
                strArgs = "Аргументы: [";
                for (int i = 0; i < arguments.length; i++) {
                    if (i>0) strArgs += ", " ;
                    strArgs += i + ":" + arguments[i].toString();
                }
                strArgs += "]";
            }

        }
        // вызов перехватываемого метода
        Object out = joinPoint.proceed();
        //
        if (fileName != null) {
            if (out != null) {
                String ss = out.toString();
                strTarget = "Возврат: " + ss.substring(ss.lastIndexOf(".") + 1);
            }
            className = className.substring(className.lastIndexOf(".")+1);
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");
            String str = dt.format(dtf) + " " + className + "." + methodName +
                         "\n                 " + strArgs + "\n                 " + strTarget;
            Path path = Path.of(fileName);
            if (path.toFile().exists() && path.toFile().length()>0) str="\n"+str;
            Files.writeString(path, str, StandardOpenOption.APPEND, StandardOpenOption.CREATE);
        }
        return out;
    }

    // класс имеет аннотацию @LogTransformation? Получить имя файла журнала
    public String logFile(Class clas) {
        Annotation annotation = clas.getAnnotation(LogTransformation.class);
        if (annotation == null) return null;
        LogTransformation anno = (LogTransformation) annotation;
        return anno.fileName();
    }
}
