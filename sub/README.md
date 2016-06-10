# Using an Annotation Processor to Trigger Sub-Compilations within javac

This subproject encapsulates experiments where an annotation processor running
within Oracle's `javac` is used to trigger sub-compilations within Oracle's
`javac`; by this, we mean that an annotation processor running within a `javac`
instance dynamically invokes another `javac` instance. In general, experiments
focus on how one might design information flow between the initial compilation
and subcompilation(s).

The annotation processor defined in `:sub:proc` can be triggered by building
`:sub:driver`. This can be done by running `./gradlew clean :sub:driver:build`
from the root of the project.
