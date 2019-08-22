package com.robust.tools.kit.base.type;

import com.robust.tools.kit.base.ExceptionUtil;

/**
 * @Description: 适用于异常信息需要变更的情况, 可通过clone(), 不经过构造函数(也就避免了获得StackTrace)地从之前定义的静态异常中克隆, 再设定新的异常
 * @Author: robust
 * @CreateDate: 2019/7/23 20:41
 * @Version: 1.0
 * @see CloneableException
 */
public class CloneableRuntimeException extends RuntimeException implements Cloneable {

    protected String message;

    public CloneableRuntimeException() {
        super((Throwable) null);
    }

    public CloneableRuntimeException(String message) {
        super((Throwable) null);
        this.message = message;
    }

    public CloneableRuntimeException(String message, Throwable cause) {
        super(cause);
        this.message = message;
    }

    @Override
    public CloneableRuntimeException clone() {
        try {
            return (CloneableRuntimeException) super.clone();
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
     */
    public CloneableRuntimeException setStackTrace(Class<?> throwClazz, String throwMethod) {
        ExceptionUtil.setStackTrace(this, throwClazz, throwMethod);
        return this;
    }

    /**
     * 简便函数, clone并重新设定Message
     */
    public CloneableRuntimeException clone(String message) {
        CloneableRuntimeException newException = this.clone();
        newException.setMessage(message);
        return newException;
    }

    /**
     * 简便函数, 重新设定Message
     */
    public CloneableRuntimeException setMessage(String message) {
        this.message = message;
        return this;
    }
}
