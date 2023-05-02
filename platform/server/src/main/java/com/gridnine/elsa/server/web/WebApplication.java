/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.web;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public record WebApplication(String path, File warOrDir) {}
