package com.redphase.dto.task;


import com.redphase.framework.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaskDetailDto extends BaseDto {
    public enum TestTechnology {
        UHF(1, "UHF"),
        AE(2, "AE"),
        HFCT(3, "HFCT");

        @Getter
        private int value;
        @Getter
        private String text;

        public static String getTextByValue(int value) {
            for (TestTechnology testTechnology : TestTechnology.values()) {
                if (testTechnology.getValue() == value) {
                    return testTechnology.getText();
                }
            }
            return "";
        }

        TestTechnology(int value, String text) {
            this.value = value;
            this.text = text;
        }
    }

    /**
     * ID
     */
    //@Size(max = 20, message = "id最大20字符")
    private String id;
    /**
     * 任务单ID
     */
    //@Size(max = 20, message = "taskId最大20字符")
    private String taskId;
    /**
     * 检测点字典名称
     */
    //@Size(max = 100, message = "siteName最大100字符")
    private String siteName;
    /**
     * 检测技术:1.特高频（UHF）;2.AE;3.高频（HFCT）
     */
    //@Size(max = 1, message = "testTechnology最大1字符")
    private String testTechnology;
    /**
     * 删除标识
     */
    //@Size(max = 1, message = "delFlag最大1字符")
    private String delFlag;
    /**
     * 创建时间
     */
    private Date dateCreated;
    /**
     * 更新时间
     */
    private Date dateUpdated;
    /**
     * 修改者id
     */
    //@Size(max = 20, message = "updateId最大20字符")
    private String updateId;
    /**
     * BI时间戳
     */
    private Date biUpdateTs;

}