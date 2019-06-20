package com.tkk.jpahelper.condition.impl;

import com.tkk.jpahelper.builder.QueryItem;
import com.tkk.jpahelper.condition.QueryCondition;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import java.util.List;

/**
 *
 */
public class NotInCondition extends QueryCondition {

    @Override
    protected Predicate innerToCondition(QueryItem item, CriteriaBuilder criteriaBuilder, Expression expression) {
        List list = (List) item.getValue();
        if (list.isEmpty()) {
            return null;
        }
        CriteriaBuilder.In in = criteriaBuilder.in(expression);
        for (Object o : list) {
            in.value(o);
        }
        return criteriaBuilder.not(in);
    }
}
