package com.piveguyz.empickbackend.common.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.piveguyz.empickbackend.**.mapper")
public class MapperConfig {

}
