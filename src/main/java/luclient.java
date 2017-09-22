import redis.clients.jedis.Jedis;
import java.util.Iterator;
import java.util.Set;

import redis.clients.jedis.JedisPubSub;

public class luclient {

    public static void main(String[] args)
    {
        System.out.println("This is lu redis client");

        // Connection

        Jedis jedis = new Jedis("172.16.240.128"); // redis server
        jedis.auth("lucky"); // password

        System.out.println("Send ping to redis server : "+jedis.ping());



        // key - value

        //jedis.set("luweb", "lucky521.github.io");
        System.out.println(jedis.get("luweb"));

        Set<String> keys = jedis.keys("*");
        Iterator<String> it = keys.iterator();

        while(it.hasNext())
        {
            String key = it.next();
            if (jedis.type(key).equals("string"))
                System.out.println(key + " -> " + jedis.get(key));
            else
                System.out.println(key + " is type" + jedis.type(key));
        }

        // publish and subcribe

        JedisPubSub lupubsub = new JedisPubSub() {
            @Override
            public void onMessage(String channel, String message) {
                super.onMessage(channel, message);
                System.out.println(channel + " " + message);
                if(message.equals("exit"))
                {
                    System.out.println("Exit " + channel);
                    this.unsubscribe();
                }
            }

            @Override
            public void onSubscribe(String channel, int subscribedChannels) {
                System.out.println("I am subscribing " + channel);
            }

        };

        jedis.subscribe(lupubsub, "luchatroom"); // blocking

        System.out.println("This is the end of the lu redis client");
    }
}
