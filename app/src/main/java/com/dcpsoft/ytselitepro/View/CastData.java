package com.dcpsoft.ytselitepro.View;

/**
 * Created by Soumya on 07-Aug-16.
 */
public class CastData {

    private String orgName,characterName,img;

    public CastData(String orgName, String characterName, String img) {
        this.orgName = orgName;
        this.characterName = characterName;
        this.img = img;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getCharacterName() {
        return characterName;
    }

    public void setCharacterName(String characterName) {
        this.characterName = characterName;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
