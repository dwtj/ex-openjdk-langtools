package me.dwtj.ex.openjdk.langtools.types;

import com.sun.source.tree.AnnotatedTypeTree;
import com.sun.source.tree.ArrayTypeTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.MemberSelectTree;
import com.sun.source.tree.PrimitiveTypeTree;
import com.sun.source.tree.VariableTree;
import com.sun.source.util.TreeScanner;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.tree.JCTree;
import me.dwtj.ex.openjdk.langtools.utils.ExperimentProc;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

/**
 * @author dwtj
 */
@SupportedAnnotationTypes("*")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class TypesProc extends ExperimentProc {

    public void init(ProcessingEnvironment procEnv) {
        super.init(procEnv);
    }

    @Override
    public boolean process() {
        if (!roundEnv.processingOver()) {
            printListOfAllPriorCompilationUnits();
            printBasicTypeInfoOfVariableDeclarations();
            lookupTypeElementAndPrintIt("me.dwtj.ex.openjdk.langtools.types.UserDefinedClass");
            //lookupTypeElementAndPrintIt("java.lang.String");
        }
        return false;
    }

    private void printListOfAllPriorCompilationUnits() {
        note("## Prior Compilation Units:");
        getPriorCompilationUnitTrees().stream()
                .forEach(cu -> note("  " + cu.getSourceFile().getName()));
    }

    private void printBasicTypeInfoOfVariableDeclarations() {
        note("## Basic Type Information of Variable Declarations");
        for (CompilationUnitTree cu : getPriorCompilationUnitTrees()) {

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

    private void lookupTypeElementAndPrintIt(String name) {
        TypeElement typeElem = elementUtils.getTypeElement(name);
        note(stringifyTypeElement(typeElem));
    }

    private String stringifyTypeElement(TypeElement typeElem) {
        try (Writer w = new StringWriter()) {
            elementUtils.printElements(w, typeElem);
            return w.toString();
        } catch (IOException ex) {
            // This should never happen: according to the class's javadoc, `StringWriter#close()`
            // should never throw an `IOException`.
            throw new AssertionError(ex);
        }
    }

    private static Symbol getSymbol(IdentifierTree tree) {
        return ((JCTree.JCIdent) tree).sym;
    }

    private static Symbol getSymbol(MemberSelectTree tree) {
        return ((JCTree.JCFieldAccess) tree).sym;
    }
}
