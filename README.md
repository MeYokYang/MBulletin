## 1.项目需求

## 2.项目框架

项目采用**前后端分离**。前端使用单页面模式，使用React。后端使用微服务架构，Java开发，使用Nacos。数据库采用MySQL。缓存采用Redis。日志收集为Logstash。日志管理为Elasticsearch。日志展示为Kibana。

前端项目命名为mbulletin-page、后端项目命名为mbulletin-server。

项目部署采用**伪分布式**，即部署到同一台主机上的不同端口。

---

### mbulletin-page

#### 服务配置

node.js：

* 服务端口：3000



---

### mbulletin-server

#### 服务配置

nacos（dev为单体，pro为集群）：

* 服务端口：8848（dev），16101、16102、16103（pro）

* 命名空间（dev）：45a0c9a0-40ad-451b-b462-e4732a52226e



gateway（dev为单体，pro为集群）：

* 服务端口：10010（dev）,16001、16002、16003（pro）



user-service：

* 服务端口：16011、16012、16013



device-service：

* 服务端口：16021、16022、16023



bulletin-service：

* 服务端口：16031、16032、16033





mysql（单体）：

* 服务端口：3306



redis（单体）：

 * 服务端口：6379


## 3.核心问题

---

### 跨域

前后端分离，部署到不同的端口上，属于不同的域。 

对于前端使用ajax访问后端，浏览器发现属于不同域，会拦截访问的资源。解决方法是为后端返回的数据添加```Access-Control-Allow-Origin：http://localhost:3000```，这里做统一配置，在gateway模块中，配置允许前端域进行访问。 

对于其它情况，解决方法为拒绝外来访问直接访问后端接口。配置ufw，后端端口只允许本机访问。

---

### 微服务下session共享

gateway设置的微服务负载均衡策略为**轮询**，同一个会话下，会出现访问不同的微服务实例，导致session不一致而无法做到session资源共享。

通过**重写shiro管理session的实现类SessionDao，将session保存到redis当中实现共享**。

注意：axios请求需要配置```withCredentials: true```，确保每次会话cookie中SESSIONID相同。

---

### 权限管理

使用shiro框架，权限全由后端进行管理。权限管理可分为页面访问权限与端口调用权限。

#### 页面访问权限

只分**是否登录**能访问的页面。


#### 端口调用权限