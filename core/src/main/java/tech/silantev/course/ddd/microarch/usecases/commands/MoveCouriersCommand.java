package tech.silantev.course.ddd.microarch.usecases.commands;

import an.awesome.pipelinr.Command;
import com.github.sviperll.result4j.Result;

public record MoveCouriersCommand(

) implements Command<Result<Void, String>> {

}
