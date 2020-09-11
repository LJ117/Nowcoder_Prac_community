package com.nowcoder.community.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;



//@Component
//@Aspect
public class AlphaAspect {

    // execution 固定关键字
    // 第一个 *：返回值类型 ；
    // com.nowcoder.community.service : 包名
    // 第二个 *：service 里面的类， ；
    // 第三个 *：所有的方法；
    // (..) : 所有的参数
    @Pointcut("execution(* com.nowcoder.community.service.*.*(..))")
    public void pointcut(){

    }

    // 下面是通知的五个位置: 前，后，返回后，抛异常后，环绕
    // @位置（连接点（））
    @Before("pointcut()")
    public void before(){
        System.out.println("before");
    }

    @After("pointcut()")
    public void after(){
        System.out.println("after");
    }

    @AfterReturning("pointcut()")
    public void afterReturning(){
        System.out.println("afterReturning");
    }

    @AfterThrowing("pointcut()")
    public void afterThrowing(){
        System.out.println("afterThrowing");
    }

    // 连接点前后都要通知，有返回类型，有参数【连接点类型】
    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable{
        System.out.println("around before");

        // 这句话是原始对象【接入点】
        Object obj = joinPoint.proceed();

        System.out.println("around after");
        return obj;
    }



}
