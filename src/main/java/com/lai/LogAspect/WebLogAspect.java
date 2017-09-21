package com.lai.LogAspect;

import com.mongodb.BasicDBObject;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lailai on 2017/9/21.
 */
@Aspect
@Component
@Order(1)
public class WebLogAspect {
    private Logger logger= Logger.getLogger("mongodb");

    //定义切入点，com.lai.web包下面所有方法都是切入点，对所有请求
    @Pointcut("execution(public * com.lai.web..*.*(..))")
    public void webLog(){}

    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) throws Throwable{
        ServletRequestAttributes attributes= (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletRequest request=attributes.getRequest();
        //获取要记录的日志内容
        BasicDBObject basicDBObject=getBasicDBObject(request,joinPoint);
        logger.info(basicDBObject);
    }

    /**
     * 从HttpServletRequest和JoinPoint对象中获取请求信息，并组装成BasicDBObject
     * @param request
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    private BasicDBObject getBasicDBObject(HttpServletRequest request,JoinPoint joinPoint) throws Throwable{
        BasicDBObject r=new BasicDBObject();
        r.append("requestURL",request.getRequestURL().toString());
        r.append("requestURI",request.getRequestURI());
        r.append("queryString",request.getQueryString());
        r.append("remoteAddr",request.getRemoteAddr());
        r.append("remoteHost",request.getRemoteHost());
        r.append("remotePort",request.getRemotePort());
        r.append("localAddr",request.getLocalAddr());
        r.append("localName",request.getLocalName());
        r.append("method",request.getMethod());
        r.append("headers",getHeadersInfo(request));
        r.append("parameters",request.getParameterMap());
        r.append("className", joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        r.append("args", Arrays.toString(joinPoint.getArgs()));
        return r;
    }

    /**
     * 获取请求头信息
     * @param request
     * @return
     */
    private Map<String,String> getHeadersInfo(HttpServletRequest request){
        Map<String,String> map=new HashMap<>();
        Enumeration<String> headerNames=request.getHeaderNames();
        while (headerNames.hasMoreElements()){
            String key=(String )headerNames.nextElement();
            String value=request.getHeader(key);
            map.put(key,value);
        }
        return map;
    }
}
