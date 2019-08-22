package com.robust.tools.kit.base.type;

import com.robust.tools.kit.base.ExceptionUtil;

/**
 * @Description: 适用于异常信息需要变更的情况, 可通过clone()，不经过构造函数（也就避免了获得StackTrace）地从之前定义的静态异常中克隆，再设定新的异常信息
 * <p>
 * private static CloneableException TIMEOUT_EXCEPTION = new CloneableException("Timeout") .setStackTrace(My.class,
 * "hello"); ...
 * <p>
 * throw TIMEOUT_EXCEPTION.clone("Timeout for 40ms");
 * @Author: robust
 * @CreateDate: 2019/7/23 20:43
 * @Version: 1.0
 */
public class CloneableException extends Exception implements Cloneable {

    protected String message;

    public CloneableException() {
        super((Throwable) null);
    }

    public CloneableException(String message) {
        super((Throwable) null);
        this.message = message;
    }

    public CloneableException(String message, Throwable throwable) {
        super(throwable);
        this.message = message;
    }

    @Override
    public CloneableException clone() {
        try {
            return (CloneableException) super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    @Override
    public String getMessage() {
        return message;
    }

    /**
     * 简便函数，定义静态异常时使用
     *
     * @param throwClz
     * @param throwMethod
     * @return
     */
    public CloneableException setStackTrace(Class<?> throwClz, String throwMethod) {
        ExceptionUtil.setStackTrace(this, throwClz, throwMethod);
        return this;
    }

    /**
     * 简便函数，clone并重新设定Message
     *
     * @param message
     * @return
     */
    public CloneableException clone(String message) {
        CloneableException newException = this.clone();
        newException.setMessage(message);
        return newException;
    }

    /**
     * 简便函数，重新设定Message.
     *
     * @param message
     */
    private CloneableException setMessage(String message) {
        this.message = message;
        return this;
    }
}
