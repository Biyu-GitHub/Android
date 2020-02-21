# 1 为什么要自定义控件

* 特定的显示风格
* 处理特有的用户交互
* 优化我们的布局
* 封装等。。。

# 2 如何自定义控件

1. 自定义属性的声明与获取
2. 测量onMeasure
3. 绘制onDraw
4. 状态的存储与恢复

## 2.1 自定义属性的声明与获取

1. 分析需要的自定义属性
2. 在res/valus/attrs.xml定义声明
3. 在layout xml文件中进行使用
4. 在View的构造方法中进行获取

## 2.2 测量onMeasure

1. EXACTLY(设置了确定的大小，可以直接使用此大小), AT_MOST, UNSPECIFIED
2. MeasureSpec
3. setMeasuredDimension
4. requestLayout()

## 2.3 绘制onDraw

1. 绘制内容区域
2. invalidate(), postInvalidate()
3. Canvas.drawXXX
4. Translate, rotate, scale, skew
5. save(), restore()

## 2.4 状态的存储与恢复

1. onSaveInstanceState
2. onRestoreInstanceState

# 3 代码

## 3.1 自定义属性的声明与获取

### 3.1.1 声明

1. 首先需要新建一个View类并且继承View

   ```java
   import android.view.View;
   
   public class TestView extends View {}
   ```

2. 在values中新建资源文件，例如attrs.xml，可以在其中自定义控件的属性，其中`declare-styleable name`指向自定义的View

   ```xml
   <?xml version="1.0" encoding="utf-8"?>
   <resources>
       <declare-styleable name="TestView">
           <attr name="test_boolean" format="boolean"></attr>
           <attr name="test_string" format="string"></attr>
           <attr name="test_integer" format="integer"></attr>
           <attr name="test_enum" format="enum">
               <enum name="top" value="1"></enum>
               <enum name="bottom" value="2"></enum>
           </attr>
           <attr name="test_dimension" format="dimension"></attr>
       </declare-styleable>
   </resources>
   ```

### 3.1.2 获取

1. 首先在布局文件中加入命名空间（自动添加好的，app可以替换成任意字符），并且添加自定义的控件，指定自定义的属性，以`app`开头。

   ```xml
   xmlns:app="http://schemas.android.com/apk/res-auto"
   ```

   ```xml
   <com.example.a09_customview.TestView
           android:layout_width="200dp"
           android:layout_height="200dp"
           android:layout_centerInParent="true"
           android:background="#44ff00"
           app:test_boolean="true"
           app:test_dimension="100dp"
           app:test_enum="top"
           app:test_integer="1"
           app:test_string="TestView" />
   ```

2. 在自定义View的构造方法中获取这些属性。注意：需要实现带两个参数的构造方法，并且不要忘记回收`ta`

   ```java
   public class TestView extends View {
   
       public TestView(Context context, @Nullable AttributeSet attrs) {
           super(context, attrs);
   
           TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TestView);
   
           ta.getBoolean(R.styleable.TestView_test_boolean, false);
           ta.getDimension(R.styleable.TestView_test_dimension, 150);
           ta.getInt(R.styleable.TestView_test_enum, 1);
           ta.getInteger(R.styleable.TestView_test_integer, -1);
           ta.getString(R.styleable.TestView_test_string);
   
           ta.recycle();
       }
   }
   ```

3. 直接使用get方法去获取属性值会造成一定的问题，例如如果没有在布局文件中设置test_string的值，直接调用get方法会返回null，造成默认值的丢失，因此，可以使用for循环的方法来获取属性值。只有当遍历到了test_string时，才会对testString进行赋值，使用此种方法比较安全。

   ```java
   for (int i = 0; i < ta.getIndexCount(); i++) {
     switch (ta.getIndex(i)) {
       case R.styleable.TestView_test_string:
         testString = ta.getString(R.styleable.TestView_test_string);
         break;
       default:
         break;
     }
   }
   ```

## 3.2 测量onMeasure

1. 重写`onMeasure()`方法，下面给出了测量宽度的示例。首先要获取从父容器传来的`mode`与宽度值，并且根据不同的`mode`获得不同的宽度。其中的`measureWidth()`方法需要自己去实现，之后再说。另外，如果要支持`padding`的话，不要忘记加上`getPadding()`方法。高度的测量方法与宽度相同。

   ```java
   @Override
   protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
     
     super.onMeasure(widthMeasureSpec, heightMeasureSpec);
   
     int widthMode = MeasureSpec.getMode(widthMeasureSpec);
     int widthSize = MeasureSpec.getSize(widthMeasureSpec);
   
     int width = 0;
     if (widthMode == MeasureSpec.EXACTLY) {
       width = widthSize;
       
     } else {
       int needWidth = measureWidth() + getPaddingLeft() + getPaddingRight();
   
       if (widthMode == MeasureSpec.AT_MOST) {
         width = Math.min(needWidth, widthSize);
         
       } else {
         width = needWidth;
       }
     }
   }
   ```

2. 最后不要忘记调用`setMeasuredDimension()`方法。

   ```java
   setMeasuredDimension(width, height);
   ```

> 测量的写法是固定的。

## 3.3 绘制onDraw

1. 使用画笔去绘制，需要新建一个画笔

   ```java
   private Paint mPaint;
   
   mPaint.setTextSize(72);
   mPaint.setStyle(Paint.Style.FILL);
   mPaint.setStrokeWidth(0);
   ```

2. 重写onDraw方法，例如绘制文本

   ```java
   @Override
   protected void onDraw(Canvas canvas) {
     canvas.drawText(testString, 0, testString.length(), 0, getHeight(), mPaint);
   }
   ```

## 3.4 状态的存储与恢复

### 3.4.1 存储

```java
@Nullable
@Override
protected Parcelable onSaveInstanceState() {
  Bundle bundle = new Bundle();
  bundle.putString(KEY_TEXT, testString);
  bundle.putParcelable(INSTANCE, super.onSaveInstanceState());
  return bundle;
}
```

### 3.4.2 恢复

```java
@Override
protected void onRestoreInstanceState(Parcelable state) {
  if (state instanceof Bundle) {
    Bundle bundle = (Bundle) state;
    Parcelable parcelable = bundle.getParcelable(INSTANCE);
    super.onRestoreInstanceState(parcelable);

    testString = bundle.getString(KEY_TEXT);
    return;
  }
  super.onRestoreInstanceState(state);
}
```

> 一定要给View设置id才能成功恢复状态

# 4 问题

1. onMeasure方法中调用super可以实现相同的效果，为什么还要重写此方法？

