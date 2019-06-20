package com.tkk.jpahelper.condition;

import com.tkk.jpahelper.condition.impl.*;
import lombok.Getter;

/**
 * Created by Tkk on 2018/7/21.
 */
@Getter
public enum QueryType {

    // 等于
    equal(new EqualCondition()),

    // 大于
    gt(new GTCondition()),

    // 大于等于
    ge(new GECondition()),

    // 小于
    lt(new LTCondition()),

    // 小于等于
    le(new LECondition()),

    // 不等于
    notEqual(new NotEqualCondition()),

    //
    like(new LikeCondition()),

    // 右%
    rightLike(new RightLikeCondition()),

    // 左
    leftLike(new LeftLikeCondition()),

    //
    notLike(new NotLikeCondition()),

    //
    between(new BetweenCondition()),

    //
    notNull(new NotNullCondition()),

    //
    in(new InCondition()),;

    private QueryCondition queryCondition;

    private QueryType(QueryCondition queryCondition) {
        this.queryCondition = queryCondition;
    }
}
