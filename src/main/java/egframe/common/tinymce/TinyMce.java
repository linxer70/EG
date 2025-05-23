package egframe.common.tinymce;

import com.vaadin.flow.component.AbstractCompositeField;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.Focusable;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.dom.DomEventListener;
import com.vaadin.flow.dom.DomListenerRegistration;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.dom.ShadowRoot;
import com.vaadin.flow.function.SerializableConsumer;
import com.vaadin.flow.shared.Registration;
import elemental.json.Json;
import elemental.json.JsonArray;
import elemental.json.JsonObject;
import elemental.json.JsonValue;
import egframe.common.*;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;
@Tag("div")
@JavaScript("context://frontend/tinymceConnector.js")
@StyleSheet("context://frontend/tinymceLumo.css")
public class TinyMce extends AbstractCompositeField<Div, TinyMce, String>
        implements HasSize, Focusable<TinyMce> {

    private final DomListenerRegistration domListenerRegistration;
    private String id;
    private boolean initialContentSent;
    private String currentValue = "";
    private String rawConfig;
    public JsonObject config = Json.createObject();
    private Element ta = new Element("div");

    private int debounceTimeout = 0;
    private boolean basicTinyMCECreated;
    private boolean enabled = true;
    private boolean readOnly = false;

    @Deprecated
    public TinyMce(boolean shadowRoot) {
        super("");
        //setHeight("500px");
        
        ta.getStyle().set("height", "100%");
         if (shadowRoot) {
            ShadowRoot shadow = getElement().attachShadow();
            shadow.appendChild(ta);
        } else {
            getElement().appendChild(ta);
        }
         
        domListenerRegistration = getElement().addEventListener("tchange",
                (DomEventListener) event -> {
                    boolean value = event.getEventData()
                            .hasKey("event.htmlString");
                    String htmlString = event.getEventData()
                            .getString("event.htmlString");
                    currentValue = htmlString;
                    setModelValue(htmlString, true);
                });
        domListenerRegistration.addEventData("event.htmlString");
        domListenerRegistration.debounce(debounceTimeout);
    }

    public void setValueChangeMode(ValueChangeMode mode) {
        if (mode == ValueChangeMode.BLUR) {
            runBeforeClientResponse(ui -> {
                getElement().callJsFunction("$connector.setMode", "blur");
            });
        } else if (mode == ValueChangeMode.TIMEOUT) {
            runBeforeClientResponse(ui -> {
                getElement().callJsFunction("$connector.setMode", "timeout");
            });
        } else if (mode == ValueChangeMode.CHANGE) {
            runBeforeClientResponse(ui -> {
                getElement().callJsFunction("$connector.setMode", "change");
            });
        }
    }

    public void setDebounceTimeout(int debounceTimeout) {
        if (debounceTimeout > 0) {
            runBeforeClientResponse(ui -> {
                getElement().callJsFunction("$connector.setEager", "timeout");
            });
        } else {
            runBeforeClientResponse(ui -> {
                getElement().callJsFunction("$connector.setEager", "change");
            });
        }
        domListenerRegistration.debounce(debounceTimeout);
    }

    public TinyMce() {
        this(false);
        this.configure("skin", "oxide-dark");
        this.configure("statusbar", false);
		String config = "{"
                + "  language: "
                + "    'ko_KR'"
                + "  "
                + "}";
		this.setConfig(config);
		this.configurePlugin(true, Plugin.FORMULA);
		this.configureMenubar(true, Menubar.FORMAT);
		this.configurePlugin(true,Plugin.TABLE).configureMenubar(true,Menubar.TABLE).configureToolbar(true,Toolbar.TABLE);

        //this.configure("menubar", "false");
        this.configure("toolbar", "false");
        this.setWidthFull();
    }

    @Deprecated(forRemoval = true)
    public void setEditorContent(String html) {
        setPresentationValue(html);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        if (id == null) {
            id = UUID.randomUUID().toString();
            ta.setAttribute("id", id);
        }
        if (!getEventBus().hasListener(BlurEvent.class)) {
            addBlurListener(e -> {
            });
        }
        if (!attachEvent.isInitialAttach()) {
            initialContentSent = true;
        }
        super.onAttach(attachEvent);
        if (attachEvent.isInitialAttach())
            injectTinyMceScript();
        initConnector();
        saveOnClose();
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        if (isVisible()) {
            detachEvent.getUI().getPage().executeJs("""
                    tinymce.get($0).remove();
                    """, id);
        }
        super.onDetach(detachEvent);
        initialContentSent = false;
    }

    @SuppressWarnings("deprecation")
    private void initConnector() {

        runBeforeClientResponse(ui -> {
            if(rawConfig == null) {
                rawConfig = "{}";
            }
            ui.getPage().executeJs(
                    "const editor = $0;" +
                    "const rawconfig = " + rawConfig + "; " +
                    "window.Vaadin.Flow.tinymceConnector.initLazy(rawconfig, $0, $1, $2, $3, $4)",
                    getElement(), ta, config, currentValue,
                    (enabled && !readOnly))
                    .then(res -> initialContentSent = true);
        });
        //System.out.println("AAAAAAAAAAAAAAAAA"+this.id);
    }
    
    public void saveOnClose(){
        runBeforeClientResponse(ui -> {
            getElement().callJsFunction("$connector.saveOnClose");});
    }

    void runBeforeClientResponse(SerializableConsumer<UI> command) {
        getElement().getNode().runWhenAttached(ui -> ui
                .beforeClientResponse(this, context -> command.accept(ui)));
    }

    public String getCurrentValue() {
        return currentValue;
    }
    public void setConfig(String jsConfig) {
        this.rawConfig = jsConfig;
    }

    public TinyMce configure(String configurationKey, String value) {
        config.put(configurationKey, value);
        return this;
    }

    public TinyMce configure(String configurationKey, String... value) {
        JsonArray array = Json.createArray();
        for (int i = 0; i < value.length; i++) {
            array.set(i, value[i]);
        }
        config.put(configurationKey, array);
        return this;
    }

    public TinyMce configure(String configurationKey, boolean value) {
        config.put(configurationKey, value);
        return this;
    }

    public TinyMce configure(String configurationKey, double value) {
        config.put(configurationKey, value);
        return this;
    }

    public TinyMce configureLanguage(Language language) {
        config.put("language", language.toString());
        return this;
    }
    public void replaceSelectionContent(String htmlString) {
        runBeforeClientResponse(ui -> getElement().callJsFunction(
                "$connector.replaceSelectionContent", htmlString));
    }
    protected void injectTinyMceScript() {
        getUI().get().getPage().addJavaScript(
                "context://frontend/tinymce_addon/tinymce/tinymce.min.js");
    }

    @Override
    public void focus() {
        runBeforeClientResponse(ui -> {
            getElement().executeJs("""
                    const el = this;
                    if(el.$connector.isInDialog()) {
                        setTimeout(() => {
                            el.$connector.focus()
                        }, 150);
                    } else {
                        el.$connetor.focus();
                    }
                    """);
            ;
        });
    }

    @Override
    public Registration addFocusListener(
            ComponentEventListener<FocusEvent<TinyMce>> listener) {
        DomListenerRegistration domListenerRegistration = getElement()
                .addEventListener("tfocus", event -> listener
                        .onComponentEvent(new FocusEvent<>(this, false)));
        return domListenerRegistration;
    }

    @Override
    public Registration addBlurListener(
            ComponentEventListener<BlurEvent<TinyMce>> listener) {
        DomListenerRegistration domListenerRegistration = getElement()
                .addEventListener("tblur", event -> listener
                        .onComponentEvent(new BlurEvent<>(this, false)));
        return domListenerRegistration;
    }

    @Override
    public void blur() {
        throw new RuntimeException(
                "Not implemented, TinyMce does not support programmatic blur.");
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        adjustEnabledState();
    }

    private void adjustEnabledState() {
        boolean reallyEnabled = this.enabled && !this.readOnly;
        super.setEnabled(reallyEnabled);
        runBeforeClientResponse(ui -> getElement()
                .callJsFunction("$connector.setEnabled", reallyEnabled));
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
        super.setReadOnly(readOnly);
        adjustEnabledState();
    }

    @Override
	public void setPresentationValue(String html) {
        this.currentValue = html;
        if (initialContentSent) {
            runBeforeClientResponse(ui -> getElement()
                    .callJsFunction("$connector.setEditorContent", html));
        }
    }

    private TinyMce createBasicTinyMce() {
        setValue("");
        this.configure("branding", false);

        this.basicTinyMCECreated = true;
        this.configurePlugin(false, Plugin.ADVLIST, Plugin.AUTOLINK,
                Plugin.LISTS, Plugin.SEARCH_REPLACE,Plugin.FORMULA);
        this.configureMenubar(false, Menubar.FILE, Menubar.EDIT, Menubar.VIEW,
                Menubar.FORMAT);
        this.configureToolbar(false, Toolbar.UNDO, Toolbar.REDO,
                Toolbar.SEPARATOR, Toolbar.FORMAT_SELECT, Toolbar.SEPARATOR,
                Toolbar.BOLD, Toolbar.ITALIC, Toolbar.SEPARATOR,
                Toolbar.ALIGN_LEFT, Toolbar.ALIGN_CENTER, Toolbar.ALIGN_RIGHT,
                Toolbar.ALIGN_JUSTIFY, Toolbar.SEPARATOR, Toolbar.OUTDENT,
                Toolbar.INDENT,Toolbar.FORMULA);
        
        return this;

    }

    public TinyMce configurePlugin(boolean basicTinyMCE, Plugin... plugins) {
        if (basicTinyMCE && !basicTinyMCECreated) {
            createBasicTinyMce();
        }

        JsonArray jsonArray = config.get("plugins");
        int initialIndex = 0;

        if (jsonArray != null) {
            initialIndex = jsonArray.length();
        } else {
            jsonArray = Json.createArray();
        }

        for (int i = 0; i < plugins.length; i++) {
            jsonArray.set(initialIndex, plugins[i].pluginLabel);
            initialIndex++;
        }

        config.put("plugins", jsonArray);
        return this;
    }

    public TinyMce configureMenubar(boolean basicTinyMCE, Menubar... menubars) {
        if (basicTinyMCE && !basicTinyMCECreated) {
            createBasicTinyMce();
        }

        String newconfig = Arrays.stream(menubars).map(m -> m.menubarLabel)
                .collect(Collectors.joining(" "));

        String menubar;
        if (config.hasKey("menubar")) {
            menubar = config.getString("menubar");
            menubar = menubar + " " + newconfig;
        } else {
            menubar = newconfig;
        }

        config.put("menubar", menubar);
        return this;
    }

    public TinyMce configureToolbar(boolean basicTinyMCE, Toolbar... toolbars) {
        if (basicTinyMCE && !basicTinyMCECreated) {
            createBasicTinyMce();
        }

        JsonValue jsonValue = config.get("toolbar");
        String toolbarStr = "";

        if (jsonValue != null) {
            toolbarStr = toolbarStr.concat(jsonValue.asString());
        }

        for (int i = 0; i < toolbars.length; i++) {
            toolbarStr = toolbarStr.concat(" ").concat(toolbars[i].toolbarLabel)
                    .concat(" ");
        }

        config.put("toolbar", toolbarStr);
        return this;
    }
    
    public String getID() {
    	return this.id;
    }
}
