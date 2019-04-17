| 属性                           | 说明                                                                 | 类型      | 默认值                  |
|--------------------------------|----------------------------------------------------------------------|-----------|-------------------------|
| direction                      | 轮播图滚动方向<br/>RecyclerView.HORIZONTAL<br/>RecyclerView.VERTICAL | integer   | RecyclerView.HORIZONTAL |
| timeInterval                   | 轮播时间间隔,单位毫秒                                                | integer   | 6000毫秒                |
| timeScroll                     | 视图滑动时间,单位毫秒                                                | integer   | 600毫秒                 |
| autoPlay                       | 是否自动滑动                                                         | boolean   | true                    |
| reverse                        | 滑动方向是否反向                                                     | boolean   | false                   |
| useGesture                     | 是否可以手势滑动                                                     | boolean   | true                    |
| indicatorHidden                | 是否隐藏指示器                                                       | boolean   | false                   |
| indicatorDistance              | 指示器间距                                                           | dimension | 6dp                     |
| indicatorSelectDrawable        | 选中时的指示器                                                       | reference | 默认灰色圆点            |
| indicatorUnSelectDrawable      | 未选中的指示器                                                       | reference | 默认白色圆点            |
| indicatorSelectDrawableColor   | 设置选中的指示器颜色                                                 | color     | 无默认值                |
| indicatorUnSelectDrawableColor | 设置未中的指示器颜色                                                 | color     | 无默认值                |
| indicatorDrawableWidth         | 设置指示器宽度                                                       | dimension | 无默认值                |
| indicatorDrawableHeight        | 设置指示器高度                                                       | dimension | 无默认值                |
| indicatorDrawableRadius        | 设置指示器圆角                                                       | dimension |                         |

#### 单一item视图布局
```java
MyBannerView banner=findViewById(R.id.banner);
List<String> list=new ArrayList<>();
list.add("https://js.isheji5.com/album/15547179695cab1d11c8ed9.png");
list.add("https://js.isheji5.com/album/15547179695cab1d11c8ed9.png");

banner.setList(list);
banner.addBannerItem(new SimpleBannerItem<String>() {
    
    public int getItemLayoutId() {
        return R.layout.yourItemLayoutId;
    }
    
    public void bindData(BannerHolder holder, String item, int position, int dataCount) {
        ImageView imageView = holder.getImageView(R.id.yourImageViewId);
        Glide.with(context).load(item).into(imageView);
    }
    /*可重写isItemType，onItemClick方法*/
});

banner.start();
```

#### 多item视图布局
```java
MyBannerView banner=findViewById(R.id.banner);
List<TestBean> list=new ArrayList<>();
list.add(new TestBean());
list.add(new TestBean());

banner.setList(list);
/*为banner多视图(此处有三种item布局)绑定数据*/
banner.addBannerItem(new TestViewItem1(this));
banner.addBannerItem(new TestViewItem2(this));
banner.addBannerItem(new TestViewItem3(this));

banner.start();

public class TestBean {
    public String imageUrl;
    public int itemType;
    public String title;
}

/*每个BannerItem对应一个item*/
public class TestViewItem1 implements BannerItem<TestBean> {
	private Activity activity;
	
	public TestViewItem1(Activity activity) {
	    this.activity = activity;
	}
	
	public int getItemLayoutId() {
	    return R.layout.item1;
	}
	
	public boolean isItemType(TestBean item, int position, int dataCount) {
	    /*如果itemType==1 则使用当前对象的getItemLayoutId()返回的item布局*/
	    return item.itemType==1;
	}
	
	public void onItemClick(TestBean item, int position, int dataCount) {
	    Toast.makeText(activity,"点击"+position+"下标的"+item.title,Toast.LENGTH_SHORT).show();
	}
	
	public void bindData(BannerHolder holder, TestBean item, final int position, int dataCount) {
	    ImageView iv = holder.getView(R.id.iv);
	    Glide.with(iv.getContext()).load(item.imageUrl).into(iv);
	}
}
```

#### 添加视图监听,item点击事件在BannerItem中设置
```java
banner.setPagerListener(new OnPagerListener<T>() {
    @Override
    public void onPageSelected(T item, int position, int beforePosition) {
        /*item为当前数据*/
        上个item下标：beforePosition
        当前item下标：position
    }
});

```

#### 减少CPU资源消耗请添加如下代码
```java
protected void onResume() {
    super.onResume();
    /*此处代码主要是减少cpu的资源消耗,不写对内存影响不大*/
    banner.startAutoPlay();
}
protected void onPause() {
    super.onPause();
    banner.stopAutoPlay();
}
```

### 注意！注意！注意！最后调用start()开启轮播
**banner.setList(yourData);**<br/>
**banner.addBannerItem(yourBannerItem);**<br/>
**banner.addBannerItem(yourBannerItem);**<br/>
**banner.set...**<br/>
**.**<br/>
**.**<br/>
**banner.start();**<br/>


---
#### 控制indicator位置(指示器)
##### MyBannerView继承RelativeLayout
> 在**MyBannerView**内部添加一个空的**LinearLayout**,设置对应属性即可控制所在位置,如果不添加LinearLayout则默认添加一个**底部居中**的LinearLayout指示器视图。如果布局中有**多个**LinearLayout,则取最后一个LinearLayout当做指示器视图
##### 
```xml
 <com.github.banner.MyBannerView
        android:id="@+id/banner"
        android:layout_width="match_parent"
        android:layout_height="200dp"
        app:autoPlay="true"
        app:direction="horizontal"
        app:indicatorDistance="8dp"
        app:indicatorDrawableHeight="8dp"
        app:indicatorDrawableRadius="4dp"
        app:indicatorDrawableWidth="8dp"
        app:indicatorSelectDrawableColor="#000000"
        app:indicatorUnSelectDrawableColor="#FFFDFBFB"
        app:reverse="false"
        app:timeInterval="5000"
        app:timeScroll="600"
        app:useGesture="true"
        >
        <LinearLayout
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:orientation="horizontal"
            android:layout_alignParentBottom="true"
            android:layout_centerHorizontal="true"
            >
        </LinearLayout>
    </com.github.banner.MyBannerView>
```
