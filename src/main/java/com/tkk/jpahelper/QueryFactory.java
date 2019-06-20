package com.tkk.jpahelper;

import com.tkk.jpahelper.annoation.*;
import com.tkk.jpahelper.builder.QueryBuilder;
import com.tkk.jpahelper.builder.QueryItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Tkk
 */
@Slf4j
public class QueryFactory {

    /**
     *
     */
    @Data
    @AllArgsConstructor
    private static class QueryDesc {
        Field field;
        QueryConfig[] queryItem;
        boolean isAnd;
    }

    /**
     * 查询缓存
     */
    private static Map<Class, List<QueryDesc>> FIELD_CACHE = new ConcurrentHashMap<>();
    private static Map<Class, List<FetchConfig>> FETCH_CACHE = new ConcurrentHashMap<>();
    private static Map<Class, List<SortConfig>> SORT_CACHE = new ConcurrentHashMap<>();

    /**
     * @param object
     * @param
     * @return
     */
    public static <T> Specification<T> build(Object object) {
        Class clazz = object.getClass();
        QueryBuilder<T> queryBuilder = QueryBuilder
                .<T>builder()
                .fetchs(getFetch(clazz))
                .sorts(getSort(clazz))
                .build();

        //
        List<QueryDesc> queryDescMap = getQueryDescMap(clazz);
        for (QueryDesc desc : queryDescMap) {
            List<QueryItem> queryItems = toItems(desc, object);
            if (queryItems.isEmpty()) {
                break;
            } else if (desc.isAnd) {
                queryBuilder.and(queryItems);
            } else {
                queryBuilder.or(queryItems);
            }
        }
        return (root, criteriaQuery, cb) -> queryBuilder.toPredicate(root, criteriaQuery, cb);
    }

    /**
     * @param desc
     * @param object
     * @return
     */
    private static List<QueryItem> toItems(QueryDesc desc, Object object) {
        try {
            Object value = desc.field.get(object);
            return Stream
                    .of(desc.getQueryItem())
                    .map(queryItem -> {
                        String column = StringUtils.isBlank(queryItem.column()) ? desc.field.getName() : queryItem.column();
                        return new QueryItem(column, value, queryItem.type(), queryItem.join(), queryItem.nullable(), queryItem.blankable());
                    })
                    .collect(Collectors.toList());
        } catch (IllegalAccessException e) {
            log.error(String.format("字段[ %s ]获取数值错误 ==> [ %s ]", desc.field.getName(), e.getMessage()), e);
            throw new RuntimeException(e);
        }
    }

    /**
     * @param clazz
     * @return
     */
    private static List<FetchConfig> getFetch(Class clazz) {
        return FETCH_CACHE.computeIfAbsent(clazz, (c) -> {
            FetchConfigs fetchConfigs = (FetchConfigs) clazz.getAnnotation(FetchConfigs.class);
            if (fetchConfigs == null) {
                return Collections.emptyList();
            }
            return Arrays.asList(fetchConfigs.value());
        });
    }

    /**
     * @param clazz
     * @return
     */
    private static List<SortConfig> getSort(Class clazz) {
        return SORT_CACHE.computeIfAbsent(clazz, (c) -> {
            SortConfigs sortConfigs = (SortConfigs) clazz.getAnnotation(SortConfigs.class);
            if (sortConfigs == null) {
                return Collections.emptyList();
            }
            return Arrays.asList(sortConfigs.value());
        });
    }

    /**
     * @param clazz
     * @return
     */
    private static List<QueryDesc> getQueryDescMap(Class clazz) {
        return FIELD_CACHE.computeIfAbsent(clazz, c -> {
            List<QueryDesc> real = new ArrayList<>();
            ReflectionUtils.doWithFields(clazz, (field) -> {
                QueryDesc queryDec = getQueryDec(field);
                if (queryDec == null) {
                    return;
                }
                field.setAccessible(true);
                real.add(queryDec);
            });
            return real;
        });
    }

    /**
     * @param field
     * @return
     */
    private static QueryDesc getQueryDec(Field field) {
        QueryConfigs queryItems = field.getAnnotation(QueryConfigs.class);
        if (queryItems != null) {
            return new QueryDesc(field, queryItems.value(), queryItems.isAnd());
        }
        QueryConfig annotation = field.getAnnotation(QueryConfig.class);
        if (annotation == null) {
            return null;
        }
        return new QueryDesc(field, new QueryConfig[]{annotation}, true);
    }
}

