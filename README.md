# **机票预定**系统项目介绍

**本项目基于Spring boot + vue开发，使用Spring Security实现登陆和鉴权，MySQL存储数据，redis作为缓存和分布式锁。**

前端代码：https://github.com/ChenZixinn/Aircraft



### 1、开发环境

开发工具：Intellij IDEA，Visual Studio Code

运行环境：JDK8

主要组件： **Redis、SpringBoot、vue、MySQL、Spring Security、Redisson**

### 2、数据表

##### 用户表（user）

![image-20230613132402727](./README.assets/image-20230613132402727.png)

##### 机票表 （ticket）

![image-20230613132425367](./README.assets/image-20230613132425367.png)

##### 订单表（order_ticket）

![image-20230613132444726](./README.assets/image-20230613132444726.png)

### 3、界面展示

##### 机票预订页

##### ![image-20230613132514532](/Users/chenzixin/Library/Mobile Documents/com~apple~CloudDocs/Documents/Code/java/TicketReservationSystem/README.assets/image-20230613132514532.png)



##### 订单管理页

![image-20230613132530259](./README.assets/image-20230613132530259.png)



##### **登录注册页**

![image-20230613132558394](./README.assets/image-20230613132558394.png)

![image-20230613132607322](./README.assets/image-20230613132607322.png)
