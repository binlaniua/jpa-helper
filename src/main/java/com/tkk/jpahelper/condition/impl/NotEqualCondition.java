package com.tkk.jpahelper.condition.impl;

import com.tkk.jpahelper.builder.QueryItem;
import com.tkk.jpahelper.condition.QueryCondition;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;

/**
 *
 */
public class NotEqualCondition extends QueryCondition {

    @Override
    protected Predicate innerToCondition(QueryItem item, CriteriaBuilder criteriaBuilder, Expression expression) {
        return criteriaBuilder.notEqual(expression, item.getValue());
    }
}
