package me.dwtj.ex.openjdk.langtools.types;

import static java.util.stream.Collectors.toList;

import com.sun.source.tree.AnnotatedTypeTree;
import com.sun.source.tree.ArrayTypeTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.MemberSelectTree;
import com.sun.source.tree.PrimitiveTypeTree;
import com.sun.source.tree.VariableTree;
import com.sun.source.util.TreeScanner;
import com.sun.source.util.Trees;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.tree.JCTree;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;
import java.util.Set;

/**
 * @author dwtj
 */

@SupportedAnnotationTypes("*")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class TypesProc extends AbstractProcessor {

    public void init(ProcessingEnvironment procEnv) {
        super.init(procEnv);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        printBasicTypeInfoOfVariableDeclarations(roundEnv);
        return false;
    }

    private void printBasicTypeInfoOfVariableDeclarations(RoundEnvironment roundEnv) {
        note("## Basic Type Information of Variable Declarations");
        for (CompilationUnitTree cu : getCompilationUnits(processingEnv, roundEnv)) {

            cu.accept(new TreeScanner<Void,Void>() {

                @Override public Void visitVariable(VariableTree var, Void v) {
                    note("VariableTree: " + var);
                    note("VariableTree.getType():");
                    var.getType().accept(new TreeScanner<Void,Void>() {

                        @Override public Void visitIdentifier(IdentifierTree tree, Void v) {
                            note("  IdentifierTree: " + tree);
                            note("  Symbol: " + getSymbol(tree));
                            return null;
                        }

                        @Override public Void visitPrimitiveType(PrimitiveTypeTree tree, Void v) {
                            note("  PrimitiveTypeTree.getPrimitiveTypeKind(): "
                                  + tree.getPrimitiveTypeKind());
                            return null;
                        }

                        @Override public Void visitArrayType(ArrayTypeTree tree, Void v) {
                            note("  ArrayTypeTree: " + tree);
                            note("  ArrayTypeTree.getType(): " + tree.getType());
                            note("  ArrayTypeTree.getType().getClass(): "
                                  + tree.getType().getClass());
                            return null;
                        }

                        @Override public Void visitAnnotatedType(AnnotatedTypeTree tree, Void v) {
                            note("  AnnotatedTypeTree: " + tree);
                            note("  AnnotatedTypeTree.getUnderlyingType(): "
                                  + tree.getUnderlyingType());
                            note("  AnnotatedTypeTree.getUnderlyingType().getClass(): "
                                  + tree.getUnderlyingType().getClass());
                            return null;
                        }

                        @Override public Void visitMemberSelect(MemberSelectTree tree, Void v) {
                            note("  MemberSelectTree: " + tree);
                            note("  Symbol: " + getSymbol(tree));
                            return null;
                        }
                    }, null);
                    note("");
                    return null;
                }
            }, null);
        }
    }

    private static Symbol getSymbol(IdentifierTree tree) {
        return ((JCTree.JCIdent) tree).sym;
    }

    private static Symbol getSymbol(MemberSelectTree tree) {
        return ((JCTree.JCFieldAccess) tree).sym;
    }

    /**
     * Use the given processing environment to get all of the compilation units generated in the
     * prior round.
     */
    private static List<CompilationUnitTree> getCompilationUnits(ProcessingEnvironment procEnv,
                                                                 RoundEnvironment roundEnv){
        return roundEnv.getRootElements().stream()
                .map(root -> getCompilationUnit(procEnv, root))
                .collect(toList());
    }

    /**
     * Use the given processing environment to get the compilation unit in which the given element
     * resides.
     */
    private static CompilationUnitTree getCompilationUnit(ProcessingEnvironment env, Element e) {
        return Trees.instance(env).getPath(e).getCompilationUnit();
    }

    private void note(String str) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, str);
    }
    private void note(Object obj) {
        note(obj.toString());
    }

    private void warning(String str) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, str);
    }
    private void warning(Object obj) {
        warning(obj.toString());
    }

    private void mandatoryWarning(String str) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.MANDATORY_WARNING, str);
    }
    private void mandatoryWarning(Object obj) {
        mandatoryWarning(obj.toString());
    }

    private void error(String str) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, str);
    }
    private void error(Object obj) {
        error(obj.toString());
    }
}
