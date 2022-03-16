/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.codegen.domain;

import com.gridnine.elsa.common.meta.common.*;
import com.gridnine.elsa.common.meta.domain.*;
import com.gridnine.elsa.gradle.codegen.common.CodeGeneratorUtils;
import com.gridnine.elsa.gradle.codegen.common.JavaCodeGenerator;
import com.gridnine.elsa.gradle.utils.BuildExceptionUtils;
import com.gridnine.elsa.gradle.utils.BuildTextUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class JavaDomainFieldsClassCodeGenerator {
    public static void generate(DomainMetaRegistry registry,  File destDir, Set<File> generatedFiles) throws Exception {
        registry.getSearchableProjections().values().forEach(it -> BuildExceptionUtils.wrapException(() -> generateFieldsClass(it, destDir, generatedFiles)));
        registry.getAssets().values().forEach(it -> BuildExceptionUtils.wrapException(() -> generateFieldsClass(it, destDir, generatedFiles)));
    }


    private static void generateFieldsClass(BaseSearchableDescription sd, File destDir, Set<File> generatedFiles) throws Exception {
        var gen = new JavaCodeGenerator();
        var packageName = CodeGeneratorUtils.getPackage(sd.getId());
        gen.setPackageName(packageName);
        gen.wrapWithBlock("public class %sFields".formatted(CodeGeneratorUtils.getSimpleName(sd.getId())), () -> {
            for (DatabasePropertyDescription pd : sd.getProperties().values()) {
                gen.blankLine();
                gen.printLine("public final static _%1$sField %1$s = new _%1$sField(\"%1$s\");".formatted(pd.getId()));
            }
            for (DatabaseCollectionDescription cd : sd.getCollections().values()) {
                gen.blankLine();
                gen.printLine("public final static _%1$sField %1$s = new _%1$sField(\"%1$s\");".formatted(cd.getId()));
            }
            for (DatabasePropertyDescription pd : sd.getProperties().values()) {
                gen.blankLine();
                gen.addImport("com.gridnine.elsa.common.core.search.FieldNameSupport");
                var sb = new StringBuilder("private static class _%sField extends FieldNameSupport".formatted(pd.getId()));
                switch (pd.getType()){
                    case LONG, INT ->{
                        gen.addImport("com.gridnine.elsa.common.core.search.EqualitySupport");
                        gen.addImport("com.gridnine.elsa.common.core.search.ComparisonSupport");
                        gen.addImport("com.gridnine.elsa.common.core.search.NumberOperationsSupport");
                        gen.addImport("com.gridnine.elsa.common.core.search.SortSupport");
                        sb.append(" implements EqualitySupport, ComparisonSupport, NumberOperationsSupport, SortSupport");
                    }
                    case LOCAL_DATE_TIME ->{
                        gen.addImport("com.gridnine.elsa.common.core.search.ComparisonSupport");
                        gen.addImport("com.gridnine.elsa.common.core.search.SortSupport");
                        sb.append(" implements ComparisonSupport, SortSupport");
                    }
                    case LOCAL_DATE ->{
                        gen.addImport("com.gridnine.elsa.common.core.search.ComparisonSupport");
                        gen.addImport("com.gridnine.elsa.common.core.search.SortSupport");
                        gen.addImport("com.gridnine.elsa.common.core.search.EqualitySupport");
                        sb.append(" implements ComparisonSupport, SortSupport, EqualitySupport");
                    }
                    case ENTITY_REFERENCE ->{
                        gen.addImport("com.gridnine.elsa.common.core.search.SortSupport");
                        gen.addImport("com.gridnine.elsa.common.core.search.EqualitySupport");
                        sb.append(" implements SortSupport, EqualitySupport");
                    }
                    case BIG_DECIMAL ->{
                        gen.addImport("com.gridnine.elsa.common.core.search.ComparisonSupport");
                        gen.addImport("com.gridnine.elsa.common.core.search.NumberOperationsSupport");
                        gen.addImport("com.gridnine.elsa.common.core.search.SortSupport");
                        sb.append(" implements ComparisonSupport, NumberOperationsSupport, SortSupport");
                    }
                    case BOOLEAN ->{
                        gen.addImport("com.gridnine.elsa.common.core.search.EqualitySupport");
                        sb.append(" implements EqualitySupport");
                    }
                    case STRING ->{
                        gen.addImport("com.gridnine.elsa.common.core.search.EqualitySupport");
                        gen.addImport("com.gridnine.elsa.common.core.search.StringOperationsSupport");
                        gen.addImport("com.gridnine.elsa.common.core.search.SortSupport");
                        sb.append(" implements EqualitySupport, StringOperationsSupport, SortSupport");
                    }
                    case ENUM ->{
                        gen.addImport("com.gridnine.elsa.common.core.search.EqualitySupport");
                        gen.addImport("com.gridnine.elsa.common.core.search.SortSupport");
                        sb.append(" implements EqualitySupport, SortSupport");
                    }
                }
                gen.wrapWithBlock(sb.toString(), () ->{
                    gen.wrapWithBlock("_%sField(String name)".formatted(pd.getId()), () ->{
                        gen.printLine("super(name);");
                    });
                });
            }

            for (DatabaseCollectionDescription cd : sd.getCollections().values()) {
                gen.blankLine();
                gen.addImport("com.gridnine.elsa.common.core.search.FieldNameSupport");
                var sb = new StringBuilder("private static class _%sField extends FieldNameSupport".formatted(cd.getId()));
                switch (cd.getElementType()){
                    case ENTITY_REFERENCE,ENUM,STRING ->{
                        gen.addImport("com.gridnine.elsa.common.core.search.CollectionSupport");
                        sb.append(" implements CollectionSupport");
                    }
                }
                gen.wrapWithBlock(sb.toString(), () ->{
                    gen.wrapWithBlock("_%sField(String name)".formatted(cd.getId()), () ->{
                        gen.printLine("super(name);");
                    });
                });
            }
        });

        var file = CodeGeneratorUtils.saveIfDiffers(gen.toString(), "%sFields.java"
                .formatted(sd.getId()), destDir);
        generatedFiles.add(file);
    }
}
