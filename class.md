# 类的说明文档

## Main

用于管理其它需要调用的类及其方法

## Requests 请求

获取输入，判断指令是否有效，并将输入的有效指令转换成RequestsQueue接受的数据类型

`public RequestsQueue readin()` 读入输入序列

`private boolean judger(String str)1` 判断传入的字符串是否合法

`private RequestsQueue creater(String str, RequestsQueue requestQueue)` 构造器，将指令格式进行转换并传递给RequestsQueue

`private void print(String s)` 调用时打印对于不合法请求的输出内容

## RequesQueue 请求队列

管理请求的队列

`public boolean isEmpty(int current)` `public boolean isEmpty()` 判断指令队列是否为空

`public void push(long[] info)` 指令入队

`public long[] pop()` 指令出队

`public long[] get(int i)` 获取指定位置的指令
`public void setInvalid(int i)` 将指定位置的指令设为同质指令

## Floor 楼层

记录楼层对应的指示灯（请求按钮）的使用情况

## Elevator 电梯

记录电梯内对应的指示灯（请求按钮）的使用情况

## Dispatcher 调度器

用于管理电梯的运行状况，控制时间

`public void run(RequestsQueue requestsQueue, Floor floor, Elevator elevator)` 电梯运行的管理

`private void print(long floor, int status, double timer)` `public void print(long index)` 调用时打印输出结果
