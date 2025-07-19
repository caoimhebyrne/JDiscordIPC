package dev.caoimhe.jdiscordipc.activity.model.assets;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.caoimhe.jdiscordipc.activity.model.Activity;
import org.jspecify.annotations.Nullable;

/**
 * The assets to customize how the {@link Activity} is displayed to users.
 */
public class ActivityAssets {
    /**
     * The unique identifier (as it is in the developer portal) or URL for the large image.
     * <p>
     * The large image is rendered as a large square icon on a user's rich presence.
     */
    @JsonProperty("large_image")
    private final @Nullable String largeImage;

    /**
     * A tooltip string that is shown when the user hovers over the {@link #largeImage}.
     */
    @JsonProperty("large_text")
    private final @Nullable String largeText;

    /**
     * The unique identifier (as it is in the developer portal) or URL for the small image.
     * <p>
     * The small image is rendered as a small circle on top of the {@link #largeImage}.
     */
    @JsonProperty("small_image")
    private final @Nullable String smallImage;

    /**
     * A tooltip string that is shown when the user hovers over the {@link #smallImage}.
     */
    @JsonProperty("small_text")
    private final @Nullable String smallText;

    /**
     * Initializes a new {@link ActivityAssets} instance.
     *
     * @param largeImage The unique identifier (as it is in the developer portal) or URL for the large image.
     * @param largeText  A tooltip string that is shown when the user hovers over the {@link #largeImage}.
     * @param smallImage The unique identifier (as it is in the developer portal) or URL for the small image.
     * @param smallText  A tooltip string that is shown when the user hovers over the {@link #smallImage}.
     */
    public ActivityAssets(
        final @Nullable String largeImage,
        final @Nullable String largeText,
        final @Nullable String smallImage,
        final @Nullable String smallText
    ) {
        this.largeImage = largeImage;
        this.largeText = largeText;
        this.smallImage = smallImage;
        this.smallText = smallText;
    }

    @JsonCreator
    protected ActivityAssets() {
        this.largeImage = null;
        this.largeText = null;
        this.smallImage = null;
        this.smallText = null;
    }

    /**
     * Returns a {@link ActivityAssetsBuilder} instance to construct an {@link ActivityAssets} instance.
     */
    public static ActivityAssetsBuilder builder() {
        return new ActivityAssetsBuilder();
    }
}
