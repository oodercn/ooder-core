# onecode-core

#### Description
onecode-core is the core component of the onecode platform, providing essential functionalities including annotation-driven parsing, D2C (Design to Code) conversion, and full-stack OneCode compilation support. This component forms the foundation and core capabilities of the onecode platform.

#### Software Architecture
onecode-core adopts a modular design with the following key modules:
1. Annotation-driven parsing module: Responsible for parsing custom annotations and generating corresponding code
2. D2C conversion module: Converts design documents into executable code
3. Full-stack compilation support module: Supports compilation and packaging of front-end and back-end code
4. Core utilities module: Provides common utility classes and methods

#### Technology Stack
- Java 8+
- Spring Framework 5.3.x
- JavaParser 3.18.0
- Netty 4.1.x
- Maven 3.x

#### Installation
1. Add dependency in your Maven project:
```xml
<dependency>
    <groupId>cn.raddev</groupId>
    <artifactId>onecode-core</artifactId>
    <version>3.0.1</version>
</dependency>
```
2. Ensure the Maven repository contains onecode related dependency packages
3. Build the project: `mvn clean install`

#### Instructions
1. Include the onecode-core dependency
2. Configure relevant annotations and parameters
3. Call core APIs for code generation or conversion
4. For detailed usage examples, please refer to official documentation or sample projects

#### Contribution
1. Fork the repository
2. Create Feat_xxx branch
3. Commit your code
4. Create Pull Request

#### Gitee Feature
1. You can use Readme_XXX.md to support different languages, such as Readme_en.md, Readme_zh.md
2. Gitee blog [blog.gitee.com](https://blog.gitee.com)
3. Explore open source project [https://gitee.com/explore](https://gitee.com/explore)
4. The most valuable open source project [GVP](https://gitee.com/gvp)
5. The manual of Gitee [https://gitee.com/help](https://gitee.com/help)
6. The most popular members  [https://gitee.com/gitee-stars/](https://gitee.com/gitee-stars/)
