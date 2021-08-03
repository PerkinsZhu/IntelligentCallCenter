package com.perkins.icc.domain.call;

import com.alibaba.cola.dto.Response;

/**
 * @author: perkins Zhu
 * @date: 2021/8/3 22:54
 * @description:
 **/
public interface TransferToAgent {
    Response transfer(String uuid);
}
