package com.jd.arthas.common.handle;

/**
 * 
 * @ClassName: TriggerHandle
 * @Description: 定时器触发接口
 * @author dsj
 * @date 2017年9月14日 下午7:59:59
 *
 */
public interface Handle {

    /**
     * 
     * @Title: execute
     * @Description: 定时器触发时的操作 
     * @throws
     */
    public void execute();

}
