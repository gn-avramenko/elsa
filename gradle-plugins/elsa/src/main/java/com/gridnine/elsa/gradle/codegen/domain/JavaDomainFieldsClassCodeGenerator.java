/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.codegen.domain;

import com.gridnine.elsa.common.meta.domain.BaseSearchableDescription;
import com.gridnine.elsa.common.meta.domain.DatabaseCollectionDescription;
import com.gridnine.elsa.common.meta.domain.DatabasePropertyDescription;
import com.gridnine.elsa.common.meta.domain.DomainMetaRegistry;
import com.gridnine.elsa.gradle.codegen.common.CodeGeneratorUtils;
import com.gridnine.elsa.gradle.codegen.common.JavaCodeGenerator;
import com.gridnine.elsa.gradle.utils.BuildExceptionUtils;

import java.io.File;
import java.util.Set;

public class JavaDomainFieldsClassCodeGenerator {
    public static void generate(DomainMetaRegistry registry, File destDir, Set<File> generatedFiles) throws Exception {
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
                gen.printLine("public final static _%1$sField %1$s = new _%1$sField();".formatted(pd.getId()));
            }
            for (DatabaseCollectionDescription cd : sd.getCollections().values()) {
                gen.blankLine();
                gen.printLine("public final static _%1$sField %1$s = new _%1$sField();".formatted(cd.getId()));
            }
            for (DatabasePropertyDescription pd : sd.getProperties().values()) {
                gen.blankLine();
                gen.addImport("com.gridnine.elsa.common.core.search.FieldNameSupport");
                var sb = new StringBuilder("private static class _%sField extends FieldNameSupport".formatted(pd.getId()));
                switch (pd.getType()) {
                    case LONG -> {
                        gen.addImport("com.gridnine.elsa.common.core.search.EqualitySupport");
                        gen.addImport("com.gridnine.elsa.common.core.search.ComparisonSupport");
                        gen.addImport("com.gridnine.elsa.common.core.search.NumberOperationsSupport");
                        gen.addImport("com.gridnine.elsa.common.core.search.SortSupport");
                        gen.addImport("com.gridnine.elsa.common.core.search.ArgumentType");
                        sb.append(" implements EqualitySupport, ComparisonSupport, NumberOperationsSupport, SortSupport, ArgumentType<Long>");
                    }
                    case INT -> {
                        gen.addImport("com.gridnine.elsa.common.core.search.EqualitySupport");
                        gen.addImport("com.gridnine.elsa.common.core.search.ComparisonSupport");
                        gen.addImport("com.gridnine.elsa.common.core.search.NumberOperationsSupport");
                        gen.addImport("com.gridnine.elsa.common.core.search.SortSupport");
                        gen.addImport("com.gridnine.elsa.common.core.search.ArgumentType");
                        sb.append(" implements EqualitySupport, ComparisonSupport, NumberOperationsSupport, SortSupport, ArgumentType<Integer>");
                    }
                    case LOCAL_DATE_TIME -> {
                        gen.addImport("com.gridnine.elsa.common.core.search.ComparisonSupport");
                        gen.addImport("com.gridnine.elsa.common.core.search.SortSupport");
                        gen.addImport("java.time.LocalDateTime");
                        gen.addImport("com.gridnine.elsa.common.core.search.ArgumentType");
                        sb.append(" implements ComparisonSupport, SortSupport, ArgumentType<LocalDateTime>");
                    }
                    case LOCAL_DATE -> {
                        gen.addImport("com.gridnine.elsa.common.core.search.ComparisonSupport");
                        gen.addImport("com.gridnine.elsa.common.core.search.SortSupport");
                        gen.addImport("com.gridnine.elsa.common.core.search.EqualitySupport");
                        gen.addImport("java.time.LocalDate");
                        gen.addImport("com.gridnine.elsa.common.core.search.ArgumentType");
                        sb.append(" implements ComparisonSupport, SortSupport, EqualitySupport,  ArgumentType<LocalDate>");
                    }
                    case ENTITY_REFERENCE -> {
                        gen.addImport("com.gridnine.elsa.common.core.search.SortSupport");
                        gen.addImport("com.gridnine.elsa.common.core.search.EqualitySupport");
                        gen.addImport("com.gridnine.elsa.common.core.search.ArgumentType");
                        gen.addImport("com.gridnine.elsa.common.core.model.domain.EntityReference");
                        gen.addImport(pd.getClassName());
                        sb.append(" implements SortSupport, EqualitySupport, ArgumentType<EntityReference<%s>>".formatted(CodeGeneratorUtils.getSimpleName(pd.getClassName())));
                    }
                    case BIG_DECIMAL -> {
                        gen.addImport("com.gridnine.elsa.common.core.search.ComparisonSupport");
                        gen.addImport("com.gridnine.elsa.common.core.search.NumberOperationsSupport");
                        gen.addImport("com.gridnine.elsa.common.core.search.SortSupport");
                        gen.addImport("com.gridnine.elsa.common.core.search.ArgumentType");
                        gen.addImport("java.math.BigDecimal");
                        sb.append(" implements ComparisonSupport, NumberOperationsSupport, SortSupport, ArgumentType<BigDecimal>");
                    }
                    case BOOLEAN -> {
                        gen.addImport("com.gridnine.elsa.common.core.search.EqualitySupport");
                        gen.addImport("com.gridnine.elsa.common.core.search.ArgumentType");
                        sb.append(" implements EqualitySupport, ArgumentType<Boolean>");
                    }
                    case STRING -> {
                        gen.addImport("com.gridnine.elsa.common.core.search.EqualitySupport");
                        gen.addImport("com.gridnine.elsa.common.core.search.StringOperationsSupport");
                        gen.addImport("com.gridnine.elsa.common.core.search.SortSupport");
                        gen.addImport("com.gridnine.elsa.common.core.search.ArgumentType");
                        sb.append(" implements EqualitySupport, StringOperationsSupport, SortSupport, ArgumentType<String>");
                    }
                    case ENUM -> {
                        gen.addImport("com.gridnine.elsa.common.core.search.EqualitySupport");
                        gen.addImport("com.gridnine.elsa.common.core.search.SortSupport");
                        gen.addImport("com.gridnine.elsa.common.core.search.ArgumentType");
                        gen.addImport(pd.getClassName());
                        sb.append(" implements EqualitySupport, SortSupport, ArgumentType<%s>".formatted(CodeGeneratorUtils.getSimpleName(pd.getClassName())));
                    }
                }
                gen.wrapWithBlock(sb.toString(), () -> gen.wrapWithBlock("_%sField()".formatted(pd.getId()), () -> gen.printLine("super(\"%s\");".formatted(pd.getId()))));
            }

