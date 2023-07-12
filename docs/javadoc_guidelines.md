# Javadoc Guidelines

Before we start the BurningOKR team wants to say thank you to [Stephen Colebourne](https://blog.joda.org/ "Stephen's blog")
whose awesome [blog post](https://blog.joda.org/2012/11/javadoc-coding-standards.html) about his thoughts on Javadoc
coding standards helped us a lot when creating the draft of our Javadoc guidelines (Follow Stephen on
[Twitter](https://twitter.com/jodastephen)).

To ensure a homogeneous documentation and a high degree of readability and maintainability we want to
establish a few rules when it comes to documenting your (or somebody else's) code. Those rules (or guidelines)
are supposed to enable new contributors to quickly get a grasp of the code they are looking at and so keep the
process of understanding the project as frictionless as we can. To achieve that it is mandatory to keep documentation
complete, consistent and readable.

## General

Every part of a Javadoc comment needs to follow these rules:

- Grammatical correctness (spelling and punctuation)
- short but informative
- repress any puns or jokes
- do NOT use ``@author``
- each element (header, body, the param-block, etc) is separated by a \<p>

`
Note that character limits are not hard limits. Depending on the complexity of the documented code, the limit can be exceeded by quite a bit. Keep in mind, however to keep everything as short as possible.
`

### Header

The header exists to give a short explanation of the documented code's purpose.

- a precise, single-line sentence describing the method or class
- no more than 100 characters
- should be plain text
- no in-depth context

```java
/**
  * Takes an integer and returns twice its value.
  *
  * [...]
  */
public String double(int number){return 2*number;}
```

### Body

The body can be used to give more in-depth information about the context the method is used in.
Notice, however, that in some cases the header might already give enough information about the
purpose of the class or method. Some guidelines for the body:

- give context
- no more than 100 characters per line
- may use HTML tags
- reference other classes, methods etc. (`@code` and `@link`)
- can and should be split into multiple paragraphs (\<p>) for more complicated contexts

`
Important:
When referencing other classes in the body only use @link on the first occurrence and @code each time after that.
`

```java
/**
  * [...]
  * Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod 
  * tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua.
  * <p>
  * Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy 
  * eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam 
  * voluptua.
  * [...]
  */
public String foo(int number, Object obj){...}
```

<!--The body is separated from the header by a paragraph (\<p>) and may contain more in depth information
about the function and the context of the documented part of the code. Is is important to note
that the Body itself can also be divided into multiple paragraphs to keep readability high and
sophisticated contexts well structured.-->

### Params

The `@param` entries are used to explain the parameters given to a specific method. It is also necessary to
clarify the behaviour of the method should a parameter be null. To do that we define this kind of behaviour as a suffix
behind the parameter explanation separated by a comma. The types of suffixes are:

| Suffix               | Description                                                                                       |
| :------------------- | :------------------------------------------------------------------------------------------------ |
| not null             | parameter cannot be null. Will otherwise result in a ``NullPointerException``.                    |
| nullable -> [result] | parameter can be null. [result] represents a description of how a null value is handled.          |
| null returns [value] | parameter can be null. A custom [value] can be defined as a return value, e.g. "null returns -1". |

The parameter descriptions themselves should be no more a short description. If there is too much context to a parameter
it should be explained in the body of the Javadoc comment. The general guidelines are:

- every parameter needs an `@parameter`
- every parameter needs one type of suffix specified in the table above
- the order of the `@params` is equivalent to the order they appear in the method header
- description is no complete sentence

```java
/**
  * [...]
  * </p>
  * @param number  an integer value, not null
  * @param obj  an random object, null returns null
  * [...]
  */
public String foo(int number, Object obj){...}
```

### Returns

The `@return` describes the return value of a method's result. Use it as if `@return` initiates
a sentence.

- keep it precise
- document it as if ist a sentence beginning with 'returns'
- void type methods do not require an ```@return```

```java
/**
  * [...]
  *
  * @param x  an integer value, not null
  * @param y  an integer value, not null
  * @return the product of two integers
  */
public String multiply(int y, int y){...}
```
