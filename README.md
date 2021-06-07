# JedisAPI
Redis Java API

***

### Gradle

```gradle
	dependencies {
	        implementation 'com.github.therealdamt:JedisAPI:1.0-SNAPSHOT'
	}

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

### Maven

```xml
	<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>

	<dependency>
	    <groupId>com.github.therealdamt</groupId>
	    <artifactId>JedisAPI</artifactId>
	    <version>1.0-SNAPSHOT</version>
	</dependency>
````

### How To Use

* Example Main Class
```java
    public static void main(String[] args) {
        RedisHandler redisHandler = new RedisHandler("127.0.0.1", "", "redis"
        , 6379, false);

        redisHandler.send(new TestPacket());
    }
```

* Example Packet Class
```java
public class TestPacket implements RedisPacket {

    @Override
    public void onReceived() {
        System.out.println("cool bro!");
    }

    @Override
    public void onSend() {
        System.out.println("sent");
    }
}
```

### Credits
Some of this project was inspired by the [old project](https://github.com/NotNV6/JedisAPI) of NoSequel / NV6 

* [Telegram](https://t.me/therealdamt)
* [Website](https://damt.xyz)
* Discord | damt#0886
