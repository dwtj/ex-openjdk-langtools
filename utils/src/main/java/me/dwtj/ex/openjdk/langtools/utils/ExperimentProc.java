package me.dwtj.ex.openjdk.langtools.utils;

import static java.util.stream.Collectors.toList;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.util.JavacTask;
import com.sun.source.util.TaskEvent;
import com.sun.source.util.TaskListener;
import com.sun.source.util.Trees;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;


/**
 * @author dwtj
 */
@SupportedAnnotationTypes("*")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public abstract class ExperimentProc extends AbstractProcessor {

    protected Elements elementUtils;
    protected Types typeUtils;
    protected Trees treeUtils;
    protected JavacTask javacTask;

    protected Set<? extends TypeElement> roundAnnotations;
    protected RoundEnvironment roundEnv;

    public void init(ProcessingEnvironment procEnv) {
        super.init(procEnv);
        elementUtils = procEnv.getElementUtils();
        typeUtils = procEnv.getTypeUtils();
        treeUtils = Trees.instance(procEnv);
        javacTask = JavacTask.instance(procEnv);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        this.roundAnnotations = annotations;
        this.roundEnv = roundEnv;
        try {
            return process();
        } catch (Throwable t) { throw new RuntimeException(t); }
    }

    public abstract boolean process() throws Throwable;

    /**
     * Returns the tree representation of the compilation unit in which the given element resides.
     */
    protected CompilationUnitTree getCompilationUnitTree(Element e) {
        return treeUtils.getPath(e).getCompilationUnit();
    }

    /**
     * Returns a newly instantiated list of the tree representations of all compilation units
     * generated in the prior round.
     */
    protected List<CompilationUnitTree> getPriorCompilationUnitTrees() {
        return roundEnv.getRootElements().stream()
                .map(root -> getCompilationUnitTree(root))
                .collect(toList());
    }

    /**
     * A helper-method for adding a {@link TaskListener} to the current {@link JavacTask} which
     * applies the given callback to the unboxed values any post-analysis javac {@link TaskEvent}s
     * the given callback will not be applied to any other javac events.
     */
    protected void addPostAnalysisCallback(BiConsumer<CompilationUnitTree, TypeElement> cb) {
        javacTask.addTaskListener(new TaskListener() {
            @Override public void started(TaskEvent e) {
                // Ignore all of these events.
            }
            @Override public void finished(TaskEvent e) {
                if (e.getKind() == TaskEvent.Kind.ANALYZE) {
                    assert e.getCompilationUnit() != null:
                            "Expected a post-analysis javac event to have a compilation unit tree.";
                    assert e.getTypeElement() != null:
                            "Expected a post-analysis javac event to have a type element.";
                    cb.accept(e.getCompilationUnit(), e.getTypeElement());
                }
                // Otherwise, ignore any other kind of event.
            }
        });
    }

    /**
     * Optionally returns the tree representation of any compilation unit which contains a root
     * type element with the given qualified name which was generated in the prior round. This is
     * really just a helper method for `getPriorRootTypeElement()` and `getCompilationUnitTree()`.
     */
    protected Optional<CompilationUnitTree> getPriorCompilationUnitTreeContaining(String qualifiedName) {
        return getPriorRootTypeElement(qualifiedName).map(this::getCompilationUnitTree);
    }

    /**
     * Optionally returns a root type element with the given qualified name which was generated.
     */
    protected Optional<TypeElement> getPriorRootTypeElement(String qualifiedName) {
        return roundEnv.getRootElements().stream()
                .filter(elem -> elem instanceof TypeElement)
                .map(elem -> (TypeElement) elem)
                .filter(typeElem -> typeElem.getQualifiedName().contentEquals(qualifiedName))
                .findAny();
    }

    protected void note(String str) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, str);
    }
    protected void note(Object obj) {
        note(obj.toString());
    }

    protected void warning(String str) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, str);
    }
    protected void warning(Object obj) {
        warning(obj.toString());
    }

    protected void mandatoryWarning(String str) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.MANDATORY_WARNING, str);
    }
    protected void mandatoryWarning(Object obj) {
        mandatoryWarning(obj.toString());
    }

    protected void error(String str) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, str);
    }
    protected void error(Object obj) {
        error(obj.toString());
    }
}
