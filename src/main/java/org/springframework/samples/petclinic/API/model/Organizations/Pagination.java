
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
    "count_per_page",
    "total_count",
    "current_page",
    "total_pages",
    "_links"
})
public class Pagination {

    @JsonProperty("count_per_page")
    private Integer countPerPage;
    @JsonProperty("total_count")
    private Integer totalCount;
    @JsonProperty("current_page")
    private Integer currentPage;
    @JsonProperty("total_pages")
    private Integer totalPages;
    @JsonProperty("_links")
    private Links_ links;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("count_per_page")
    public Integer getCountPerPage() {
        return countPerPage;
    }

    @JsonProperty("count_per_page")
    public void setCountPerPage(Integer countPerPage) {
        this.countPerPage = countPerPage;
    }

    @JsonProperty("total_count")
    public Integer getTotalCount() {
        return totalCount;
    }

    @JsonProperty("total_count")
    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    @JsonProperty("current_page")
    public Integer getCurrentPage() {
        return currentPage;
    }

    @JsonProperty("current_page")
    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    @JsonProperty("total_pages")
    public Integer getTotalPages() {
        return totalPages;
    }

    @JsonProperty("total_pages")
    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    @JsonProperty("_links")
    public Links_ getLinks() {
        return links;
    }

    @JsonProperty("_links")
    public void setLinks(Links_ links) {
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
