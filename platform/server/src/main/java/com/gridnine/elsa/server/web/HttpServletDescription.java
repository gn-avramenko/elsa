/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.web;

import jakarta.servlet.http.HttpServlet;


public record HttpServletDescription<T extends HttpServlet>(Class<T> servletClass, String path) {
}
