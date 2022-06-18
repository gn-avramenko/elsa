/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.demo.remoting.sse;

import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.io.IOException;

@RestController
@RequestMapping("/remoting/private")
public class PrivateRestFluxService {

    private FluxSink<ServerSentEvent<String>> sink;

    @PostMapping("request")
    @ResponseBody
    public TestRestResponse request(@RequestBody TestRestRequest request){
        var result = new TestRestResponse();
        result.setMessage("Hello world");
        new Thread("sse"){
            @Override
            public void run() {
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                sink.next(ServerSentEvent.<String> builder().data("server async").build());
            }
        }.start();
        return result;
    }

    @GetMapping(value = "subscribe",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> subscribe() throws IOException {
        return Flux.create((emitter) ->{
            sink = emitter;
            emitter.next(ServerSentEvent.<String> builder().data("ping").build());
        });
    }

    @GetMapping(value = "check")
    public void check(){
    }


}
