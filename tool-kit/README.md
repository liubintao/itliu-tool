# 1. Overview
开发基础类库
综合众多开源类库的精华
参照 [vipshop](https://github.com/vipshop/vjtools)

一是对[Guava](https://github.com/google/guava) 与[Common Lang](https://github.com/apache/commons-lang)中最常用的API的提炼归类，避免了大家直面茫茫多的API(但有些工具类如Guava Cache还是建议直接使用，详见[直用三方工具类](docs/direct_3rd.md) )

二是对各门各派的精华的借鉴移植：比如一些大项目的附送基础库： [Netty](https://github.com/netty/netty/)，[ElasticSearch](https://github.com/elastic/elasticsearch)， 一些专业的基础库 ： [Jodd](https://github.com/oblac/jodd/), [commons-io](https://github.com/apache/commons-io), [commons-collections](https://github.com/apache/commons-collections)； 一些大厂的基础库：[Facebook JCommon](https://github.com/facebook/jcommon)，[twitter commons](https://github.com/twitter/commons)

# 2. Dependency
要求JDK 7.0及以上版本。

| Project | Version | Optional|
|--- | --- | --- |
|[Guava](https://github.com/google/guava) | 20.0 ||
|[Apache Common Lang](https://github.com/apache/commons-lang) | 3.8.1 ||
|[Slf4j](https://www.slf4j.org) | 1.7.25 ||
|[Dozer](http://dozermapper.github.io/) | 5.5.1 |Optional for BeanMapper，[选型](https://github.com/vipshop/vjtools/blob/master/vjkit/src/main/java/com/vip/vjtools/vjkit/reflect/BeanMapper.java#L11) |

如果使用Optional的依赖，请参考pom文件在业务项目自行引入
