# 🚀 Newlangs Docker 部署指南 (Newlangs4-main)

嗨，亲爱的用户！(｡･∀･)ﾉﾞ 只需几步，就能用 Docker 快速部署本项目啦！

---

## 🧰 1. 准备工作

- 你需要先安装好：
  - [Docker](https://docs.docker.com/get-docker/) 🐳
  - [docker-compose](https://docs.docker.com/compose/install/) 🧩（Docker Desktop 已自带）

---

## 📦 2. 获取项目代码

```bash
git clone [https://github.com/github.com/houlangs/Newlangs4](https://github.com/houlangs/Newlangs4.git)
cd Newlangs4
```

---

## ⚙️ 3. 配置（可选）

如需自定义端口、数据库等参数，可编辑 `docker-compose.yml` 文件。

---

## ▶️ 4. 一键启动！

在项目根目录下运行：

```bash
docker-compose up -d --build
```

✨ 稍等片刻，服务就会自动启动啦！

---

## 🌐 5. 访问你的服务

- **管理后台**： [http://localhost:8080](http://localhost:8080)
- **门户首页**： [http://localhost:8081](http://localhost:8081)
- **后端 API**： [http://localhost:8088](http://localhost:8088)

（实际端口以 `docker-compose.yml` 为准哦！）

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

## ❓ 常见问题

- 端口被占用？换个端口，改下 `docker-compose.yml` 就好啦！
- 想保存数据？用 volumes 持久化，数据不丢失！

---

## 💡 小贴士

- 需要自定义构建参数？请看各子项目下的 README 或 Dockerfile。
- 有问题？欢迎提 issue！(ง •_•)ง

---

祝你部署愉快！(๑•̀ㅂ•́)و✧ 
