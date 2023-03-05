package com.gridnine.elsa.gradle.internal

annotation class ElsaInternalJavaConfigMarker

@ElsaInternalJavaConfigMarker
open class ElsaInternalJavaExtension {
    var artefactId: String? = null
}