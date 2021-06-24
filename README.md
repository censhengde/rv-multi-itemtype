# 介绍

   MultiAdapter是一个轻松优雅实现RecyclerView多样式的强大组件。它将item的行为及表现抽象为一个ItemType，不同类型的item都自己独立的点击事件处理及视图绑定行为，极大地降低了耦合度，极大简化了item相关点击事件处理过程。
其内部封装了若干实用的工具组件以满足RecyclerView日常需求，如列表的单选/多选。
正是因为有了上述功能支持，我们在给RecyclerView添加头布局、脚布局、Error布局、Empty布局的时候，就简单的太多了！


依赖： 由于jcenter已跑路，目前项目已迁至JitPack，依赖方式如下：

1）在工程根目录build.gradle添加：  
allprojects {  
repositories {  
maven { url 'https://jitpack.io' }  
}  
}

2）在module build.gradle 添加：  
dependencies { 

implementation 'com.github.censhengde.MultiAdapter:core:v1.1.0'  //核心模块(必选）

//分页模块（可选），提供谷歌paging3 PagingDataAdapter 的改造：MultiPagedAdapter

implementation 'com.github.censhengde.MultiAdapter:paging:v1.1.0'

}

# 版本更新说明
1 v1.1.0版本 用MultiItemType 代替 SimpleItemType。SimpleItemType已移除。

2 修复MultiHelper addItemType方法引发崩溃隐患。

3 开放用户自定义ViewHolder限制，用户自定义ViewHolder需继承ItemTypeImpl类。

# 问题反馈
 1 请加群：

![MultiAdapter问题反馈群群聊二维码.png](image/MultiAdapter问题反馈群群聊二维码.png)

[详细文档](https://www.jianshu.com/p/b6b5f03ff304)
