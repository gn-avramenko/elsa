/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.core.remoting;

import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class StalledChannelsCleaner {

    private Timer timer;

    private List<BaseRemotingController> controllers = new ArrayList<>();

    @Autowired(required = false)
    private void setControllers(List<BaseRemotingController> ctrs){
        controllers.addAll(ctrs);

        timer = new Timer("stalled-channels-cleaner", true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                controllers.forEach(BaseRemotingController::deleteStalledChannels);
                controllers.forEach(BaseRemotingController::checkStalledClientCalls);

            }
        }, 10000L, 10000L);

    }

    @PreDestroy
    public void preDestroy(){
        timer.cancel();
    }

}
