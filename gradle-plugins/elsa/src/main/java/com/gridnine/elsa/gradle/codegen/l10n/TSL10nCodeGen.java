/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.gradle.codegen.l10n;

import com.gridnine.elsa.gradle.codegen.common.GenPropertyDescription;
import com.gridnine.elsa.gradle.codegen.common.TsCodeGeneratorUtils;
import com.gridnine.elsa.gradle.codegen.common.TypeScriptCodeGenerator;
import com.gridnine.elsa.meta.l10n.L10nMetaRegistry;
import com.gridnine.elsa.meta.l10n.L10nTypesRegistry;
import com.gridnine.elsa.meta.remoting.RemotingTypesRegistry;
import com.gridnine.elsa.meta.serialization.SerializableMetaRegistry;
import com.gridnine.elsa.meta.serialization.SerializableTypesRegistry;
import kotlin.Pair;

import java.io.File;
import java.util.Map;
import java.util.Set;

public class TSL10nCodeGen {

    public void generate(L10nMetaRegistry registry, SerializableMetaRegistry sRegistry, SerializableTypesRegistry stRegistry, L10nTypesRegistry l10ntr, File module, Set<File> generatedFiles, String packageName, File projectFolder, Map<String, Pair<String, String>> associations) throws Exception {
        var gen = new TypeScriptCodeGenerator(packageName, module, projectFolder, associations);

        gen.wrapWithBlock("const l10nFactory = ", () -> {
            for (var bundle : registry.getBundles().values()) {
                gen.getTsImports().add("elsa-core:ensureBundleLoaded");
                gen.getTsImports().add("elsa-core:L10nCallOptions");
                gen.blankLine();
                gen.printLine("ensureLoaded: (options?: L10nCallOptions) => ensureBundleLoaded('%s', options),".formatted(bundle.getId()));
                for (var message : bundle.getMessages().values()) {
                    gen.getTsImports().add("elsa-core:getMessage");
                    var args1 = new StringBuilder();
                    var args2 = new StringBuilder();
                    message.getParameters().values().forEach(pd ->{
                        if(!args1.isEmpty()){
                            args1.append(", ");
                        }
                        var descr = new GenPropertyDescription();
                        descr.setTagName(pd.getTagName());
                        descr.setId(pd.getId());
                        descr.setNonNullable(pd.isNonNullable());
                        descr.setTagDescription(l10ntr.getParameterTypeTags().get(descr.getTagName()));
                        args1.append("%s: %s".formatted(pd.getId(), TsCodeGeneratorUtils.calculateDeclarationType(descr, stRegistry, gen)));
                        args2.append(", %s".formatted(pd.getId()));
                    });
                    gen.blankLine();
                    gen.printLine("%s: (%s) => getMessage('%s', '%s'%s),".formatted(message.getId(), args1.toString(), bundle.getId(), message.getId(), args2.toString()));
                }
            }
        });
        gen.print(";\n");
        gen.printLine("export default l10nFactory;\n");
        var file = TsCodeGeneratorUtils.saveIfDiffers(gen.toString(), module);
        generatedFiles.add(file);
    }
}
