# RingCircleView
头像、图片裁剪
# 预览
![效果](./ic_preview.png)
# 资源
|名字|资源|
|-|-|
|AAR|[ring_cirvle_view.aar](https://github.com/RelinRan/RingCircleView/blob/master/ring_cirvle_view.aar)|
|GitHub |[RingCircleView](https://github.com/RelinRan/RingCircleView)|
|Gitee|[RingCircleView](https://gitee.com/relin/RingCircleView)|
# Maven
1.build.grade | setting.grade
```
repositories {
	...
	maven { url 'https://jitpack.io' }
}
```
2./app/build.grade
```
dependencies {
	implementation 'com.github.RelinRan:ClipView:2022.7.18.1'
}
```
# xml
~~~
<androidx.widget.RingCircleView
   android:id="@+id/ring"
   android:layout_width="match_parent"
   android:layout_height="match_parent"/>
~~~
# attrs.xml
~~~
<!--裁剪宽度-->
<attr name="clipWidth" format="dimension" />
<!--裁剪高度-->
<attr name="clipHeight" format="dimension" />
<!--蒙版颜色-->
<attr name="maskColor" format="color" />
<!--最大缩放值-->
<attr name="maxScale" format="float" />
<!--最小缩放值-->
<attr name="minScale" format="float" />
<!--裁剪图形-->
<attr name="clipShape" format="enum">
    <enum name="rect" value="0" />
    <enum name="circle" value="1" />
    <enum name="round" value="2" />
</attr>
<!--矩形圆角大小-->
<attr name="roundRadius" format="dimension" />
<!--图片来源-->
<attr name="clipSrc" format="reference"/>
~~~
# 使用
~~~
RingCircleView ring = findViewById(R.id.ring);
ring.setMax(100);
ring.setProgress(50);
~~~
