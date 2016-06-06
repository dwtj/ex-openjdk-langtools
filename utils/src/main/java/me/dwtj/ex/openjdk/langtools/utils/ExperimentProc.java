package me.dwtj.ex.openjdk.langtools.utils;

import static java.util.stream.Collectors.toList;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.util.Trees;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.util.List;


/**
 * @author dwtj
 */
@SupportedAnnotationTypes("*")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public abstract class ExperimentProc extends AbstractProcessor {

    protected Elements elementUtils;
    protected Types typeUtils;
    protected Trees treeUtils;

    public void init(ProcessingEnvironment procEnv) {
        elementUtils = procEnv.getElementUtils();
        typeUtils = procEnv.getTypeUtils();
        treeUtils = Trees.instance(procEnv);
        super.init(procEnv);
    }

    /**
     * Use the given processing environment to get all of the compilation units generated in the
     * prior round.
     */
    protected List<CompilationUnitTree> getCompilationUnits(RoundEnvironment roundEnv) {
        return roundEnv.getRootElements().stream()
                .map(root -> getCompilationUnit(root))
                .collect(toList());
    }

    /**
     * Use the given processing environment to get the compilation unit in which the given element
     * resides.
     */
    protected CompilationUnitTree getCompilationUnit(Element e) {
        return treeUtils.getPath(e).getCompilationUnit();
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
