# JRecyclerView
JRecyclerView是一个继承自Recyclerview的扩展类库，简化了viewholder的用法，增加了滑动到底部加载更多，一行代码开启item拖动滑动以及适配器中的数据操作。堪称是居家旅行，开发必备之良品，当然，这个项目很久之前就开始开发了，一直拖到2017年初才开始写readme，实在是够懒！

#集成方法
step1：在根build.gradle中加入
        
	dependencies {
	        compile 'com.jtechlib2:jrecycler-library:2.0.5'
	}
	
#下拉刷新&加载更多
该项目并没有将下拉刷新与加载更多集成一体，也没有做成overscroll的效果，所以需要同时使用这两种功能的小伙伴需要使用这种嵌套的方式去实现
，当然，也可以拆开使用
```xml
    <com.jtech.view.RefreshLayout
        android:id="@+id/refreshlayout"
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        <com.jtech.view.JRecyclerView
            android:id="@+id/jrecyclerview"
            android:layout_width="match_parent"
            android:layout_height="match_parent" />
    </com.jtech.view.RefreshLayout>
```
#下拉刷新
copy了官方的SwipeRefreshLayout代码修复了其中的bug（官方代码中出现的重复下拉的bug已经修复，并不会出现）并且加入了新的功能
#主动发起下拉刷新并设置监听
```java
refreshLayout.startRefreshing();
refreshLayout.setOnRefreshListener(new RefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                
            }
        });
```
#刷新完成时调用的状态还原方法
```java
refreshLayout.refreshingComplete();
```
#滑动到底部加载更多
该方法通过嵌套适配器的方式实现了一个footer，通用于三种布局，并且footer可以实现自定义效果
#开启加载更多并且设置监听
```java
jRecyclerView.setLoadMore(true);
jRecyclerView.setOnLoadListener(new OnLoadListener() {
            @Override
            public void loadMore() {

            }
        });
```
#自定义footer
自定义footer需要在设置适配器的同时传递进去，通过适配器的方式完成(simpleloadfooteradapter是默认的footer适配器实现，不传递的时候就会调用他)
```java
SimpleLoadFooterAdapter simpleLoadFooterAdapter = new SimpleLoadFooterAdapter(context);
jRecyclerView.setAdapter(testAdapter, simpleLoadFooterAdapter);
```
#自定义footer适配器实现，继承自LoadFooterAdapter
```java
public class SimpleLoadFooterAdapter extends LoadFooterAdapter {
    
    @Override
    public View getFooterView(LayoutInflater layoutInflater, ViewGroup parent) {
        //获取footer的视图
        return null;
    }
    
    @Override
    public void loadFailState(RecyclerHolder recyclerHolder) {
        //加载失败状态，需要用户调用jRecyclerView.setLoadFailState();
    }
    
    @Override
    public void loadingState(RecyclerHolder recyclerHolder) {
        //加载中状态
    }
    
    @Override
    public void noMoreDataState(RecyclerHolder recyclerHolder) {
        //无更多数据状态，需要用户调用jRecyclerView.setLoadFinishState();
    }
    
    @Override
    public void normalState(RecyclerHolder recyclerHolder) {
        //默认状态，无动作
    }
}
```
#item点击事件
item中无拦截的时候生效,与listview的使用方法相同，只是返回的参数不同
```java
jRecyclerView.setOnItemClickListener(new OnItemClickListener() {
    @Override
    public void onItemClick(RecyclerHolder holder, View view, int position) {
        
    }
});
```
#item长点击事件
item中无拦截的时候生效，不过与下面要说的长点击拖动换位功能可能会出现冲突，需要自己抉择
```java
jRecyclerView.setOnItemLongClickListener(new OnItemLongClickListener() {
    @Override
    public boolean onItemLongClick(RecyclerHolder holder, View view, int position) {
        return false;
    }
});
```
#长点击拖动或者拖动换位
```java
//longPressDragEnabled为是否开启长点击拖动换位，也可以用户手动触发该功能；dragFlags为动作标记，onItemViewMoveListener为监听
//最底层的拖动方法，一般情况下可以不使用
jRecyclerView.setMove(longPressDragEnabled, dragFlags, onItemViewMoveListener);
//自由拖动，上下左右皆可拖动
jRecyclerView.setMoveFree(longPressDragEnabled, onItemViewMoveListener);
//左右拖动，顾名思义
jRecyclerView.setMoveLeftRight(longPressDragEnabled, onItemViewMoveListener);
//上♂下♀拖动，千万别乱想
jRecyclerView.setMoveUpDown(longPressDragEnabled, onItemViewMoveListener);
//也可以单独设置监听,上面几个方法可以传null
jRecyclerView.setOnItemViewMoveListener(onItemViewMoveListener);
```
#手动触发拖动监听事件
比如点击item中的某个图标实现拖动换位的功能,需要传递当前item的viewholder
```java
jRecyclerView.startDrag(viewHolder);
```
#滑动删除或归档
```java
//swipeEnabled为是否开启滑动，也可以用户手动触发该功能；swipeFlags为动作标记，onItemViewSwipeListener为监听
//最底层的滑动方法，一般情况下可以不使用
jRecyclerView.setSwipe(swipeEnabled,swipeFlags,onItemViewSwipeListener);
//自由滑动
jRecyclerView.setSwipeFree(swipeEnabled,onItemViewSwipeListener);
//从左到右滑动
jRecyclerView.setSwipeStart(swipeEnabled,onItemViewSwipeListener);
//从右到左滑动
jRecyclerView.setSwipeEnd(swipeEnabled,onItemViewSwipeListener);
//依然可以单独设置监听,上面方法设置为null
jRecyclerView.setOnItemViewSwipeListener(onItemViewSwipeListener);
```
#手动触发滑动
并不知道为什么要搞这个功能，可能是为了对称？至少我是没发现使用场景
```java
jRecyclerView.startSwipe(viewHolder);
```
#适配器的食用方法（重点来了！）
目前有三种列表适配器加一个footer的适配器供各位使用，分别是
```java
//这是基础适配器，所有的方法都在里面，用户可以使用自定义的viewholder，并不局限于recyclerholder
BaseJAdapter.class
//一般情况下都会使用这个方法，相比于基础适配器，它更简单
RecyclerAdapter.class
//为了扩展swip动作而专门搞得一个适配器，足以应对swip动作的所有需求，当然，只在jrecyclerview中生效，不通用
RecyclerSwipeAdapter.class
```
#BaseJAdapter
```java
    //第一个参数是viewholder，第二个是参数类型，列表中需要展示的数据类型，不是集合！不是集合！不是集合！
    //如果列表并不是一个完整的集合，也可以随便填个类型
    class TestAdapter extends BaseJAdapter<TestAdapter.ViewHolder,String>{
        public TestAdapter(Context context) {
            super(context);
    	}
        @Override
        public ViewHolder createHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
            //在这里创建holder，与基础的适配器无异
            return null;
        }

        @Override
        public void convert(ViewHolder holder, int viewType, int position) {
            //在这里convert，与基础的适配器无异
        }
        //same
        class ViewHolder extends RecyclerView.ViewHolder{
            public ViewHolder(View itemView) {
                super(itemView);
            }
        }
    }
```
#RecyclerAdapter
```java
    //常用方法，viewholder使用的是recyclerholder，下面会有详解,泛型依然需要填入一个列表的参数类型，不是集合！不是集合！不是集合！
    class TestAdapter extends RecyclerAdapter<String> {
        public TestAdapter(Context context) {
            super(context);
        }

        @Override
        protected View createView(LayoutInflater layoutInflater, ViewGroup viewGroup, int viewType) {
            //创建视图，直接通过layoutinflater创建视图即可
            return null;
        }

        @Override
        protected void convert(RecyclerHolder holder, String model, int position) {
            //convert数据，就是辣么简单
        }
    }
```
#RecyclerSwipeAdapter
```java
    //为swipe定制的方法，可以实现分层滑动,泛型依然需要填入一个列表的参数类型，不是集合！不是集合！不是集合！
    class TestAdapter extends RecyclerSwipeAdapter<String> {
        public TestAdapter(Context context) {
            super(context);
        }

        @Override
        public void clearView(RecyclerHolder recyclerHolder) {
            //在这里，需要通过recyclerholder还原滑动状态
        }

        @Override
        public View getSwipeView(RecyclerHolder recyclerHolder) {
            //获取需要滑动的视图，可以分为多层，比如上层滑动，下层显示颜色或者图标,这里只需要返回需要滑动的视图即可，必须返回
            return null;
        }

        @Override
        public void onSwipe(RecyclerHolder recyclerHolder, int direction, float dx) {
            //滑动中状态，可以执行自定义操作
        }

        @Override
        public void onSwipeStart(RecyclerHolder recyclerHolder, float dx) {
            //并非抽象方法，需要功能细分的可以重写
            //对应前边的swip动作设置
        }

        @Override
        public void onSwipeEnd(RecyclerHolder recyclerHolder, float dx) {
            //并非抽象方法，需要功能细分的可以重写
            //对应前边的swip动作设置
        }

        @Override
        protected View createView(LayoutInflater layoutInflater, ViewGroup viewGroup, int viewType) {
            //创建视图，直接通过layoutinflater创建视图即可
            return null;
        }

        @Override
        protected void convert(RecyclerHolder holder, String model, int position) {
            //convert数据，就是辣么简单
        }
    }
```
#RecyclerHolder
这里的概念部分借鉴了另一个项目，叫啥我忘了，不过扩展了很多，并且完美的集成进了JRecyclerView
```java
    //在convert可以更简单的填充数据，而不需要事先定义viewholder
    @Override
    protected void convert(RecyclerHolder holder, String model, int position) {
        //设置文本
        holder.setText(R.id.textview, model);
        //获取一个视图，无需强转
        TextView textView = holder.getView(R.id.textview);
        //设置点击事件
        holder.setClickListener(R.id.textview, onClickListener);
        //设置enable
        holder.setEnabled(R.id.textview, enabled);
        //设置是否可视
        holder.setViewVisible(R.id.textview, isVisible);
        //当然了，还有很多的实现没有列出来，类似的实现也可以无限的举例，
        // 不过并不会搞非常多非常全的实现，只会做基础以及常用的实现，各位有什么更好的想法可以留言给我，增减皆可
    }
```
#适配器的数据操作
在实际开发中，一定会操作列表数据的增删改，每次都去写简直不可饶恕
```java
//添加数据，并不是只有一个方法，还可以插入指定位置
testAdapter.addData(data);
//也可以添加一个集合，以及添加到指定位置
testAdapter.addDatas(datas);
//移除一条数据
testAdapter.removeData(position);
//更新一条数据
testAdapter.updataItem(position, data);
//将一条数据从一个位置移动到另一个位置，用在拖动换位，简直逆天的方便，有木有？
testAdapter.moveData(fromPosition,toPosition);
//上述的所有操作都已经调用了notify，是按照不同功能调用的不同notify，不用担心动画的问题
```
#翻页操作
列表的翻页操作简直不能更常见，为了简化使用，我也是拼了
```java
//翻页操作被简化为两个方法，当然了，这其中的过程经历过很多次迭代，方法都可以调用
//在传递参数的时候可以通过getpage的方式得到当前的页码，这个页码记录在适配器中，不需要用户单独记录
testAdapter.getPage(loadMore);
//作为搭配方法，单页显示数量也存在，不嫌麻烦可以通过这个方法调用，因为可以统一修改
testAdapter.getDisplayNumber();
//在请求到数据之后，并不需要通过adddatas的方式添加数据，只需要setdatas即可，loadmore选填，不填为数据覆盖，填入则会依据情况自增或者覆盖
testAdapter.setDatas(datas,loadMore);
//loadMore为是否加载更多，是个布尔值
//true：加载更多，页码会自增加一，设置的数据会自动衔接在已存在数据的后面
//false：下拉刷新,页码会重置为初始值（可以设置初始值），数据会完全覆盖，列表中只存在所设置的集合
//setdatas方法已调用notify，无需再次调用，具体使用可以查看示例
```
#木有更多内容
