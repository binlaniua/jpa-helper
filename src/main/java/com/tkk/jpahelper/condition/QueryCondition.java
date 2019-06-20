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
public abstract class QueryCondition {


    /**
     * @param item
     * @param criteriaBuilder
     * @param expression
     * @return
     */
    public Predicate toCondition(QueryItem item, CriteriaBuilder criteriaBuilder, Expression expression) {
        return innerToCondition(item, criteriaBuilder, expression);
    }

    protected abstract Predicate innerToCondition(QueryItem item, CriteriaBuilder criteriaBuilder, Expression expression);


}
