package com.tkk.jpahelper.condition;


import com.tkk.jpahelper.builder.QueryItem;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;

/**
 *
 */
@Slf4j
public class QueryConditionFactory {

    /**
     * @param item
     * @param criteriaBuilder
     * @param expression
     * @return
     */
    public static Predicate toCondition(QueryItem item, CriteriaBuilder criteriaBuilder, Expression expression) {
        return item.getType().getQueryCondition().toCondition(item, criteriaBuilder, expression);
    }
}
