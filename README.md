# springboot-redis
springboot使用redis作为缓存，期间遇到的问题和错误总结在这里。
- - -
1. java.lang.IllegalArgumentException: DefaultSerializer requires a Serializable payload but received an object of type [org.apache.catalina.connector.RequestFacade].

原因：demo为了简单起见，直接在Controller层使用了@Cacheable，方法中传入一个参数，参数作为Key无法被序列化。

    @RequestMapping("/GET/newsCategory")
    @Cacheable("newsCategory")
    public List<NewsCategory> getNewsCategory(HttpServletRequest httpServletRequest) {
        System.out.print("cache is coming");
        return sqlSession.selectList("getNewsCategory");
    }

我们知道缓存是由键值对存储的，而且必须经过序列化以后存储到缓存数据库中（如redis），那么键的生成策略是怎么样的呢？

键的生成策略有两种，一种是默认策略，一种是自定义策略。默认的key生成策略是通过KeyGenerator生成的，其默认策略如下：
+ 如果方法没有参数，则使用0作为key。
+ 如果只有一个参数的话则使用该参数作为key。
+ 如果参数多于一个的话则使用所有参数的hashCode作为key。

回到上面的代码中，不难发现我们方法使用了HttpServletRequest对象作为参数，只有这一个参数，所以使用该参数作为Key。存储该Key之前需要将其序列化，而spring-data-redis对Key默认使用String序列化，就会报参数错误，默认序列化需要的对象类型不符合。

解决办法：删除方法中的参数HttpServletRequest对象，使用0作为Key。最好的办法是乖乖在service或dao层使用@Cacheable注解吧。

