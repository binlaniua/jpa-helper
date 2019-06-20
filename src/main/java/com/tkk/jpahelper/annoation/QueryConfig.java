package com.tkk.jpahelper.annoation;

import com.tkk.jpahelper.condition.QueryType;

import javax.persistence.criteria.JoinType;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Tkk on 2018/7/21.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface QueryConfig {

    /**
     * 数据库字段
     *
     * @return
     */
    String column() default "";


    /**
     * 查询条件
     *
     * @return
     */
    QueryType type() default QueryType.equal;

    /**
     * Join条件
     *
     * @return
     */
    JoinType join() default JoinType.INNER;

    /**
     * object是否可以为null
     *
     * @return
     */
    boolean nullable() default false;

    /**
     * 字符串是否可为空
     *
     * @return
     */
    boolean blankable() default false;
}
