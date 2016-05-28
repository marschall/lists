Lists [![Build Status](https://travis-ci.org/marschall/lists.svg)](https://travis-ci.org/marschall/lists) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.marschall/lists/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.marschall/lists) [![Javadocs](http://www.javadoc.io/badge/com.github.marschall/lists.svg)](http://www.javadoc.io/doc/com.github.marschall/lists)
=====

Special purpose implementations of `java.util.List` that in the right niche use case can be much more efficient than implementations shipped with the JDK.

```xml
<dependency>
    <groupId>com.github.marschall</groupId>
    <artifactId>lists</artifactId>
    <version>1.1.0</version>
</dependency>
```

The implementations have optimized implementations of `java.util.Spliterator`.
The implementations support serialization but this has not been optimized.

Currently includes classes:
<dl>
<dt>[SingletonList](http://static.javadoc.io/com.github.marschall/lists/1.0.0/com/github/marschall/lists/SingletonList.html)</dt>
<dd>Like Collections.singletonList but mutable</dd>
<dt>[RepeatingList](http://static.javadoc.io/com.github.marschall/lists/1.0.0/com/github/marschall/lists/RepeatingList.html)</dt>
<dd>one element repeated several times, immutable</dd>
<dt>[MappedList](http://static.javadoc.io/com.github.marschall/lists/1.0.0/com/github/marschall/lists/MappedList.html)</dt>
<dd>a map function applied to an other list, just a view</dd>
<dt>[PrefixedList](http://static.javadoc.io/com.github.marschall/lists/1.0.0/com/github/marschall/lists/PrefixedList.html)</dt>
<dd>a Lisp-style list with a head and a tail</dd>
</dl>

All methods are below 325 byte and should therefore HotSpot should be able to inline them if they are hot.

None of the lists or iterators are fail-fast.

