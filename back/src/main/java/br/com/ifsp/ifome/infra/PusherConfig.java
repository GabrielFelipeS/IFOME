package br.com.ifsp.ifome.infra;

import com.pusher.rest.Pusher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class PusherConfig {

    @Bean
    public Pusher createPusher() {
        Pusher pusher = new Pusher("1886660", "a06e37233576d7712076", "d0a8b782ad718d118fb2");
        pusher.setCluster("us2");
        pusher.setEncrypted(true);

//        pusher.trigger("my-channel", "my-event", Collections.singletonMap("message", "hello world"));
        return pusher;
    }
}
