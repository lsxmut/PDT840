package com.redphase.framework;

import com.github.pagehelper.PageInfo;

public class PageUtil {
    public static void copy(PageDto pageDto, PageInfo pageInfo) {
        if (pageInfo == null) {
            return;
        }
        pageDto.setData(pageInfo.getList());
        pageDto.setDataCount((int) pageInfo.getTotal());
        pageDto.setPageIndex(pageInfo.getPageNum());
        pageDto.setPageSize(pageInfo.getPageSize());
        pageDto.setPageCount(pageInfo.getPages());
        pageDto.setPrePage(pageInfo.getPrePage());
        pageDto.setNextPage(pageInfo.getNextPage());

    }

    public static PageDto copy(PageInfo pageInfo) {
        PageDto pageDto = new PageDto();
        copy(pageDto, pageInfo);
        return pageDto;
    }
}
