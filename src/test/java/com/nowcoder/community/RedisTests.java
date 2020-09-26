package com.nowcoder.community;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class RedisTests {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void testStrings() {
        String redisKey = "test:count";

        // String 类型的值
        redisTemplate.opsForValue().set(redisKey, 1);

        System.out.println(redisTemplate.opsForValue().get(redisKey));
        System.out.println(redisTemplate.opsForValue().increment(redisKey));
        System.out.println(redisTemplate.opsForValue().decrement(redisKey));
    }

    // 访问 hash
    @Test
    public void testHashes() {
        String redisKey = "test:user";
        redisTemplate.opsForHash().put(redisKey, "id", 1);
        redisTemplate.opsForHash().put(redisKey, "username", "张三");

        System.out.println(redisTemplate.opsForHash().get(redisKey, "id"));
        System.out.println(redisTemplate.opsForHash().get(redisKey, "username"));

    }

    // redis 列表访问
    @Test
    public void testLists() {
        // 左进列表
        String redisKey = "test:ids";

        // 放入数据
        redisTemplate.opsForList().leftPush(redisKey, 101);
        redisTemplate.opsForList().leftPush(redisKey, 102);
        redisTemplate.opsForList().leftPush(redisKey, 103);

        // 取出数据
        // 统计当前元素个数
        System.out.println(redisTemplate.opsForList().size(redisKey));
        // 获取指定位置的元素, 获取某个索引所对应的元素
        System.out.println(redisTemplate.opsForList().index(redisKey, 0));
        // 按照 索引范围获取元素
        System.out.println(redisTemplate.opsForList().range(redisKey, 0, 2));
        // 弹出元素, 左出
        System.out.println(redisTemplate.opsForList().leftPop(redisKey));
        System.out.println(redisTemplate.opsForList().leftPop(redisKey));
        System.out.println(redisTemplate.opsForList().leftPop(redisKey));
    }


    @Test
    public void testSets() {
        String redisKey = "test:teachers";

        redisTemplate.opsForSet().add(redisKey, "刘备", "关羽", "张飞", "赵云", "孔明");

        // 统计元素个数
        System.out.println(redisTemplate.opsForSet().size(redisKey));
        // 弹出一个数据, 随机弹出
        System.out.println(redisTemplate.opsForSet().pop(redisKey));
        // 统计现在集合中的数据都是什么, 展示现在集合中所有元素
        System.out.println(redisTemplate.opsForSet().members(redisKey));

    }

    @Test
    public void testSortedSets() {
        String redisKey = "test:students";

        // 添加 元素, 及其对应的分数
        redisTemplate.opsForZSet().add(redisKey, "唐僧", 80);
        redisTemplate.opsForZSet().add(redisKey, "悟空", 90);
        redisTemplate.opsForZSet().add(redisKey, "八戒", 50);
        redisTemplate.opsForZSet().add(redisKey, "沙僧", 70);
        redisTemplate.opsForZSet().add(redisKey, "龙马", 60);

        // 统计元素总数
        System.out.println(redisTemplate.opsForZSet().zCard(redisKey));
        // 查询某个元素的分数
        System.out.println(redisTemplate.opsForZSet().score(redisKey, "八戒"));
        // 查询某个元素的排名, 默认由小到大
        System.out.println(redisTemplate.opsForZSet().rank(redisKey, "八戒"));
        // 倒叙排名, 按分数由大到小
        System.out.println(redisTemplate.opsForZSet().reverseRank(redisKey, "八戒"));
        // 从小到大取前三
        System.out.println(redisTemplate.opsForZSet().range(redisKey, 0, 2));
        // 从大到小取前三
        System.out.println(redisTemplate.opsForZSet().reverseRange(redisKey, 0, 2));

    }

    // 访问公共类型 key
    @Test
    public void testKeys() {

        // 程序中一般不会用 keys * 这个命令, 这个命令一般是直接查询

        redisTemplate.delete("test:user");

        // 判断 key 是否存在
        System.out.println(redisTemplate.hasKey("test:user"));

        // 设置 key 的过期时间
        // TimeUnit. 设置时间 单位: 日, 时, 分, 秒, 毫秒...
        redisTemplate.expire("test:students", 10, TimeUnit.SECONDS);
    }

    // 多次访问同一个 key
    @Test
    public void testBoundOperations() {
        String redisKey = "test:count";

        // Bound XXX Operations: XXX : 你具体要访问的数据类型, 这里是String
        // redisTemplate.bound XXX Ops : 绑定具体的数据类型, 这里是String
        BoundValueOperations operations = redisTemplate.boundValueOps(redisKey);
        // 这样就是绑定了 key 不用每次都写了.
        operations.increment();
        operations.increment();
        operations.increment();
        operations.increment();
        operations.increment();
        System.out.println(operations.get());

    }

    // Redis 非关系型数据库, 不用严格遵守 ACID
    // 通常用编程式事务来控制

    // 编程式事务
    @Test
    public void testTransactional() {
        Object obj = redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String redisKey = "test:tx";
                // 启用事务
                operations.multi();

                operations.opsForSet().add(redisKey, "张三");
                operations.opsForSet().add(redisKey, "李四");
                operations.opsForSet().add(redisKey, "王五");

                // 事务过程是不能允许读的
                System.out.println(operations.opsForSet().members(redisKey));
                //operations.exec() : 提交事务
                return operations.exec();
            }
        });

        System.out.println(obj);
    }

    // 统计 20 万个重复数据的独立总数
    @Test
    public void testHyperLogLog() {
        String redisKey = "test:hll:01";

        for (int i = 1; i < 100000; i++) {
            redisTemplate.opsForHyperLogLog().add(redisKey, i);
        }

        for (int i = 1; i < 100000; i++) {
            int r = (int) (Math.random() * 10000 + 1);
            redisTemplate.opsForHyperLogLog().add(redisKey, r);
        }

        System.out.println(redisTemplate.opsForHyperLogLog().size(redisKey));
    }

    // 将 3 组数据合并, 再统计合并后总数据里的独立总数
    @Test
    public void testHyperLogLogUnion(){
        String redisKey2 = "test:hll:02";
        for (int i = 1; i < 10001; i++) {
            redisTemplate.opsForHyperLogLog().add(redisKey2,i);
        }
        String redisKey3 = "test:hll:03";
        for (int i = 5001; i < 15001; i++) {
            redisTemplate.opsForHyperLogLog().add(redisKey3,i);
        }
        String redisKey4 = "test:hll:04";
        for (int i = 10001; i < 20001; i++) {
            redisTemplate.opsForHyperLogLog().add(redisKey4,i);
        }

        String unionKey = "test:hll:union";
        redisTemplate.opsForHyperLogLog().union(unionKey,redisKey2,redisKey3,redisKey4);
        System.out.println(redisTemplate.opsForHyperLogLog().size(unionKey));

    }


    // 统计一组数据的布尔值
    @Test
    public void testBitmap(){
        String redisKey = "test:bmap:01";

        // 记录 [默认位置是 false]
        redisTemplate.opsForValue().setBit(redisKey,1,true);
        redisTemplate.opsForValue().setBit(redisKey,4,true);
        redisTemplate.opsForValue().setBit(redisKey,7,true);

        // 查询
        System.out.println(redisTemplate.opsForValue().getBit(redisKey, 0));
        System.out.println(redisTemplate.opsForValue().getBit(redisKey, 1));
        System.out.println(redisTemplate.opsForValue().getBit(redisKey, 2));

        // 统计 [ 通过 Redis 底层链接获取 ]
        Object obj = redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                // 统计数组中 1 的个数
                return connection.bitCount(redisKey.getBytes());
            }
        });

        System.out.println(obj);
    }


    // 统计三组数据的布尔值, 并对三组数据做 OR 运算 [ 或运算, 只有双 0 ---> 0]
    @Test
    public void testBitMapOperation(){
      String redisKey2 = "test:bmap:02";
      redisTemplate.opsForValue().setBit(redisKey2,0,true);
      redisTemplate.opsForValue().setBit(redisKey2,1,true);
      redisTemplate.opsForValue().setBit(redisKey2,2,true);

        String redisKey3 = "test:bmap:03";
        redisTemplate.opsForValue().setBit(redisKey3,2,true);
        redisTemplate.opsForValue().setBit(redisKey3,3,true);
        redisTemplate.opsForValue().setBit(redisKey3,4,true);

        String redisKey4 = "test:bmap:04";
        redisTemplate.opsForValue().setBit(redisKey4,4,true);
        redisTemplate.opsForValue().setBit(redisKey4,5,true);
        redisTemplate.opsForValue().setBit(redisKey4,6,true);

        String redisKey = "test:bmap:or";
        Object obj = redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                // 参数: 指定逻辑运算方式, 存储运算结果的 key,参与逻辑运算的 bit数组
                connection.bitOp(RedisStringCommands.BitOperation.OR,
                        redisKey.getBytes(),redisKey2.getBytes(),redisKey3.getBytes(),redisKey4.getBytes());
                return connection.bitCount(redisKey.getBytes());
            }
        });

        System.out.println(obj);
        for (int i = 0; i < 7; i++) {
            System.out.println(redisTemplate.opsForValue().getBit(redisKey, i));
        }

    }

}
