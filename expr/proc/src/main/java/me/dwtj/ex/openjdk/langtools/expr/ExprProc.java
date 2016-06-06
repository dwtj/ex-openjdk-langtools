package me.dwtj.ex.openjdk.langtools.expr;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.util.TreeScanner;
import me.dwtj.ex.openjdk.langtools.utils.ExperimentProc;

import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;

/**
 * @author dwtj
 */
@SupportedAnnotationTypes("*")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class ExprProc extends ExperimentProc {

    private final String PKG = "me.dwtj.ex.openjdk.langtools.expr";
    private final String QUALIFIED_STATIC_RECURSIVE_METHOD_NAME = PKG + ".StaticRecursiveMethod";

    @Override
    public boolean process() {
        if (!roundEnv.processingOver()) {
            printMethodsAndInvocations(QUALIFIED_STATIC_RECURSIVE_METHOD_NAME);
        }
        return false;
    }

    public void printMethodsAndInvocations(String qualifiedName) {
        CompilationUnitTree cu = getPriorCompilationUnitTreeContaining(qualifiedName).get();
        cu.accept(new TreeScanner<Void,Void>() {
            @Override public Void visitMethod(MethodTree method, Void v) {
                note("method: " + method);
                return super.visitMethod(method, v);
            }
            @Override public Void visitMethodInvocation(MethodInvocationTree invocation, Void v) {
                note("invocation: " + invocation);
                return super.visitMethodInvocation(invocation, v);
            }
        }, null);
    }
}
