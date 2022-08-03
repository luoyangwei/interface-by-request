# Java接口定义的方式发送Http请求

当我们项目过度依赖其他外部数据源时，项目里就会出现大量的Http请求类或者方法。如果我们要做到请求只做请求的事情，
那么工具类过于单一导致业务大量耦合请求的代码，对后期维护带来更多的工作量，这时我们对Http请求的工具方式有了更高的要求。

## 1. RequestByInterface介绍

RequestByInterface使用接口定义的方式去请求Http，如果你用过Feign来做微服务的话， 那么一定对这种方式有了解。

```java
/**
 * @author luoyangwei by 2022-08-01 14:52 created
 */
@RequestMark(hostname = "http://xx.xxxx.xx/xx-xx")
public interface SearchDemoClient {

    /**
     * test load content
     *
     * @return string
     */
    @Request(url = "/hobby/production/search", requestMode = RequestMode.GET)
    String loadContents();

    @Post(url = "/hobby/user/search")
    @Headers(headers = {@Header(key = "Content-Type", val = "application/json")})
    SearchBaseResult<Object> loadUsers(@Body SearchCommandReq req);

}
```

我们从Demo可以看到一些列的注解，这些注解可以参考以下含义：

- `@RequestMark` 标识这个接口是一个Request接口
- `@Request` 标识这个抽象方法是单独的一个Http请求，也可以使用单独的@Get @Post @Put 之类的单独设置
- `@Post` 指定Post的请求方式
- `@Headers` 设置header，请求的时候可以带上
- `@Body` 标识这个对象是使用raw方式

这些基本已经可以满足简单的场景需求了。

## 2.实现方式和原理

RequestByInterface 是依赖于SpringBoot来构建的一套请求方式，实际的请求是用到了OkHttp包做底层的请求。
在SpringBoot启动的时候，RequestByInterface做了什么事情呢？

#### 2.1 RequestByInterface启动

我们知道SpringBoot提供了一系列拓展开发接口，其中就包括了我们这次用到的 `ImportBeanDefinitionRegistrar`，在SpringBoot
启动的时候我们需要把RequestByInterface的标记的接口找到然后注入到SpringBoot容器里，`ImportBeanDefinitionRegistrar`就可以做到这点。

```java
public class RequestClassScannerRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware, BeanFactoryAware {
    // 详细内容查看 项目里的 com.req.client.RequestClassScannerRegistrar
}
```

#### 2.2 RequestByInterface执行请求

以代码为例，这里我们使用了Spring的注解`@Autowired`，实际这里使用的并不是真实的SearchDemoClient类，其实是一个代理类，这里借鉴了Mybatis的Mapper方式，
当调用SearchDemoClient类的抽象方法时，实际执行的是代理方法。

```java
public class Tests {

    @Autowired
    private SearchDemoClient searchDemoClient;

}
```

## 3. RequestByInterface后续还需完善的功能

- 没有对微服务支持，无法使用中心化配置里的链接地址，目前只能写死。
- 会有泛型擦除的问题，因为底层对返回结果的转换是使用的fastjson实现的，fastjson在转换成对象的时候就有泛型擦除的问题。
- 注解的便捷性优化，目前是雏形所以对注解参数设置是没有太多的优化，并不是很人性化。

## 4. 写在最后

如果你想好的想法，或者想一起来完成这个项目，可以联系我微信 `lyw1821761` (添加请表明来意)
