package com.robust.tools.kit.base;

import com.google.common.base.Throwables;
import com.robust.tools.kit.base.annotation.NotNull;
import com.robust.tools.kit.base.annotation.Nullable;
import com.robust.tools.kit.base.type.UnCheckedException;
import com.robust.tools.kit.io.type.StringBuilderWriter;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.concurrent.ExecutionException;

/**
 * @Description: 关于异常的工具类
 * <p>
 * 1、Checked/UnChecked及Wrap(如{@link java.util.concurrent.ExecutionException})的转换
 * 2、打印Exception的辅助函数.(其中一些来自{@link org.apache.commons.lang3.exception.ExceptionUtils})
 * 3、查找Cause.(其中一些来自{@link com.google.common.base.Throwables})
 * 4、StackTrace性能优化相关，尽量使用静态异常避免异常生成时获取StackTrace(Netty)
 * @Author: robust
 * @CreateDate: 2019/7/23 21:11
 * @Version: 1.0
 * @see com.robust.tools.kit.base.type.CloneableException
 */
public class ExceptionUtil {

    private static final StackTraceElement[] EMPTY_STACK_TRACE = new StackTraceElement[0];

    /*-----Checked/UnChecked及Wrap(如{@link java.util.concurrent.ExecutionException})的转换-----*/

    /**
     * 将CheckedException转换为RuntimeException重新抛出, 可以减少函数签名中的CheckException定义.
     * <p>
     * CheckedException会用UndeclaredThrowableException包裹，RunTimeException和Error则不会被转变.
     * <p>
     * copy from {@link org.apache.commons.lang3.exception.ExceptionUtils}
     * <p>
     * 虽然unchecked()里已直接抛出异常，但仍然定义返回值，方便欺骗Sonar。因此本函数也改变了一下返回值
     * <p>
     * e.g.
     *
     * <pre>
     * 	 try{ ... }catch(Exception e){ throw unchecked(t); }
     * </pre>
     *
     * @param t
     * @return
     * @see ExceptionUtils#wrapAndThrow(Throwable)
     */
    public static RuntimeException unchecked(@Nullable Throwable t) {
        if (t instanceof RuntimeException) {
            throw (RuntimeException) t;
        }
        if (t instanceof Error) {
            throw (Error) t;
        }
        throw new UnCheckedException(t);
    }

    /**
     * 如果是著名的包裹类，从cause中获得真正异常，其他异常则不变.
     * <p>
     * Future中使用的ExecutionException 与 反射时定义的InvocationTargetException， 真正的异常都封装在Cause中
     * <p>
     * 前面 unchecked() 使用的UncheckedException同理.
     */
    public static Throwable unwrap(@Nullable Throwable throwable) {
        if (throwable instanceof UnCheckedException || throwable instanceof ExecutionException
                || throwable instanceof InvocationTargetException || throwable instanceof UndeclaredThrowableException) {
            return throwable.getCause();
        }
        return throwable;
    }

    /**
     * 组合unwrap和unchecked，用于处理反射/Callable的异常
     */
    public static RuntimeException unwrapAndUnchecked(@Nullable Throwable throwable) {
        throw unchecked(unwrap(throwable));
    }

    /*--------------------------------输出内容相关-------------------------------------*/

    /**
     * 将StackTrace[]转换为String,供Logger或e.printStackTrace()外的其他方法使用.
     * 为了使用StringBuilderWriter，没有用{@link com.google.common.base.Throwables#getStackTraceAsString(Throwable)}
     *
     * @param throwable
     * @return
     */
    public static String stackTraceText(@Nullable Throwable throwable) {
        StringBuilderWriter stringWriter = new StringBuilderWriter();
        throwable.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }

    /**
     * 拼装短异常类名: 异常信息
     * 与{@link Throwable#toString()}相比使用了短类名
     *
     * @param throwable
     * @return
     * @see ExceptionUtils#getMessage(Throwable)
     */
    public static String toStringWithShortName(@Nullable Throwable throwable) {
        return ExceptionUtils.getMessage(throwable);
    }

