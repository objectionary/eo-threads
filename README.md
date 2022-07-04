<img src="https://www.yegor256.com/images/books/elegant-objects/cactus.svg" height="100px" />

[![EO principles respected here](https://www.elegantobjects.org/badge.svg)](https://www.elegantobjects.org)
[![DevOps By Rultor.com](http://www.rultor.com/b/objectionary/eo-threads)](http://www.rultor.com/p/objectionary/eo-threads)
[![We recommend IntelliJ IDEA](https://www.elegantobjects.org/intellij-idea.svg)](https://www.jetbrains.com/idea/)

[![Hits-of-Code](https://hitsofcode.com/github/objectionary/eo-threads)](https://hitsofcode.com/view/github/objectionary/eo-threads)
![Lines of code](https://img.shields.io/tokei/lines/github/objectionary/eo-threads)
[![License](https://img.shields.io/badge/license-MIT-green.svg)](https://github.com/objectionary/eo-threads/blob/master/LICENSE.txt)

[EO](https://www.eolang.org) objects for managing execution threads.

To run slow code in a new thread and wait for its finish in a loop:

```
QQ.threads.thread > x
  [t]
    very-slow-object > @
while.
  t.is-running
  [i]
    seq > @
      QQ.io.stdout
        "still waiting..."
      QQ.threads.sleep
        QQ.dt.millisecond.mul 100
      if.
        i.gt 10
        t.terminate
        TRUE
```

The object `thread` has attributes:

  * `is-running` is TRUE if the thread is still working
  * `terminate` immediately terminates it

Dataization of the `thread` object means waiting for its finish.
If the thread is terminated, dataization returns `error`.

A thread starts on the first attempt to take `@` from it.

## How to Contribute

Fork repository, make changes, send us a pull request.
We will review your changes and apply them to the `master` branch shortly,
provided they don't violate our quality standards. To avoid frustration,
before sending us your pull request please run full Maven build:

```bash
$ mvn clean install -Pqulice
```

You will need Maven 3.3+ and Java 8+.

