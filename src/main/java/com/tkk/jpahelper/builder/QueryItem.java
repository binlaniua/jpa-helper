package com.tkk.jpahelper.builder;

import com.tkk.jpahelper.condition.QueryType;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.criteria.JoinType;
import java.util.Date;

/**
 * Created by Kun Tang on 2018/10/4.
 */
@Data
public class QueryItem {

    public QueryItem(String column) {
        this.column = column;
    }

    public QueryItem(String column, JoinType join) {
        this.column = column;
        this.join = join;
    }

    public QueryItem(String column, Object value) {
        this.column = column;
        this.value = value;
    }

    public QueryItem(String column, Object value, QueryType type) {
        this.column = column;
        this.value = value;
        this.type = type;
    }

    public QueryItem(String column, Object value, QueryType type, JoinType join, boolean nullAble, boolean blankAble) {
        this.column = column;
        this.value = value;
        this.type = type;
        this.join = join;
        this.nullAble = nullAble;
        this.blankAble = blankAble;
    }

    String column;

    Object value;

    QueryType type = QueryType.equal;

    JoinType join = JoinType.INNER;

    boolean nullAble = false;

    boolean blankAble = false;

    public boolean valid() {
        if (!isNullAble() && getValue() == null) {
            return false;
        }
        if (!isBlankAble() && StringUtils.isBlank(getValue().toString())) {
            return false;
        }
        return true;
    }

    public boolean isString(){
        return value.getClass().isAssignableFrom(String.class);
    }

    public boolean isDate(){
        return value.getClass().isAssignableFrom(Date.class);
    }
}
