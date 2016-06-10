package me.dwtj.ex.openjdk.langtools.sub;

import me.dwtj.ex.openjdk.langtools.utils.ExperimentProc;

import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;

/**
 * @author dwtj
 */
@SupportedAnnotationTypes("*")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class SubCompilationProc extends ExperimentProc {

    @Override
    public boolean process() {
        if (roundEnv.processingOver()) {
            note("Hello, from `SubCompilationProc`.");
        }
        return false;
    }
}
