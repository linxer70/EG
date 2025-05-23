/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.vaadin.flow.component.Component
 *  com.vaadin.flow.component.Composite
 *  com.vaadin.flow.component.HasSize
 *  com.vaadin.flow.component.HasStyle
 *  com.vaadin.flow.component.HasTheme
 *  com.vaadin.flow.component.avatar.Avatar
 *  com.vaadin.flow.component.avatar.AvatarGroup
 *  com.vaadin.flow.component.avatar.AvatarGroup$AvatarGroupI18n
 *  com.vaadin.flow.component.avatar.AvatarGroup$AvatarGroupItem
 *  com.vaadin.flow.component.avatar.AvatarGroupVariant
 *  com.vaadin.flow.component.shared.HasOverlayClassName
 *  com.vaadin.flow.component.shared.ThemeVariant
 *  com.vaadin.flow.function.SerializableSupplier
 *  com.vaadin.flow.internal.UsageStatistics
 *  com.vaadin.flow.server.AbstractStreamResource
 *  com.vaadin.flow.shared.Registration
 */
package egframe.common.SysChat;

import egframe.common.SysChat.CollaborationEngine;
import egframe.common.SysChat.ComponentConnectionContext;
import egframe.common.SysChat.ConnectionContext;
import egframe.common.SysChat.PresenceManager;
import egframe.frame.entity.sys_user;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.HasTheme;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.avatar.AvatarGroup;
import com.vaadin.flow.component.avatar.AvatarGroupVariant;
import com.vaadin.flow.component.shared.HasOverlayClassName;
import com.vaadin.flow.component.shared.ThemeVariant;
import com.vaadin.flow.function.SerializableSupplier;
import com.vaadin.flow.internal.UsageStatistics;
import com.vaadin.flow.server.AbstractStreamResource;
import com.vaadin.flow.shared.Registration;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CollaborationAvatarGroup
extends Composite<AvatarGroup>
implements HasSize,
HasStyle,
HasTheme,
HasOverlayClassName {
    private final SerializableSupplier<CollaborationEngine> ceSupplier;
    private final sys_user localUser;
    private final List<sys_user> userInfoCache = new ArrayList<sys_user>();
    private PresenceManager presenceManager;
    private String topicId;
    private ImageProvider imageProvider;
    private boolean ownAvatarVisible;

    public CollaborationAvatarGroup(sys_user localUser, String topicId) {
        this(localUser, topicId, (SerializableSupplier<CollaborationEngine>)((SerializableSupplier & Serializable)CollaborationEngine::getInstance));
    }

    CollaborationAvatarGroup(sys_user localUser, String topicId, SerializableSupplier<CollaborationEngine> ceSupplier) {
        this.localUser = Objects.requireNonNull(localUser, "User cannot be null");
        this.ceSupplier = ceSupplier;
        this.ownAvatarVisible = true;
        this.setTopic(topicId);
        this.refreshItems();
    }

    public void setTopic(String topicId) {
        if (Objects.equals(this.topicId, topicId)) {
            return;
        }
        if (this.presenceManager != null) {
            this.presenceManager.close();
            this.presenceManager = null;
        }
        this.topicId = topicId;
        if (topicId != null) {
            this.presenceManager = new PresenceManager((ConnectionContext)new ComponentConnectionContext((Component)this), this.localUser, topicId, this.ceSupplier);
            this.presenceManager.markAsPresent(true);
            this.presenceManager.setPresenceHandler(context -> {
                sys_user userInfo = context.getUser();
                this.userInfoCache.add(userInfo);
                this.refreshItems();
                return (Registration & Serializable)() -> {
                    this.userInfoCache.remove(userInfo);
                    this.refreshItems();
                };
            });
        }
    }

    public Integer getMaxItemsVisible() {
        return ((AvatarGroup)this.getContent()).getMaxItemsVisible();
    }

    public void setMaxItemsVisible(Integer max) {
        ((AvatarGroup)this.getContent()).setMaxItemsVisible(max);
    }

    public void addThemeVariants(AvatarGroupVariant ... variants) {
        //origin
    	//((AvatarGroup)this.getContent()).addThemeVariants((ThemeVariant[])variants);
        AvatarGroupVariant[] avatarGroupVariants = Arrays.stream(variants)
        	    .map(v -> (AvatarGroupVariant) v)  // 변환 로직
        	    .toArray(AvatarGroupVariant[]::new);

        	((AvatarGroup) this.getContent()).addThemeVariants(avatarGroupVariants);
    }

    public void removeThemeVariants(AvatarGroupVariant ... variants) {
    	//origin
        //((AvatarGroup)this.getContent()).removeThemeVariants((ThemeVariant[])variants);
        AvatarGroupVariant[] avatarGroupVariants = Arrays.stream(variants)
        	    .map(v -> (AvatarGroupVariant) v)  // 변환 로직
        	    .toArray(AvatarGroupVariant[]::new);
        ((AvatarGroup)this.getContent()).removeThemeVariants(avatarGroupVariants);

    }

    public AvatarGroup.AvatarGroupI18n getI18n() {
        return ((AvatarGroup)this.getContent()).getI18n();
    }

    public void setI18n(AvatarGroup.AvatarGroupI18n i18n) {
        ((AvatarGroup)this.getContent()).setI18n(i18n);
    }

    private void refreshItems() {
        List items = Stream.concat(Stream.of(this.localUser), this.userInfoCache.stream()).distinct().filter(user -> this.ownAvatarVisible || this.isNotLocalUser((sys_user)user)).map(this::userToAvatarGroupItem).collect(Collectors.toList());
        ((AvatarGroup)this.getContent()).setItems(items);
    }

    private AvatarGroup.AvatarGroupItem userToAvatarGroupItem(sys_user user) {
        AvatarGroup.AvatarGroupItem item = new AvatarGroup.AvatarGroupItem();
        item.setName(user.getUserNm());
        item.setAbbreviation(user.getAbbreviation());
        if (this.imageProvider == null) {
            item.setImage(user.getImage());
        } else {
            item.setImageResource(this.imageProvider.getImageResource(user));
        }
        item.setColorIndex(Integer.valueOf(this.getCollaborationEngine().getUserColorIndex(user)));
        return item;
    }

    private CollaborationEngine getCollaborationEngine() {
        return (CollaborationEngine)this.ceSupplier.get();
    }

    private boolean isNotLocalUser(sys_user user) {
        return !this.localUser.equals(user);
    }

    public void setImageProvider(ImageProvider imageProvider) {
        this.imageProvider = imageProvider;
        this.refreshItems();
    }

    public ImageProvider getImageProvider() {
        return this.imageProvider;
    }

    public boolean isOwnAvatarVisible() {
        return this.ownAvatarVisible;
    }

    public void setOwnAvatarVisible(boolean ownAvatarVisible) {
        this.ownAvatarVisible = ownAvatarVisible;
        this.refreshItems();
    }

    public Avatar createOwnAvatar() {
        return new Avatar(this.localUser.getUserNm(), this.localUser.getImage());
    }

    static {
        UsageStatistics.markAsUsed((String)"vaadin-collaboration-engine/CollaborationAvatarGroup", (String)"6.4");
    }

    @FunctionalInterface
    public static interface ImageProvider {
        public AbstractStreamResource getImageResource(sys_user var1);
    }
}
