/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.demo.remoting.sse;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@RestController
@RequestMapping("/remoting/public")
public class PublicRestService {

    private SseEmitter emitter;

    @PostMapping("request")
    @ResponseBody
    public TestRestResponse request(@RequestBody TestRestRequest request){
        new Thread("sse"){
            @Override
            public void run() {
                for(int n=0; n< 3; n++){
                    var event = SseEmitter.event()
                            .data("hello server");
                    try {
                        Thread.sleep(2000L);
                        emitter.send(event);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
        var result = new TestRestResponse();
        result.setMessage("Hello world");
        return result;
    }

    @GetMapping(value = "subscribe",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe() throws IOException {
        emitter = new SseEmitter();
        emitter.send(SseEmitter.event()
                .data("started"));
        return emitter;
    }
}
