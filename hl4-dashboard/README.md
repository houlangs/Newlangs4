# 厚浪二级域名系统控制面板

这是厚浪二级域名系统的控制面板，提供给用户和管理员使用，它需要预先打包，打包后的内容可以托管在任何静态内容托管服务之上。


## ❗️ 重要链接

- 📄 [Docs](https://hlyun.org/article/houlangs/hl4-install)
- 🚨 [Issues](https://github.com/houlangs/Newlangs4/issues)
- 💬 [QQ](https://github.com/houlangs/Newlangs4/issues)

## 💿 安装

使用您喜欢的包管理器设置您的项目。使用相应的命令安装依赖项：

| 包管理器                                                      | 指令           |
|---------------------------------------------------------------|----------------|
| [pnpm](https://pnpm.io/installation)                          | `pnpm install` |
| [npm](https://docs.npmjs.com/cli/v7/commands/npm-install)     | `npm install`  |
| [yarn](https://yarnpkg.com/getting-started)                   | `yarn install` |

完成安装后，您的环境就可以进行构建了。

## 💡 用法

本节介绍如何启动开发服务器并构建生产项目。

### 启动开发服务器

要使用热重载启动开发服务器，请运行以下命令。服务器将可通过 [http://localhost:3000](http://localhost:3000) 访问：

```bash
npm run dev
```

（也可以使用yarn或pnpm）


### 生产构建
您需要修改项目中的部分内容，请至少修改以下部分：

#### ⚠️修改后端地址
在src/axios/axios.js中将baseURL替换为你的后端地址
#### 修改logo/页面内容
logo地址：public/favicon.ico 直接替换文字即可
脚页/菜单等部件：src/layout
首页：src/views/Dashboard.vue
其它页面：src/views
*万能大法：通过VScode全局搜索功能，想改什么搜什么。*

然后，您可以构建您的项目


#### 要构建生产项目，请使用：

```bash
npm run build
```

（也可以使用yarn或pnpm）

构建过程完成后，您的应用程序就可以部署到生产环境中了。编译后的静态文件会输出在dist目录，将目录中的文件全部上传至服务器即可访问。

## 📑 License
[MIT](http://opensource.org/licenses/MIT)

Copyright (c) 2023-present 厚浪
