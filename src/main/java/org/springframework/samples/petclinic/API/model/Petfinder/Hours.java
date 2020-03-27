
package org.springframework.samples.petclinic.API.model.Petfinder;

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
    "monday",
    "tuesday",
    "wednesday",
    "thursday",
    "friday",
    "saturday",
    "sunday"
})
public class Hours {

    @JsonProperty("monday")
    private Object monday;
    @JsonProperty("tuesday")
    private Object tuesday;
    @JsonProperty("wednesday")
    private Object wednesday;
    @JsonProperty("thursday")
    private Object thursday;
    @JsonProperty("friday")
    private Object friday;
    @JsonProperty("saturday")
    private Object saturday;
    @JsonProperty("sunday")
    private Object sunday;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("monday")
    public Object getMonday() {
        return monday;
    }

    @JsonProperty("monday")
    public void setMonday(Object monday) {
        this.monday = monday;
    }

    @JsonProperty("tuesday")
    public Object getTuesday() {
        return tuesday;
    }

    @JsonProperty("tuesday")
    public void setTuesday(Object tuesday) {
        this.tuesday = tuesday;
    }

    @JsonProperty("wednesday")
    public Object getWednesday() {
        return wednesday;
    }

    @JsonProperty("wednesday")
    public void setWednesday(Object wednesday) {
        this.wednesday = wednesday;
    }

    @JsonProperty("thursday")
    public Object getThursday() {
        return thursday;
    }

    @JsonProperty("thursday")
    public void setThursday(Object thursday) {
        this.thursday = thursday;
    }

    @JsonProperty("friday")
    public Object getFriday() {
        return friday;
    }

    @JsonProperty("friday")
    public void setFriday(Object friday) {
        this.friday = friday;
    }

    @JsonProperty("saturday")
    public Object getSaturday() {
        return saturday;
    }

    @JsonProperty("saturday")
    public void setSaturday(Object saturday) {
        this.saturday = saturday;
    }

    @JsonProperty("sunday")
    public Object getSunday() {
        return sunday;
    }

    @JsonProperty("sunday")
    public void setSunday(Object sunday) {
        this.sunday = sunday;
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
