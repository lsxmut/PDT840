package com.redphase.dto;

import com.redphase.framework.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SkillItemDto extends BaseDto {
    public enum Skill {
        AA("AA", "空气式超声波"),
        AE("AE", "接触式超声波"),
        TEV("TEV", "地电波"),
        UHF("UHF", "特高频"),
        HFCT("HFCT", "高频电流"),
        ;
        private String key;
        private String name;

        Skill(String key, String name) {
            this.key = key;
            this.name = name;
        }
    }

    private String fullName;
    private Skill skill;
    private Object value;

    public SkillItemDto(String fullName, Skill skill, Object value) {
        this.fullName = fullName;
        this.skill = skill;
        this.value = value;
    }

    private List<SkillItemDto> nodes;

    @Override
    public String toString() {
        return skill.key;
    }
}