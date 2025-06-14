# 🚀 Newlangs4 Docker 部署指南

---

## 🧰 1. 准备工作

- 你需要先安装好：
  - [Docker](https://docs.docker.com/get-docker/) 🐳
  - [docker-compose](https://docs.docker.com/compose/install/) 🧩（Docker Desktop 已自带）

---

## 📦 2. 获取项目代码

```bash
git clone https://github.com/houlangs/Newlangs4.git
cd Newlangs4
```

---

## ⚙️ 3. 配置（可选）

如需自定义端口、数据库等参数，可编辑 `docker-compose.yml` 文件。

---

## ▶️ 4. 一键启动！

在项目根目录下运行：

```bash
docker-compose up -d
```

✨ 稍等片刻，服务就会自动启动啦！

---

## 🌐 5. 访问你的服务

[http://localhost:8080](http://localhost:8080)

---

## 🛠️ 6. 常用命令

- 查看日志：
  ```bash
  docker-compose logs -f
  ```
- 停止服务：
  ```bash
  docker-compose down
  ```
- 重启服务：
  ```bash
  docker-compose restart
  ```

---
