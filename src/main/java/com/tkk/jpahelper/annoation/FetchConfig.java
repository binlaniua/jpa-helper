package com.tkk.jpahelper.annoation;

import javax.persistence.criteria.JoinType;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Tkk on 2018/7/21.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface FetchConfig {

    /**
     * 数据库字段
     *
     * @return
     */
    String column() default "";


    /**
     * @return
     */
    JoinType join() default JoinType.INNER;
}
