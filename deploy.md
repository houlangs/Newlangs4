# Newlangs4 部署最佳实践指南

## 概述

为便于根据您的业务需求进行定制开发，我们建议您 **Fork 本仓库** 并在 Fork 后的版本中进行代码修改。如需同步上游更新，可随时创建 Pull Request 以合并主仓库的改动至您的分支。

## 推荐部署方式：Docker

我们推荐使用 **Docker Compose** 进行部署。请参考仓库中的 `docker-compose.yml` 文件，并使用以下命令启动服务：

```bash
docker-compose up -d
```

在部署前，请根据您的实际需求，对 `/docker` 目录下的配置文件进行适当修改。尤其是您可能需要编辑 `docker-compose.yml` 文件，以增删服务组件（如数据库、Redis 等）或调整配置参数。

如果您需要连接其他数据库，我们建议您设法将其放置在私有网络中，并直接将数据库连接信息提交至GitHub（这也是安全的），且可以保证一致性，否则您需要注意git pull时可能出现的冲突问题。

## 数据库与缓存服务说明

系统在首次启动时将自动初始化数据库结构。默认的管理员账户为：

* **用户名**：`admin@houlangs.com`
* **密码**：`123456`

**请在首次部署后立即修改默认密码以保障系统安全。**

注意：初始化 SQL 脚本位于 `docker/mysql/docker-entrypoint-initdb.d/newlangs-config.sql`，该文件仅在数据库首次启动时生效。若需重置数据库，请 **删除 `/docker/mysql/data` 目录**，Redis 服务亦遵循相同逻辑，删除 `/docker/redis/data` 目录可重置其数据。

## 服务架构说明

`docker-compose.yml` 定义了如下服务结构：

* `hl4_network`：用于容器间通信的专属 Docker 网络。
* `backend`：后端服务，基于 `hl4-backend` 目录构建镜像并运行。
* `dashboard`：前端管理面板，基于 `hl4-dashboard` 构建。在 Node 环境中编译后，使用 Nginx 作为静态资源服务器并反向代理 API 请求至 `backend` 容器（容器之间可通过服务名互相访问）。
* `mysql`：MySQL 数据库服务。
* `redis`：Redis 缓存服务。

各服务将由 Docker Compose 自动协调启动，其中后端服务将等待数据库准备完成后再行初始化，内部网络通信由 Docker 自动管理，无需手动配置。

## 数据持久化与备份建议

* **MySQL** 数据目录：`/docker/mysql/data`
* **Redis** 数据目录：`/docker/redis/data`

为确保数据安全，请定期备份上述目录。您也可以将数据目录映射至由 Docker 管理的命名卷，例如将：

```yaml
./docker/mysql/data → mysql-data
./docker/redis/data → redis-data
```

并取消注释 `docker-compose.yml` 中的 `volumes` 部分，以启用 Docker 卷机制进行持久化管理。

## 部署流程回顾

完成上述配置后，您将拥有一套完整的 DevOps 工作流程：

1. 在本地开发环境中修改代码并推送至 GitHub 仓库；
2. 在服务器上拉取最新代码并运行以下命令重建镜像并部署服务：

```bash
docker-compose up --build -d
```
<details>
<summary><strong>自动部署</strong></summary>

您可以借助计划任务（如 `crontab`）或编写 Shell 脚本，自动执行代码拉取与部署操作。以下为示例脚本：

```bash
git pull
docker-compose up --build -d
```

此脚本可用于构建简易的自动化部署流程。

</details>