# 介绍
   MultiAdapter是一个轻松优雅实现RecyclerView多样式Item的组件，支持注解形式实现Item及Item子View点击/长点击事件监听、支持 RecyclerView Item 单选/多选功能；  
   其中抽象出来的 MultiHelper核心类能够完美帮助您改造任意RecyclerView.Adapter（如Google paging2、paging3库的Adapter）以实现RecyclerView多样式；
   MultiViewHolder提供的getView方法能够帮您轻松获取到Item任意子View，且内部实现了View缓存能有效较少findViewById所带来的消耗。

# 用法
## 一、轻松实现RecyclerView Item多样式
###  先看实现效果，如图：
### 1.编写item布局：
item_a:

item_b、item_c。