package tech.silantev.course.ddd.microarch.configuration;

import an.awesome.pipelinr.Command;
import an.awesome.pipelinr.Pipeline;
import an.awesome.pipelinr.Pipelinr;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PipelineConfiguration {

    @Bean
    public Pipeline createPipeline(ObjectProvider<Command.Handler> commandHandlers) {
        return new Pipelinr()
                .with(commandHandlers::stream);
    }
}
