package com.redphase.service;

import com.redphase.dto.ViewDto;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Data
public class GlobalService {
    protected Map<String, ViewDto> tabPaneViewMap = new HashMap<>();
}
