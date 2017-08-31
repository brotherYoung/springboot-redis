# springboot-redis
springboot使用redis作为缓存，期间遇到的问题和错误总结在这里。

springboot强大就强大在autoconfigure，我们可以在org.springframework.boot.autofigure.data.redis包中找到springboot自动帮我们完成的一些bean配置，重点有CacheManager，StringTemplate，RedisTemplate，所以此demo中并没有重写这些bean的实现，当然如果想自己定制这些bean的话，就得重写了。

NOTE:

+ 要缓存的 Java 对象必须实现 Serializable 接口，因为 Spring 会将对象先序列化再存入 Redis，比如本文中的 com.defonds.bdp.city.bean.City 类，如果不实现 Serializable 的话将会遇到类似这种错误：nested exception is java.lang.IllegalArgumentException: DefaultSerializer requires a Serializable payload but received an object of type [com.defonds.bdp.city.bean.City]]。
+ 缓存的生命周期我们可以配置，然后托管 Spring CacheManager，不要试图通过 redis-cli 命令行去管理缓存。
+ CacheManager 必须设置缓存过期时间，否则缓存对象将永不过期，这样做的原因如上，避免一些野数据“永久保存”。此外，设置缓存过期时间也有助于资源利用最大化，因为缓存里保留的永远是热点数据。
+ 缓存适用于读多写少的场合，查询时缓存命中率很低、写操作很频繁等场景不适宜用缓存。

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

