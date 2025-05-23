/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.vaadin.flow.function.SerializableConsumer
 *  com.vaadin.flow.function.SerializableFunction
 */
package egframe.common.SysChat;

import egframe.common.SysChat.CollaborationMessage;
import egframe.common.SysChat.MessageManager;
import com.vaadin.flow.function.SerializableConsumer;
import com.vaadin.flow.function.SerializableFunction;
import java.io.Serializable;
import java.time.Instant;
import java.util.EventObject;
import java.util.Objects;
import java.util.stream.Stream;

public interface CollaborationMessagePersister
extends Serializable {
    public static CollaborationMessagePersister fromCallbacks(final SerializableFunction<FetchQuery, Stream<CollaborationMessage>> fetchCallback, final SerializableConsumer<PersistRequest> persistCallback) {
        Objects.requireNonNull(fetchCallback, "The fetch callback cannot be null");
        Objects.requireNonNull(persistCallback, "The persist callback cannot be null");
        return new CollaborationMessagePersister(){

            @Override
            public Stream<CollaborationMessage> fetchMessages(FetchQuery query) {
                return (Stream)fetchCallback.apply(query);
            }

            @Override
            public void persistMessage(PersistRequest request) {
                persistCallback.accept(request);
            }
        };
    }

    public Stream<CollaborationMessage> fetchMessages(FetchQuery var1);

    public void persistMessage(PersistRequest var1);

    public static class PersistRequest
    extends EventObject {
        private final String topicId;
        private final CollaborationMessage message;

        PersistRequest(MessageManager manager, String topicId, CollaborationMessage message) {
            super(manager);
            this.topicId = topicId;
            this.message = message;
        }

        public String getTopicId() {
            return this.topicId;
        }

        public CollaborationMessage getMessage() {
            return this.message;
        }

        @Override
        public MessageManager getSource() {
            return (MessageManager)super.getSource();
        }
    }

    public static class FetchQuery
    extends EventObject {
        private final String topicId;
        private final Instant since;
        private boolean getSinceCalled = false;
        private boolean getTopicIdCalled = false;

        FetchQuery(MessageManager manager, String topicId, Instant since) {
            super(manager);
            this.topicId = topicId;
            this.since = since;
        }

        public String getTopicId() {
            this.getTopicIdCalled = true;
            return this.topicId;
        }

        public Instant getSince() {
            this.getSinceCalled = true;
            return this.since;
        }

        @Override
        public MessageManager getSource() {
            return (MessageManager)super.getSource();
        }

        void throwIfPropsNotUsed() {
            if (!this.getSinceCalled && !this.getTopicIdCalled) {
                throw new IllegalStateException("FetchQuery.getSince() and FetchQuery.getTopicId() were not called when fetching messages from the persister. These values need to be used to fetch only the messages belonging to the correct topic and submitted after the already fetched messages. Otherwise the message list will display duplicates or messages from other topics.");
            }
            if (!this.getSinceCalled) {
                throw new IllegalStateException("FetchQuery.getSince() was not called when fetching messages from the persister. This value needs to be used to fetch only the messages which have been submitted after the already fetched messages. Otherwise the message list will display duplicates.");
            }
            if (!this.getTopicIdCalled) {
                throw new IllegalStateException("FetchQuery.getTopicId() was not called when fetching messages from the persister. This value needs to be used to fetch only the messages belonging to the correct topic. Otherwise the message list will display messages from other topics.");
            }
        }
    }
}
