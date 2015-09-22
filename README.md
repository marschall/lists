Lists [![Build Status](https://travis-ci.org/marschall/lists.svg)](https://travis-ci.org/marschall/lists) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.marschall/lists/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.marschall/lists)
=====

Special purpose implementations of `java.util.List` that in the right niche use case can be much more efficient than implementations shipped with the JDK.

```xml
<dependency>
    <groupId>com.github.marschall</groupId>
    <artifactId>lists</artifactId>
    <version>1.0.0</version>
</dependency>
```

The implementations have optimized implementations of `java.util.Spliterator`.
The implementations support serialization but this has not been optimized.

Currently includes classes:
<dl>
<dt>SingletonList</dt>
<dd>Like Collections.singletonList but mutable</dd>
<dt>RepeatingList</dt>
<dd>one element repeated several times</dd>
<dt>MappedList</dt>
<dd>a map function applied to an other list, just a view</dd>
</dl>

All methods are below 325 byte and should therefore HotSpot should be able to inline them if they are hot.

