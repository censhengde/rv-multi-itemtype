# 最新版本：1.1.5
# 介绍

   rv-multi-itemtype
   是一个轻松优雅实现RecyclerView多样式的强大组件。它将item的行为及表现抽象为一个ItemType，不同类型的item都自己独立的点击事件处理及视图绑定行为，极大地降低了耦合度，极大简化了item相关点击事件处理过程。
   其内部封装了若干实用的工具组件以满足RecyclerView日常需求，如列表的单选/多选。
   正是因为有了上述功能支持，我们在给RecyclerView添加头布局、脚布局以及复杂多样式item布局、RecyclerView嵌套布局的时候，就简单的太多了！

# 核心优势
**1.相比于BaseQuickAdapter及 MultiType库，rv-multi-itemtype 对RecyclerView
多样式item实现更优雅、更简单！**

**2.支持不同类型Item之间实体类类型隔离（类似于MultiType），但相比于MultiType，MultiAdapter 对item view 相关点击事件处理过程简单得太多了！甚至在嵌套RecyclerView的场景下也能轻松简单将内层RecyclerView点击事件带到任意环境！**

**3.相比于其他任意RecyclerView辅助组件，rv-multi-itemtype库提供的核心组件MultiItemManager可低成本任意改造其他RecyclerView
Adapter，达到无缝兼容类似于jetpack Paging2、3系列组件！**

**4.rv-multi-itemtype 应用泛型+反射技术以减少模板代码创建，大大简化开发者应用。**

**5.支持DiffUtil。**

**6.支持ViewBinding。**


# 依赖
 由于jcenter已跑路，目前项目已迁至JitPack，依赖方式如下：

1）在工程根目录build.gradle添加：
```
allprojects {
    repositories {  
    
    maven { url 'https://jitpack.io' }  
    
      }  
    }
```

2）在module build.gradle 添加：
```
dependencies {

implementation 'com.github.censhengde.MultiAdapter:core:1.1.5'  //核心模块(必选）

//分页模块（可选），提供谷歌paging3 PagingDataAdapter 的改造：MultiPagingDataAdapter
implementation 'com.github.censhengde.MultiAdapter:paging:1.1.5'

}
```

# 版本更新说明
1.1.2版本新增DiffUtil支持。

1.1.3版本移除ItemType getViewType方法；ItemType onBindViewHolder方法的 MultiHelper helper参数替换为 T bean。

1.1.4版本新增ViewBinding支持。

1.1.5版本新增不同item 之间 实体类类型隔离。

# 问题反馈
 1 请加群：

![MultiAdapter问题反馈群群聊二维码.png](image/MultiAdapter问题反馈群群聊二维码.png)

[详细文档](https://www.jianshu.com/p/5bc618cb1c1d)
