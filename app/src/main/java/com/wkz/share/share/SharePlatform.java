package com.wkz.share.share;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.SOURCE)
public @interface SharePlatform {
    /**
     * 微信
     */
    int WeChat = 1;
    /**
     * 微信朋友圈
     */
    int WeChatMoments = 2;
    /**
     * QQ
     */
    int QQ = 3;
    /**
     * QQ空间
     */
    int QQSpace = 4;
    /**
     * 微博
     */
    int MicroBlog = 5;
    /**
     * 复制链接
     */
    int Link = 6;
}
