package me.dwtj.ex.openjdk.langtools.types;

import com.sun.source.util.Trees;
import com.sun.tools.javac.tree.JCTree.*;
import com.sun.tools.javac.tree.TreeScanner;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.ArrayList;
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

        System.out.println();

        for (JCCompilationUnit cu : getCompilationUnits(roundEnv)) {

            cu.accept(new TreeScanner() {

                @Override public void visitVarDef(JCVariableDecl varDecl) {
                    System.out.println(varDecl);
                    System.out.println("JCVariableDecl.getType():");
                    varDecl.getType().accept(new TreeScanner() {

                        @Override public void visitIdent(JCIdent tree) {
                            System.out.println("  JCIdent: " + tree);
                            System.out.println("  JCIdent.sym: " + tree.sym);
                        }

                        @Override public void visitTypeIdent(JCPrimitiveTypeTree tree) {
                            System.out.println("  JCPrimitiveTypeTree: "
                                                + tree.getPrimitiveTypeKind());
                        }

                        @Override public void visitTypeArray(JCArrayTypeTree tree) {
                            System.out.println("  JCArrayTypeTree: " + tree);
                            System.out.println("  JCArrayTypeTree.getType(): " + tree.getType());
                        }

                        @Override public void visitAnnotatedType(JCAnnotatedType tree) {
                            System.out.println("  JCArrayTypeTree: " + tree);
                            System.out.println("  JCArrayTypeTree.getUnderlyingType(): "
                                                + tree.getUnderlyingType());
                        }

                        @Override public void visitSelect(JCFieldAccess tree) {
                            System.out.println("  JCFieldAccess: " + tree);
                            System.out.println("  JCFieldAccess.sym: " + tree.sym);
                        }
                    });
                    System.out.println();
                }
            });
        }

        return false;
    }

    private List<JCCompilationUnit> getCompilationUnits(RoundEnvironment roundEnv) {
        Trees treeUtils = Trees.instance(processingEnv);
        List<JCCompilationUnit> list = new ArrayList<>();
        for (Element root : roundEnv.getRootElements()) {
            list.add((JCCompilationUnit) treeUtils.getPath(root).getCompilationUnit());
        }
        return list;
    }
}
