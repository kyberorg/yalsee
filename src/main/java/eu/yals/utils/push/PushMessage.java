package eu.yals.utils.push;

import lombok.Data;

@Data(staticConstructor = "withCommand")
public class PushMessage {
    private final PushCommand command;
}
