
package org.springframework.samples.petclinic.API.model.Petfinder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.samples.petclinic.API.model.Petfinder.Address;
import org.springframework.samples.petclinic.API.model.Petfinder.Adoption;
import org.springframework.samples.petclinic.API.model.Petfinder.Hours;
import org.springframework.samples.petclinic.API.model.Petfinder.Links;
import org.springframework.samples.petclinic.API.model.Petfinder.Photo;
import org.springframework.samples.petclinic.API.model.Petfinder.SocialMedia;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "name",
    "email",
    "phone",
    "address",
    "hours",
    "url",
    "website",
    "mission_statement",
    "adoption",
    "social_media",
    "photos",
    "distance",
    "_links"
})
public class Organization {

    @JsonProperty("id")
    private String id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("email")
    private String email;
    @JsonProperty("phone")
    private String phone;
    @JsonProperty("address")
    private Address address;
    @JsonProperty("hours")
    private Hours hours;
    @JsonProperty("url")
    private String url;
    @JsonProperty("website")
    private Object website;
    @JsonProperty("mission_statement")
    private Object missionStatement;
    @JsonProperty("adoption")
    private Adoption adoption;
    @JsonProperty("social_media")
    private SocialMedia socialMedia;
    @JsonProperty("photos")
    private List<Photo> photos = null;
    @JsonProperty("distance")
    private Object distance;
    @JsonProperty("_links")
    private Links links;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    @JsonProperty("email")
    public void setEmail(String email) {
        this.email = email;
    }

    @JsonProperty("phone")
    public String getPhone() {
        return phone;
    }

    @JsonProperty("phone")
    public void setPhone(String phone) {
        this.phone = phone;
    }

    @JsonProperty("address")
    public Address getAddress() {
        return address;
    }

    @JsonProperty("address")
    public void setAddress(Address address) {
        this.address = address;
    }

    @JsonProperty("hours")
    public Hours getHours() {
        return hours;
    }

    @JsonProperty("hours")
    public void setHours(Hours hours) {
        this.hours = hours;
    }

    @JsonProperty("url")
    public String getUrl() {
        return url;
    }

    @JsonProperty("url")
    public void setUrl(String url) {
        this.url = url;
    }

    @JsonProperty("website")
    public Object getWebsite() {
        return website;
    }

    @JsonProperty("website")
    public void setWebsite(Object website) {
        this.website = website;
    }

    @JsonProperty("mission_statement")
    public Object getMissionStatement() {
        return missionStatement;
    }

    @JsonProperty("mission_statement")
    public void setMissionStatement(Object missionStatement) {
        this.missionStatement = missionStatement;
    }

    @JsonProperty("adoption")
    public Adoption getAdoption() {
        return adoption;
    }

    @JsonProperty("adoption")
    public void setAdoption(Adoption adoption) {
        this.adoption = adoption;
    }

    @JsonProperty("social_media")
    public SocialMedia getSocialMedia() {
        return socialMedia;
    }

    @JsonProperty("social_media")
    public void setSocialMedia(SocialMedia socialMedia) {
        this.socialMedia = socialMedia;
    }

    @JsonProperty("photos")
    public List<Photo> getPhotos() {
        return photos;
    }

    @JsonProperty("photos")
    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    @JsonProperty("distance")
    public Object getDistance() {
        return distance;
    }

    @JsonProperty("distance")
    public void setDistance(Object distance) {
        this.distance = distance;
    }

    @JsonProperty("_links")
    public Links getLinks() {
        return links;
    }

    @JsonProperty("_links")
    public void setLinks(Links links) {
        this.links = links;
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
