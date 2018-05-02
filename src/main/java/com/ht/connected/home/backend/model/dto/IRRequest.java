package com.ht.connected.home.backend.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ht.connected.home.backend.model.entity.IR;

/**
 * ir 요청시 토픽 URI 경로 구분자(/)로 분할하여 getter통해 쉽게 사용하기 위한 클래스
 * 
 * @author injeongLee
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IRRequest extends IR{

    
}