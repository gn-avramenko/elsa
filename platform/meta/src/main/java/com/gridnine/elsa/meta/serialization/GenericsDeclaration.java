/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.meta.serialization;

import java.util.ArrayList;
import java.util.List;

public class GenericsDeclaration implements GenericDeclaration {
    private final List<GenericDeclaration> generics = new ArrayList<>();

    public List<GenericDeclaration> getGenerics() {
        return generics;
    }
}
