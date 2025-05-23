package egframe.common.SysChat;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
class EventUtil {
    private EventUtil() {
        // Only static helpers
    }
    static <T> void fireEvents(List<T> listeners, Consumer<T> action,
            boolean removeFailingListeners) {
    	
    	if (action == null) {
    	    throw new IllegalArgumentException("Action cannot be null");
    	}
        if (listeners == null) {
            return;
        }

        RuntimeException firstException = null;
        for (T listener : new ArrayList<>(listeners)) {
            try {
                action.accept(listener);
            } catch (RuntimeException e) {
                if (removeFailingListeners) {
                    listeners.remove(listener);
                }

                if (firstException == null) {
                    firstException = e;
                } else {
                    firstException.addSuppressed(e);
                }
            }
        }

        if (firstException != null) {
            throw firstException;
        }
    }
}