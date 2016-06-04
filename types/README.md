# Accessing Type Information Within a `javac` Annotation Processor

This subproject includes experiments to understand the typing information which
is available to a Java annotation processor when it is run within Oracle's
`javac`.

For full directions on debugging the annotation processor, see the root
`README.md` file.

To debug just this sub-project, you can run the following from the project's
root:

```
$ ./gradlew clean :types:driver:build
```
