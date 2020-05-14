package eu.yals.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.Gson;
import eu.yals.utils.UrlExtraValidator;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static eu.yals.utils.UrlExtraValidator.URL_MAX_SIZE;
import static eu.yals.utils.UrlExtraValidator.URL_MIN_SIZE;

/**
 * Store Endpoint incoming JSON.
 *
 * @since 1.0
 */
@Data(staticConstructor = "create")
public class StoreRequestJson implements YalsJson {
    @NotNull(message = "must be present")
    @Size(min = URL_MIN_SIZE, max = URL_MAX_SIZE)
    @URL(message = UrlExtraValidator.URL_NOT_VALID)
    @JsonProperty("link")
    private String link;

    @Size(max = 32)
    @JsonProperty("session")
    private String session;

    /**
     * Creates {@link StoreRequestJson} with provided link.
     *
     * @param longLink field with long link to shorten
     * @return JSON which contains long link in {@link #link} param
     */
    public StoreRequestJson withLink(final String longLink) {
        this.link = longLink;
        return this;
    }

    /**
     * Adds session id for storing it to {@link eu.yals.models.LinkInfo}.
     *
     * @param sessionId 32 chars string with session identifier
     * @return same object, but with session set
     */
    public StoreRequestJson addSessionId(final String sessionId) {
        this.session = sessionId;
        return this;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