    /**
     * 拼接 短异常类名: 异常信息 <-- RootCause的短异常类名: 异常信息
     *
     * @param throwable
     * @return
     */
    public static String toStringWithRootCause(@Nullable Throwable throwable) {
        if (throwable == null) {
            return StringUtils.EMPTY;
        }
        final String clzName = ClassUtils.getShortClassName(throwable, null);
        final String message = StringUtils.defaultString(throwable.getMessage());
        Throwable rootCause = getRootCause(throwable);
        StringBuilder sb = new StringBuilder(128).append(clzName).append(": ").append(message);
        if (rootCause != null) {
            sb.append("; <--").append(toStringWithShortName(rootCause));
        }
        return sb.toString();
    }

    /*----------------------------------Cause相关-----------------------------------*/

    /**
     * 获取异常的RootCause
     * 如无底层Cause, 则返回自身
     *
     * @param throwable
     * @return
     * @see Throwables#getRootCause(Throwable)
     */
    public static Throwable getRootCause(@Nullable Throwable throwable) {
        return Throwables.getRootCause(throwable);
    }

    /**
     * 获取某种类型的Cause，如果没有则返回空
     * copy from {@see https://github.com/oblac/jodd/blob/master/jodd-core/src/main/java/jodd/exception/ExceptionUtil.java}
     *
     * @param throwable
     * @param cause
     * @param <T>
     * @return
     */
    public static <T extends Throwable> T findCause(@Nullable Throwable throwable, Class<?> cause) {
        while (throwable != null) {
            if (throwable.getCause().equals(cause)) {
                return (T) throwable;
            }
            throwable = throwable.getCause();
        }
        return null;
    }

    /**
     * 判断异常是否由某些底层的异常引起
     *
     * @param throwable
     * @param causeExceptionClasses
     * @return
     */
    public static boolean isCauseBy(@Nullable Throwable throwable, Class<? extends Exception>... causeExceptionClasses) {
        Throwable cause = throwable;

        while (cause != null) {
            for (Class<? extends Exception> causeClass : causeExceptionClasses) {
                if (causeClass.isInstance(cause)) {
                    return true;
                }
            }
            cause = cause.getCause();
        }
        return false;
    }

    /*-----------------------------StackTrace性能优化相关-----------------------------------------*/

    /**
     * copy from netty{@see io.netty.util.internal.ThrowableUtil#unknownStackTrace}
     * 对于某些已知且经常抛出的异常,不需要每次创建异常类并很消耗性能的生成完整的StackTrace.此时可使用静态声明的异常.
     * 如果异常可能在多个地方抛出，使用本函数设置抛出的类名和方法名.
     *
     * <pre>
     * 	private static RuntimeException TIMEOUT_EXCEPTION = ExceptionUtil.setStackTrace(new RuntimeException("Timeout"),
     * 		MyClass.class, "method");
     * </pre>
     *
     * @param throwable
     * @param throwClass
     * @param method
     * @param <T>
     * @return
     */
    public static <T extends Throwable> T setStackTrace(@NotNull T throwable, Class<?> throwClass, String method) {
        throwable.setStackTrace(new StackTraceElement[]{new StackTraceElement(throwClass.getName(), method, null, -1)});
        return throwable;
    }

    /**
     * 清除StackTrace.假设StackTrace已生成, 但把它打印出来也有不小的消耗
     * 如果不能控制StackTrace的生成，也不能控制它的打印端(如logger)，可用此方法暴力清除Trace.
     * 但Cause链不能清除，只能清除每一个Cause的StackTrace
     *
     * @param throwable
     * @param <T>
     * @return
     */
    public static <T extends Throwable> T clearStackTrace(@NotNull T throwable) {
        Throwable cause = throwable;
        while (cause != null) {
            cause.setStackTrace(EMPTY_STACK_TRACE);
            cause = cause.getCause();
        }
        return throwable;
    }
}
