package com.yfaan.xjob.utils;

import com.yfaan.xjob.dto.UserDTO;

public class UserHolder {
    //UserHolder 类在 JVM 首次主动使用该类时被加载和初始化
    //由于包含静态变量 tl，类加载时会初始化这个 ThreadLocal 实例

    //用于在多线程环境中存储和管理当前用户的信息。它基于 ThreadLocal 实现，确保每个线程都有自己独立的 UserDTO 实例。
    private static final ThreadLocal<UserDTO> tl = new ThreadLocal<>();
    // final 表示 tl 引用不可改变，确保 tl 始终指向同一个 ThreadLocal<UserDTO> 实例
    public static void saveUser(UserDTO user){
        tl.set(user);
    }

    public static UserDTO getUser(){
        return tl.get();
    }
    //get() 是 ThreadLocal<T> 类的一个公共方法，get到一个UserDTO 对象，返回给调用者。因为在ThreadLocal<T>已经指定了UserDTO类型，所以返回的也是UserDTO对象。

    public static void removeUser(){
        tl.remove();
    }
}
