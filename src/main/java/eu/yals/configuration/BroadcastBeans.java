package eu.yals.configuration;

import eu.yals.utils.push.PushMessage;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.UnicastProcessor;

@Component
public class BroadcastBeans {

    @Bean
    UnicastProcessor<PushMessage> publisher() {
        return UnicastProcessor.create();
    }

    @Bean
    Flux<PushMessage> messages(UnicastProcessor<PushMessage> publisher) {
        return publisher.replay(30).autoConnect();
    }
}
