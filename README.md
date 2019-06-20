

<pre>
用注解来代替jpa查询
</pre>


## 实体

```java

// 实体

@Entity
class User {
    private Long id;    
    private String name;
    private String phone;
    private Date createTime;
    
    @OneToOne
    private Address address;
}



@Entity
class Address {
    private Long id;
    private String city;
}




```

## 存储

```java
interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {}
interface AddressRepository extends JpaRepository<Address, Long>, JpaSpecificationExecutor<Address> {} 
```

## 查询对象, 由前端或者手动构造数值

```java
@SortConfigs({
    @SortConfig(column = "createTime", desc = true)
})
@FetchConfig({
    @FetchConfig(column = "address", join = JoinType.INNER)
})
class Query {
    
    // 模糊查询姓名
    @QueryConfig(column = "name", type = QueryType.like)
    private String name;
    
    // 关联实体查询
    @QueryConfig(column = "address.city", type = QueryType.eq)
    private String city;
    
    // 模糊查询姓名或者手机号
    @QueryConfigs(value = {
        @QueryConfig(column= "name", type = QueryType.like),
        @QueryConfig(column= "phone", type = QueryType.like)
    }, isAnd = false)
    private String keyword;
}
```

## 查询

```java
class UserService {
    private UserRepository userRepository;
    
    public List<User> query(Query query){
        List<User> userList = userRepository.findAll(QueryFactory.build(request);
        return user;
    }
    
    public <T> List<T> query(Query query, Converter<User, T> converter){
        List<T> userList = userRepository.findAll(QueryFactory.build(request))
                                            .stream()
                                            .map(converter)
                                            .collect(Collections.toList());
        return user;
    }
}
```