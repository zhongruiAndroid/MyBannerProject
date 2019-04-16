package com.test.banner;

import java.util.ArrayList;
import java.util.List;

/***
 *   created by android on 2019/4/16
 */
public class ImageURL {
    public static List getStringURLList() {
        List<String> list = new ArrayList<>();
        list.add("https://js.isheji5.com/album/15538442465c9dc8169a308.png");
        list.add("https://js.isheji5.com/album/15547179695cab1d11c8ed9.png");
        list.add("https://js.isheji5.com/album/%E6%8B%9B%E8%81%98.png");
        list.add("https://js.isheji5.com/album/%E6%98%A5%E5%AD%A3.png");
        return list;
    }
    public static List getTestBeanList() {
        List<TestBean> list = new ArrayList<>();
        TestBean testBean = new TestBean();
        testBean.imageUrl = "https://js.isheji5.com/album/15538442465c9dc8169a308.png";
        testBean.title = "第1种banner布局";
        testBean.itemType = 1;
        list.add(testBean);

        testBean = new TestBean();
        testBean.imageUrl = "https://js.isheji5.com/album/%E6%8B%9B%E8%81%98.png";
        testBean.title = "第2种banner布局";
        testBean.itemType = 2;
        list.add(testBean);

        testBean = new TestBean();
        testBean.imageUrl = "https://js.isheji5.com/album/15547179695cab1d11c8ed9.png";
        testBean.title = "第3种banner布局";
        testBean.itemType = 3;
        list.add(testBean);

        testBean = new TestBean();
        testBean.imageUrl = "https://js.isheji5.com/album/%E6%98%A5%E5%AD%A3.png";
        testBean.title = "第2种banner布局";
        testBean.itemType = 2;
        list.add(testBean);
        return list;
    }
}
