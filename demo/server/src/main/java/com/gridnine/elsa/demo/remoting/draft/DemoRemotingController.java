/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.demo.remoting.draft;

import com.gridnine.elsa.server.core.remoting.BaseRemotingController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/remoting/demo")
public class DemoRemotingController extends BaseRemotingController {

    public DemoRemotingController() {
        super("demo");
    }


}
