# work-order-system

运维工单系统 · 基于 Spring Cloud 的微服务工程。

运维工程师提交 bug 工单 → 项目经理派发 → 处理工程师处理并反馈 → 运维/项目经理验证闭环。

## 当前目录结构

```text
work-order-system/
├── pom.xml                # 父工程（packaging=pom，依赖管理 / BOM）
├── README.md
├── .gitignore
└── workorder-common/      # 公共模块：统一 Result / 异常 / 工具 / 常量 / 自动装配
```

当前 Maven 父工程只启用了一个子模块：

- `workorder-common`：已创建，包含公共返回对象、异常、雪花 ID、查询工具、CORS 配置等。
- `workorder-gateway`、`workorder-user`、`workorder-ticket`、`workorder-message`：仍在 `pom.xml` 中以注释形式保留，后续创建对应模块时再取消注释。

## 技术栈

- Java 21
- Spring Boot 3.3.5
- Spring Cloud 2023.0.1
- Spring Cloud Alibaba 2023.0.1.0（Nacos 注册/配置）
- Spring Cloud OpenFeign + LoadBalancer（服务间远程调用）
- SpringDoc / OpenAPI（接口文档）
- Nacos（默认 `127.0.0.1:8848`）

## 父工程约定

- `dependencyManagement`：统一导入 Spring Boot / Cloud / Alibaba / SpringDoc 的 BOM，并在需要时管理 MyBatis-Plus、Lombok 等依赖版本。
- 顶层 `<dependencies>`：所有子模块默认继承 Nacos、Feign、Actuator、Web、OpenAPI、Test 等公共依赖。
- 子模块是独立可运行的 Spring Boot 服务时，在自己的 `build/plugins` 中启用 `spring-boot-maven-plugin`；父工程已在 `pluginManagement` 中管理其版本。

## 快速开始

1. 启动 Nacos（默认 `127.0.0.1:8848`）。
2. 编译当前已启用模块：`mvn -pl workorder-common -am install`。
3. 后续创建网关/业务服务时，在 `pom.xml` 的 `<modules>` 中取消对应 `<module>` 注释。
4. 各可运行服务在自己的 `application.yml` 中设置 `spring.application.name` 与 Nacos 地址。
5. 可运行服务使用 `mvn spring-boot:run` 启动，必要时访问 `http://localhost:<port>/swagger-ui.html` 查看接口文档。

## 前端工程

前端工程位于 `workorder-web`，使用 Vue 3 + Vite + TypeScript + Element Plus。  
前端开发时通过 Vite 代理把 `/api` 请求转发到本机网关 `http://localhost:8080`。
