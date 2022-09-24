# MySQL-API
A set of tools for working with mysql for java

## Description
A library implemented from the "builder" pattern classes for working with [MySQL](https://mvnrepository.com/artifact/mysql/mysql-connector-java/8.0.25). Uses API version `8.0.25`.

## Requirements
1. `Java 1.8` or higher
2. `Maven` or other build tool

## Installation
Add the library to your `Maven` or `Gradle` project: 

### Maven
```xml
<dependency>
  <groupId>io.github.inahiki-dev</groupId>
  <artifactId>mysql-api</artifactId>
  <version>1.3.0</version>
</dependency>
```

### Gradle
```Gradle
dependencies {  
   implementation 'io.github.inahiki-dev:mysql-api:1.3.0'
}
```

# Examples
### Open connection
```java
SQLConnection connection = SQLConnection.builder()
		.host("localhost")
		.port("3306")
		.user("root")
		.password("__password__")
		.database("clans")
		.createAndOpen();
```
### CREATE TABLE
Request for MySQL:
```sql
CREATE TABLE IF NOT EXISTS Players (
   id       INT NOT NULL AUTO_INCREMENT,
   clan_id  INT,
   name     VARCHAR(32) NOT NULL,

   PRIMARY KEY (id) USING BTREE,
   
   CONSTRAINT players_clan_id_fk
   FOREIGN KEY (clan_id)
	  REFERENCES clan (id)
	  ON DELETE SET NULL
	  ON UPDATE CASCADE
)
```
Implementation in Java:
```java
connection.createTable("Players")
        .ifNotExists()
        .column("id").type(Column.INT).notNull().autoInc().add()
        .column("clan_id").type(Column.INT).add()
        .column("name").type(Column.VARCHAR, 32).notNull().add()
        .primaryKey("id").using(IndexType.BTREE).add()
        .foreignKey("clan_id").constraint()
            .references("clan", "id")
            .onDelete(Action.SET_NULL)
            .onUpdate(Action.CASCADE)
            .add()
        .execute();
```
### ALTER TABLE
Request for MySQL:
```sql
ALTER TABLE Players
   ADD COLUMN id INT NOT NULL AUTO_INCREMENT,
   ADD CONSTRAINT players_chk_235 CHECK (clan_id <= 1024),
   DROP COLUMN tag
```
Implementation in Java:
```java
connection.alterTable("Players")
        .addColumn("id").type(Column.INT).notNull().autoInc().add()
        .addCheck("").lessEqual("clan_id", 1024).add()
        .dropColumn("tag")
        .execute()
```

### SELECT
Request for MySQL:
```sql
SELECT Players.name AS name, Clan.tag AS tag, Clan.lvl AS lvl
   FROM Players INNER JOIN Clan ON (Players.clan_id = Clan.id)
   WHERE lvl >= 5 AND lvl < 10
   LIMIT 5
```
Implementation in Java:
```java
connection.select()
        .column("Players.name", "name")
        .column("Clan.tag", "tag")
        .column("Clan.lvl", "lvl")
        .table("Players")
        .innerJoin("Clan", "id", "Players", "clan_id")
        .where().greaterEqual("lvl", 5).and().less("lvl", 10).add()
        .limit(5)
```
### REPLACE INTO / INSERT INTO
Request for MySQL:
```sql
INSERT INTO Players (name, clan_id)
   VALUES ('Igor', 5), ('Maksim', 12)
```
Implementation in Java:
```java
connection.insertInto("Players")
        .columns("name", "clan_id")
        .value("Igor", 5)
        .value("Maskim", 12)
        .execute()
```
### DELETE
Request for MySQL:
```sql
DELETE FROM Owner 
   WHERE (id > 109 OR name = 'Hazzer') AND clan_id = 5
```
Implementation in Java:
```java
connection.delete("Players")
        .where()
            .up()
                .greater("id", 3).or().equal("name", "Hazzer")
                .down()
            .and().equal("clan_id", 5).add()
        .execute()
```
