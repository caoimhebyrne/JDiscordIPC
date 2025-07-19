package dev.caoimhe.jdiscordipc.activity.model.assets;

import org.jspecify.annotations.Nullable;

/**
 * A builder for {@link ActivityAssets}.
 */
public class ActivityAssetsBuilder {
    /**
     * The unique identifier (as it is in the developer portal) or URL for the large image.
     * <p>
     * The large image is rendered as a large square icon on a user's rich presence.
     */
    private @Nullable String largeImage;

    /**
     * A tooltip string that is shown when the user hovers over the {@link #largeImage}.
     */
    private @Nullable String largeText;

    /**
     * The unique identifier (as it is in the developer portal) or URL for the small image.
     * <p>
     * The small image is rendered as a small circle on top of the {@link #largeImage}.
     */
    private @Nullable String smallImage;

    /**
     * A tooltip string that is shown when the user hovers over the {@link #smallImage}.
     */
    private @Nullable String smallText;

    /**
     * Initializes a new {@link ActivityAssetsBuilder} with no assets.
     */
    public ActivityAssetsBuilder() {
        this.largeImage = null;
        this.largeText = null;
        this.smallImage = null;
        this.smallText = null;
    }

    /**
     * Sets the large image, which is rendered as a large square icon on a user's rich presence.
     *
     * @param id The unique identifier (as it is in the developer portal) or URL for the large image.
     */
    public ActivityAssetsBuilder largeImage(final String id) {
        this.largeImage = id;
        return this;
    }

    /**
     * Sets the large image, which is rendered as a large square icon on a user's rich presence.
     *
     * @param id   The unique identifier (as it is in the developer portal) or URL for the large image.
     * @param text The text to display as a tooltip when hovering over the large image.
     */
    public ActivityAssetsBuilder largeImage(final String id, final String text) {
        this.largeImage = id;
        this.largeText = text;
        return this;
    }

    /**
     * Sets the small image, which is rendered as a small circle on top of the large icon.
     *
     * @param id The unique identifier (as it is in the developer portal) or URL for the small image.
     */
    public ActivityAssetsBuilder smallImage(final String id) {
        this.smallImage = id;
        return this;
    }

    /**
     * Sets the large image, which is rendered as a large square icon on a user's rich presence.
     *
     * @param id   The unique identifier (as it is in the developer portal) or URL for the small image.
     * @param text The text to display as a tooltip when hovering over the small image.
     */
    public ActivityAssetsBuilder smallImage(final String id, final String text) {
        this.smallImage = id;
        this.smallText = text;
        return this;
    }

    /**
     * Initializes a {@link ActivityAssets} instance from the data contained within this builder.
     */
    public ActivityAssets build() {
        return new ActivityAssets(this.largeImage, this.largeText, this.smallImage, this.smallText);
    }
}
