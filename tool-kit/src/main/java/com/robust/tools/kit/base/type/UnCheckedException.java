package com.robust.tools.kit.base.type;

/**
 * @Description: CheckedException的wrapper.
 * 返回Message时，将返回内层Exception的Message.
 * @Author: robust
 * @CreateDate: 2019/7/23 20:27
 * @Version: 1.0
 */
public class UnCheckedException extends RuntimeException {

    public UnCheckedException(Throwable cause) {
        super(cause);
    }

    @Override
    public String getMessage() {
        return super.getCause().getMessage();
    }
}
