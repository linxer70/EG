package egframe.common.SysChat;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import egframe.common.SysChat.AbstractCollaborationManager;
import egframe.common.SysChat.CollaborationEngine;
import egframe.common.SysChat.CollaborationEngineServiceInitListener;
import egframe.common.SysChat.CollaborationList;
import egframe.common.SysChat.CollaborationMap;
import egframe.common.SysChat.ComponentConnectionContext;
import egframe.common.SysChat.ConnectionContext;
import egframe.common.SysChat.EntryScope;
import egframe.common.SysChat.HasExpirationTimeout;
import egframe.common.SysChat.HighlightHandler;
import egframe.common.SysChat.HighlightHandler.HighlightContext;
import egframe.common.SysChat.ListChangeEvent;
import egframe.common.SysChat.ListKey;
import egframe.common.SysChat.ListOperation;
import egframe.common.SysChat.MapChangeEvent;
import egframe.common.SysChat.PropertyChangeHandler;
import egframe.common.SysChat.PropertyChangeHandler.PropertyChangeEvent;
import egframe.common.SysChat.TopicConnection;
import egframe.frame.entity.sys_user;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.function.SerializableFunction;
import com.vaadin.flow.function.SerializableSupplier;
import com.vaadin.flow.internal.UsageStatistics;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.shared.Registration;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serial;
import java.io.Serializable;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class FormManager extends AbstractCollaborationManager
implements HasExpirationTimeout {
/**
* Maps the focused user to the index of the focused element inside the
* field. The index is needed for components such as radio button group,
* where the highlight should be displayed on an individual radio button
* inside the group.
*/
static final class FocusedEditor implements Serializable {
public final sys_user user;
public final int fieldIndex;
public final String propertyName;

@JsonCreator
public FocusedEditor(@JsonProperty("user") sys_user user,
        @JsonProperty("fieldIndex") int fieldIndex,
        @JsonProperty("propertyName") String propertyName) {
    this.user = user;
    this.fieldIndex = fieldIndex;
    this.propertyName = propertyName;
}

@Override
public boolean equals(Object other) {
    if (other instanceof FocusedEditor) {
        FocusedEditor otherEditor = (FocusedEditor) other;
        return Objects.equals(otherEditor.user.getUserCd(), user.getUserCd())
                && Objects.equals(otherEditor.propertyName,
                        propertyName);
    }
    return false;
}

@Override
public int hashCode() {
    return Objects.hash(user.getUserCd(), propertyName);
}
}

private static class UserEntry implements Serializable {
private int count = 0;
private Registration registration;
}

static {
UsageStatistics.markAsUsed(
        CollaborationEngine.COLLABORATION_ENGINE_NAME + "/FormManager",
        CollaborationEngine.COLLABORATION_ENGINE_VERSION);
}

static final String COLLECTION_NAME = FormManager.class.getName();

private transient CollaborationMap map;
private transient CollaborationList list;
private ConnectionContext context;
private final Map<FocusedEditor, UserEntry> userEntries = new LinkedHashMap<>();

private PropertyChangeHandler propertyChangeHandler;
private HighlightHandler highlightHandler;
private transient Registration subscribeRegistration;
private Duration expirationTimeout;

/**
* Creates a new manager for the provided connection context.
* <p>
* The provided user information is used to set the highlight for the local
* user with {@link #highlight(String,boolean)} or
* {@link #highlight(String,boolean,int)} (the default is {@code false}).
*
* @param context
*            the context that manages connection status, not {@code null}
* @param localUser
*            the information of the local user, not {@code null}
* @param topicId
*            the id of the topic to connect to, not {@code null}
* @param collaborationEngine
*            the collaboration engine instance to use, not {@code null}
* @deprecated This constructor is not compatible with serialization
*/
@Deprecated(since = "6.1", forRemoval = true)
public FormManager(ConnectionContext context, sys_user localUser,
    String topicId, CollaborationEngine collaborationEngine) {
this(context, localUser, topicId, () -> collaborationEngine);
}

/**
* Creates a new manager for the provided connection context.
* <p>
* The provided user information is used to set the highlight for the local
* user with {@link #highlight(String,boolean)} or
* {@link #highlight(String,boolean,int)} (the default is {@code false}).
*
* @param context
*            the context that manages connection status, not {@code null}
* @param localUser
*            the information of the local user, not {@code null}
* @param topicId
*            the id of the topic to connect to, not {@code null}
* @param ceSupplier
*            the collaboration engine instance to use, not {@code null}
*/
public FormManager(ConnectionContext context, sys_user localUser,
    String topicId,
    SerializableSupplier<CollaborationEngine> ceSupplier) {
super(localUser, topicId, ceSupplier);
this.context = context;
openTopicConnection(context, this::onConnectionActivate);
}

/**
* Creates a new manager for the provided component.
* <p>
* The provided user information is used to set the highlight for the local
* user with {@link #highlight(String,boolean)} or
* {@link #highlight(String,boolean,int)} (the default is {@code false}).
*
* @param component
*            the component which holds UI access, not {@code null}
* @param localUser
*            the information of the local user, not {@code null}
* @param topicId
*            the id of the topic to connect to, not {@code null}
*/
public FormManager(Component component, sys_user localUser,
    String topicId) {
this(new ComponentConnectionContext(component), localUser, topicId,
        CollaborationEngine::getInstance);
}

@Serial
private void readObject(ObjectInputStream in)
    throws IOException, ClassNotFoundException {
in.defaultReadObject();
CollaborationEngineServiceInitListener
        .addReinitializer(this::reinitialize);
}

private void reinitialize(VaadinService service) {
openTopicConnection(context, this::onConnectionActivate);
}

/**
* Sets the value for the provided property name.
*
* @param propertyName
*            the name of the property that is being set, not {@code null}
* @param value
*            the value to set
*/
public void setValue(String propertyName, Object value) {
Objects.requireNonNull(propertyName, "Property name can't be null.");
if (map != null) {
    map.put(propertyName, value);
}
}

<T> T getValue(String propertyName, Class<T> type) {
if (map != null) {
    return map.get(propertyName, type);
} else {
    return null;
}
}

<T> T getValue(String propertyName, TypeReference<T> typeReference) {
if (map != null) {
    return map.get(propertyName, typeReference);
} else {
    return null;
}
}

/**
* Sets the highlight state of the provided property name for the local
* user. For properties that require a field index, this defaults to 0.
*
* @param propertyName
*            the name of the property to highlight or remove highlight, not
*            {@code null}
* @param highlight
*            the highlight state to set
*/
public void highlight(String propertyName, boolean highlight) {
highlight(propertyName, highlight, getLocalUser());
}

/**
* Sets the highlight state at the provided field index of the provided
* property name for the local user.
*
* @param propertyName
*            the name of the property to highlight or unhighlight, not
*            {@code null}
* @param highlight
*            the highlight state to set
* @param fieldIndex
*            the field index to highlight or unhighlight
*/
public void highlight(String propertyName, boolean highlight,
    int fieldIndex) {
highlight(propertyName, highlight, getLocalUser(), fieldIndex);
}

/**
* Sets the highlight state of the provided property name for the provided
* user. For properties that require a field index, this defaults to 0.
*
* @param propertyName
*            the name of the property to highlight or unhighlight, not
*            {@code null}
* @param highlight
*            the highlight state to set
* @param user
*            the user that the highlight corresponds to, not {@code null}
*/
void highlight(String propertyName, boolean highlight, sys_user user) {
highlight(propertyName, highlight, user, 0);
}

/**
* Sets the highlight state at the provided field index of the provided
* property name for the provided user.
*
* @param propertyName
*            the name of the property to highlight or unhighlight, not
*            {@code null}
* @param highlight
*            the highlight states to set
* @param user
*            the user that the highlight corresponds to, not {@code null}
* @param fieldIndex
*            the field index to highlight or unhighlight
*/
void highlight(String propertyName, boolean highlight, sys_user user,
    int fieldIndex) {
Objects.requireNonNull(propertyName, "Property name can't be null.");
Objects.requireNonNull(user, "User can't be null.");

if (list != null) {
    if (highlight) {
        ListOperation operation = ListOperation.insertLast(
                new FocusedEditor(user, fieldIndex, propertyName))
                .withScope(EntryScope.CONNECTION);
        list.apply(operation);
    } else {
        list.getKeys().forEach(key -> {
            FocusedEditor editor = list.getItem(key,
                    FocusedEditor.class);
            if (editor.propertyName.equals(propertyName)
                    && editor.user.equals(user)) {
                ListOperation operation = ListOperation.set(key, null)
                        .withScope(EntryScope.CONNECTION);
                list.apply(operation);
            }
        });
    }
}
}

boolean isHighlight(String propertyName) {
if (list != null) {
    for (FocusedEditor editor : list.getItems(FocusedEditor.class)) {
        if (editor.propertyName.equals(propertyName)) {
            return true;
        }
    }
}
return false;
}

/**
* Sets a handler which will be invoked when a property changes value.
* <p>
* The handler accepts a {@link PropertyChangeEvent} instance as a
* parameter.
*
* @param handler
*            the property change handler, or {@code null} to remove an
*            existing handler
*/
public void setPropertyChangeHandler(PropertyChangeHandler handler) {
propertyChangeHandler = handler;
if (handler != null && map != null) {
    map.subscribe(this::onMapChange);
}
}

/**
* Sets a handler which will be invoked when a highlight is set.
* <p>
* The handler accepts a {@link HighlightContext} instance as a parameter
* and should return a {@link Registration} which will be removed when the
* highlight is toggled off.
* <p>
* Replacing an existing handler will remove all registrations from the
* previous one.
*
* @param handler
*            the highlight handler, or {@code null} to remove an existing
*            handler
*/
public void setHighlightHandler(HighlightHandler handler) {
resetEntries();
highlightHandler = handler;
if (handler != null && list != null) {
    subscribeRegistration = list.subscribe(this::onListChange);
}
}

/**
* Sets the expiration timeout of the field property data. If set, data is
* cleared when {@code expirationTimeout} has passed after the last
* connection to the related topic is closed. If set to {@code null}, the
* timeout is cancelled.
*
* @param expirationTimeout
*            the expiration timeout
*/
@Override
public void setExpirationTimeout(Duration expirationTimeout) {
this.expirationTimeout = expirationTimeout;
if (map != null) {
    map.setExpirationTimeout(expirationTimeout);
}
}

/**
* Gets the optional expiration timeout of the field property data. An empty
* {@link Optional} is returned if no timeout it set, which means data is
* not cleared when there are no connected users to the related topic.
*
* @return the expiration timeout
*/
@Override
public Optional<Duration> getExpirationTimeout() {
return Optional.ofNullable(expirationTimeout);
}

private Registration onConnectionActivate(TopicConnection topicConnection) {
map = topicConnection.getNamedMap(COLLECTION_NAME);
map.subscribe(this::onMapChange);

list = topicConnection.getNamedList(COLLECTION_NAME);
if (this.highlightHandler != null && subscribeRegistration == null) {
    subscribeRegistration = list.subscribe(this::onListChange);
}

if (expirationTimeout != null) {
    map.setExpirationTimeout(expirationTimeout);
}

return this::onConnectionDeactivate;
}

private void onConnectionDeactivate() {
map = null;
list = null;
resetEntries();
}

private void onMapChange(MapChangeEvent event) {
applyPropertyChangeHandler(event.getKey(),
        event.getValue(Object.class));
}

private void onListChange(ListChangeEvent event) {
switch (event.getType()) {
case INSERT:
    FocusedEditor editor = event.getValue(FocusedEditor.class);
    handleNewHighlight(editor);
    break;
case SET:
    if (event.getValue(FocusedEditor.class) == null) {
        FocusedEditor oldEditor = event
                .getOldValue(FocusedEditor.class);
        handleRemovedHighlight(oldEditor);
    } else {
        // Unexpected, but no problem in ignoring
    }
    break;
case MOVE:
    // Unexpected, but no problem in ignoring
}
}

private void applyPropertyChangeHandler(String propertyName, Object value) {
if (propertyChangeHandler != null) {
    PropertyChangeHandler.PropertyChangeEvent event = new DefaultPropertyChangeEvent(
            propertyName, value);
    propertyChangeHandler.handlePropertyChange(event);
}
}

private void handleNewHighlight(FocusedEditor editor) {
UserEntry userEntry = userEntries.computeIfAbsent(editor,
        ignore -> new UserEntry());
if (userEntry.count++ == 0) {
    if (highlightHandler != null) {
    	if (userEntry.registration != null) {
    	    throw new IllegalStateException("userEntry.registration must be null before setting it.");
    	}

    	userEntry.registration = highlightHandler.handleHighlight(
    	        new DefaultHighlightContext(editor.user,
    	                editor.propertyName, editor.fieldIndex));    }
}
}

private void handleRemovedHighlight(FocusedEditor editor) {
UserEntry userEntry = userEntries.get(editor);
if (userEntry == null) {
    throw new IllegalArgumentException("userEntry must not be null");
}

if (--userEntry.count == 0) {
    removeRegistration(userEntry);
    userEntries.remove(editor);
}
}

private void removeRegistration(UserEntry entry) {
Registration registration = entry.registration;
if (registration != null) {
    registration.remove();
    entry.registration = null;
}
}

private void resetEntries() {
if (subscribeRegistration != null) {
    subscribeRegistration.remove();
    subscribeRegistration = null;
}

userEntries.values().forEach(this::removeRegistration);
userEntries.clear();
}

static class DefaultPropertyChangeEvent implements PropertyChangeEvent {
private final String propertyName;
private final Object value;

public DefaultPropertyChangeEvent(String propertyName, Object value) {
    this.propertyName = propertyName;
    this.value = value;
}

@Override
public String getPropertyName() {
    return propertyName;
}

@Override
public Object getValue() {
    return value;
}
}

static class DefaultHighlightContext implements HighlightContext {
private final sys_user user;
private final String propertyName;
private final int fieldIndex;

public DefaultHighlightContext(sys_user user, String propertyName,
        int fieldIndex) {
    this.user = user;
    this.propertyName = propertyName;
    this.fieldIndex = fieldIndex;
}

@Override
public sys_user getUser() {
    return user;
}

@Override
public String getPropertyName() {
    return propertyName;
}

@Override
public int getFieldIndex() {
    return fieldIndex;
}
}
}
