# BasementLib

Plugin for the management of a complex network and created to simplify the work of developers who can use optimized and simplified functions for their plugins, based on a single core that will never be changed in order to centralize everything.

[![](https://jitpack.io/v/ServerBasement/BasementLib.svg)](https://jitpack.io/#ServerBasement/BasementLib)

## Authors

- [@ohAleee](https://github.com/ohAleee)

## Features

- MySQL library with custom query builder
- Advanced Redis support with Redisson and built in messages¹
- Server scoreboard adapter
- Simple YAML config library
- Add server dynamically to Velocity (only with BasementLib on velocity)¹ ²
- Shared servers¹
- Global player counter¹ ² (for servers and networks)
- Execute velocity methods directly from bukkit¹ ²

1 Velocity nedded, 2 Redis nedded
## Installation

Adding BasementLib to your project
The API artifact is published to the GitHub Packages Maven repository. You can add it to your project by adding the following to your build script.

### Gradle
If you're using Gradle, you need to add these lines to your build script.

### Groovy DSL:
```bash
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    # General API
    compileOnly 'it.ohalee.basementlib:api:1.0'
    
    # Bukkit API
    compileOnly 'it.ohalee.basementlib:api-bukkit:1.0'
    
    # Velocity API
    compileOnly 'it.ohalee.basementlib:api-velocity:1.0'
}
```

### Kotlin DSL:
```bash
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    # General API
    compileOnly("com.github.ServerBasement:BasementLib:api:1.0")
    
    # Bukkit API
    compileOnly("it.ohalee.basementlib:api-bukkit:1.0")
    
    # Velocity API
    compileOnly("it.ohalee.basementlib:api-velocity:1.0")
}
```

## Obtaining an instance of the API
The root API interface is BasementLib. You need to obtain an instance of this interface in order to do anything.

### Using the Bukkit ServicesManager
When the plugin is enabled, an instance of BasementLib will be provided in the Bukkit ServicesManager. (obviously you need to be writing your plugin for Bukkit!)

```java
RegisteredServiceProvider<BasementLib> provider = Bukkit.getServicesManager().getRegistration(BasementLib.class);
if (provider != null) {
    BasementLib api = provider.getProvider();
    
}
```

### Using the singleton (static access)
When the plugin is enabled, an instance of BasementLib can be obtained statically from the BasementProvider class. (this will work on all platforms)

Note: this method will throw an IllegalStateException if the API is not loaded.

```java
BasementLib api = BasementProvider.get();
``` 
## API Reference

### Generic API
#### Config

```java
private final ConfigurationAdapter configuration;

public ConfigExample(BasementLib basement, String fileName) {
    configuration = basement.plugin().provideConfigurationAdapter(ConfigExample.class, new File(fileName), true);
}

public List<String> getNames() {
    return configuration.getStringList("names", List.of());
}

public void reload() {
    configuration.reload();
}
```

#### Logger and Plugin Interfaces

In the it.ohalee.basementlib.api.plugin package there is a generic interface that can be implemented in your plugin if you wish. Under it.ohalee.basementlib.api.plugin.logging there is another interface that you can use in your plugin to implement your own logger, the BasementLib one can be obtained via basementLib.plugin().logger()

```java
basementLib.plugin().logger().warn("Test");
```

#### MySQL

It may seem that the database APIs are complicated but in reality it is enough to understand and learn how they work to know how to use them in all their power.
If the database from the config is not enabled, all the methods we are going to see now will return null

To use a database from the basementLib API you need to "load" it: when the plugin is enabled, the database inserted in the config is already loaded. It is then possible to perform operations external to this database by specifying it in the queries.
```java
basementLib.holder().createDatabase("name").ifNotExists(true).build();
```

NOTE: A database is loaded if a create database query is run (even if the database already exists) and is unloaded if a drop database query is run (queries must be run on the server in question for this to happen, is not synchronized between all servers)

To query a database just do:
```java
AbstractMariaDatabase customDatabase = basementLib.database("name");
AbstractMariaDatabase defaultDatabase = basementLib.database();

defaultDatabase.insert()
defaultDatabase.select()
defaultDatabase.update()
defaultDatabase.replace()
defaultDatabase.delete()
defaultDatabase.useTable()
defaultDatabase.createTable()
defaultDatabase.dropTable()
```

Differences between methods without "NQ" and with "NQ":
NQ stands for "not quoted" and is used to indicate whether or not to quote quotes (obviously you need to know a minimum of sql)

"Where" clause:
```java
something.where(WhereBuilder.builder().equalsNQ("uuid", "?").close())
```

At the end of each query (when you actually use it) you need to call the build() method. It is also possible to save queries (generally not built) in variables. When using them it is necessary to use the patternClone() method to copy the query and not modify the original one (this only if the operation is performed in another thread).

You can create another pool with the following method in case you need it:
```java
Connector connector = basementLib.createConnector(minPool, maxPool, poolName)
```

- Batch queries:
    ```java
    try (PreparedStatement prepared = someUpdate.patternClone().build().asPrepared()) {
        ...

        prepared.executeBatch();
  
        // Close connection
        prepared.getConnection().close();
    } catch (SQLException e) {
        e.printStackTrace();
    }  
  ```

#### Redis
You can send messages via redis and receive them anywhere:

https://github.com/redisson/redisson/wiki/6.-Distributed-objects#67-topic
```java
// Publish message
basementLib.redisManager().publishMessage(BasementMessage message);

// Register Topic Listener
basementLib.redisManager().registerTopicListener(String name, BasementMessageHandler<T extends BasementMessage> basementMessageHandler);

// Unregister Topic Listeners
basementLib.redisManager().unregisterTopicListener(String name, Integer... listenerId);

// Clear Topic Listeners
basementLib.redisManager().clearTopicListeners(String name);
```

Message Example
```java

// Custom Message class
public class ServerShutdownMessage extends BasementMessage {

    // Channel
    public static final String TOPIC = "server-shutdown";

    // Data
    private final String sender;
    private final String receiver;

    public ServerShutdownMessage() {
        super(TOPIC);
        this.sender = null;
        this.receiver = null;
    }

    public ServerShutdownMessage(String sender, String receiver) {
        super(TOPIC);
        this.sender = sender;
        this.receiver = receiver;
    }
}


// Handler
public class ServerShutdownHandler implements BasementMessageHandler<ServerShutdownMessage> {

    private final ProxyServer server;

    @Override
    public void execute(ServerShutdownMessage message) {
        // Do something
    }

    @Override
    public Class<ServerShutdownMessage> getCommandClass() {
        return ServerShutdownMessage.class;
    }
}

// Register
basementLib.redisManager().registerTopicListener(ServerShutdownMessage.TOPIC, new ServerShutdownHandler(this));

// Publish message
basementLib.redisManager().publishMessage(new ServerShutdownMessage("banana", "moon"));
```

Use redisson client: https://github.com/redisson/redisson
```java
RedissonClient client = basementLib.redisManager().redissonClient();
```

#### Remote services
These services need Redis to work.

RemoteCerebrumService allows via Cerebrum (software that manages servers in a modern and dynamic way via docker, available soon) to create and initialize servers even when the other servers are turned on
```java
public interface RemoteCerebrumService {

    /**
     * Create a new server
     *
     * @param name the server name
     */
    void createServer(String name);
}
```

RemoteVelocityService allows you to access the velocity functions from bukkit and check in real time if a player is in a server or in a section. Or if we want to send it to another server, or if we want to record a server directly on the velocity
```java
public interface RemoteVelocityService {

    /**
     * Gets if a player is in a server that names start with {@param server}
     *
     * @param player the player name
     * @param server the server ranch name
     * @return true if player is in a server ranch, false otherwise
     */
    boolean isOnRanch(String player, String server);

    /**
     * Gets if a player is in a server that names start with {@param server}
     *
     * @param player the player uuid
     * @param server the server ranch name
     * @return true if player is in a server ranch, false otherwise
     */
    boolean isOnRanch(UUID player, String server);

    /**
     * Gets the server name of a player
     *
     * @param player the player name
     * @return the server name
     */
    String getServer(String player);

    /**
     * Send a player to a server
     *
     * @param player the player name
     * @param server the server name
     */
    void sendToServer(String player, String server);

    /**
     * Send a player to a server
     *
     * @param uuid   the player uuid
     * @param server the server name
     */
    void sendToServer(UUID uuid, String server);

    /**
     * Send same messages to a player
     *
     * @param player   player name
     * @param messages the messages to send to the player
     */
    void sendMessage(String player, String... messages);

    /**
     * Send same messages to a player only if it has a determinate permission
     *
     * @param player         player name
     * @param permissionNode the permission the player need to receive the messages
     * @param messages       the messages to send to the player
     */
    void sendMessageWithPermission(String player, String permissionNode, String... messages);

    /**
     * Send same messages to a player, serialized in json string {@link net.kyori.adventure.text.Component}
     *
     * @param player   player name
     * @param messages the messages to send to the player serialized in Json String
     */
    void sendMessageComponent(String player, String... messages);

    /**
     * Register a new server to velocity
     *
     * @param serverName the server name
     * @param port       the server port
     */
    void registerServer(String serverName, int port);

}
```

### Bukkit API
#### Server ID
The server id is the name of the server that is used in case the BasementLib plugin for velocity is prenste, in order to dynamically register the servers.

it is obtainable and modifiable via the BasementLib API:
```java
BasementBukkitPlugin basementLib = (BasementBukkitPlugin) BasementProvider.get();

basementLib.setServerID("someName");

String serverID = basementLib.getServerID();
```

#### Chat

Using the Colorizer class you can directly transform messages into colored messages
```java
public final class Colorizer {

    @Setter
    private static ColorAdapter adapter;

    public static String colorize(String string) {
        string = ChatColor.translateAlternateColorCodes('&', string);
        return adapter.translateHex(string);
    }

    public static List<String> colorize(List<String> strings) {
        return strings.stream().map(Colorizer::colorize).collect(Collectors.toList());
    }

    public static List<String> colorize(String... strings) {
        return Arrays.stream(strings).map(Colorizer::colorize).collect(Collectors.toList());
    }

    public static String translateHex(String msg) {
        return adapter.translateHex(msg);
    }

    public interface ColorAdapter {
        String translateHex(String msg);
    }

}
```

#### Events
On bukkit there are two events managed by Basement:
BasementNewServerFound: Called when a server is added to the list of servers in memory
BasementServerRemoved: Called when a server is removed or expires in the in-memory server list

#### Scoreboard
BasementLib is able to offer a basic scoreboard to players. The integration is simple.

Example:
```java
public class HubScoreboardAdapter implements ScoreboardProvider {

    private BasementBukkitPlugin basementLib;
    private JavaPlugin plugin;
    private String title = "&d&lEXAMPLE";
    private List<String> lines = Arrays.asList(
            "",
            "  &8› &fOnline: &b%basement_counter_%",
            "  &8› &fLobby: &b#1",
            "",
            "  &8› &fRank: &b%luckperms_highest_group_by_weight%",
            "",
            "  &8› &fLivello: &b0",
            "  &8› &fExp: &b0&7/&b1200%",
            "",
            "&d  play.example.com"
    );

    public HubScoreboardAdapter(JavaPlugin plugin) {
        this.plugin = plugin;
        this.basementLib = (BasementBukkitPlugin) BasementProvider.get();

        basementLib.registerScoreboard(this, 10);
        basementLib.getScoreboardManager().start();
    }

    @Override
    public String getTitle(Player player) {
        return this.title.replace("&", "§");
    }

    @Override
    public List<ScoreboardLine> getLines(Player player) {
        return this.build(player, PlaceholderAPI.setPlaceholders(player, new ArrayList<>(this.lines.stream().map(line -> line.replace("&", "§")).toList())));
    }

    @Override
    public JavaPlugin getPlugin() {
        return plugin;
    }

    @Override
    public ScoreboardAdapter getAdapter() {
        return basementLib.getScoreboardAdapter();
    }
}
```

### Velocity API
Nothing special for now
## Usage/Examples

### Random query exaple
- Create the query
  ```java
  private final QueryBuilderUpdate queryUpdateUserData;
  
  public UserManager() {
      queryUpdateUserData = basement.database().update().table("players")
          .setNQ("xp", "?")
          .setNQ("level", "?").setNQ("coins", "?")
          .setNQ("language", "?")
          .where(WhereBuilder.builder().equalsNQ("uuid", "?").close());
  }
  ```
- Sync
  ```java
    private void save(UserData data) {
        queryUpdateUserData.patternClone().clearSet()
            .setNQ("xp", data.getXp()).setNQ("level", data.getNetworkLevel())
            .setNQ("coins", data.getNetworkCoins())
            .where(WhereBuilder.builder().equals("uuid", data.getUuid()).close())
            .build().exec();
    }
  ```

- Async
  ```java
  private CompletableFuture<QueryBuilderUpdate> saveAsync(UserData data) {
      return queryUpdateUserData.patternClone().clearSet()
          .setNQ("xp", data.getXp()).setNQ("level", data.getNetworkLevel())
          .setNQ("coins", data.getNetworkCoins())
          .where(WhereBuilder.builder().equals("uuid", data.getUuid()).close())
          .build().execAsync();
  }
  ```

### Random redisson example

```java
protected final RSetCache<Data> data;

public SomeClass() {
    data = basement.redisManager().redissonClient().getSetCache("some_data");
}

public Optional<Data> getData(UUID uuid) {
    return data.stream().filter(data -> data.getUuid().equals(uuid)).findFirst();
}

```
## FAQ

#### TODO

Suggest me

## Roadmap

- Additional minecraft version support for scoreboard (newer)
- TAB

