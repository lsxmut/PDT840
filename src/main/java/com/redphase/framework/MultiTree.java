package com.redphase.framework;

import com.redphase.dto.ZTreeNodeDto;
import com.redphase.dto.account.AccountDto;
import com.redphase.framework.util.CommonConstant;
import com.redphase.framework.util.IdUtil;
import com.redphase.framework.util.ReflectUtil;
import com.redphase.framework.util.ValidatorUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class MultiTree {
    String pkAttrId;
    String[] attrIds;
    Map<String, ZTreeNodeDto> attrMap;
    List dataList;
    List<ZTreeNodeDto> treeList;
    boolean isLastDistinct=true;//尾节点是否去重

    public MultiTree(boolean isLastDistinct, String pkAttrId, List dataList, String... attrIds) {
        this.isLastDistinct=isLastDistinct;
        this.pkAttrId = pkAttrId;
        this.dataList = dataList;
        this.attrIds = attrIds;
        this.attrMap = new HashMap<>();
        this.treeList = new ArrayList<>();
        if (attrIds == null || attrIds.length < 2) {
            throw new RuntimeException("必须至少一组父子结构字段!");
        }
    }
    public MultiTree(String pkAttrId, List dataList, String... attrIds) {
        this.pkAttrId = pkAttrId;
        this.dataList = dataList;
        this.attrIds = attrIds;
        this.attrMap = new HashMap<>();
        this.treeList = new ArrayList<>();
        if (attrIds == null || attrIds.length < 2) {
            throw new RuntimeException("必须至少一组父子结构字段!");
        }
    }

    public List<ZTreeNodeDto> buildTree() {
        int attrLen = attrIds.length - 1;
        for (Object data : dataList) {
            String parentKey = "";
            String fullName = "";
            for (int i = 0; i < attrLen; i++) {
                String pkId = "" + ReflectUtil.getValueByFieldName(data, pkAttrId);
                String parentAttrId = attrIds[i];
                String parentValue = "" + ReflectUtil.getValueByFieldName(data, parentAttrId);
                if (ValidatorUtil.notEmpty(parentValue)) {
                    parentKey += CommonConstant.FEIGN_ERROR_SYMBOL_STRING + parentAttrId + "=" + parentValue;
                    ZTreeNodeDto parentZTreeNodeDto = attrMap.get(parentKey);
                    if (parentZTreeNodeDto == null) {
                        parentZTreeNodeDto = new ZTreeNodeDto();
                        parentZTreeNodeDto.setId(pkId);
//                        parentZTreeNodeDto.setData(i);
                        if ("voltageLevel".equals(parentAttrId)) {
                            parentZTreeNodeDto.setName(parentValue + "kV");
                        } else {
                            parentZTreeNodeDto.setName(parentValue);
                        }
                        parentZTreeNodeDto.setChildren(new ArrayList<>());
                        parentZTreeNodeDto.setParent(true);
                        attrMap.put(parentKey, parentZTreeNodeDto);
                        if (i == 0) {
                            treeList.add(parentZTreeNodeDto);
                        }
                        if (ValidatorUtil.notEmpty(parentZTreeNodeDto.getName())) {
                            fullName += parentZTreeNodeDto.getName() + CommonConstant.FEIGN_ERROR_SYMBOL_STRING;
                            parentZTreeNodeDto.setFullName(fullName);
                        }
                    } else {
                        if (ValidatorUtil.notEmpty(parentZTreeNodeDto.getFullName())) {
                            fullName = parentZTreeNodeDto.getFullName();
                        }
                    }
                    String childAttrId = attrIds[i + 1];
                    String childValue = "" + ReflectUtil.getValueByFieldName(data, childAttrId);
                    if (ValidatorUtil.notEmpty(childValue)) {
                        String childKey = parentKey + CommonConstant.FEIGN_ERROR_SYMBOL_STRING + childAttrId + "=" + childValue;
                        ZTreeNodeDto childZTreeNodeDto = attrMap.get(childKey);
                        if (childZTreeNodeDto == null || ((i + 1) == (attrLen) && !isLastDistinct)) {
                            childZTreeNodeDto = new ZTreeNodeDto();
                            childZTreeNodeDto.setId(pkId);
                            if ("voltageLevel".equals(childAttrId)) {
                                childZTreeNodeDto.setName(childValue + "kV");
                            } else if ("deviceType".equals(childAttrId)) {
                                switch (childValue) {
                                    case "1": {
                                        childZTreeNodeDto.setName("开关柜");
                                        break;
                                    }
                                    case "2": {
                                        childZTreeNodeDto.setName("变压器");
                                        break;
                                    }
                                    case "3": {
                                        childZTreeNodeDto.setName("组合电器");
                                        break;
                                    }
                                    case "4": {
                                        childZTreeNodeDto.setName("电缆");
                                        break;
                                    }
                                }
                            } else {
                                childZTreeNodeDto.setName(childValue);
                            }
                            childZTreeNodeDto.setChildren(new ArrayList<>());
                            attrMap.put(childKey, childZTreeNodeDto);
                            parentZTreeNodeDto.getChildren().add(childZTreeNodeDto);
                            if (i + 1 == attrLen) {
                                childZTreeNodeDto.setData(data);
                            } else {
//                                childZTreeNodeDto.setData(i + 1);
                                childZTreeNodeDto.setParent(true);
                            }
                            fullName += childZTreeNodeDto.getName() + CommonConstant.FEIGN_ERROR_SYMBOL_STRING;
                            childZTreeNodeDto.setFullName(fullName);
                        }
                    }
                }
            }
        }
        return treeList;
    }

    public static void main(String[] args) throws Exception {
        List deviceList = new ArrayList() {{
            add(new AccountDto() {{
                //private Long id;
                setId(IdUtil.nextId());
                //     * 电业局
                //    private String electricBureau;
                setElectricBureau("电业局");
                //     * 变电站
                //    private String substation;
                setSubstation("变电站");
                //     * 电压等级(单位：kV)
                //    private Integer voltageLevel;
                setVoltageLevel(110);
                //     * 设备类型(1、开关柜；2、变压器；3、组合电器；4、电缆)
                //    private Integer deviceType;
                setDeviceType(1);
                //     * 设备名称
                //    private String deviceName;
                setDeviceName("设备名称1");
            }});
            add(new AccountDto() {{
                //private Long id;
                setId(IdUtil.nextId());
                //     * 电业局
                //    private String electricBureau;
                setElectricBureau("电业局");
                //     * 变电站
                //    private String substation;
                setSubstation("变电站");
                //     * 电压等级(单位：kV)
                //    private Integer voltageLevel;
                setVoltageLevel(110);
                //     * 设备类型(1、开关柜；2、变压器；3、组合电器；4、电缆)
                //    private Integer deviceType;
                setDeviceType(1);
                //     * 设备名称
                //    private String deviceName;
                setDeviceName("设备名称1");
            }});
        }};
        MultiTree multiTree = new MultiTree(false,"id", deviceList, "electricBureau", "substation", "voltageLevel", "deviceType", "deviceName");
        List<ZTreeNodeDto> treeNode = multiTree.buildTree();
        System.out.println(treeNode);
    }

}