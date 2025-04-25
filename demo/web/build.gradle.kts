plugins {
    id("elsa-web")
}

elsa {
    codegen{
        remoting("src/generated", arrayListOf("../server/src/main/codegen/demo-remoting.xml"))
        l10n("src/generated", "../../platform/server-core/src/main/codegen/core-server-l10n-messages.xml")
    }
}