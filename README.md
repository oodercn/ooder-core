# onecode-core

#### 介绍
onecode 核心代码库，是 onecode 平台的核心组件，包含了注解驱动解析、D2C（设计到代码）转换、全栈 OneCode 编译支持等关键功能。该组件为 onecode 平台提供了基础架构和核心能力支持。

#### 软件架构
onecode-core 采用模块化设计，主要包含以下核心模块：
1. 注解驱动解析模块：负责解析自定义注解并生成相应的代码
2. D2C 转换模块：将设计文档转换为可执行代码
3. 全栈编译支持模块：支持前后端代码的编译和打包
4. 核心工具类模块：提供通用的工具类和方法

#### 技术栈
- Java 8+
- Spring Framework 5.3.x
- JavaParser 3.18.0
- Netty 4.1.x
- Maven 3.x

#### 安装教程
1. 在 Maven 项目中添加依赖：
```xml
<dependency>
    <groupId>cn.raddev</groupId>
    <artifactId>onecode-core</artifactId>
    <version>3.0.1</version>
</dependency>
```
2. 确保 Maven 仓库中包含 onecode 相关依赖包
3. 构建项目：`mvn clean install`

#### 使用说明
1. 引入 onecode-core 依赖
2. 配置相关注解和参数
3. 调用核心 API 进行代码生成或转换
4. 具体使用示例请参考官方文档或示例项目

#### 参与贡献
1. Fork 本仓库
2. 新建 Feat_xxx 分支
3. 提交代码
4. 新建 Pull Request

#### 特技
1. 使用 Readme_XXX.md 来支持不同的语言，例如 Readme_en.md, Readme_zh.md
2. Gitee 官方博客 [blog.gitee.com](https://blog.gitee.com)
3. 你可以 [https://gitee.com/explore](https://gitee.com/explore) 这个地址来了解 Gitee 上的优秀开源项目
4. [GVP](https://gitee.com/gvp) 全称是 Gitee 最有价值开源项目，是综合评定出的优秀开源项目
5. Gitee 官方提供的使用手册 [https://gitee.com/help](https://gitee.com/help)
6. Gitee 封面人物是一档用来展示 Gitee 会员风采的栏目 [https://gitee.com/gitee-stars/](https://gitee.com/gitee-stars/)
