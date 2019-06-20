package com.tkk.jpahelper.condition.impl;

import com.tkk.jpahelper.builder.QueryItem;
import com.tkk.jpahelper.condition.QueryCondition;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import java.util.Date;

/**
 *
 */
public class GECondition extends QueryCondition {

    @Override
    protected Predicate innerToCondition(QueryItem item, CriteriaBuilder criteriaBuilder, Expression expression) {
        Object value = item.getValue();
        if (item.isDate()) {
            return criteriaBuilder.greaterThanOrEqualTo(expression, (Date) value);
        } else {
            return criteriaBuilder.ge(expression, (Number) value);
        }
    }
}
