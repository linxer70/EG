/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.vaadin.flow.server.Command
 */
package egframe.common.SysChat;

import com.vaadin.flow.server.Command;
import java.io.Serializable;
import java.util.concurrent.ConcurrentLinkedQueue;

class ExecutionQueue
implements Serializable {
    private final ConcurrentLinkedQueue<Command> inbox = new ConcurrentLinkedQueue();

    ExecutionQueue() {
    }

    void add(Command command) {
        this.inbox.add(command);
    }

    void runPendingCommands() {
        Command command;
        while ((command = this.inbox.poll()) != null) {
            command.execute();
        }
    }

    boolean isEmpty() {
        return this.inbox.isEmpty();
    }
}
