设计的目标主要包含以下几点：


完成 spring核心之ioc,aop
实现扩展点BPP ,BDPP，factoryBean
实现最基本版spring-mvc
=============================================================

为了方便读取配置，所有的配置都由xml转properties文件读取
第一步是实现properties功能
第二步是实现注解的功能



=============================================================
                            applicationContext 
                   refresh              beanfactory.getbean 
                     |
                     reader.loaddb
BeanDefinition




























-------------------------------------------------------------------------
后感：
    学习很重要，但学习的方式方法更重要，在我花费小半年基本了解了spring后，我萌生了要写
    spring框架的想法，来实现基于注解的mini-spring;
    后来我又花费了一段时间去为手写做准备，于是我阅读了【Spring 5核心原理与30个类手写实战】
    基于该书分析下，我很快的实现了mini-spring，也就是这套源码，当然受该书影响的原因，最终
    实现的版本变成了类似xml的扫描和注解的DI版本，不专注于一个领域实现，哈哈！
    这段历程如果完全没有辅导资料，相关视频，同事的帮助，我可能2年也很难入门，
    所以说，学习的方式方法很重要，这也是我写这段后感与大家分享的基本目的，
    学习一定要有一个好的学习方式！
    
    怎样学一个新的知识？ 从应用到设计到架构到源码到仿造;从视频到书籍到实践等等！
    
    
    
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
这个版本还不是很完善，缺失了很重要的bdpp扩展点;
我希望，如果有兴趣的同学可以参与，实现如下相关功能
当然，这还不是圆满的，还有factorybean ,Bdpp ，基于注解