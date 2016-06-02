# Accessing `javac` Types Within an Annotation Processor

This project is a set of experiments to understand the typing information
available to a Java annotation processor (i.e. a Java compiler plugin) when it
is run within Oracle's `javac`.


## Background

**TODO:** The AST provided to an annotation processor within `javac` is based
on the `JCTree` API.

**TODO:** Everything!


## Debugging

To aid in learning about what typing information might be available to an
annotation processor, this project assists in setting up a development
environment which can use a Java IDE (e.g. Eclipse or IntelliJ IDEA) to debug
an annotation processor, `proc`, when it is run over a project, `driver`. We
want to be able to use the IDE GUI to set breakpoints in the annotation
processor and to step-through its code. This this will be done by debugging the
[Gradle Daemon](https://docs.gradle.org/current/userguide/gradle_daemon.html)
process used to build the project. (This idea came from [this StackOverflow
answer](http://stackoverflow.com/a/36765029) by Gagandeep Singh.)

The `gradle.properties` file has has been configured so that the project is
compiled via a Gradle Daemon process to which we can attach our debugger. This
way, when the user builds the project, the debugger can be used to step through
the annotation processor, `proc`, when the annotation processor code is called
during compilation of the `driver` subproject.

The preferred way of building/debugging the project is with the provided
[Gradle Wrapper](https://docs.gradle.org/current/userguide/gradle_wrapper.html).
Note that the instructions below describe using the provided Gradle Wrapper,
since it is the preferred way of compiling the project. However, you may use
your own installation's version of Gradle if you would prefer.


## Setup a Debugging Environment in IntelliJ IDEA

(1) Clone the Git repository.

(2) Import the project into IntelliJ Idea as a Gradle project.

(3) Enter the "Run" > "Edit Configurations..." dialog.

(4) Add a new "Remote" configuration, give it a name, and make sure that it will
    connect to `localhost:5005`.

    ![](img/intellij_idea_remote_debug_configuration.png)

(5) Using the command line, in the root of the project, start the daemon with
    `./gradlew --daemon`.

(6) Back in IntelliJ IDEA, start the debug configuration which you just created.
    The debug console should print a message saying:

    ```
    Connected to the target VM, address: 'localhost:5005', transport: 'socket'
    ```

(7) Set a breakpoint in the annotation processing code.

(8) On the command line, in the root of the project, build the project with
    `./gradlew clean build`. The Gradle daemon process should pause when the
    build's execution reaches the annotation processor use in `driver`, and we
    can now step through the code as one would expect.
