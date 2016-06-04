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
        log();
        log("## Basic Type Information of Variable Declarations");
        for (CompilationUnitTree cu : getCompilationUnits(processingEnv, roundEnv)) {

            cu.accept(new TreeScanner<Void,Void>() {

                @Override public Void visitVariable(VariableTree var, Void v) {
                    log(var);
                    log("VariableTree.getType():");
                    var.getType().accept(new TreeScanner<Void,Void>() {

                        @Override public Void visitIdentifier(IdentifierTree tree, Void v) {
                            log("  IdentifierTree: " + tree);
                            log("  Symbol: " + getSymbol(tree));
                            return null;
                        }

                        @Override public Void visitPrimitiveType(PrimitiveTypeTree tree, Void v) {
                            log("  PrimitiveTypeTree.getPrimitiveTypeKind(): "
                                 + tree.getPrimitiveTypeKind());
                            return null;
                        }

                        @Override public Void visitArrayType(ArrayTypeTree tree, Void v) {
                            log("  ArrayTypeTree: " + tree);
                            log("  ArrayTypeTree.getType(): " + tree.getType());
                            log("  ArrayTypeTree.getType().getClass(): "
                                 + tree.getType().getClass());
                            return null;
                        }

                        @Override public Void visitAnnotatedType(AnnotatedTypeTree tree, Void v) {
                            log("  AnnotatedTypeTree: " + tree);
                            log("  AnnotatedTypeTree.getUnderlyingType(): "
                                 + tree.getUnderlyingType());
                            log("  AnnotatedTypeTree.getUnderlyingType().getClass(): "
                                 + tree.getUnderlyingType().getClass());
                            return null;
                        }

                        @Override public Void visitMemberSelect(MemberSelectTree tree, Void v) {
                            log("  MemberSelectTree: " + tree);
                            log("  Symbol: " + getSymbol(tree));
                            return null;
                        }
                    }, null);
                    log();
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

    private static void log(String str) {
        System.out.println(str);
    }

    private static void log() {
        log("");
    }

    private static void log(Object obj) {
        log(obj.toString());
    }
}