            for (DatabaseCollectionDescription cd : sd.getCollections().values()) {
                gen.blankLine();
                gen.addImport("com.gridnine.elsa.common.core.search.FieldNameSupport");
                var sb = new StringBuilder("private static class _%sField extends FieldNameSupport".formatted(cd.getId()));
                switch (cd.getElementType()) {
                    case ENTITY_REFERENCE -> {
                        gen.addImport("com.gridnine.elsa.common.core.model.domain.EntityReference");
                        gen.addImport("com.gridnine.elsa.common.core.search.ArgumentType");
                        gen.addImport("com.gridnine.elsa.common.core.search.CollectionSupport");
                        gen.addImport(cd.getElementClassName());
                        sb.append(" implements CollectionSupport, ArgumentType<EntityReference<%s>>".formatted(CodeGeneratorUtils.getSimpleName(cd.getElementClassName())));
                    }
                    case ENUM -> {
                        gen.addImport("com.gridnine.elsa.common.core.model.domain.EntityReference");
                        gen.addImport("com.gridnine.elsa.common.core.search.ArgumentType");
                        gen.addImport("com.gridnine.elsa.common.core.search.CollectionSupport");
                        gen.addImport(cd.getElementClassName());
                        sb.append(" implements CollectionSupport, ArgumentType<%s>".formatted(CodeGeneratorUtils.getSimpleName(cd.getElementClassName())));
                    }
                    case STRING -> {
                        gen.addImport("com.gridnine.elsa.common.core.search.CollectionSupport");
                        gen.addImport("com.gridnine.elsa.common.core.search.ArgumentType");
                        sb.append(" implements CollectionSupport, ArgumentType<String>");
                    }
                }
                gen.wrapWithBlock(sb.toString(), () -> gen.wrapWithBlock("_%sField()".formatted(cd.getId()), () -> gen.printLine("super(\"%s\");".formatted(cd.getId()))));
            }
        });

        var file = CodeGeneratorUtils.saveIfDiffers(gen.toString(), "%sFields.java"
                .formatted(sd.getId()), destDir);
        generatedFiles.add(file);
    }
}
