package com.jd.arthas.service.handle;

/**
 * 
 * @ClassName: TriggerHandle
 * @Description: 定时器触发接口
 * @author dsj
 * @date 2017年9月14日 下午7:59:59
 *
 */
public interface TriggerHandle {

    /**
     * 
     * @Title: execute
     * @Description: 定时器触发时的操作 
     * @throws
     */
    public void execute();

}
