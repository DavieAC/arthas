package com.arthas.common.utils.paraUtils;

public class ParameterUtil {

    public static boolean checkNull(Object... objs) {

        for (Object obj : objs) {
            if (obj == null) {
                return false;
            }
        }

        return true;
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

}
