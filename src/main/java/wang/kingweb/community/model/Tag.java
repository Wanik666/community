package wang.kingweb.community.model;

import java.io.Serializable;

public class Tag implements Serializable {
    private Integer id;

    private String tagName;

    private String tagType;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName == null ? null : tagName.trim();
    }

    public String getTagType() {
        return tagType;
    }

    public void setTagType(String tagType) {
        this.tagType = tagType == null ? null : tagType.trim();
    }
}