package space.yizhu.exception;/* Created by xiuxi on 2018.5.21.*/

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@CrossOrigin
@RestControllerAdvice
public class BootExceptionHandler {
    private static Logger LOGGER = LoggerFactory.getLogger(BootExceptionHandler.class);


//    @ExceptionHandler(Exception.class)
//    @ResponseBody
//    public String defaultExceptionHandler(HttpServletRequest req,Exception e){
////是返回的String.
//
////ModelAndView -- 介绍 模板引擎...?
//// ModelAndView mv = new ModelAndView();
//// mv.setViewName(viewName);
//
//        return "对不起，服务器繁忙，请稍后再试！";
//    }


    @ExceptionHandler
    public String processException(Exception ex, HttpServletRequest request, HttpServletResponse response) {

        if (ex instanceof MissingServletRequestParameterException) {
            return ex.toString();
        }
        if (ex instanceof NoPermissions) {

            LOGGER.error("=======" + ex.getMessage() + "=======");
            return "sorry，无权限！";

        }

        /**
         * 未知异常
         */
        LOGGER.error(ex.toString());
        return ex.getMessage();

    }
}
