package me.dwtj.ex.openjdk.langtools.types;

import com.sun.source.util.Trees;
import com.sun.tools.javac.tree.JCTree.JCCompilationUnit;

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
        for (JCCompilationUnit cu : getCompilationUnits(roundEnv)) {
            System.out.println(cu.toString());
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
