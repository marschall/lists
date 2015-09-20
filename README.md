Lists
=====

Special purpose implementations of `java.util.List` that in the right niche use case can be much more efficient than implementations shipped with the JDK.

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

