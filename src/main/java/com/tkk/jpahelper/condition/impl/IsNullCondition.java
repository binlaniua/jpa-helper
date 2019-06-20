package com.tkk.jpahelper.condition.impl;

import com.tkk.jpahelper.builder.QueryItem;
import com.tkk.jpahelper.condition.QueryCondition;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import java.util.Objects;

/**
 *
 */
public class IsNullCondition extends QueryCondition {

    @Override
    protected Predicate innerToCondition(QueryItem item, CriteriaBuilder criteriaBuilder, Expression expression) {
        Object value = item.getValue();
        if (value instanceof Boolean && Boolean.valueOf(value.toString())) {
            return criteriaBuilder.isNull(expression);
        }
        //
        if (!Objects.isNull(value)) {
            return criteriaBuilder.isNull(expression);
        }
        return null;
    }
}
