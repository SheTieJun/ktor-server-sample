Jedis是一个Java编写的Redis客户端，提供了一套完整的Redis API。以下是一些主要的API及其作用：

- `set(String key, String value)`: 将字符串值关联到指定的键。如果键已经持有其他值，那么这个命令会覆盖旧值。

- `get(String key)`: 返回与指定键相关联的字符串值。如果键不存在，那么返回特殊值nil。

- `exists(String key)`: 检查给定键是否存在。

- `del(String... keys)`: 删除给定的一个或多个键。不存在的键会被忽略。

- `expire(String key, int seconds)`: 为给定键设置过期时间。如果键过期，它会被自动删除。

- `ttl(String key)`: 以秒为单位，返回给定键的剩余生存时间。

- `incr(String key)`: 将键的整数值递增1。如果键不存在，那么在执行命令前会先将它的值设为0。

- `decr(String key)`: 将键的整数值递减1。如果键不存在，那么在执行命令前会先将它的值设为0。

- `hset(String key, String field, String value)`: 将哈希表key中的字段field的值设为value。

- `hget(String key, String field)`: 返回哈希表key中给定字段field的值。

- `hgetAll(String key)`: 返回哈希表key中，所有的字段和值。

- `rpush(String key, String... strings)`: 将一个或多个值插入到列表的尾部(最右边)。

- `lpush(String key, String... strings)`: 将一个或多个值插入到列表头部。

- `lpop(String key)`: 移除并返回列表的第一个元素。

- `rpop(String key)`: 移除并返回列表的最后一个元素。

- `sadd(String key, String... members)`: 将一个或多个成员元素加入到集合中。

- `smembers(String key)`: 返回集合中的所有成员。

- `sismember(String key, String member)`: 判断member元素是否是集合key的成员。


Jedis 是一个用于 Java 的流行的 Redis 客户端库，用于与 Redis 数据库进行交互。它提供了简单易用的 API，使得在 Java 应用程序中操作 Redis 数据库变得方便。

以下是一些 Jedis 的常见用法示例：

1. 连接到 Redis：

```java
import redis.clients.jedis.Jedis;

Jedis jedis = new Jedis("localhost", 6379);
jedis.auth("your-password"); // 使用您的实际密码进行身份验证
```

通过创建 Jedis 对象并指定 Redis 服务器的主机名和端口，您可以连接到 Redis 数据库。如果 Redis 服务器设置了密码，您需要使用 `auth` 方法进行身份验证。

2. 执行 Redis 命令：

```java
jedis.set("key", "value");
String value = jedis.get("key");
```

使用 Jedis 的 `set` 方法可以将键值对存储到 Redis 中，使用 `get` 方法可以获取键对应的值。Jedis 还提供了其他许多 Redis 命令的对应方法，如 `incr`、`hset`、`lpush` 等。

3. 使用管道（Pipeline）进行批量操作：

```java
Pipeline pipeline = jedis.pipelined();
pipeline.set("key1", "value1");
pipeline.get("key2");
Response<String> response = pipeline.get("key3");
pipeline.sync(); // 执行批量操作
String value1 = response.get();
```

通过使用 Jedis 的 `pipelined` 方法创建一个管道对象，您可以在管道中执行多个 Redis 命令，然后通过调用 `sync` 方法一次性执行这些命令。这样可以减少与 Redis 服务器之间的通信次数，提高效率。

4. 使用订阅与发布功能：

```java
JedisPubSub listener = new JedisPubSub() {
    @Override
    public void onMessage(String channel, String message) {
        System.out.println("Received message: " + message + " from channel: " + channel);
    }
};

jedis.subscribe(listener, "channel");
```

通过创建一个继承自 `JedisPubSub` 的监听器对象，并使用 `subscribe` 方法订阅一个频道，您可以接收到发布到该频道的消息。

这些只是 Jedis 的一些常见用法示例。Jedis 还提供了许多其他功能，如事务管理、连接池、Lua 脚本执行等。您可以根据您的需求和具体场景使用 Jedis 提供的功能来操作 Redis 数据库。请注意，Jedis 是一个阻塞式的 Redis 客户端，如果您需要非阻塞式的操作，可以考虑使用 Lettuce 或 Redisson 等其他库。