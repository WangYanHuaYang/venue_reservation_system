package com.genolo.venue_reservation_system.config;

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.genolo.venue_reservation_system.Util.Utils;
import org.springframework.stereotype.Component;

/**
 * @program: venue_reservation_system
 * @description: 自定义主键生成策略
 * @author: WYHY
 * @create: 2020-03-03 09:55
 **/
@Component
public class CustomIdGenerator implements IdentifierGenerator {

    @Override
    public Number nextId(Object entity) {
        return null;
    }

    @Override
    public String nextUUID(Object entity) {
        return Utils.getsUUID();
    }
}
