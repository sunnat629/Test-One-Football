package onefootball.com.testonefootball.data;

public class PushItem
{
    private Long id;
    private Long pushItemId;
    private Integer pushItemType;
    private String pushItemName;
    private String pushOption;

    public static final int TYPE_TEAM_PUSH = 1;

    public PushItem() {
    }

    public PushItem(Long id) {
        this.id = id;
    }

    public PushItem(Long id, Long pushItemId, Integer pushItemType, String pushItemName, String pushOption) {
        this.id = id;
        this.pushItemId = pushItemId;
        this.pushItemType = pushItemType;
        this.pushItemName = pushItemName;
        this.pushOption = pushOption;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPushItemId() {
        return pushItemId;
    }

    public void setPushItemId(Long pushItemId) {
        this.pushItemId = pushItemId;
    }

    public Integer getPushItemType() {
        return pushItemType;
    }

    public void setPushItemType(Integer pushItemType) {
        this.pushItemType = pushItemType;
    }

    public String getPushItemName() {
        return pushItemName;
    }

    public void setPushItemName(String pushItemName) {
        this.pushItemName = pushItemName;
    }

    public String getPushOption() {
        return pushOption;
    }

    public void setPushOption(String pushOption) {
        this.pushOption = pushOption;
    }
}
