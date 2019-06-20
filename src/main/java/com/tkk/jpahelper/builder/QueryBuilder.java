package com.tkk.jpahelper.builder;

import com.tkk.jpahelper.annoation.FetchConfig;
import com.tkk.jpahelper.annoation.SortConfig;
import com.tkk.jpahelper.condition.QueryConditionFactory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by Kun Tang on 2018/10/4.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueryBuilder<T> implements Specification<T> {

    @Data
    @AllArgsConstructor
    private static class QueryItems {
        private List<QueryItem> items;
        private boolean and;
    }

    private List<QueryItems> wheres = new ArrayList<>();
    private List<FetchConfig> fetchs = new ArrayList<>();
    private List<SortConfig> sorts = new ArrayList<>();

    public QueryBuilder and(List<QueryItem> items) {
        if (!items.isEmpty()) {
            this.wheres.add(new QueryItems(items, true));
        }
        return this;
    }

    public QueryBuilder or(List<QueryItem> items) {
        if (!items.isEmpty()) {
            this.wheres.add(new QueryItems(items, false));
        }
        return this;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new LinkedList<>();

        // fetch
        boolean isFetch = needFetch(criteriaQuery);
        if (isFetch) {
            fetchs.forEach(fetchConfig -> root.fetch(fetchConfig.column(), fetchConfig.join()));
        }

        // sort
        if (isFetch) {
            sorts.forEach(sortConfig -> {
                Expression expression = toExpression(root, new QueryItem(sortConfig.column()));
                if (sortConfig.desc()) {
                    criteriaQuery.orderBy(criteriaBuilder.desc(expression));
                } else {
                    criteriaQuery.orderBy(criteriaBuilder.asc(expression));
                }
            });
        }

        // 条件
        for (QueryItems where : wheres) {
            Predicate[] r = doWhere(where.getItems(), root, criteriaQuery, criteriaBuilder);
            if (r.length == 0) {
                continue;
            }
            if (where.and) {
                predicates.add(criteriaBuilder.and(r));
            } else {
                predicates.add(criteriaBuilder.or(r));
            }
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
    }

    /**
     * @param items
     * @param root
     * @param criteriaQuery
     * @param criteriaBuilder
     * @return
     */
    private Predicate[] doWhere(List<QueryItem> items, Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        List<Predicate> collect = items
                .stream()
                .map((item) -> doQueryItem(item, root, criteriaQuery, criteriaBuilder))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        return collect.toArray(new Predicate[collect.size()]);
    }

    /**
     * @param root
     * @param item
     * @return
     */
    private Expression toExpression(Root<T> root, QueryItem item) {
        String[] split = StringUtils.split(item.getColumn(), ".");
        Join join;
        switch (split.length) {
            case 1:
                return root.get(split[0]);
            default:
                join = root.join(split[0], item.join);
        }
        for (int i = 1; i < split.length - 1; i++) {
            String column = split[i];
            join = root.join(column, item.join);
        }
        return join.get(split[split.length - 1]);
    }

    /**
     * @param item
     * @param root
     * @param criteriaQuery
     * @param criteriaBuilder
     * @return
     */
    private Predicate doQueryItem(QueryItem item, Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        return QueryConditionFactory.toCondition(item, criteriaBuilder, toExpression(root, item));
    }

    /**
     * 是否能用fetch关联获取
     *
     * @param criteriaQuery
     * @return
     */
    public boolean needFetch(CriteriaQuery<?> criteriaQuery) {
        Class<?> resultType = criteriaQuery.getResultType();
        if (resultType.isAssignableFrom(Long.class)) {
            return false;
        }
        if (resultType.isAssignableFrom(Integer.class)) {
            return false;
        }
        if (resultType.isAssignableFrom(Boolean.class)) {
            return false;
        }
        if (resultType.isAssignableFrom(Double.class)) {
            return false;
        }
        return true;
    }
}
