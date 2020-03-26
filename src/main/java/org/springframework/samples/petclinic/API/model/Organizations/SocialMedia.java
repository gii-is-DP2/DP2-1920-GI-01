
package org.springframework.samples.petclinic.API.model.Organizations;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "facebook",
    "twitter",
    "youtube",
    "instagram",
    "pinterest"
})
public class SocialMedia {

    @JsonProperty("facebook")
    private Object facebook;
    @JsonProperty("twitter")
    private Object twitter;
    @JsonProperty("youtube")
    private Object youtube;
    @JsonProperty("instagram")
    private Object instagram;
    @JsonProperty("pinterest")
    private Object pinterest;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("facebook")
    public Object getFacebook() {
        return facebook;
    }

    @JsonProperty("facebook")
    public void setFacebook(Object facebook) {
        this.facebook = facebook;
    }

    @JsonProperty("twitter")
    public Object getTwitter() {
        return twitter;
    }

    @JsonProperty("twitter")
    public void setTwitter(Object twitter) {
        this.twitter = twitter;
    }

    @JsonProperty("youtube")
    public Object getYoutube() {
        return youtube;
    }

    @JsonProperty("youtube")
    public void setYoutube(Object youtube) {
        this.youtube = youtube;
    }

    @JsonProperty("instagram")
    public Object getInstagram() {
        return instagram;
    }

    @JsonProperty("instagram")
    public void setInstagram(Object instagram) {
        this.instagram = instagram;
    }

    @JsonProperty("pinterest")
    public Object getPinterest() {
        return pinterest;
    }

    @JsonProperty("pinterest")
    public void setPinterest(Object pinterest) {
        this.pinterest = pinterest;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
