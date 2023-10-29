import axios from 'axios'

const axiosIns = axios.create({
baseURL: '后端接口地址',
	timeout: 20000
})

// 添加请求拦截器
axiosIns.interceptors.request.use(function (config) {
    // 在发送请求之前做些什么
	let accessToken = localStorage.getItem('accessToken')
	
	config.headers = {
		'Content-Type':'application/json;charset=UTF-8',
		'accessToken': accessToken
	}
    return config
  }, function (error) {
    // 对请求错误做些什么
    return Promise.reject(error)
  })

// 添加响应拦截器
axiosIns.interceptors.response.use(
  function (response) {
    // 2xx 范围内的状态码都会触发该函数。
    // 对响应数据做点什么
    let message = String(response.data.msg)
    if (message.includes('未能读取到有效 token') || message.includes('token 无效')) {
      window.location.href = '/#/auth/login'
    }
    return response
  }, function (error) {

    // 超出 2xx 范围的状态码都会触发该函数。
    // 对响应错误做点什么
    return Promise.reject(error)
  })
export default axiosIns