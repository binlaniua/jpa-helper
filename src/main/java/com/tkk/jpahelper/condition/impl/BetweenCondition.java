package com.tkk.jpahelper.condition.impl;

import com.tkk.jpahelper.builder.QueryItem;
import com.tkk.jpahelper.condition.QueryCondition;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import java.util.Date;

/**
 *
 */
@Slf4j
public class BetweenCondition extends QueryCondition {

    @Override
    protected Predicate innerToCondition(QueryItem item, CriteriaBuilder criteriaBuilder, Expression expression) {
        Date[] timeRange = (Date[]) item.getValue();
        if (timeRange.length == 2) {
            return criteriaBuilder.between(expression, timeRange[0], timeRange[1]);
        } else {
            if (log.isInfoEnabled()) {
                log.warn("betweenDate 需要两个日期, 但是只传入了 [ {} ]", item.getValue());
            }
            return null;
        }
    }
}
