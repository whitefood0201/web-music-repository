package com.whitefood.util;

import java.lang.reflect.Field;

public class BeanUtil {
    
    private BeanUtil() {
    }
    
    public static void popular(Object origin, Object dest) {
        
        try {
            // 判断两个对象是否是同一类型
            if (!origin.getClass().equals(dest.getClass())) {
                throw new IllegalArgumentException("Different Class");
            }
            
            Field[] fields = origin.getClass().getDeclaredFields();
            for (Field f : fields) {
                // 排除uid
                if ("serialVersionUID".equals(f.getName()))
                    continue;
                // 打破封装
                f.setAccessible(true);
                // 赋值
                Object fDes = f.get(dest);
                if (fDes != null)
                    f.set(origin, f.get(dest));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
}
